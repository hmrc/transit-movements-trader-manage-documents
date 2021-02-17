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

package utils
import generators.GeneratorHelpers
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class StringTransformerSpec extends FreeSpec with MustMatchers with GeneratorHelpers with ScalaCheckPropertyChecks {

  import utils.StringTransformer._

  "shorten" - {

    "must" - {

      "return correct value when string length equals limit" in {

        forAll(stringWithMaxLength(20)) {
          string =>
            val limit = string.length
            string.shorten(limit)("***") mustBe string
        }
      }

      "return correct value when string length less than limit" in {

        forAll(stringWithMaxLength(20)) {
          string =>
            val limit = string.length + 1
            string.shorten(limit)("***") mustBe string

            string.shorten(limit)("***").length mustBe string.length
        }
      }

      "return correct value when string length exceeds limit" in {

        forAll(stringWithMaxLength(20)) {
          string =>
            val limit = string.length - 1

            if (string.length <= "***".length) {
              string.shorten(limit)("***") mustNot endWith("***")
            } else {
              string.shorten(limit)("***") must endWith("***")
              string.shorten(limit)("***").length mustBe limit
            }
        }
      }

      "return trimmed value excluding concealed characters" in {

        val string = "on"
        val limit  = 1

        string.shorten(limit)("***") mustBe "o"
      }

    }

  }

}
