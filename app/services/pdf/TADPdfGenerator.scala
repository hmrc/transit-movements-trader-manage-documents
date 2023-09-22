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
import models.P5.departure.IE029Data
import org.apache.xmlgraphics.util.MimeConstants
import services.conversion.TransitAccompanyingDocumentConversionService
import viewmodels.TransitAccompanyingDocumentP5TransitionPDF
import viewmodels.TransitAccompanyingDocumentPDF
import views.xml.TransitAccompanyingDocument
import views.xml.TransitAccompanyingDocumentP5
import views.xml.TransitAccompanyingDocumentP5Transition
import viewmodels.P5._

import javax.inject.Inject

class TADPdfGenerator @Inject() (
  fop: PlayFop,
  document: TransitAccompanyingDocument,
  documentP5: TransitAccompanyingDocumentP5,
  documentTransitionP5: TransitAccompanyingDocumentP5Transition
) {

  def generate(permission: TransitAccompanyingDocumentPDF): Array[Byte] = {

    val renderedDocument = document.render(permission)

    fop.processTwirlXml(renderedDocument, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
  }

  def generateP5TADPostTransition(ie029Data: IE029Data): Array[Byte] = {

    val renderedDocument = documentP5.render(TableViewModel()(ie029Data), ConsignmentItemViewModel()(ie029Data))

    fop.processTwirlXml(renderedDocument, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
  }

  def generateP5TADTransition(permission: TransitAccompanyingDocumentP5TransitionPDF): Array[Byte] = {

    val renderedDocument = documentTransitionP5.render(permission)

    fop.processTwirlXml(renderedDocument, MimeConstants.MIME_PDF, autoDetectFontsForPDF = true)
  }

}
