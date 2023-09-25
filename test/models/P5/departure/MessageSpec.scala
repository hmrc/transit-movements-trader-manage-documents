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

package models.P5.departure

import base.SpecBase
import play.api.libs.json.Json

class MessageSpec extends SpecBase {

  "must serialise" - {
    "when IE015" in {
      val json = Json.parse("""
          |{
          |  "body" : {
          |    "n1:CC015C": {
          |      "TransitOperation": {
          |        "limitDate": "22-09-2023"
          |      }
          |    }
          |  }
          |}
          |""".stripMargin)

      json.as[IE015] mustBe IE015(
        IE015MessageData(
          TransitOperation = IE015TransitOperation(
            limitDate = Some("22-09-2023")
          )
        )
      )
    }
  }

}
