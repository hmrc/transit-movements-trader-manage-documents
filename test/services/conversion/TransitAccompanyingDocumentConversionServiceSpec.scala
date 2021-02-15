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
import cats.data.Validated.Valid
import cats.implicits._
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import generators.ModelGenerators
import models.Consignee
import models.Consignor
import models.DeclarationType
import models.TransitAccompanyingDocument
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import services.ReferenceDataNotFound
import services.ReferenceDataRetrievalError
import services.ReferenceDataService
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import utils.StringTransformer._

class TransitAccompanyingDocumentConversionServiceSpec
    extends FreeSpec
    with MustMatchers
    with MockitoSugar
    with ValidatedMatchers
    with ValidatedValues
    with ScalaFutures
    with IntegrationPatience
    with ModelGenerators
    with ScalaCheckPropertyChecks {

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
    numberOfItems = 1,
    numberOfPackages = Some(3),
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

    "must return a view model" - {

      "all data exists" in {

        forAll(arbitrary[Country], arbitrary[TransitAccompanyingDocument], arbitrary[Consignor], arbitrary[Consignee], stringWithMaxLength(17)) {
          (countriesGen, transitAccompanyingDocumentGen, consignorGen, consigneeGen, mrn) =>
            val referenceDataService = mock[ReferenceDataService]
            when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(Seq(countriesGen, Country("valid", "AA", "Country A"))))
            when(referenceDataService.kindsOfPackage()(any(), any())) thenReturn Future.successful(Valid(kindsOfPackage))
            when(referenceDataService.documentTypes()(any(), any())) thenReturn Future.successful(Valid(documentTypes))
            when(referenceDataService.additionalInformation()(any(), any())) thenReturn Future.successful(Valid(additionalInfo))

            val consignorGenUpdated = Some(consignorGen.copy(countryCode = "AA"))
            val consigneeGenUpdated = Some(consigneeGen.copy(countryCode = "AA"))

            val transitAccompanyingDocument =
              transitAccompanyingDocumentGen.copy(
                principal = transitAccompanyingDocumentGen.principal.copy(countryCode = countriesGen.code),
                consignor = consignorGenUpdated,
                consignee = consigneeGenUpdated
              )

            val validModelUpdated = validModel.copy(
              declarationType = transitAccompanyingDocument.declarationType,
              countryOfDispatch = Some(countriesGen.code),
              countryOfDestination = Some(countriesGen.code),
              transportIdentity = transitAccompanyingDocument.transportIdentity,
              transportCountry = Some(countriesGen.code),
              numberOfItems = transitAccompanyingDocument.numberOfItems,
              numberOfPackages = transitAccompanyingDocument.numberOfPackages,
              grossMass = transitAccompanyingDocument.grossMass,
              principal = transitAccompanyingDocument.principal,
              consignor = transitAccompanyingDocument.consignor,
              consignee = transitAccompanyingDocument.consignee,
              departureOffice = transitAccompanyingDocument.departureOffice,
              seals = transitAccompanyingDocument.seals
            )

            val expectedResult = viewmodels.PermissionToStartUnloading(
              movementReferenceNumber = mrn,
              declarationType = transitAccompanyingDocument.declarationType,
              singleCountryOfDispatch = Some(countriesGen),
              singleCountryOfDestination = Some(countriesGen),
              transportIdentity = transitAccompanyingDocument.transportIdentity,
              transportCountry = Some(countriesGen),
              acceptanceDate = None,
              acceptanceDateFormatted = None,
              numberOfItems = transitAccompanyingDocument.numberOfItems,
              numberOfPackages = transitAccompanyingDocument.numberOfPackages,
              grossMass = transitAccompanyingDocument.grossMass,
              principal = viewmodels.Principal(
                transitAccompanyingDocument.principal.name,
                transitAccompanyingDocument.principal.streetAndNumber,
                transitAccompanyingDocument.principal.streetAndNumber.shorten(32)("***"),
                transitAccompanyingDocument.principal.postCode,
                transitAccompanyingDocument.principal.city,
                countriesGen,
                transitAccompanyingDocument.principal.eori,
                transitAccompanyingDocument.principal.tir
              ),
              consignor = Some(
                viewmodels.Consignor(
                  transitAccompanyingDocument.consignor.get.name,
                  transitAccompanyingDocument.consignor.get.streetAndNumber,
                  transitAccompanyingDocument.consignor.get.streetAndNumber.shorten(32)("***"),
                  transitAccompanyingDocument.consignor.get.postCode,
                  transitAccompanyingDocument.consignor.get.city,
                  countries.head,
                  transitAccompanyingDocument.consignor.get.eori
                )),
              consignee = Some(
                viewmodels.Consignee(
                  transitAccompanyingDocument.consignee.get.name,
                  transitAccompanyingDocument.consignee.get.streetAndNumber,
                  transitAccompanyingDocument.consignee.get.streetAndNumber.shorten(32)("***"),
                  transitAccompanyingDocument.consignee.get.postCode,
                  transitAccompanyingDocument.consignee.get.city,
                  countries.head,
                  transitAccompanyingDocument.consignee.get.eori
                )),
              traderAtDestination = None,
              departureOffice = transitAccompanyingDocument.departureOffice,
              departureOfficeTrimmed = transitAccompanyingDocument.departureOffice.shorten(45)("***"),
              presentationOffice = None,
              seals = transitAccompanyingDocument.seals,
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
                  consignor = Some(viewmodels
                    .Consignor("consignor name", "consignor street", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
                  consignee = Some(viewmodels
                    .Consignee("consignee name", "consignee street", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
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

            val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

            val result: ValidationResult[viewmodels.PermissionToStartUnloading] = service.toViewModel(validModelUpdated, mrn).futureValue

            result.valid.value mustEqual expectedResult
        }

      }

      "mandatory data exists" in {

        forAll(arbitrary[Country], arbitrary[TransitAccompanyingDocument], stringWithMaxLength(17)) {
          (countriesGen, transitAccompanyingDocumentGen, mrn) =>
            val referenceDataService = mock[ReferenceDataService]
            when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(Seq(countriesGen, Country("valid", "AA", "Country A"))))
            when(referenceDataService.kindsOfPackage()(any(), any())) thenReturn Future.successful(Valid(kindsOfPackage))
            when(referenceDataService.documentTypes()(any(), any())) thenReturn Future.successful(Valid(documentTypes))
            when(referenceDataService.additionalInformation()(any(), any())) thenReturn Future.successful(Valid(additionalInfo))

            val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

            val transitAccompanyingDocument =
              transitAccompanyingDocumentGen.copy(principal = transitAccompanyingDocumentGen.principal.copy(countryCode = countriesGen.code))

            val validModelUpdated = validModel.copy(
              declarationType = transitAccompanyingDocument.declarationType,
              countryOfDispatch = None,
              countryOfDestination = None,
              transportIdentity = None,
              transportCountry = None,
              numberOfItems = transitAccompanyingDocument.numberOfItems,
              numberOfPackages = transitAccompanyingDocument.numberOfPackages,
              grossMass = transitAccompanyingDocument.grossMass,
              principal = transitAccompanyingDocument.principal,
              consignor = None,
              consignee = None,
              departureOffice = transitAccompanyingDocument.departureOffice,
              seals = Nil
            )

            val expectedResult = viewmodels.PermissionToStartUnloading(
              movementReferenceNumber = mrn,
              declarationType = transitAccompanyingDocument.declarationType,
              singleCountryOfDispatch = None,
              singleCountryOfDestination = None,
              transportIdentity = None,
              transportCountry = None,
              acceptanceDate = None,
              acceptanceDateFormatted = None,
              numberOfItems = transitAccompanyingDocument.numberOfItems,
              numberOfPackages = transitAccompanyingDocument.numberOfPackages,
              grossMass = transitAccompanyingDocument.grossMass,
              principal = viewmodels.Principal(
                transitAccompanyingDocument.principal.name,
                transitAccompanyingDocument.principal.streetAndNumber,
                transitAccompanyingDocument.principal.streetAndNumber.shorten(32)("***"),
                transitAccompanyingDocument.principal.postCode,
                transitAccompanyingDocument.principal.city,
                countriesGen,
                transitAccompanyingDocument.principal.eori,
                transitAccompanyingDocument.principal.tir
              ),
              consignor = None,
              consignee = None,
              traderAtDestination = None,
              departureOffice = transitAccompanyingDocument.departureOffice,
              departureOfficeTrimmed = transitAccompanyingDocument.departureOffice.shorten(45)("***"),
              presentationOffice = None,
              seals = Nil,
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
                  consignor = Some(viewmodels
                    .Consignor("consignor name", "consignor street", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
                  consignee = Some(viewmodels
                    .Consignee("consignee name", "consignee street", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
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

            val result: ValidationResult[viewmodels.PermissionToStartUnloading] = service.toViewModel(validModelUpdated, mrn).futureValue

            result.valid.value mustEqual expectedResult
        }
      }
    }

    "must return errors when reference data cannot be retrieved" in {

      val referenceDataService = mock[ReferenceDataService]

      when(referenceDataService.countries()(any(), any()))
        .thenReturn(Future.successful(ReferenceDataRetrievalError("countries", 500, "body").invalidNec))

      when(referenceDataService.kindsOfPackage()(any(), any()))
        .thenReturn(Future.successful(ReferenceDataRetrievalError("kindsOfPackage", 501, "body").invalidNec))

      when(referenceDataService.documentTypes()(any(), any()))
        .thenReturn(Future.successful(ReferenceDataRetrievalError("documentTypes", 502, "body").invalidNec))

      when(referenceDataService.additionalInformation()(any(), any()))
        .thenReturn(Future.successful(ReferenceDataRetrievalError("additionalInformation", 503, "body").invalidNec))

      val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

      val result = service.toViewModel(validModel, "mrn").futureValue

      val expectedErrors = Seq(
        ReferenceDataRetrievalError("countries", 500, "body"),
        ReferenceDataRetrievalError("kindsOfPackage", 501, "body"),
        ReferenceDataRetrievalError("documentTypes", 502, "body"),
        ReferenceDataRetrievalError("additionalInformation", 503, "body")
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }

    "must return errors when reference data can be retrieved but the conversion fails" in {

      val referenceDataService = mock[ReferenceDataService]
      when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(countries))
      when(referenceDataService.kindsOfPackage()(any(), any())) thenReturn Future.successful(Valid(kindsOfPackage))
      when(referenceDataService.documentTypes()(any(), any())) thenReturn Future.successful(Valid(documentTypes))
      when(referenceDataService.additionalInformation()(any(), any())) thenReturn Future.successful(Valid(additionalInfo))

      val service = new TransitAccompanyingDocumentConversionService(referenceDataService)

      val invalidRefData = validModel copy (countryOfDispatch = Some("non-existent code"))

      val result = service.toViewModel(invalidRefData, "mrn").futureValue

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("countryOfDispatch", "non-existent code"))
    }
  }
}
