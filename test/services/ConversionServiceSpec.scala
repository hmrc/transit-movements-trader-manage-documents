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

package services

import java.time.LocalDate

import cats.data.NonEmptyList
import cats.implicits._
import cats.data.Validated.Valid
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.DeclarationType
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ConversionServiceSpec
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

  private val validUnloadingPermission = models.PermissionToStartUnloading(
    movementReferenceNumber = "mrn",
    declarationType = DeclarationType.T1,
    transportIdentity = Some("identity"),
    transportCountry = Some(countries.head.code),
    acceptanceDate = LocalDate.now(),
    numberOfItems = 1,
    numberOfPackages = 3,
    grossMass = 1.0,
    principal =
      models.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", countries.head.code, Some("Principal EORI"), None),
    traderAtDestination = models.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
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
        countryOfDispatch = countries.head.code,
        countryOfDestination = countries.head.code,
        producedDocuments = Seq(models.ProducedDocument(documentTypes.head.code, None, None)),
        specialMentions = Seq(
          models.SpecialMentionEc(additionalInfo.head.code),
          models.SpecialMentionNonEc(additionalInfo.head.code, countries.head.code),
          models.SpecialMentionNoCountry(additionalInfo.head.code)
        ),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head.code, None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head.code, None)),
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

  "convertUnloadingPermission" - {

    "must return a view model" in {

      val referenceDataService = mock[ReferenceDataService]
      when(referenceDataService.countries()(any(), any())) thenReturn Future.successful(Valid(countries))
      when(referenceDataService.kindsOfPackage()(any(), any())) thenReturn Future.successful(Valid(kindsOfPackage))
      when(referenceDataService.documentTypes()(any(), any())) thenReturn Future.successful(Valid(documentTypes))
      when(referenceDataService.additionalInformation()(any(), any())) thenReturn Future.successful(Valid(additionalInfo))

      val service = new ConversionService(referenceDataService)

      val expectedResult = viewmodels.PermissionToStartUnloading(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        transportIdentity = Some("identity"),
        transportCountry = Some(countries.head),
        acceptanceDate = LocalDate.now(),
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal =
          viewmodels.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", countries.head, Some("Principal EORI"), None),
        traderAtDestination = viewmodels.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
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
            countryOfDispatch = countries.head,
            countryOfDestination = countries.head,
            producedDocuments = Seq(viewmodels.ProducedDocument(documentTypes.head, None, None)),
            specialMentions = Seq(
              viewmodels.SpecialMentionEc(additionalInfo.head),
              viewmodels.SpecialMentionNonEc(additionalInfo.head, countries.head),
              viewmodels.SpecialMentionNoCountry(additionalInfo.head)
            ),
            consignor = Some(viewmodels.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
            consignee = Some(viewmodels.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
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

      val result = service.convertUnloadingPermission(validUnloadingPermission).futureValue

      result.valid.value mustEqual expectedResult
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

      val service = new ConversionService(referenceDataService)

      val result = service.convertUnloadingPermission(validUnloadingPermission).futureValue

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

      val service = new ConversionService(referenceDataService)

      val invalidUnloadingPermission = validUnloadingPermission copy (transportCountry = Some("non-existent code"))

      val result = service.convertUnloadingPermission(invalidUnloadingPermission).futureValue

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("transportCountry", "non-existent code"))
    }
  }
}
