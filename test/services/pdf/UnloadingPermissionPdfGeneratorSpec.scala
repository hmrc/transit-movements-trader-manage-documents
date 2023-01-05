/*
 * Copyright 2023 HM Revenue & Customs
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

package services.pdf

import cats.data.NonEmptyList
import generators.ViewmodelGenerators
import models.DeclarationType
import models.SensitiveGoodsInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Environment
import services.pdf.UnloadingPermissionPdfGeneratorConstants.permissionToUnloadViewModel
import viewmodels._

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

class UnloadingPermissionPdfGeneratorSpec
    extends AnyFreeSpec
    with Matchers
    with GuiceOneAppPerSuite
    with ViewmodelGenerators
    with OptionValues
    with ScalaFutures {

  private lazy val service: UnloadingPermissionPdfGenerator = app.injector.instanceOf[UnloadingPermissionPdfGenerator]
  val env: Environment                                      = app.injector.instanceOf[Environment]

  "UnloadingPermissionPdfGenerator" - {

    "return pdf" in {

      val unloadingPermissionObject = arbitrary[PermissionToStartUnloading]

      val unloadingPermission = unloadingPermissionObject.sample.value

      service.generate(unloadingPermission) mustBe an[Array[Byte]]

    }

    "must match with the 'Unloading Permission' template" in {

      val pdfPath          = Paths.get("test/resources/unloading-permission-pdf")
      val pdf: Array[Byte] = Files.readAllBytes(pdfPath)

      val pdfDocument: PDDocument         = PDDocument.load(service.generate(permissionToUnloadViewModel))
      val expectedPdfDocument: PDDocument = PDDocument.load(pdf)

      try {
        val pdfData         = new PDFTextStripper().getText(pdfDocument)
        val expectedPdfData = new PDFTextStripper().getText(expectedPdfDocument)
        pdfData mustBe expectedPdfData
      } finally {
        pdfDocument.close()
        expectedPdfDocument.close()
      }
    }

  }
}

object UnloadingPermissionPdfGeneratorConstants {

  private val goodsItem = GoodsItem(
    itemNumber = 1,
    commodityCode = None,
    declarationType = None,
    description = "Flowers",
    grossMass = Some(1000),
    netMass = Some(999),
    countryOfDispatch = Some(Country("GB", "United Kingdom")),
    countryOfDestination = Some(Country("GB", "United Kingdom")),
    methodOfPayment = None,
    commercialReferenceNumber = None,
    unDangerGoodsCode = None,
    producedDocuments = List(ProducedDocument(DocumentType("18", "Movement certificate A.TR.1", false), Some("Ref."), None)),
    previousDocumentTypes = List(),
    specialMentions = List(),
    consignor = None,
    consignee = None,
    containers = List("container 1", "container 2"),
    packages = NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 1, "Ref.")),
    sensitiveGoodsInformation = Vector(SensitiveGoodsInformation(Some("1"), 1)),
    securityConsignor = None,
    securityConsignee = None
  )

  private val goodsItem1 = GoodsItem(
    itemNumber = 2,
    commodityCode = None,
    declarationType = None,
    description = "Vases",
    grossMass = Some(10000),
    netMass = Some(9999),
    countryOfDispatch = Some(Country("GB", "United Kingdom")),
    countryOfDestination = Some(Country("GB", "United Kingdom")),
    methodOfPayment = None,
    commercialReferenceNumber = None,
    unDangerGoodsCode = None,
    producedDocuments = List(ProducedDocument(DocumentType("18", "Movement certificate A.TR.1", false), Some("Ref."), None)),
    previousDocumentTypes = Nil,
    specialMentions = List(),
    consignor = Some(
      Consignor("Test Consignor Name", "Street name and number", "Street name and number", "PostCode", "City", Country("GB", "United Kingdom"), Some("AB123"))
    ),
    consignee = Some(
      Consignee("Tesy Consignee Name", "Street name and number", "Street name and number", "Postcode", "City", Country("GB", "United Kingdom"), Some("AB123"))
    ),
    containers = List("container 3", "container 4"),
    packages = NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 10, "Ref.")),
    sensitiveGoodsInformation = Vector(SensitiveGoodsInformation(Some("1"), 1)),
    securityConsignor = None,
    securityConsignee = None
  )

  val permissionToUnloadViewModel: PermissionToStartUnloading = PermissionToStartUnloading(
    "99IT9876AB88901209",
    DeclarationType.T1,
    Some(Country("IT", "Italy")),
    Some(Country("GB", "United Kingdom")),
    Some("abcd"),
    Some(Country("IT", "Italy")),
    Some(LocalDate.parse("2019-09-12")),
    Some("12/09/2019"),
    1,
    Some(1),
    1000,
    Principal("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("IT", "Italy"), Some("IT444100201000"), None),
    Some(Consignor("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("IT", "Italy"), Some("IT444100201000"))),
    Some(Consignee("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("IT", "Italy"), Some("IT444100201000"))),
    Some(
      TraderAtDestinationWithEori(
        "GB163910077000",
        Some("The Luggage Carriers"),
        Some("225 Suedopolish Yard"),
        Some("SS8 2BB"),
        Some(","),
        Some(Country("GB", "United Kingdom"))
      )
    ),
    "IT021100",
    "IT021100",
    Some("GB000060"),
    List("Seals01", "Seals02", "Seals03"),
    NonEmptyList.of(goodsItem, goodsItem1)
  )
}
