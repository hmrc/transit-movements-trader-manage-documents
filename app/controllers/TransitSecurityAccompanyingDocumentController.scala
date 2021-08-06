/*
 * Copyright 2021 HM Revenue & Customs
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

import cats.data.Validated
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.ParseSuccess
import com.lucidchart.open.xtract.PartialParseSuccess
import logging.Logging
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import services.XMLToReleaseForTransit
import services.conversion.TransitSecurityAccompanyingDocumentConversionService
import services.pdf.TSADPdfGenerator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.NodeSeq
import utils.FileNameSanitizer

class TransitSecurityAccompanyingDocumentController @Inject() (
  conversionService: TransitSecurityAccompanyingDocumentConversionService,
  pdf: TSADPdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def get(): Action[NodeSeq] = Action.async(parse.xml) {
    implicit request =>
      XMLToReleaseForTransit.convert(request.body) match {
        case ParseSuccess(releaseForTransit) =>
          conversionService.toViewModel(releaseForTransit).map {
            case Validated.Valid(viewModel) =>
              val fileName = s"TSAD_${FileNameSanitizer(releaseForTransit.header.movementReferenceNumber)}.pdf"
              Ok(pdf.generate(viewModel))
                .withHeaders(
                  CONTENT_TYPE        -> "application/pdf",
                  CONTENT_DISPOSITION -> s"""attachment; filename="$fileName""""
                )
            case Validated.Invalid(errors) =>
              logger.info(s"Failed to convert to TransitSecurityAccompanyingDocument with following errors: $errors")
              InternalServerError
          } recover {
            case e =>
              logger.info(s"Exception thrown while converting to TransitSecurityAccompanyingDocument: ${e.getMessage}")
              BadGateway
          }
        case PartialParseSuccess(result, errors) =>
          logger.info(s"Partially failed to parse xml to TransitSecurityAccompanyingDocument with the following errors: $errors and result $result")
          Future.successful(BadRequest)
        case ParseFailure(errors) =>
          logger.info(s"Failed to parse xml to TransitSecurityAccompanyingDocument with the following errors: $errors")
          Future.successful(BadRequest)
      }
  }
}
