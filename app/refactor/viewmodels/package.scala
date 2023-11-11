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

import generated.p5.Flag

import javax.xml.datatype.XMLGregorianCalendar

package object viewmodels {

  implicit class RichString(value: String) {

    private def takeN(n: Int): String =
      if (value.length > n) {
        value.take(n - 3) + "..."
      } else {
        value
      }

    def take10: String  = takeN(10)
    def take20: String  = takeN(20)
    def take30: String  = takeN(30)
    def take50: String  = takeN(50)
    def take60: String  = takeN(60)
    def take100: String = takeN(100)
    def take200: String = takeN(200)
  }

  implicit class RichStringSeq(value: Seq[String]) {
    def commaSeparate: String         = value.mkString(", ")
    def separateWithSemiColon: String = value.mkString("; ")
    def separateWithEllipsis: String  = value.mkString("...")

    def toBeContinued: String = value match {
      case Nil         => ""
      case head :: Nil => head
      case head :: _   => s"$head..."
    }

    def firstAndLast: String = {
      val (headOption, lastOption) = value match {
        case head :: tail => (Some(head), tail.lastOption)
        case _            => (None, None)
      }
      Seq(headOption, lastOption).flatten.mkString("...")
    }
  }

  implicit class RichStringOption(value: Option[String]) {
    def orElseBlank: String = value.getOrElse("")
  }

  implicit class RichXMLGregorianCalendar(value: XMLGregorianCalendar) {
    def asString: String = value.formatted("dd/MM/yyyy HH:mm")
  }

  implicit class RichFlag(value: Flag) {
    def asString: String = value.toString
  }
}
