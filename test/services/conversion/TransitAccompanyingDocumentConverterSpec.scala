/*
 * Copyright 2021 HM Revenue & Customs
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
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.ControlResult
import models.CustomsOfficeOfTransit
import models.DeclarationType
import models.GuaranteeDetails
import models.GuaranteeReference
import models.PreviousAdministrativeReference
import models.ReturnCopiesCustomsOffice
import models.reference.AdditionalInformation
import models.reference.ControlResultData
import models.reference.Country
import models.reference.CustomsOffice
import models.reference.DocumentType
import models.reference.KindOfPackage
import models.reference.PreviousDocumentTypes
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import services.ReferenceDataNotFound
import utils.FormattedDate
import viewmodels.CustomsOfficeWithOptionalDate
import viewmodels.PreviousDocumentType

import java.time.LocalDate
import java.time.LocalDateTime

class TransitAccompanyingDocumentConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val countries                 = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))
  private val kindsOfPackage            = Seq(KindOfPackage("P1", "Package 1"), KindOfPackage("P2", "Package 2"))
  private val documentTypes             = Seq(DocumentType("T1", "Document 1", transportDocument = true), DocumentType("T2", "Document 2", transportDocument = false))
  private val additionalInfo            = Seq(AdditionalInformation("I1", "Info 1"), AdditionalInformation("I2", "info 2"))
  private val sensitiveGoodsInformation = Nil
  private val departureOffice           = CustomsOfficeWithOptionalDate(CustomsOffice("AB124", Some("Departure Office"), "AB"), None)
  private val destinationOffice         = CustomsOfficeWithOptionalDate(CustomsOffice("AB125", Some("Destination Office"), "AB"), None)
  private val transitOffices = Seq(
    CustomsOfficeWithOptionalDate(CustomsOffice("AB123", Some("Transit Office"), "AB"), Some(LocalDateTime.of(2020, 1, 1, 0, 0))))
  private val previousDocumentTypes = Seq(PreviousDocumentTypes("123", "Some Description"), PreviousDocumentTypes("124", "Some Description2"))
  private val invalidCode           = "non-existent code"
  private val controlResult         = Some(viewmodels.ControlResult(ControlResultData("code", "description a2"), ControlResult("code", LocalDate.of(1990, 2, 3))))

  "toViewModel" - {

    val specialMentionEc                 = models.SpecialMention(None, additionalInfo.head.code, Some(true), None)
    val specialMentionEcViewModel        = viewmodels.SpecialMention(additionalInfo.head, specialMentionEc)
    val specialMentionNonEc              = models.SpecialMention(None, additionalInfo.head.code, None, countries.headOption.map(_.code))
    val specialMentionNonEcViewModel     = viewmodels.SpecialMention(additionalInfo.head, specialMentionNonEc)
    val specialMentionNoCountry          = models.SpecialMention(Some("Description"), additionalInfo.head.code, None, None)
    val specialMentionNoCountryViewModel = viewmodels.SpecialMention(additionalInfo.head, specialMentionNoCountry)

    "must return a view model when all of the necessary reference data can be found" in {

      val model = models.ReleaseForTransit(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(countries.head.code),
        countryOfDestination = Some(countries.head.code),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head.code),
        acceptanceDate = LocalDate.of(2020, 1, 1),
        numberOfItems = 1,
        numberOfPackages = Some(3),
        grossMass = 1.0,
        printBindingItinerary = true,
        authId = Some("AuthId"),
        returnCopy = false,
        circumstanceIndicator = None,
        security = None,
        commercialReferenceNumber = None,
        methodOfPayment = None,
        principal = models.Principal("Principal name",
                                     "Principal street",
                                     "Principal postCode",
                                     "Principal city",
                                     countries.head.code,
                                     Some("Principal EORI"),
                                     Some("tir")),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head.code, None, None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head.code, None, None)),
        customsOfficeOfTransit = Seq(
          CustomsOfficeOfTransit("AB123", Some(LocalDateTime.of(2020, 1, 1, 0, 0)))
        ),
        guaranteeDetails = NonEmptyList.one(
          GuaranteeDetails("A", Seq(GuaranteeReference(Some("RefNum"), None, None, Nil)))
        ),
        departureOffice = "AB124",
        destinationOffice = "AB125",
        controlResult = Some(ControlResult("code", LocalDate.of(1990, 2, 3))),
        returnCopiesCustomsOffice = Some(ReturnCopiesCustomsOffice("office", "street", "postcode", "city", countries.head.code)),
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
            previousAdminRef = Seq(
              PreviousAdministrativeReference("123", "ABABA", None)
            ),
            producedDocuments = Seq(models.ProducedDocument(documentTypes.head.code, None, None)),
            specialMentions = Seq(
              specialMentionEc,
              specialMentionNonEc,
              specialMentionNoCountry
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

      val expectedResult = viewmodels.TransitAccompanyingDocumentPDF(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        singleCountryOfDispatch = Some(countries.head),
        singleCountryOfDestination = Some(countries.head),
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head),
        acceptanceDate = Some(FormattedDate(LocalDate.of(2020, 1, 1))),
        numberOfItems = 1,
        numberOfPackages = Some(3),
        grossMass = 1.0,
        printBindingItinerary = true,
        authId = Some("AuthId"),
        copyType = false,
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
        departureOffice = CustomsOfficeWithOptionalDate(CustomsOffice("AB124", Some("Departure Office"), "AB"), None),
        destinationOffice = CustomsOfficeWithOptionalDate(CustomsOffice("AB125", Some("Destination Office"), "AB"), None),
        customsOfficeOfTransit =
          Seq(CustomsOfficeWithOptionalDate(CustomsOffice("AB123", Some("Transit Office"), "AB"), Some(LocalDateTime.of(2020, 1, 1, 0, 0)))),
        guaranteeDetails = Seq(
          GuaranteeDetails("A", Seq(GuaranteeReference(Some("RefNum"), None, None, Nil)))
        ),
        seals = Seq("seal 1"),
        Some(viewmodels.ReturnCopiesCustomsOffice("office", "street", "postcode", "city", countries.head)),
        controlResult = controlResult,
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
            previousDocumentTypes = Seq(
              PreviousDocumentType(
                PreviousDocumentTypes("123", "Some Description"),
                PreviousAdministrativeReference("123", "ABABA", None)
              )
            ),
            specialMentions = Seq(
              specialMentionEcViewModel,
              specialMentionNonEcViewModel,
              specialMentionNoCountryViewModel
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

      val result = TransitAccompanyingDocumentConverter.toViewModel(model,
                                                                    countries,
                                                                    additionalInfo,
                                                                    kindsOfPackage,
                                                                    documentTypes,
                                                                    departureOffice,
                                                                    destinationOffice,
                                                                    transitOffices,
                                                                    previousDocumentTypes,
                                                                    controlResult)

      result.valid.value mustEqual expectedResult
    }

    "must return errors when codes cannot be found in the reference data" in {

      val model = models.ReleaseForTransit(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        countryOfDispatch = Some(invalidCode),
        countryOfDestination = Some(invalidCode),
        transportIdentity = Some("identity"),
        transportCountry = Some(invalidCode),
        acceptanceDate = LocalDate.now(),
        numberOfItems = 1,
        numberOfPackages = Some(3),
        grossMass = 1.0,
        printBindingItinerary = true,
        authId = Some("SomeId"),
        returnCopy = false,
        circumstanceIndicator = None,
        security = None,
        commercialReferenceNumber = None,
        methodOfPayment = None,
        principal =
          models.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", invalidCode, Some("Principal EORI"), Some("tir")),
        consignor = None,
        consignee = None,
        customsOfficeOfTransit = Seq(
          CustomsOfficeOfTransit("SomeRef", Some(LocalDateTime.now()))
        ),
        guaranteeDetails = NonEmptyList(
          GuaranteeDetails("A", Seq(GuaranteeReference(Some("ref"), None, None, Nil))),
          Nil
        ),
        departureOffice = "The Departure office, less than 45 characters long",
        destinationOffice = "The Destination office, less than 45 characters long",
        returnCopiesCustomsOffice = None,
        controlResult = Some(ControlResult("SomeCode", LocalDate.now())),
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
            previousAdminRef = Seq(
              PreviousAdministrativeReference(invalidCode, "ABASD", Some("Something"))
            ),
            producedDocuments = Seq(models.ProducedDocument(invalidCode, None, None)),
            specialMentions = Seq(
              specialMentionEc.copy(additionalInformationCoded = invalidCode),
              specialMentionNonEc.copy(additionalInformationCoded = invalidCode),
              specialMentionNoCountry.copy(additionalInformationCoded = invalidCode)
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

      val result = TransitAccompanyingDocumentConverter.toViewModel(model,
                                                                    countries,
                                                                    additionalInfo,
                                                                    kindsOfPackage,
                                                                    documentTypes,
                                                                    departureOffice,
                                                                    destinationOffice,
                                                                    transitOffices,
                                                                    previousDocumentTypes,
                                                                    controlResult)

      val expectedErrors = Seq(
        ReferenceDataNotFound("countryOfDispatch", invalidCode),
        ReferenceDataNotFound("countryOfDestination", invalidCode),
        ReferenceDataNotFound("transportCountry", invalidCode),
        ReferenceDataNotFound("principal.countryCode", invalidCode),
        ReferenceDataNotFound("goodsItems[0].countryOfDispatch", invalidCode),
        ReferenceDataNotFound("goodsItems[0].countryOfDestination", invalidCode),
        ReferenceDataNotFound("goodsItems[0].producedDocuments[0].documentType", invalidCode),
        ReferenceDataNotFound("goodsItems[0].previousAdministrativeReference[0].previousDocumentTypes", invalidCode),
        ReferenceDataNotFound("goodsItems[0].specialMentions[0].additionalInformationCoded", invalidCode),
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
