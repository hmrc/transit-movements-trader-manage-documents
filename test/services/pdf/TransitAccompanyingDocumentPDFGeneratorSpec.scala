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
import models.DeclarationType
import models.GuaranteeDetails
import models.GuaranteeReference
import models.PreviousAdministrativeReference
import models.reference._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks.forAll
import play.api.Application
import play.api.Environment
import play.api.inject
import play.api.inject.guice.GuiceApplicationBuilder
import services.pdf.TransitAccompanyingDocumentPDFGeneratorSpec.transitAccompanyingDocumentPDF
import utils.FormattedDate
import viewmodels._
import views.xml.components._

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime

class TransitAccompanyingDocumentPDFGeneratorSpec
    extends AnyFreeSpec
    with Matchers
    with GuiceOneAppPerSuite
    with ViewmodelGenerators
    with OptionValues
    with ScalaFutures {

  // Spying on the tables to ensure the right values are passed in
  lazy val spiedTable1: table_1.table    = Mockito.spy(new table_1.table())
  lazy val spiedTable2: table_2.table    = Mockito.spy(new table_2.table())
  lazy val spiedTable3: table_3.table    = Mockito.spy(new table_3.table())
  lazy val spiedTable4: table_4.table    = Mockito.spy(new table_4.table())
  lazy val spiedTable5: table_5.table    = Mockito.spy(new table_5.table())
  lazy val spiedPage2: second_page.table = Mockito.spy(new second_page.table())

  implicit override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(
      inject.bind(classOf[table_1.table]).toInstance(spiedTable1),
      inject.bind(classOf[table_2.table]).toInstance(spiedTable2),
      inject.bind(classOf[table_3.table]).toInstance(spiedTable3),
      inject.bind(classOf[table_4.table]).toInstance(spiedTable4),
      inject.bind(classOf[table_5.table]).toInstance(spiedTable5),
      inject.bind(classOf[second_page.table]).toInstance(spiedPage2)
    )
    .build()

  private lazy val service: TADPdfGenerator = app.injector.instanceOf[TADPdfGenerator]
  val env: Environment                      = app.injector.instanceOf[Environment]

  "TransitAccompanyingDocumentPDFGenerator" - {

    "return pdf" in {

      forAll(arb[TransitAccompanyingDocumentPDF]) {
        tad =>
          service.generate(tad) mustBe an[Array[Byte]]

          verify(spiedTable1, times(1))
            .apply(
              "TRANSIT - ACCOMPANYING DOCUMENT",
              boldHeading = true,
              tad.movementReferenceNumber,
              printVariousConsignors = tad.printVariousConsignors,
              tad.consignorOne,
              tad.declarationType,
              printListOfItems = tad.printListOfItems,
              tad.consignor,
              tad.numberOfItems,
              tad.totalNumberOfPackages,
              printVariousConsignees = tad.printVariousConsignees,
              tad.consigneeOne,
              tad.singleCountryOfDispatch,
              tad.singleCountryOfDestination,
              tad.transportIdentity,
              tad.transportCountry,
              tad.returnCopiesCustomsOffice
            )

          verify(spiedTable2, times(1))
            .apply(
              tad.printListOfItems,
              tad.numberOfItems,
              tad.goodsItems,
              tad.grossMass,
              tad.printBindingItinerary
            )

          verify(spiedTable3, times(1)).apply()

          verify(spiedTable4, times(1))
            .apply(
              tad.principal,
              tad.departureOffice.reference,
              tad.acceptanceDate.map(_.formattedDate),
              tad.customsOfficeOfTransit,
              tad.guaranteeDetails,
              Some(tad.destinationOffice),
              tad.authId,
              tad.controlResult
            )

          verify(spiedTable5, times(1))
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
              tad.declarationType
            )

          reset(spiedTable1, spiedTable2, spiedTable3, spiedTable4, spiedTable5)
      }
    }

    "must match with the 'Transit Accompanying Document' template" in {

      val pdfPath          = Paths.get("test/resources/transit-accompanying-document-pdf")
      val pdf: Array[Byte] = Files.readAllBytes(pdfPath)

      val pdfDocument: PDDocument         = PDDocument.load(service.generate(transitAccompanyingDocumentPDF))
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

object TransitAccompanyingDocumentPDFGeneratorSpec {

  private lazy val arbitraryDescription = Gen.oneOf(None, Some("T2")).sample.get

  val transitAccompanyingDocumentPDF: TransitAccompanyingDocumentPDF = TransitAccompanyingDocumentPDF(
    "21GB00006010025BD1",
    DeclarationType.TMinus,
    Some(Country("GB", "United Kingdom")),
    Some(Country("IT", "Italy")),
    Some("TAD ID Departure"),
    Some(Country("GB", "United Kingdom")),
    Some(FormattedDate(LocalDate.parse("2021-03-12"))),
    3,
    Some(12),
    12000,
    printBindingItinerary = false,
    None,
    copyType = false,
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
    None,
    None,
    CustomsOfficeWithOptionalDate(CustomsOffice("GB000061", Some("DUNDEE 1"), "GB"), None),
    CustomsOfficeWithOptionalDate(CustomsOffice("IEDUB919", Some("Dublin Mail Centre"), "IE"), None, 45),
    List(
      CustomsOfficeWithOptionalDate(CustomsOffice("FR001260", Some("Dunkerque port bureau"), "FR"), Some(LocalDateTime.parse("2021-03-13T10:00")), 18),
      CustomsOfficeWithOptionalDate(CustomsOffice("IT277100", Some("MILANO 1"), "IT"), Some(LocalDateTime.parse("2021-03-14T11:00")), 18)
    ),
    List(GuaranteeDetails("3", List(GuaranteeReference(None, Some("TAD001"), None, List())))),
    List("TAD_Seals_001", "TAD_Seals_002", "TAD_Seals_003"),
    Some(
      ReturnCopiesCustomsOffice("Central Community Transit Office", "BT-CCTO, HM Revenue and Customs", "BX9 1EH", "SALFORD", Country("GB", "United Kingdom"))
    ),
    Some(ControlResult(ControlResultData("A2", "Considered satisfactory"), models.ControlResult("A2", LocalDate.parse("2021-03-14")))),
    NonEmptyList.of(
      GoodsItem(
        1,
        None,
        Some(DeclarationType.T1),
        "Snow sports",
        Some(4000),
        Some(3996),
        None,
        None,
        None,
        None,
        None,
        List(ProducedDocument(DocumentType("235", "Container list", transportDocument = true), Some("TAD001"), None)),
        List(PreviousDocumentType(PreviousDocumentTypes("T2", arbitraryDescription), PreviousAdministrativeReference("T2", "TAD001", None))),
        List(SpecialMention(AdditionalInformation("DG1", "Export subject to duties"), models.SpecialMention(Some("TAD001"), "DG1", None, Some("GB")))),
        Some(
          Consignor(
            "QUIRM ENGINEERING",
            "125 Psuedopolis",
            "125 Psuedopolis",
            "SS99 1AA",
            "Ank-Morpork",
            Country("GB", "United Kingdom"),
            Some("GB60207001070000")
          )
        ),
        Some(
          Consignee("DROFYL POTTERY", "125 Psuedopolis", "125 Psuedopolis", "SS99 1AA", "Ank-Morpork", Country("GB", "United Kingdom"), Some("GB658120050000"))
        ),
        List(),
        NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 4, "RedBull snow sports")),
        Vector(),
        None,
        None
      ),
      GoodsItem(
        2,
        None,
        Some(DeclarationType.T2),
        "Snow Sports",
        Some(4000),
        Some(3996),
        None,
        None,
        None,
        None,
        None,
        List(ProducedDocument(DocumentType("235", "Container list", transportDocument = true), Some("TAD001"), None)),
        List(PreviousDocumentType(PreviousDocumentTypes("T2", arbitraryDescription), PreviousAdministrativeReference("T2", "TAD001", None))),
        List(),
        Some(
          Consignor(
            "QUIRM ENGINEERING",
            "125 Psuedopolis",
            "125 Psuedopolis",
            "SS99 1AA",
            "Ank-Morpork",
            Country("GB", "United Kingdom"),
            Some("GB60207001070000")
          )
        ),
        Some(
          Consignee("DROFYL POTTERY", "125 Psuedopolis", "125 Psuedopolis", "SS99 1AA", "Ank-Morpork", Country("GB", "United Kingdom"), Some("GB658120050000"))
        ),
        List(),
        NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 4, "RedBull snow sports")),
        Vector(),
        None,
        None
      ),
      GoodsItem(
        3,
        None,
        Some(DeclarationType.T2),
        "Snow Sports",
        Some(4000),
        Some(3996),
        None,
        None,
        None,
        None,
        None,
        List(ProducedDocument(DocumentType("730", "Road consignment note", transportDocument = true), Some("TAD001"), None)),
        List(PreviousDocumentType(PreviousDocumentTypes("T2", arbitraryDescription), PreviousAdministrativeReference("T2", "TAD001", None))),
        List(),
        Some(
          Consignor("DROFYL POTTERY", "125 Psuedopolis", "125 Psuedopolis", "SS99 1AA", "Ank-Morpork", Country("GB", "United Kingdom"), Some("GB658120050000"))
        ),
        Some(
          Consignee(
            "QUIRM ENGINEERING",
            "125 Psuedopolis",
            "125 Psuedopolis",
            "SS99 1AA",
            "Ank-Morpork",
            Country("GB", "United Kingdom"),
            Some("GB60207001070000")
          )
        ),
        List(),
        NonEmptyList.of(RegularPackage(KindOfPackage("BX", "Box"), 4, "RedBull snow sports")),
        Vector(),
        None,
        None
      )
    )
  )
}
