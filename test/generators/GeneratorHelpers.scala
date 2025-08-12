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

package generators

import cats.data.NonEmptyList
import org.scalacheck.Gen.{choose, listOfN}
import org.scalacheck.{Arbitrary, Gen, Shrink}

import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}

trait GeneratorHelpers {

  implicit lazy val arbitraryDeclarationType: Arbitrary[String] =
    Arbitrary {
      Gen.oneOf("T-", "T", "T1", "T2", "T2F", "T2SM", "TIR")
    }

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  // Certain characters are not valid XML. This ensures only alpha-numeric strings are generated.
  def string: Gen[String] = Gen.alphaNumStr

  def stringWithMaxLength(maxLength: Int): Gen[String] =
    for {
      length <- Gen.choose(1, maxLength)
      chars  <- Gen.listOfN(length, Gen.alphaNumChar)
    } yield chars.mkString

  def nonEmptyString: Gen[String] =
    for {
      length <- choose(1, 50)
      chars  <- listOfN(length, Gen.alphaNumChar)
    } yield chars.mkString

  def listWithMaxSize[T](maxSize: Int = 10)(implicit arbitraryT: Arbitrary[T]): Gen[Seq[T]] =
    listWithMaxSize(arbitraryT.arbitrary, maxSize)

  def listWithMaxSize[T](gen: Gen[T], maxSize: Int): Gen[Seq[T]] =
    for {
      size  <- Gen.choose(0, maxSize)
      items <- Gen.listOfN(size, gen)
    } yield items

  def nonEmptyListWithMaxSize[T](maxSize: Int = 10)(implicit arbitraryT: Arbitrary[T]): Gen[NonEmptyList[T]] =
    nonEmptyListWithMaxSize(arbitraryT.arbitrary, maxSize)

  def nonEmptyListWithMaxSize[T](gen: Gen[T], maxSize: Int): Gen[NonEmptyList[T]] =
    for {
      head     <- gen
      tailSize <- Gen.choose(1, maxSize - 1)
      tail     <- Gen.listOfN(tailSize, gen)
    } yield NonEmptyList(head, tail)

  def datesBetween(min: LocalDate, max: LocalDate): Gen[LocalDate] = {

    def toMillis(date: LocalDate): Long =
      date.atStartOfDay.atZone(ZoneOffset.UTC).toInstant.toEpochMilli

    Gen.choose(toMillis(min), toMillis(max)).map {
      millis =>
        Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDate
    }
  }

  def dateTimeBetween(min: LocalDateTime, max: LocalDateTime): Gen[LocalDateTime] = {

    def toMillis(date: LocalDateTime): Long =
      date.atZone(ZoneOffset.UTC).toInstant.toEpochMilli

    Gen.choose(toMillis(min), toMillis(max)).map {
      millis =>
        Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDateTime
    }
  }
}
