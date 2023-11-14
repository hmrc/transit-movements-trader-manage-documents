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

package controllers.P5

import config.PhaseConfig
import controllers.actions.AuthenticateActionProvider
import generated.p5.CC015CType
import generated.p5.CC029CType
import models.P5.Phase.PostTransition
import models.P5.Phase.Transition
import play.api.Logging
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import refactor.services.pdf.TADPdfGenerator
import services.P5.DepartureMessageP5Service
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.FileNameSanitizer

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class TransitAccompanyingDocumentP5Controller @Inject() (
  pdf: TADPdfGenerator,
  service: DepartureMessageP5Service,
  authenticate: AuthenticateActionProvider,
  cc: ControllerComponents
)(implicit ec: ExecutionContext, phaseConfig: PhaseConfig)
    extends BackendController(cc)
    with Logging {

  def get(departureId: String, messageId: String): Action[AnyContent] = authenticate().async {
    implicit request =>
      service.getIE015MessageId(departureId).flatMap {
        case Some(ie015MessageId) =>
          for {
            ie015 <- service.getMessage[CC015CType](departureId, ie015MessageId)
            ie029 <- service.getMessage[CC029CType](departureId, messageId)
          } yield {
            val bytes = phaseConfig.phase match {
              case PostTransition => pdf.generateP5TADPostTransition(ie029)
              case Transition     => pdf.generateP5TADTransition(ie015, ie029, Seq.empty)
            }
            val fileName = s"TAD_${FileNameSanitizer(ie029.TransitOperation.MRN)}.pdf"
            Ok(bytes)
              .withHeaders(
                CONTENT_TYPE        -> "application/pdf",
                CONTENT_DISPOSITION -> s"""attachment; filename="$fileName""""
              )
          }
        case None =>
          logger.error(s"Failed to find IE015 message for departure $departureId")
          Future.successful(InternalServerError)
      }
  }
}
