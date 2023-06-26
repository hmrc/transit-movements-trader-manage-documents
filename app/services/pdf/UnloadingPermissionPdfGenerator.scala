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

import com.dmanchester.playfop.sapi.PlayFop
import models.P5.unloading.IE043Data
import org.apache.xmlgraphics.util.MimeConstants
import viewmodels.PermissionToStartUnloading
import views.xml.UnloadingPermissionDocument
import views.xml.UnloadingPermissionDocumentP5

import javax.inject.Inject

class UnloadingPermissionPdfGenerator @Inject() (
  fop: PlayFop,
  document: UnloadingPermissionDocument,
  documentP5: UnloadingPermissionDocumentP5
) {

  def generate(permission: PermissionToStartUnloading): Array[Byte] = {

    val renderedDocument = document.render(permission)

    fop.processTwirlXml(renderedDocument, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
  }

  def generateP5(ie043Data: IE043Data, mrn: String): Array[Byte] = {

    val renderedDocument = documentP5.render(ie043Data, mrn)

    fop.processTwirlXml(renderedDocument, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
  }

}
