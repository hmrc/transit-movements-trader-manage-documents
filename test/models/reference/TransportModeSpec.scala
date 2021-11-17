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

package models.reference

import generators.ReferenceModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json

class TransportModeSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ReferenceModelGenerators {

  "TransportMode" - {

    "deserialise from json to TransportMode" in {

      forAll(arbitrary[TransportMode]) {
        transportMode =>
          val json = Json.obj("code" -> transportMode.code, "description" -> transportMode.description)

          json.validate[TransportMode] mustEqual JsSuccess(transportMode)
      }
    }

    "deserialise from json to TransportMode when code is Int" in {
      val json = Json.obj("code" -> 12, "description" -> "description")

      json.validate[TransportMode] mustEqual JsSuccess(TransportMode("12", "description"))
    }
  }

}
