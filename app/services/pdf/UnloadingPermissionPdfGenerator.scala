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

import generated.CC043CType
import viewmodels.unloadingpermission.P5UnloadingPermissionPdfViewModel
import views.xml.unloadingpermission.UnloadingPermissionDocumentP5

import javax.inject.Inject

class UnloadingPermissionPdfGenerator @Inject() (
  fopService: FopService,
  documentP5: UnloadingPermissionDocumentP5
) {

  def generateP5(ie043: CC043CType): Array[Byte] =
    fopService.processTwirlXml(documentP5.render(P5UnloadingPermissionPdfViewModel(ie043)))
}
