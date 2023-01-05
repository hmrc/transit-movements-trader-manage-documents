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

import cats.data.Validated
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.ParseSuccess
import com.lucidchart.open.xtract.PartialParseSuccess
import play.api.Logging
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import services._
import services.conversion.UnloadingPermissionConversionService
import services.pdf.UnloadingPermissionPdfGenerator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.FileNameSanitizer

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.xml.NodeSeq

class UnloadingPermissionController @Inject() (
  conversionService: UnloadingPermissionConversionService,
  pdf: UnloadingPermissionPdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def get(): Action[NodeSeq] = Action.async(parse.xml) {
    implicit request =>
      XMLToPermissionToStartUnloading.convert(request.body) match {
        case ParseSuccess(unloadingPermission) =>
          conversionService
            .toViewModel(unloadingPermission)
            .map {
              case Validated.Valid(viewModel) =>
                val fileName = s"UnloadingPermission_${FileNameSanitizer(unloadingPermission.movementReferenceNumber)}.pdf"
                Ok(pdf.generate(viewModel))
                  .withHeaders(
                    CONTENT_TYPE        -> "application/pdf",
                    CONTENT_DISPOSITION -> s"""attachment; filename="$fileName""""
                  )
              case Validated.Invalid(errors) =>
                logger.error(s"Failed to convert to UnloadingPermissionViewModel with following errors: $errors")
                InternalServerError
            }
            .recover {
              case NonFatal(e) =>
                logger.error("Exception thrown while converting to UnloadingPermission", e)
                BadGateway
            }
        case PartialParseSuccess(result, errors) =>
          logger.error(s"Partially failed to parse xml to UnloadingPermission with the following errors: $errors and result $result")
          Future.successful(BadRequest)
        case ParseFailure(errors) =>
          logger.error(s"Failed to parse xml to UnloadingPermission with the following errors: $errors")
          Future.successful(BadRequest)
      }
  }
}
