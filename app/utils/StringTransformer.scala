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

package utils

object StringTransformer {

  implicit class StringFormatter(val x: String) {

    val shorten: Int => String => String = {
      maxLength => concealCharacters =>
        if (x.length > maxLength) {
          if (x.length <= concealCharacters.length) {
            x.slice(0, maxLength)
          } else x.slice(0, maxLength - (concealCharacters.length - 1)) + concealCharacters
        } else x
    }
  }

}
