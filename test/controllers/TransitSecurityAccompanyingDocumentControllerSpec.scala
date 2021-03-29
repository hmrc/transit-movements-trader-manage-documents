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

import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import play.api.test.FakeRequest
import play.api.test.Helpers.OK
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.status
import play.api.test.Helpers.stubControllerComponents
import play.api.test.Helpers.contentType

import scala.xml.NodeSeq

class TransitSecurityAccompanyingDocumentControllerSpec extends FreeSpec with MustMatchers {

  val controller = new TransitSecurityAccompanyingDocumentController(stubControllerComponents())

  "TransitSecurityAccompanyingDocumentController" - {
    "get" - {
      "should return a pdf if an xml is passed through" in {
        val fakeRequest = FakeRequest("", "")
          .withBody[NodeSeq](<ValidXml>Some Text</ValidXml>)

        val response = controller.get().apply(fakeRequest)
        status(response) mustBe OK
        contentType(response) mustBe Some("application/octet-stream")
      }
    }
  }
}
