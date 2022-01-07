/*
 * Copyright 2022 HM Revenue & Customs
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
import org.apache.xmlgraphics.util.MimeConstants
import viewmodels.PermissionToStartUnloading
import views.xml.UnloadingPermissionDocument

import javax.inject.Inject

class UnloadingPermissionPdfGenerator @Inject() (
  fop: PlayFop,
  document: UnloadingPermissionDocument
) {

  def generate(permission: PermissionToStartUnloading): Array[Byte] = {

    val renderedDocument = document.render(permission)

    fop.processTwirlXml(renderedDocument, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
  }

}
