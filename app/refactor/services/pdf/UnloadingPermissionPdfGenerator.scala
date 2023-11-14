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
import generated.p5.CC043CType
import org.apache.xmlgraphics.util.MimeConstants
import play.twirl.api.XmlFormat
import refactor.viewmodels.p5.unloadingpermission.P5UnloadingPermissionPdfViewModel
import refactor.views.xml.p5.unloadingpermission.UnloadingPermissionDocumentP5

import javax.inject.Inject

class UnloadingPermissionPdfGenerator @Inject() (
  fop: PlayFop,
  documentP5: UnloadingPermissionDocumentP5
) {

  def generateP5(ie043: CC043CType): Array[Byte] =
    processDocument(documentP5.render(P5UnloadingPermissionPdfViewModel(ie043)))

  private def processDocument(document: XmlFormat.Appendable): Array[Byte] =
    fop.processTwirlXml(document, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)

}
