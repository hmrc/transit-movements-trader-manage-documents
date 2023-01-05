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

package models

import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsError
import play.api.libs.json.JsString
import play.api.libs.json.Json

class DeclarationTypeSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Declaration Type" - {

    "Json" - {

      "must serialise and deserialise" in {
        forAll(arbitrary[DeclarationType]) {
          declarationType =>
            Json.toJson(declarationType).validate[DeclarationType].asOpt.value mustBe declarationType
        }
      }

      "must fail to deserialise" in {
        JsString("Invalid").validate[DeclarationType] mustBe a[JsError]
      }
    }

    "XML" - {

      "must deserialise" in {
        forAll(arbitrary[DeclarationType]) {
          declarationType =>
            val xml    = <TypOfDecHEA24>{declarationType.toString}</TypOfDecHEA24>
            val result = XmlReader.of[DeclarationType].read(xml).toOption.value

            result mustBe declarationType
        }
      }

      "must fail to deserialise" in {
        val xml    = <TypOfDecHEA24>Invalid value</TypOfDecHEA24>
        val result = XmlReader.of[DeclarationType].read(xml).toOption

        result mustBe None
      }
    }
  }

}
