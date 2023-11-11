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

import com.dmanchester.playfop.sapi.PlayFop
import generated.p5.CC015CType
import generated.p5.CC029CType
import models.reference.Country
import org.apache.xmlgraphics.util.MimeConstants
import play.twirl.api.XmlFormat
import refactor.viewmodels.p5.tad.P5TadPdfViewModel
import refactor.views.xml.p5.tad.TransitAccompanyingDocumentP5

import javax.inject.Inject

class TADPdfGenerator @Inject() (
  fop: PlayFop,
  documentP5: TransitAccompanyingDocumentP5
) {

  def generateP5TADPostTransition(ie029: CC029CType): Array[Byte] =
    processDocument(documentP5.render(P5TadPdfViewModel(ie029)))

  def generateP5TADTransition(ie015: CC015CType, ie029: CC029CType, countries: Seq[Country]): Array[Byte] =
    ???

  private def processDocument(document: XmlFormat.Appendable): Array[Byte] =
    fop.processTwirlXml(document, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
}
