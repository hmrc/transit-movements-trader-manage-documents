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

import generated.*

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.datatype.XMLGregorianCalendar
import scala.annotation.tailrec

package object viewmodels {

  implicit class RichString(value: String) {

    private val Ellipsis = "..."

    // approximate values since we don't use a fixed width font
    private val NarrowLineCharLength      = 45
    private val NarrowLineBlankCharLength = 80 // a narrow line with only spaces need approx. 80 characters
    private val WideLineCharLength        = NarrowLineCharLength * 2
    private val WideLineBlankCharLength   = NarrowLineBlankCharLength * 2 // a narrow line with only spaces need approx. 160 characters

    def takeN(n: Int, chars: String = Ellipsis): String =
      if (value.length > n) {
        value.take(n - 3) + chars
      } else {
        value
      }

    def appendPeriod: String = value match {
      case v if v.nonEmpty && v.endsWith("...") => v
      case x if x.nonEmpty                      => s"$x."
      case _                                    => value
    }
    def adjustFor2NarrowLines: String = adjustForNNarrowLines(2)
    def adjustFor3NarrowLines: String = adjustForNNarrowLines(3)

    def adjustFor2WideLines: String = adjustForNWideLines(2)

    private def adjustForNNarrowLines(n: Int): String =
      adjustForNLines(n, NarrowLineCharLength, NarrowLineBlankCharLength)

    private def adjustForNWideLines(n: Int): String =
      adjustForNLines(n, WideLineCharLength, WideLineBlankCharLength)

    private def adjustForNLines(n: Int, lineLength: Int, blankLineLength: Int): String = {
      val charLength = lineLength * n
      if (value.length >= charLength) {
        value.take(charLength - 3) + Ellipsis
      } else {
        pad(n, lineLength, blankLineLength)
      }
    }

    private def pad(lines: Int, lineLength: Int, blankLineLength: Int): String = {
      @tailrec
      def rec(i: Int): String =
        if (i == lines) {
          value
        } else {
          if (value.length < lineLength * i) {
            value + (" " * blankLineLength * (lines - i))
          } else {
            rec(i + 1)
          }
        }

      rec(1)
    }

    def take10: String  = takeN(10)
    def take20: String  = takeN(20)
    def take30: String  = takeN(30)
    def take40: String  = takeN(40)
    def take45: String  = takeN(45)
    def take50: String  = takeN(50)
    def take60: String  = takeN(60)
    def take90: String  = takeN(90)
    def take100: String = takeN(100)
    def take200: String = takeN(200)
  }

  implicit class RichStringSeq(value: Seq[String]) {
    def commaSeparate: String     = value.mkString(", ")
    def slashSeparate: String     = value.mkString("/")
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

    def take1: String                           = takeN(1)(_.mkString)
    def take2(f: Seq[String] => String): String = takeN(2)(f)
    def take3(f: Seq[String] => String): String = takeN(3)(f)

    def takeSampleWithoutPeriod: String =
      if (value.length > 3) {
        firstAndLast(";...;")
      } else {
        value.mkString("; ")
      }

    def takeSample: String = {
      val sample = takeSampleWithoutPeriod
      if (sample.trim.isEmpty) sample else s"$sample."
    }
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

  implicit class RichLocalDate(value: LocalDate) {

    private def format(pattern: String): String = {
      val formatter = DateTimeFormatter.ofPattern(pattern)
      formatter.format(value)
    }

    def dateString: String      = format("dd/MM/yyyy")
    def limitDateString: String = format("yyyy-MM-dd")
  }

  implicit class RichFlag(value: Flag) {

    def asString: String = value match {
      case Number0 => ""
      case Number1 => "X"
    }

    def toBoolean: Boolean = value match {
      case Number0 => false
      case Number1 => true
    }
  }

  implicit class RichBigDecimal(value: BigDecimal) {
    def asString: String = value.toDouble.toString
  }

  /*





  implicit class RichAddressType02(value: AddressType02) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichAddressType14(value: AddressType14) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichAddressType09(value: AddressType09) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichAddressType10(value: AddressType10) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichPostcodeAddressType(value: PostcodeAddressType) {

    def asString: String = Seq(
      value.houseNumber,
      Some(value.postcode),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichConsigneeType01(value: ConsigneeType01) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsignorType10(value: ConsignorType10) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString),
      value.ContactPerson.map(_.asString)
    ).flatten.slashSeparate

    def asConsignorType03: ConsignorType03 = ConsignorType03(
      value.identificationNumber,
      value.name,
      value.Address,
      value.ContactPerson
    )
  }



  implicit class RichContactPersonType01(value: ContactPersonType01) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber)
    ).flatten.slashSeparate
  }

  implicit class RichContactPersonType03(value: ContactPersonType03) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber)
    ).flatten.commaSeparate
  }









   */
}
