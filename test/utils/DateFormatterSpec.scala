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

import java.time.LocalDate

import generators.ModelGenerators
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class DateFormatterSpec extends AnyFreeSpec with Matchers with GuiceOneAppPerSuite with OptionValues with ModelGenerators with ScalaCheckPropertyChecks {

  "DateFormatted" - {

    "must" - {

      "format LocalDate" in {

        forAll(arbitrary[LocalDate]) {
          localDate =>
            val expectedResult =
              s"${localDate.getYear}" +
                s"${"%02d".format(localDate.getMonthValue)}" +
                s"${"%02d".format(localDate.getDayOfMonth)}"

            DateFormatter.dateFormatted(localDate) mustBe expectedResult
        }

      }

      "format LocalDate with custom pattern" in {

        forAll(arbitrary[LocalDate]) {
          localDate =>
            val expectedResult =
              s"${"%02d".format(localDate.getDayOfMonth)}/" +
                s"${"%02d".format(localDate.getMonthValue)}/" +
                s"${localDate.getYear}"

            DateFormatter.dateFormatted(Some(localDate), "dd/MM/yyyy").value mustBe expectedResult
        }

      }

      "toString date if date pattern is invalid" in {

        forAll(arbitrary[LocalDate]) {
          localDate =>
            DateFormatter.dateFormatted(Some(localDate), "invalid").value mustBe localDate.toString
        }
      }
    }
  }
}
