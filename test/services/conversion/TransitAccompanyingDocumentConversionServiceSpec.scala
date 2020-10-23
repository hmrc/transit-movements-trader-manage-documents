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
import cats.data.NonEmptyList
import cats.data.Validated.Valid
import cats.implicits._
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.DeclarationType
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
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

  private val countries                 = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))
  private val kindsOfPackage            = Seq(KindOfPackage("P1", "Package 1"), KindOfPackage("P2", "Package 2"))
  private val documentTypes             = Seq(DocumentType("T1", "Document 1", transportDocument = true), DocumentType("T2", "Document 2", transportDocument = false))
  private val additionalInfo            = Seq(AdditionalInformation("I1", "Info 1"), AdditionalInformation("I2", "info 2"))
  private val sensitiveGoodsInformation = Nil

  implicit private val hc: HeaderCarrier = HeaderCarrier()

  val validModel = models.TransitAccompanyingDocument(
    localReferenceNumber = "lrn",
    declarationType = DeclarationType.T1,
    countryOfDispatch = Some(countries.head.code),
    countryOfDestination = Some(countries.head.code),
    transportIdentity = Some("identity"),
    transportCountry = Some(countries.head.code),
//    acceptanceDate = acceptanceDate,
    numberOfItems = 1,
    numberOfPackages = 3,
    grossMass = 1.0,
    principal =
      models.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", countries.head.code, Some("Principal EORI"), Some("tir")),
    consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head.code, None, None)),
    consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head.code, None, None)),
    departureOffice = "The Departure office, less than 45 characters long",
    seals = Seq("seal 1"),
    goodsItems = NonEmptyList.one(
      models.GoodsItem(
        itemNumber = 1,
        commodityCode = None,
        declarationType = None,
        description = "Description",
        grossMass = Some(1.0),
        netMass = Some(0.9),
        countryOfDispatch = Some(countries.head.code),
        countryOfDestination = Some(countries.head.code),
        producedDocuments = Seq(models.ProducedDocument(documentTypes.head.code, None, None)),
        specialMentions = Seq(
          models.SpecialMentionEc(additionalInfo.head.code),
          models.SpecialMentionNonEc(additionalInfo.head.code, countries.head.code),
          models.SpecialMentionNoCountry(additionalInfo.head.code)
        ),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head.code, None, None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head.code, None, None)),
        containers = Seq("container 1"),
        packages = NonEmptyList(
          models.BulkPackage(kindsOfPackage.head.code, Some("numbers")),
          List(
            models.UnpackedPackage(kindsOfPackage.head.code, 1, Some("marks")),
            models.RegularPackage(kindsOfPackage.head.code, 1, "marks and numbers")
          )
        ),
        sensitiveGoodsInformation = sensitiveGoodsInformation
      )
    )
  )

  "toViewModel" - {

    "must return a view model" in {

      val referenceDataService = mock[ReferenceDataService]
      when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(countries))

      val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

      //TODO: Consider using the same view model for up and tad
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
