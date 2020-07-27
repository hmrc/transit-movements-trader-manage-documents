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

package generators

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

import cats.data.NonEmptyList
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Shrink

trait GeneratorHelpers {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  def stringWithMaxLength(maxLength: Int): Gen[String] =
    for {
      length <- Gen.choose(1, maxLength)
      chars  <- Gen.listOfN(length, Gen.alphaNumChar)
    } yield chars.mkString

  def nonEmptyString: Gen[String] =
    arbitrary[String] suchThat (_.nonEmpty)

  def listWithMaxSize[T](maxSize: Int, gen: Gen[T]): Gen[Seq[T]] =
    for {
      size  <- Gen.choose(0, maxSize)
      items <- Gen.listOfN(size, gen)
    } yield items

  def nonEmptyListWithMaxSize[T](maxSize: Int, gen: Gen[T]): Gen[NonEmptyList[T]] =
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
}
