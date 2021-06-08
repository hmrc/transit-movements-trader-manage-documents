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

import base.SpecBase

class FileNameSanitizerSpec extends SpecBase {

  "when there all the characters are valid" - {
    "returns the full input" in {
      val testString = "asdfasdf"

      val fileName = FileNameSanitizer(testString)

      fileName mustEqual testString
    }
  }

  "when there is a character that is invalid" - {
    "the string returned has `x` in place of the invalid character" - {

      "when the string contains only characters that match the filters" in {
        val testString = "<<>><>"

        val fileName = FileNameSanitizer(testString)

        fileName mustEqual "xxxxxx"
      }

      "when the string contains some characters that match the filters" in {
        val testString = "ab<12AZ>40"

        val fileName = FileNameSanitizer(testString)

        fileName mustEqual "abx12AZx40"
      }

    }
  }
}
