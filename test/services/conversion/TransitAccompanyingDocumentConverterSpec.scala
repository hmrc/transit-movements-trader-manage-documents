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
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.DeclarationType
import models.reference.Country
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import services.ReferenceDataNotFound

class TransitAccompanyingDocumentConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val countries = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))

  private val invalidCode = "non-existent code"

  "toViewModel" - {

    "must return a view model when all of the necessary reference data can be found" in {

      val model = models.TransitAccompanyingDocument(
        localReferenceNumber = "lrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(countries.head.code),
        countryOfDestination = Some(countries.head.code),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head.code),
        //acceptanceDate = date,
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
      )

      //TODO: Do we need seperate view models
      val expectedResult = viewmodels.tad.TransitAccompanyingDocument(
        localReferenceNumber = "lrn",
        declarationType = DeclarationType.T1,
        singleCountryOfDispatch = Some(countries.head),
        singleCountryOfDestination = Some(countries.head)
      )

      val result = TransitAccompanyingDocumentConverter.toViewModel(model, countries)

      result.valid.value mustEqual expectedResult
    }

    "must return errors when codes cannot be found in the reference data" in {

      val model = models.TransitAccompanyingDocument(
        localReferenceNumber = "lrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(invalidCode),
        countryOfDestination = Some(invalidCode),
        transportIdentity = Some("identity"),
        transportCountry = Some(invalidCode),
        //acceptanceDate = LocalDate.now(),
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
      )

      val result = TransitAccompanyingDocumentConverter.toViewModel(model, countries)

      val expectedErrors = Seq(
        ReferenceDataNotFound("countryOfDispatch", invalidCode),
        ReferenceDataNotFound("countryOfDestination", invalidCode)
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }
  }
}
