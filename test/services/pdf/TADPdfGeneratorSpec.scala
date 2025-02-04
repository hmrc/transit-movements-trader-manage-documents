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

import base.SpecBase
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Application, Environment}
import play.api.inject.guice.GuiceApplicationBuilder
import viewmodels.DummyData

import java.nio.file.{Files, Paths}

class TADPdfGeneratorSpec extends SpecBase with Matchers with GuiceOneAppPerSuite with OptionValues with ScalaFutures with DummyData {

  implicit override lazy val app: Application = GuiceApplicationBuilder()
    .build()

  private lazy val service: TADPdfGenerator = app.injector.instanceOf[TADPdfGenerator]
  val env: Environment                      = app.injector.instanceOf[Environment]

  "TransitAccompanyingDocumentPDFGenerator" - {

    "must match with the final 'Transit Accompanying Document' template" in {
      val pdfPath          = "test/resources/documents/tad/sample.pdf"
      val pdf: Array[Byte] = Files.readAllBytes(Paths.get(pdfPath))

      val pdfDocument: PDDocument = PDDocument.load(service.generate(cc015c, cc029c))
      // pdfDocument.save(pdfPath)

      val expectedPdfDocument: PDDocument = PDDocument.load(pdf)

      try {
        val pdfData         = new PDFTextStripper().getText(pdfDocument)
        val expectedPdfData = new PDFTextStripper().getText(expectedPdfDocument)

        pdfDocument.getDocumentInformation.getAuthor mustBe "HMRC"
        pdfData mustBe expectedPdfData
      } finally {
        pdfDocument.close()
        expectedPdfDocument.close()
      }
    }
  }

}
