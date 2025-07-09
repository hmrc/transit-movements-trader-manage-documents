/*
 * Copyright 2025 HM Revenue & Customs
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

import base.SpecBase
import models.Version
import models.requests.{AuthenticatedRequest, VersionedRequest}
import org.scalatest.EitherValues
import org.scalatest.concurrent.ScalaFutures
import play.api.mvc.*
import play.api.mvc.Results.BadRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class VersionedActionSpec extends SpecBase with ScalaFutures with EitherValues {

  private class Harness extends VersionedAction {

    def callRefine[A](request: AuthenticatedRequest[A]): Future[Either[Result, VersionedRequest[A]]] =
      refine(request)
  }

  "VersionedAction" - {

    "when defined header" - {
      "should return request with version value" in {
        val action = new Harness()

        val version = "2.1"

        val request              = fakeRequest.withHeaders("API-Version" -> version)
        val authenticatedRequest = AuthenticatedRequest(request, "EORINumber")
        val result               = action.callRefine(authenticatedRequest).futureValue

        result.value mustEqual VersionedRequest(authenticatedRequest, Version(version))
      }
    }

    "undefined header" - {
      "should return bad request" in {
        val action = new Harness()

        val request              = fakeRequest
        val authenticatedRequest = AuthenticatedRequest(request, "EORINumber")
        val result               = action.callRefine(authenticatedRequest).futureValue

        result.left.value mustEqual BadRequest("Version is undefined")
      }
    }
  }
}
