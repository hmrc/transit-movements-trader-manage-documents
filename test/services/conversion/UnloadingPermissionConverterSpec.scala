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

import java.time.LocalDate

import cats.data.NonEmptyList
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.DeclarationType
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import services.ReferenceDataNotFound

class UnloadingPermissionConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val countries                 = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))
  private val kindsOfPackage            = Seq(KindOfPackage("P1", "Package 1"), KindOfPackage("P2", "Package 2"))
  private val documentTypes             = Seq(DocumentType("T1", "Document 1", transportDocument = true), DocumentType("T2", "Document 2", transportDocument = false))
  private val additionalInfo            = Seq(AdditionalInformation("I1", "Info 1"), AdditionalInformation("I2", "info 2"))
  private val sensitiveGoodsInformation = Nil

  private val invalidCode = "non-existent code"

  "toViewModel" - {

    "must return a view model when all of the necessary reference data can be found" in {

      val date = LocalDate.of(2020, 8, 1)

      val permission = models.PermissionToStartUnloading(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(countries.head.code),
        countryOfDestination = Some(countries.head.code),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head.code),
        acceptanceDate = date,
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal = models.Principal("Principal name",
                                     "Principal street",
                                     "Principal postCode",
                                     "Principal city",
                                     countries.head.code,
                                     Some("Principal EORI"),
                                     Some("tir")),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head.code, None, None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head.code, None, None)),
        traderAtDestination = models.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
        departureOffice = "Departure office",
        presentationOffice = "Presentation office",
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

      val expectedResult = viewmodels.PermissionToStartUnloading(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        singleCountryOfDispatch = Some(countries.head),
        singleCountryOfDestination = Some(countries.head),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head),
        acceptanceDate = date,
        acceptanceDateFormatted = "01/08/2020",
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal = viewmodels.Principal("Principal name",
                                         "Principal street",
                                         "Principal street",
                                         "Principal postCode",
                                         "Principal city",
                                         countries.head,
                                         Some("Principal EORI"),
                                         Some("tir")),
        consignor =
          Some(viewmodels.Consignor("consignor name", "consignor street", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
        consignee =
          Some(viewmodels.Consignee("consignee name", "consignee street", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
        traderAtDestination = viewmodels.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
        departureOffice = "Departure office",
        departureOfficeTrimmed = "Departure office",
        presentationOffice = "Presentation office",
        seals = Seq("seal 1"),
        goodsItems = NonEmptyList.one(
          viewmodels.GoodsItem(
            itemNumber = 1,
            commodityCode = None,
            declarationType = None,
            description = "Description",
            grossMass = Some(1.0),
            netMass = Some(0.9),
            countryOfDispatch = Some(countries.head),
            countryOfDestination = Some(countries.head),
            producedDocuments = Seq(viewmodels.ProducedDocument(documentTypes.head, None, None)),
            specialMentions = Seq(
              viewmodels.SpecialMentionEc(additionalInfo.head),
              viewmodels.SpecialMentionNonEc(additionalInfo.head, countries.head),
              viewmodels.SpecialMentionNoCountry(additionalInfo.head)
            ),
            consignor = Some(
              viewmodels.Consignor("consignor name", "consignor street", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
            consignee = Some(
              viewmodels.Consignee("consignee name", "consignee street", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
            containers = Seq("container 1"),
            packages = NonEmptyList(
              viewmodels.BulkPackage(kindsOfPackage.head, Some("numbers")),
              List(
                viewmodels.UnpackedPackage(kindsOfPackage.head, 1, Some("marks")),
                viewmodels.RegularPackage(kindsOfPackage.head, 1, "marks and numbers")
              )
            ),
            sensitiveGoodsInformation = sensitiveGoodsInformation
          )
        )
      )

      val result = UnloadingPermissionConverter.toViewModel(permission, countries, additionalInfo, kindsOfPackage, documentTypes)

      result.valid.value mustEqual expectedResult
    }

    "must return errors when codes cannot be found in the reference data" in {

      val permission = models.PermissionToStartUnloading(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(invalidCode),
        countryOfDestination = Some(invalidCode),
        transportIdentity = Some("identity"),
        transportCountry = Some(invalidCode),
        acceptanceDate = LocalDate.now(),
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal =
          models.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", invalidCode, Some("Principal EORI"), Some("tir")),
        consignor = None,
        consignee = None,
        traderAtDestination = models.TraderAtDestinationWithEori("Trader EORI", Some("name"), Some("street"), Some("postCode"), Some("city"), Some(invalidCode)),
        departureOffice = "Departure office",
        presentationOffice = "Presentation office",
        seals = Seq("seal 1"),
        goodsItems = NonEmptyList.one(
          models.GoodsItem(
            itemNumber = 1,
            commodityCode = None,
            declarationType = None,
            description = "Description",
            grossMass = Some(1.0),
            netMass = Some(0.9),
            countryOfDispatch = Some(invalidCode),
            countryOfDestination = Some(invalidCode),
            producedDocuments = Seq(models.ProducedDocument(invalidCode, None, None)),
            specialMentions = Seq(
              models.SpecialMentionEc(invalidCode),
              models.SpecialMentionNonEc(invalidCode, invalidCode),
              models.SpecialMentionNoCountry(invalidCode)
            ),
            consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", invalidCode, None, None)),
            consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", invalidCode, None, None)),
            containers = Seq("container 1"),
            packages = NonEmptyList(
              models.BulkPackage(invalidCode, Some("numbers")),
              List(
                models.UnpackedPackage(invalidCode, 1, Some("marks")),
                models.RegularPackage(invalidCode, 1, "marks and numbers")
              )
            ),
            sensitiveGoodsInformation = sensitiveGoodsInformation
          )
        )
      )

      val result = UnloadingPermissionConverter.toViewModel(permission, countries, additionalInfo, kindsOfPackage, documentTypes)

      val expectedErrors = Seq(
        ReferenceDataNotFound("countryOfDispatch", invalidCode),
        ReferenceDataNotFound("countryOfDestination", invalidCode),
        ReferenceDataNotFound("transportCountry", invalidCode),
        ReferenceDataNotFound("principal.countryCode", invalidCode),
        ReferenceDataNotFound("traderAtDestination.countryCode", invalidCode),
        ReferenceDataNotFound("goodsItems[0].countryOfDispatch", invalidCode),
        ReferenceDataNotFound("goodsItems[0].countryOfDestination", invalidCode),
        ReferenceDataNotFound("goodsItems[0].producedDocuments[0].documentType", invalidCode),
        ReferenceDataNotFound("goodsItems[0].specialMentions[0].additionalInformationCoded", invalidCode),
        ReferenceDataNotFound("goodsItems[0].specialMentions[1].countryCode", invalidCode),
        ReferenceDataNotFound("goodsItems[0].specialMentions[1].additionalInformationCoded", invalidCode),
        ReferenceDataNotFound("goodsItems[0].specialMentions[2].additionalInformationCoded", invalidCode),
        ReferenceDataNotFound("goodsItems[0].consignor.countryCode", invalidCode),
        ReferenceDataNotFound("goodsItems[0].consignee.countryCode", invalidCode),
        ReferenceDataNotFound("goodsItems[0].packages[0].kindOfPackage", invalidCode),
        ReferenceDataNotFound("goodsItems[0].packages[1].kindOfPackage", invalidCode),
        ReferenceDataNotFound("goodsItems[0].packages[2].kindOfPackage", invalidCode)
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }
  }
}
