package utils

import java.time.LocalDate

import generators.ModelGenerators
import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class DateFormatterSpec
  extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with OptionValues
    with ModelGenerators
    with ScalaCheckPropertyChecks {

  "DateFormatted" - {

    "must format LocalDate" in {

      forAll(arbitrary[LocalDate]) {
        localDate =>

          val expectedResult =
            s"${localDate.getYear}" +
              s"${"%02d".format(localDate.getMonthValue)}" +
              s"${"%02d".format(localDate.getDayOfMonth)}"

          DateFormatter.dateFormatted(localDate) mustBe expectedResult
      }

    }
  }

}