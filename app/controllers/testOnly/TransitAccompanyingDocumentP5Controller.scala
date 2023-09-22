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

package controllers.testOnly

import cats.data.NonEmptyChain
import cats.data.Validated
import config.PhaseConfig
import config.PostTransitionConfig
import controllers.actions.AuthenticateActionProvider
import play.api.Logging
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import services.P5.DepartureMessageP5Service
import services.ValidationError
import services.conversion.TransitAccompanyingDocumentConversionService
import services.pdf.TADPdfGenerator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.FileNameSanitizer

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class TransitAccompanyingDocumentP5Controller @Inject() (
  pdf: TADPdfGenerator,
  service: DepartureMessageP5Service,
  authenticate: AuthenticateActionProvider,
  cc: ControllerComponents,
  conversionService: TransitAccompanyingDocumentConversionService
)(implicit ec: ExecutionContext, phaseConfig: PhaseConfig)
    extends BackendController(cc)
    with Logging {

  def get(departureId: String, messageId: String): Action[AnyContent] = authenticate().async {
    implicit request =>
      service.getReleaseForTransitNotification(departureId, messageId).flatMap {
        ie029 =>
          // TODO handle this better
          val genPdf: Future[Validated[NonEmptyChain[ValidationError], Array[Byte]]] = phaseConfig match {
            case _: PostTransitionConfig => Future.successful(Validated.Valid(pdf.generateP5TADPostTransition(ie029)))
            case _                       => conversionService.fromP5ToViewModel(ie029).map(_.map(pdf.generateP5TADTransition))
          }

          genPdf.map {
            case Validated.Valid(arrayByte) =>
              val fileName = s"TAD_${FileNameSanitizer(ie029.data.TransitOperation.MRN)}.pdf"

              Ok(arrayByte)
                .withHeaders(
                  CONTENT_TYPE        -> "application/pdf",
                  CONTENT_DISPOSITION -> s"""attachment; filename="$fileName""""
                )
            case Validated.Invalid(e) =>
              logger.error(s"Failed to convert to TransitAccompanyingDocumentController with following errors: $e")
              InternalServerError
          }

      }
  }
}
