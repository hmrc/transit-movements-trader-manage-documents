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

package refactor

package object viewmodels {

  implicit class RichString(value: String) {

    private def truncate(maxLength: Int): String =
      if (value.length > maxLength) {
        value.take(maxLength - 3) + "..."
      } else {
        value
      }

    def truncateForOver10: String = truncate(10)
    def truncateForOver20: String = truncate(20)
  }

  implicit class RichStringSeq(value: Seq[String]) {
    def commaSeparate: String    = value.mkString(", ")
    def seperateEntities: String = value.mkString("; ")
  }

  implicit class RichStringOption(value: Option[String]) {
    def orElseBlank: String = value.getOrElse("")
  }
}
