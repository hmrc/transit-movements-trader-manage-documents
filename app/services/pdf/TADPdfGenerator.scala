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

import generated.rfc37.CC015CType
import generated.rfc37.CC029CType
import viewmodels.tad._
import views.xml.tad.posttransition.TransitAccompanyingDocumentP5
import views.xml.tad.transition.TransitAccompanyingDocument

import javax.inject.Inject

class TADPdfGenerator @Inject() (
  fopService: FopService,
  documentP5: TransitAccompanyingDocumentP5,
  documentP5Transition: TransitAccompanyingDocument
) {

  def generateP5TADPostTransition(ie015: CC015CType, ie029: CC029CType): Array[Byte] =
    fopService.processTwirlXml(documentP5.render(posttransition.TadPdfViewModel(ie015, ie029)))

  def generateP5TADTransition(ie015: CC015CType, ie029: CC029CType): Array[Byte] =
    fopService.processTwirlXml(documentP5Transition.render(transition.TadPdfViewModel(ie015, ie029)))
}
