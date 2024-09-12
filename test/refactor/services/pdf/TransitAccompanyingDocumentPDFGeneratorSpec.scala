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

package refactor.services.pdf

import base.SpecBase
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.Environment
import play.api.inject
import play.api.inject.guice.GuiceApplicationBuilder
import refactor.viewmodels.DummyData
import refactor.viewmodels.p4.tad.SecurityViewModel
import refactor.views.xml.p4.tad.components.security

import java.nio.file.Files
import java.nio.file.Paths

class TransitAccompanyingDocumentPDFGeneratorSpec extends SpecBase with Matchers with GuiceOneAppPerSuite with OptionValues with ScalaFutures with DummyData {

  lazy val spiedTable1: security.table_1.table = Mockito.spy[security.table_1.table](new security.table_1.table())
  lazy val spiedTable2: security.table_2.table = Mockito.spy[security.table_2.table](new security.table_2.table())
  lazy val spiedTable3: security.table_3.table = Mockito.spy[security.table_3.table](new security.table_3.table())

  implicit override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(
      inject.bind(classOf[security.table_1.table]).toInstance(spiedTable1),
      inject.bind(classOf[security.table_2.table]).toInstance(spiedTable2),
      inject.bind(classOf[security.table_3.table]).toInstance(spiedTable3)
    )
    .build()

  private lazy val service: TADPdfGenerator = app.injector.instanceOf[TADPdfGenerator]
  val env: Environment                      = app.injector.instanceOf[Environment]

  "TransitAccompanyingDocumentPDFGenerator" - {

    "return valid xml for the security tables" in {
      service.generateP5TADTransition(cc015c, cc029c) mustBe an[Array[Byte]]

      val sec: SecurityViewModel = SecurityViewModel.apply(cc029c)

      verify(spiedTable1, times(1))
        .apply(
          vm = sec,
          mrn = cc029c.TransitOperation.MRN,
          Some(cc029c.TransitOperation.LRN)
        )

      verify(spiedTable2, times(1))
        .apply(vm = sec)

      verify(spiedTable3, times(1))
        .apply(vm = sec)
      reset(spiedTable1, spiedTable2, spiedTable3)
    }

    "must match with the 'Transit Accompanying Document' template with the security page" in {

      val pdfPath          = Paths.get("test/resources/refactor/transit-accompanying-document-pdf-with-security-page.pdf")
      val pdf: Array[Byte] = Files.readAllBytes(pdfPath)

      val pdfDocument: PDDocument = PDDocument.load(service.generateP5TADTransition(cc015c, cc029c))

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

    "must match with the 'Transit Accompanying Document' template without the security page" in {

      val pdfPath            = Paths.get("test/resources/refactor/transit-accompanying-document-pdf-without-security-page.pdf")
      val pdf: Array[Byte]   = Files.readAllBytes(pdfPath)
      val cc029cSecurityZero = cc029c.copy(TransitOperation = cc029c.TransitOperation.copy(security = "0"))

      val pdfDocument: PDDocument = PDDocument.load(service.generateP5TADTransition(cc015c, cc029cSecurityZero))

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
