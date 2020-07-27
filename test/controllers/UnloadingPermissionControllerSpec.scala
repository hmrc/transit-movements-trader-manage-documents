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

package controllers

import java.time.LocalDate

import cats.data.NonEmptyChain
import cats.data.NonEmptyList
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import models.DeclarationType
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.ConversionService
import services.JsonError
import services.ReferenceDataNotFound
import services.ReferenceDataRetrievalError

import scala.concurrent.Future

class UnloadingPermissionControllerSpec extends FreeSpec with MustMatchers with GuiceOneAppPerSuite with OptionValues with MockitoSugar {

  private val validPayload = models.PermissionToStartUnloading(
    movementReferenceNumber = "mrn",
    declarationType = DeclarationType.T1,
    transportIdentity = Some("identity"),
    transportCountry = Some("AA"),
    acceptanceDate = LocalDate.now(),
    numberOfItems = 1,
    numberOfPackages = 3,
    grossMass = 1.0,
    principal = models.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", "AA", Some("Principal EORI"), Some("tir")),
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
        countryOfDispatch = "AA",
        countryOfDestination = "AA",
        producedDocuments = Seq(models.ProducedDocument("DD", None, None)),
        specialMentions = Seq(
          models.SpecialMentionEc("CC"),
          models.SpecialMentionNonEc("CC", "AA"),
          models.SpecialMentionNoCountry("CC")
        ),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", "AA", None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", "AA", None)),
        containers = Seq("container 1"),
        packages = NonEmptyList.one(models.RegularPackage("BB", 1, "marks and numbers"))
      )
    )
  )

  "post" - {

    "must return OK when the conversion service can convert the payload to a viewmodel" in {

      val country               = Country("valid", "AA", "Country")
      val kindOfPackage         = KindOfPackage("BB", "Kind of package")
      val additionalInformation = AdditionalInformation("CC", "Additional information")
      val documentType          = DocumentType("DD", "Document type", true)

      val viewmodel = viewmodels.PermissionToStartUnloading(
        movementReferenceNumber = "mrn",
        declarationType = DeclarationType.T1,
        transportIdentity = Some("identity"),
        transportCountry = Some(country),
        acceptanceDate = LocalDate.now(),
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal = viewmodels.Principal("Principal name", "Principal street", "Principal postCode", "Principal city", country, Some("Principal EORI")),
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
            countryOfDispatch = country,
            countryOfDestination = country,
            producedDocuments = Seq(viewmodels.ProducedDocument(documentType, None, None)),
            specialMentions = Seq(
              viewmodels.SpecialMentionEc(additionalInformation),
              viewmodels.SpecialMentionNonEc(additionalInformation, country),
              viewmodels.SpecialMentionNoCountry(additionalInformation)
            ),
            consignor = Some(viewmodels.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", country, None)),
            consignee = Some(viewmodels.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", country, None)),
            containers = Seq("container 1"),
            packages = NonEmptyList.one(viewmodels.RegularPackage(kindOfPackage, 1, "marks and numbers"))
          )
        )
      )

      val conversionService = mock[ConversionService]
      when(conversionService.convertUnloadingPermission(any())(any(), any())) thenReturn Future.successful(Valid(viewmodel))

      val request = FakeRequest(POST, routes.UnloadingPermissionController.post().url).withJsonBody(Json.toJson(validPayload))

      val application =
        new GuiceApplicationBuilder()
          .overrides(bind[ConversionService].toInstance(conversionService))
          .build()

      val result = route(application, request).value

      status(result) mustEqual OK

      application.stop()
    }

    "must return Bad Request when the payload cannot be read as a Permission To Start Unloading" in {

      val conversionService = mock[ConversionService]

      val request = FakeRequest(POST, routes.UnloadingPermissionController.post().url).withJsonBody(Json.toJson("foo" -> "bar"))

      val application =
        new GuiceApplicationBuilder()
          .overrides(bind[ConversionService].toInstance(conversionService))
          .build()

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      application.stop()
    }

    "must return Internal Server Error when the conversion service returns any Json errors, even if other types of error are present" in {

      val errors = Invalid(
        NonEmptyChain(
          JsonError("countries", Seq.empty),
          ReferenceDataRetrievalError("countries", 500, "body"),
          ReferenceDataNotFound("path", "value")
        ))

      val conversionService = mock[ConversionService]
      when(conversionService.convertUnloadingPermission(any())(any(), any())) thenReturn Future.successful(errors)

      val request = FakeRequest(POST, routes.UnloadingPermissionController.post().url).withJsonBody(Json.toJson(validPayload))

      val application =
        new GuiceApplicationBuilder()
          .overrides(bind[ConversionService].toInstance(conversionService))
          .build()

      val result = route(application, request).value

      status(result) mustEqual INTERNAL_SERVER_ERROR

      application.stop()
    }

    "must return Bad Gateway when the conversion service returns any Reference Data Retrieval errors and no Json Errors, even if other types of error are present" in {

      val errors = Invalid(
        NonEmptyChain(
          ReferenceDataRetrievalError("countries", 500, "body"),
          ReferenceDataNotFound("path", "value")
        ))

      val conversionService = mock[ConversionService]
      when(conversionService.convertUnloadingPermission(any())(any(), any())) thenReturn Future.successful(errors)

      val request = FakeRequest(POST, routes.UnloadingPermissionController.post().url).withJsonBody(Json.toJson(validPayload))

      val application =
        new GuiceApplicationBuilder()
          .overrides(bind[ConversionService].toInstance(conversionService))
          .build()

      val result = route(application, request).value

      status(result) mustEqual BAD_GATEWAY

      application.stop()
    }

    "must return Bad Request when the conversion service returns only Reference Data Not Found errors" in {

      val errors = Invalid(
        NonEmptyChain(
          ReferenceDataNotFound("path", "value"),
          ReferenceDataNotFound("another path", "another value")
        ))

      val conversionService = mock[ConversionService]
      when(conversionService.convertUnloadingPermission(any())(any(), any())) thenReturn Future.successful(errors)

      val request = FakeRequest(POST, routes.UnloadingPermissionController.post().url).withJsonBody(Json.toJson(validPayload))

      val application =
        new GuiceApplicationBuilder()
          .overrides(bind[ConversionService].toInstance(conversionService))
          .build()

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      application.stop()
    }
  }
}
