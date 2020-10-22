/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services.conversion
import cats.data.Validated.Valid
import cats.implicits._
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.DeclarationType
import models.reference.Country
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import services.ReferenceDataNotFound
import services.ReferenceDataRetrievalError
import services.ReferenceDataService
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TransitAccompanyingDocumentConversionServiceSpec
    extends FreeSpec
    with MustMatchers
    with MockitoSugar
    with ValidatedMatchers
    with ValidatedValues
    with ScalaFutures
    with IntegrationPatience {

  private val countries = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))

  implicit private val hc: HeaderCarrier = HeaderCarrier()

  val validModel = models.TransitAccompanyingDocument(
    localReferenceNumber = "lrn",
    declarationType = DeclarationType.T1,
    countryOfDispatch = Some(countries.head.code),
    countryOfDestination = Some(countries.head.code)
  )

  "toViewModel" - {

    "must return a view model" in {

      val referenceDataService = mock[ReferenceDataService]
      when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(countries))

      val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

      val expectedResult = viewmodels.tad.TransitAccompanyingDocument(
        localReferenceNumber = "lrn",
        declarationType = DeclarationType.T1,
        singleCountryOfDispatch = Some(countries.head),
        singleCountryOfDestination = Some(countries.head)
      )

      val result: ValidationResult[viewmodels.tad.TransitAccompanyingDocument] = service.toViewModel(validModel).futureValue

      result.valid.value mustEqual expectedResult
    }

    "must return errors when reference data cannot be retrieved" in {

      val referenceDataService = mock[ReferenceDataService]

      when(referenceDataService.countries()(any(), any()))
        .thenReturn(Future.successful(ReferenceDataRetrievalError("countries", 500, "body").invalidNec))

      val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

      val result = service.toViewModel(validModel).futureValue

      val expectedErrors = Seq(
        ReferenceDataRetrievalError("countries", 500, "body")
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }

    "must return errors when reference data can be retrieved but the conversion fails" in {

      val referenceDataService = mock[ReferenceDataService]
      when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(countries))

      val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

      val invalidRefData = validModel copy (countryOfDispatch = Some("non-existent code"))

      val result = service.toViewModel(invalidRefData).futureValue

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("countryOfDispatch", "non-existent code"))
    }
  }
}
