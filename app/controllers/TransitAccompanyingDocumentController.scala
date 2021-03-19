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
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import services._
import services.conversion.TransitAccompanyingDocumentConversionService
import services.pdf.TADPdfGenerator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.NodeSeq

class TransitAccompanyingDocumentController @Inject()(
  conversionService: TransitAccompanyingDocumentConversionService,
  pdf: TADPdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  def get(): Action[NodeSeq] = Action.async(parse.xml) {
    implicit request =>
      XMLToTransitAccompanyingDocument.convert(request.body) match {
        case ParseSuccess(transitAccompanyingDocument) =>
          conversionService.toViewModel(transitAccompanyingDocument).map {
            case Validated.Valid(viewModel) => Ok(pdf.generate(viewModel))
            case Validated.Invalid(errors) =>
              InternalServerError(s"Failed to convert to TransitAccompanyingDocumentViewModel with following errors: $errors")
          } recover {
            case e => BadGateway
          }
        case ParseFailure(errors) =>
          Future.successful(BadRequest(s"Failed to parse xml to TransitAccompanyingDocument with the following errors: $errors"))
      }
  }
}
