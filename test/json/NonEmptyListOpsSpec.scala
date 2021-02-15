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

package json

import cats.data.NonEmptyList
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import json.NonEmptyListOps._

class NonEmptyListOpsSpec extends FreeSpec with MustMatchers {

  "ops" - {

    "must write a non empty list with no tail" in {

      val json = "[1]"

      val nonEmptyList = NonEmptyList.one(1)

      Json.toJson(nonEmptyList).toString mustEqual json
    }

    "must write a non empty list with a tail" in {

      val json = "[1,2,3]"

      val nonEmptyList = NonEmptyList(1, List(2, 3))

      Json.toJson(nonEmptyList).toString mustEqual json
    }
  }

  "must read a list with one item" in {

    val json = Json.arr(1)

    json.validate[NonEmptyList[Int]] mustEqual JsSuccess(NonEmptyList.one(1))
  }

  "must read a list with multiple items" in {

    val json = Json.arr(1, 2, 3)

    json.validate[NonEmptyList[Int]] mustEqual JsSuccess(NonEmptyList(1, List(2, 3)))
  }

  "must fail to read an empty list" in {

    val json = Json.arr()

    json.validate[NonEmptyList[Int]] mustBe a[JsError]
  }
}
