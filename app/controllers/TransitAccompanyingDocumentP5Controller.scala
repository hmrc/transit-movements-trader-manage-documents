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

package controllers

import connectors.DepartureMovementP5Connector
import controllers.actions.AuthenticateActionProvider
import play.api.Logging
import play.api.libs.ws.WSResponse
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import services.P5.DepartureMessageP5Service
import services.pdf.TADPdfGenerator
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.FileNameSanitizer

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TransitAccompanyingDocumentP5Controller @Inject() (
  pdf: TADPdfGenerator,
  service: DepartureMessageP5Service,
  authenticate: AuthenticateActionProvider,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def get(departureId: String, messageId: String): Action[AnyContent] = authenticate().async {
    implicit request =>
      service.getDepartureNotificationMessage(departureId, messageId).map {
        departureNotificationMessage =>
          val fileName = s"TAD_${FileNameSanitizer(departureNotificationMessage.movementReferenceNumber.value)}.pdf"

          Ok(pdf.generateP5TAD(departureNotificationMessage.data))
            .withHeaders(
              CONTENT_TYPE        -> "application/pdf",
              CONTENT_DISPOSITION -> s"""attachment; filename="$fileName""""
            )
      }
  }

//  def get(departureId: String, messageId: String): Action[AnyContent] = Action {
//    implicit request =>
//      val fileName = s"TAD_${FileNameSanitizer("TESTMRN")}.pdf"
//
//      Ok(pdf.generateP5())
//        .withHeaders(
//          CONTENT_TYPE        -> "application/pdf",
//          CONTENT_DISPOSITION -> s"""attachment; filename="$fileName""""
//        )
//  }
}