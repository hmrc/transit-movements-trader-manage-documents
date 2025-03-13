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

import generated.CC029CType
import models.IE015
import viewmodels.tad.TadPdfViewModel
import views.xml.tad.TransitAccompanyingDocument

import javax.inject.Inject

class TADPdfGenerator @Inject() (
  fopService: FopService,
  document: TransitAccompanyingDocument
) {

  def generate(ie015: IE015, ie029: CC029CType): Array[Byte] =
    fopService.processTwirlXml(document.render(TadPdfViewModel(ie015, ie029)))
}
