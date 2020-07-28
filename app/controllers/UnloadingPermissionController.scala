/*
 * Copyright 2020 HM Revenue & Customs
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

import com.lucidchart.open.xtract.XmlReader
import javax.inject.Inject
import models.PermissionToStartUnloading
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import services._
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.NodeSeq

class UnloadingPermissionController @Inject()(
  conversionService: ConversionService,
  pdf: PdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  def get(): Action[NodeSeq] = Action.async(parse.xml) {
    implicit request =>
      XmlReader
        .of[PermissionToStartUnloading]
        .read(request.body)
        .toOption
        .map {
          permission =>
            conversionService.convertUnloadingPermission(permission).map {
              permissionToStartUnloading =>
                permissionToStartUnloading.toOption match {
                  case Some(viewModel) => Ok(pdf.generateUnloadingPermission(viewModel))
                  case None            => InternalServerError
                }
            }
        }
        .getOrElse {
          Future.successful(BadRequest)
        }
  }
}
