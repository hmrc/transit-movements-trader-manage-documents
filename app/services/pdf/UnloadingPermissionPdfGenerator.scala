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
import viewmodels.unloadingpermission.UnloadingPermissionPdfViewModel
import views.xml.unloadingpermission.UnloadingPermissionDocument

import javax.inject.Inject

class UnloadingPermissionPdfGenerator @Inject() (
  fopService: FopService,
  document: UnloadingPermissionDocument
) {

  def generateP5(ie043: CC043CType): Array[Byte] =
    fopService.processTwirlXml(document.render(UnloadingPermissionPdfViewModel(ie043)))
}
