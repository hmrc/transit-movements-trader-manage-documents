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
import java.time.LocalDateTime

import cats.data.NonEmptyList
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.ControlResult
import models.CustomsOfficeTransit
import models.DeclarationType
import models.reference._
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import services.ReferenceDataNotFound

class TransitAccompanyingDocumentConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val countries                 = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))
  private val kindsOfPackage            = Seq(KindOfPackage("P1", "Package 1"), KindOfPackage("P2", "Package 2"))
  private val documentTypes             = Seq(DocumentType("T1", "Document 1", transportDocument = true), DocumentType("T2", "Document 2", transportDocument = false))
  private val additionalInfo            = Seq(AdditionalInformation("I1", "Info 1"), AdditionalInformation("I2", "info 2"))
  private val previousDocumentTypes     = Seq(PreviousDocumentTypes("235", "Container list"), PreviousDocumentTypes("270", "Loading list (delivery note)"))
  private val sensitiveGoodsInformation = Nil

  private val invalidCode = "non-existent code"

  private val customsOfficeTransitModel     = CustomsOfficeTransit("BBBBBB", None)
  private val customsOfficeTransitViewModel = viewmodels.CustomsOfficeTransit("BBBBBB", None)

  "toViewModel" - {

    "must return a view model when all of the necessary reference data can be found" in {

      val date = LocalDate.of(2020, 8, 1)

      val model = models.TransitAccompanyingDocument(
        localReferenceNumber = "lrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(countries.head.code),
        countryOfDestination = Some(countries.head.code),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head.code),
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
        departureOffice = "Departure office",
        customsOfficeTransit = Seq(
          customsOfficeTransitModel,
          customsOfficeTransitModel,
          customsOfficeTransitModel,
          customsOfficeTransitModel,
          customsOfficeTransitModel,
          customsOfficeTransitModel,
          customsOfficeTransitModel
        ),
        controlResult = Some(ControlResult("AA", date)),
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
            previousAdministrativeReferences = Seq(
              models.PreviousAdministrativeReference(
                previousDocumentTypes.head.code,
                "ref",
                Some("information")
              )),
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

      val expectedResult = viewmodels.TransitAccompanyingDocument(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        singleCountryOfDispatch = Some(countries.head),
        singleCountryOfDestination = Some(countries.head),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head),
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
        departureOffice = "Departure office",
        departureOfficeTrimmed = "Departure office",
        customsOfficeTransit = Seq(
          customsOfficeTransitViewModel,
          customsOfficeTransitViewModel,
          customsOfficeTransitViewModel,
          customsOfficeTransitViewModel,
          customsOfficeTransitViewModel,
          customsOfficeTransitViewModel
        ),
        controlResult = Some(viewmodels.ControlResult("AA", date)),
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
            previousAdministrativeReferences = Seq(
              viewmodels.PreviousDocumentType(
                previousDocumentTypes.head,
                "ref",
                Some("information")
              )),
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

      val result =
        TransitAccompanyingDocumentConverter.toViewModel("mrn", model, countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes)

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
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal =
          models.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", invalidCode, Some("Principal EORI"), Some("tir")),
        consignor = None,
        consignee = None,
        departureOffice = "The Departure office, less than 45 characters long",
        customsOfficeTransit = Nil,
        controlResult = None,
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
            previousAdministrativeReferences = Seq(
              models.PreviousAdministrativeReference(
                invalidCode,
                "ref",
                Some("information")
              )),
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

      val result =
        TransitAccompanyingDocumentConverter.toViewModel("mrn", model, countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes)

      val expectedErrors = Seq(
        ReferenceDataNotFound("countryOfDispatch", invalidCode),
        ReferenceDataNotFound("countryOfDestination", invalidCode),
        ReferenceDataNotFound("transportCountry", invalidCode),
        ReferenceDataNotFound("principal.countryCode", invalidCode),
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
        ReferenceDataNotFound("goodsItems[0].packages[2].kindOfPackage", invalidCode),
        ReferenceDataNotFound("goodsItems[0].previousAdministrativeReference[0].previousDocumentTypes", invalidCode)
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }
  }
}
