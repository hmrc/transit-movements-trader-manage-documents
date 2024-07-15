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
import generated.p5.Number0
import generated.p5.Number1

import java.text.SimpleDateFormat
import javax.xml.datatype.XMLGregorianCalendar

package object viewmodels {

  implicit class RichString(value: String) {

    def takeN(n: Int, chars: String = "..."): String =
      if (value.length > n) {
        value.take(n - 3) + chars
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
    def commaSeparate: String     = value.mkString(", ")
    def semiColonSeparate: String = value.mkString("; ")
    def dashSeparate: String      = value.mkString(" - ")
    def ellipsisSeparate: String  = value.mkString("...")

    def toBeContinued(stringIfEmpty: String = ""): String = value.toList match {
      case Nil         => stringIfEmpty
      case head :: Nil => head
      case head :: _   => s"$head..."
    }

    def firstAndLast(separator: String = "..."): String = {
      val (headOption, lastOption) = value match {
        case head :: tail => (Some(head), tail.lastOption)
        case _            => (None, None)
      }
      Seq(headOption, lastOption).flatten.mkString(separator)
    }

    def addDefaultIfEmpty(): Seq[String] = if (value.isEmpty) Seq("--") else value

    private def takeN(n: Int)(f: Seq[String] => String): String =
      if (value.length > n) {
        f(value.take(n)) + "..."
      } else {
        f(value.take(n))
      }

    def take3(f: Seq[String] => String): String = takeN(3)(f)
  }

  implicit class RichOptionTSeq[T](value: Seq[Option[T]]) {

    def takeFirstIfAllTheSame: Option[T] = value match {
      case head :: tail if tail.forall(_ == head) => head
      case _                                      => None
    }
  }

  implicit class RichStringOption(value: Option[String]) {
    def orElseBlank: String = value.getOrElse("")

    def orElse2Dashes: String = value.getOrElse("--")
    def orElse3Dashes: String = value.getOrElse("---")
  }

  implicit class RichXMLGregorianCalendar(value: XMLGregorianCalendar) {

    private def format(pattern: String): String = {
      val date      = value.toGregorianCalendar.getTime
      val formatter = new SimpleDateFormat(pattern)
      formatter.format(date)
    }

    def dateAndTimeString: String = format("dd/MM/yyyy HH:mm")
    def dateString: String        = format("dd/MM/yyyy")
    def limitDateString: String   = format("yyyy-MM-dd")
  }

  implicit class RichFlag(value: Flag) {
    def asString: String = value.toString

    def toBoolean: Boolean = value match {
      case Number0 => false
      case Number1 => true
    }
  }

  implicit class RichBigDecimal(value: BigDecimal) {
    def asString: String = value.toDouble.toString
  }
}
