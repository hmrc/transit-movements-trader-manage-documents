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

package controllers.actions

import models.P5.Phase
import models.requests.AuthenticatedRequest
import models.requests.VersionedRequest
import play.api.http.HeaderNames._
import play.api.mvc.Results.BadRequest
import play.api.mvc.ActionRefiner
import play.api.mvc.Result

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class VersionedAction @Inject() (implicit val executionContext: ExecutionContext) extends ActionRefiner[AuthenticatedRequest, VersionedRequest] {

  override protected def refine[A](request: AuthenticatedRequest[A]): Future[Either[Result, VersionedRequest[A]]] =
    request.headers.get(ACCEPT) match {
      case Some("application/vnd.hmrc.transition+pdf") => Future.successful(Right(VersionedRequest(request, Phase.Transition)))
      case Some("application/vnd.hmrc.final+pdf")      => Future.successful(Right(VersionedRequest(request, Phase.PostTransition)))
      case _                                           => Future.successful(Left(BadRequest))
    }
}
