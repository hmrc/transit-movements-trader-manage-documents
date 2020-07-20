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

import cats.data.NonEmptyChain
import javax.inject.Inject
import models.PermissionToStartUnloading
import play.api.Logger
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import services.ConversionService
import services.JsonError
import services.ReferenceDataRetrievalError
import services.ValidationError
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext

class UnloadingPermissionController @Inject()(
  conversionService: ConversionService,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  private val logger = Logger(getClass)

  private def logErrors(errors: NonEmptyChain[ValidationError]): Unit =
    logger.warn(errors.toChain.toList.mkString("\n"))

  def post(): Action[PermissionToStartUnloading] = Action.async(parse.json[PermissionToStartUnloading]) {
    implicit request =>
      conversionService.convertUnloadingPermission(request.body).map {
        _.fold(
          errors => {
            logErrors(errors)

            errors match {
              case x if x.exists(y => y.isInstanceOf[JsonError])                   => InternalServerError
              case x if x.exists(y => y.isInstanceOf[ReferenceDataRetrievalError]) => BadGateway
              case _                                                               => BadRequest
            }
          },
          _ => Ok
        )
      }
  }
}
