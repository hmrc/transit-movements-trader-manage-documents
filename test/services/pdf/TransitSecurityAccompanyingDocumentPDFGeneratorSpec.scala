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

package services.pdf

import cats.data.NonEmptyList
import generators.ViewmodelGenerators
import models.DeclarationType.T2
import models.DeclarationType
import models.GuaranteeDetails
import models.GuaranteeReference
import models.Itinerary
import models.PreviousAdministrativeReference
import models.reference._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks.forAll
import play.api.Application
import play.api.Environment
import play.api.inject
import play.api.inject.guice.GuiceApplicationBuilder
import services.pdf.TransitSecurityAccompanyingDocumentPDFGeneratorSpec.transitSecurityAccompanyingDocumentPDF
import utils.FormattedDate
import viewmodels._
import views.xml.securityComponents._

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime

class TransitSecurityAccompanyingDocumentPDFGeneratorSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with ViewmodelGenerators
    with OptionValues
    with ScalaFutures {

  def arb[A: Arbitrary]: Gen[A] = Arbitrary.arbitrary[A]

  // Spying on the tables to ensure the right values are passed in
  lazy val spiedTable1: table_1.table    = Mockito.spy(new table_1.table())
  lazy val spiedTable2: table_2.table    = Mockito.spy(new table_2.table())
  lazy val spiedTable3: table_3.table    = Mockito.spy(new table_3.table())
  lazy val spiedTable4: table_4.table    = Mockito.spy(new table_4.table())
  lazy val spiedPage2: second_page.table = Mockito.spy(new second_page.table())

  implicit override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(
      inject.bind(classOf[table_1.table]).toInstance(spiedTable1),
      inject.bind(classOf[table_2.table]).toInstance(spiedTable2),
      inject.bind(classOf[table_3.table]).toInstance(spiedTable3),
      inject.bind(classOf[table_4.table]).toInstance(spiedTable4),
      inject.bind(classOf[second_page.table]).toInstance(spiedPage2)
    )
    .build()

  private lazy val service: TSADPdfGenerator = app.injector.instanceOf[TSADPdfGenerator]
  val env: Environment                       = app.injector.instanceOf[Environment]

  "TransitSecurityAccompanyingDocumentPDFGenerator" - {

    "return pdf" in {

      forAll(arb[TransitSecurityAccompanyingDocumentPDF]) {
        tad =>
          service.generate(tad) mustBe an[Array[Byte]]

          verify(spiedTable1, times(1))
            .apply(
              "TRANSIT / SECURITY – ACCOMPANYING DOCUMENT",
              false,
              tad
            )

          verify(spiedTable2, times(1))
            .apply(
              tad.printVariousSecurityConsignors,
              tad.securityConsigneeOne,
              tad.printVariousSecurityConsignors,
              tad.securityConsignorOne,
              tad.secCarrierFlg,
              tad.safetyAndSecurityCarrier
            )

          verify(spiedTable3, times(1))
            .apply(
              tad.principal,
              tad.departureOffice.referenceWithOfficeId,
              tad.acceptanceDate.map(_.formattedDate),
              tad.customsOfficeOfTransit,
              tad.guaranteeDetails,
              Some(tad.destinationOffice),
              tad.authId,
              tad.controlResult
            )

          verify(spiedTable4, times(1))
            .apply(
              tad.seals,
              tad.printBindingItinerary,
              tad.controlResult
            )

          verify(spiedPage2, times(1))
            .apply(
              tad.departureOffice.office.id,
              tad.movementReferenceNumber,
              tad.acceptanceDate.map(_.formattedDate),
              tad.goodsItems,
              tad.declarationType,
              tad.placeOfUnloadingCode
            )

          reset(spiedTable1, spiedTable2, spiedTable3, spiedTable4)
      }
    }

    "must match with the 'Transit Security Accompanying Document' template" in {

      val pdfPath          = Paths.get("test/resources/transit-security-accompanying-document-pdf")
      val pdf: Array[Byte] = Files.readAllBytes(pdfPath)

      val pdfDocument: PDDocument         = PDDocument.load(service.generate(transitSecurityAccompanyingDocumentPDF))
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

object TransitSecurityAccompanyingDocumentPDFGeneratorSpec {

  val transitSecurityAccompanyingDocumentPDF: TransitSecurityAccompanyingDocumentPDF = TransitSecurityAccompanyingDocumentPDF(
    "21GB00006010025BE0",
    DeclarationType.TMinus,
    Some(Country("GB", "United Kingdom")),
    Some(Country("IT", "Italy")),
    Some("TSAD ID Departure"),
    Some(Country("GB", "United Kingdom")),
    Some(FormattedDate(LocalDate.parse("2021-03-12"))),
    3,
    Some(12),
    12000,
    false,
    None,
    false,
    Some("C"),
    Some(1),
    Some("TSAD001"),
    Some("B"),
    Some("TSAD ID Border"),
    Some("GB"),
    Some("30"),
    None,
    Some("DOVER"),
    Some("Bologna"),
    None,
    Principal(
      "CITY WATCH SHIPPING",
      "125 Psuedopolis Yard",
      "125 Psuedopolis Yard",
      "SS99 1AA",
      "Ank-Morpork",
      Country("GB", "United Kingdom"),
      Some("GB652420267000"),
      None
    ),
    Some(
      Consignor(
        "QUIRM ENGINEERING",
        "125 Psuedopolis Yard",
        "125 Psuedopolis Yard",
        "SS99 1AA",
        "Ank-Morpork",
        Country("GB", "United Kingdom"),
        Some("GB602070107000")
      )
    ),
    Some(
      Consignee(
        "DROFL POTTERY",
        "125 Psuedopolis Yard",
        "125 Psuedopolis Yard",
        "SS99 1AA",
        "Ank-Morpork",
        Country("GB", "United Kingdom"),
        Some("GB658120050000")
      )
    ),
    CustomsOfficeWithOptionalDate(CustomsOffice("GB000061", Some("DUNDEE 1"), "GB"), None),
    CustomsOfficeWithOptionalDate(CustomsOffice("IEDUB804", Some("Dun Laoghaire-Rathdown"), "IE"), None),
    List(
      CustomsOfficeWithOptionalDate(CustomsOffice("FR001260", Some("Dunkerque port bureau"), "FR"), Some(LocalDateTime.parse("2021-03-13T10:00")), 32, "***"),
      CustomsOfficeWithOptionalDate(CustomsOffice("IT277100", Some("MILANO 1"), "IT"), Some(LocalDateTime.parse("2021-03-14T11:00")), 32, "***")
    ),
    List(GuaranteeDetails("3", List(GuaranteeReference(None, Some("TSAD001"), None, List())))),
    List("TSAD_Seals_001", "TSAD_Seals_002", "TSAD_Seals_003", "TSAD_Seals_004", "TSAD_Seals_005", "TSAD_Seals_006"),
    Some(
      ReturnCopiesCustomsOffice("Central Community Transit Office", "BT-CCTO, HM Revenue and Customs", "BX9 1EH", "SALFORD", Country("GB", "United Kingdom"))
    ),
    Some(ControlResult(ControlResultData("A2", "Considered satisfactory"), models.ControlResult("A2", LocalDate.parse("2021-03-14")))),
    NonEmptyList.of(
      GoodsItem(
        1,
        None,
        Some(T2),
        "Jet Ski props",
        Some(4000),
        Some(3996),
        None,
        None,
        None,
        None,
        None,
        List(ProducedDocument(DocumentType("235", "Container list", true), Some("TSAD001"), None)),
        List(PreviousDocumentType(PreviousDocumentTypes("T2", "T2"), PreviousAdministrativeReference("T2", "TSAD001", None))),
        List(SpecialMention(AdditionalInformation("DG1", "Export subject to duties"), models.SpecialMention(Some("TSAD001"), "DG1", None, Some("GB")))),
        None,
        None,
        List("TSAD_Seals_001", "TSAD_Seals_002"),
        NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 4, "RedBull Water Sports")),
        Vector(),
        Some(SecurityConsignor(None, None, None, None, None, Some("GB658120050000"))),
        Some(SecurityConsignee(None, None, None, None, None, Some("GB602070107000")))
      ),
      GoodsItem(
        2,
        None,
        Some(T2),
        "Surf boards",
        Some(4000),
        Some(3996),
        None,
        None,
        None,
        None,
        None,
        List(ProducedDocument(DocumentType("730", "Road consignment note", true), Some("TSAD001"), None)),
        List(PreviousDocumentType(PreviousDocumentTypes("T2", "T2"), PreviousAdministrativeReference("T2", "TSAD001", None))),
        List(),
        None,
        None,
        List("TSAD_Seals_003", "TSAD_Seals_004"),
        NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 4, "RedBull Water Sports")),
        Vector(),
        Some(SecurityConsignor(None, None, None, None, None, Some("GB658120050000"))),
        Some(SecurityConsignee(None, None, None, None, None, Some("GB602070107000")))
      ),
      GoodsItem(
        3,
        None,
        Some(T2),
        "Kite surf boards",
        Some(4000),
        Some(3996),
        None,
        None,
        None,
        None,
        None,
        List(ProducedDocument(DocumentType("730", "Road consignment note", true), Some("TSAD001"), None)),
        List(PreviousDocumentType(PreviousDocumentTypes("T2", "T2"), PreviousAdministrativeReference("T2", "TSAD001", None))),
        List(),
        None,
        None,
        List(),
        NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 4, "RedBull Water Sports")),
        Vector(),
        Some(SecurityConsignor(None, None, None, None, None, Some("GB602070107000"))),
        Some(SecurityConsignee(None, None, None, None, None, Some("GB658120050000")))
      )
    ),
    List(Itinerary("GB"), Itinerary("FR"), Itinerary("IT")),
    Some(SafetyAndSecurityCarrier(None, None, None, None, None, Some("GB652420267000"))),
    None,
    None
  )
}
