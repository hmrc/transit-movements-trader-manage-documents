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
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Environment
import viewmodels.DummyData

import java.nio.file.{Files, Paths}

class UnloadingPermissionPdfGeneratorSpec extends SpecBase with Matchers with GuiceOneAppPerSuite with OptionValues with ScalaFutures with DummyData {

  private lazy val service: UnloadingPermissionPdfGenerator = app.injector.instanceOf[UnloadingPermissionPdfGenerator]
  val env: Environment                                      = app.injector.instanceOf[Environment]

  "UnloadingPermissionPdfGenerator" - {

    "must match with the 'Unloading Permission' template" in {

      val pdfPath          = "test/resources/documents/unloadingpermission/sample.pdf"
      val pdf: Array[Byte] = Files.readAllBytes(Paths.get(pdfPath))

      val pdfDocument: PDDocument = Loader.loadPDF(service.generate(cc043c))
      // pdfDocument.save(pdfPath)

      val expectedPdfDocument: PDDocument = Loader.loadPDF(pdf)

      try {
        val pdfData         = new PDFTextStripper().getText(pdfDocument)
        val expectedPdfData = new PDFTextStripper().getText(expectedPdfDocument)

        val mrn                 = cc043c.TransitOperation.MRN
        val documentInformation = pdfDocument.getDocumentInformation
        documentInformation.getAuthor mustEqual "HMRC"
        documentInformation.getTitle mustEqual s"Unloading permission document for MRN $mrn"

        pdfData mustEqual expectedPdfData
      } finally {
        pdfDocument.close()
        expectedPdfDocument.close()
      }
    }
  }

}
