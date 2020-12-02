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

package viewmodels
import java.time.LocalDateTime

import generators.ViewmodelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import utils.DateFormatter

class CustomsOfficeTransitSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ViewmodelGenerators {

  "date" - {

    "return formatted arrivalTime when arrivalTime exists" in {

      forAll(arbitrary[CustomsOfficeTransit] suchThat (x => x.arrivalTime.isDefined)) {
        customsOffice =>
          customsOffice.dateTimeFormatted mustBe
            Some(DateFormatter.arrivalDateTimeFormatted(customsOffice.arrivalTime.get))
      }
    }

    "return correctly formatted arrivalTime when arrivalTime exists" in {

      val dateTime = LocalDateTime.of(2020, 1, 4, 12, 25, 25)

      val customsOfficeTransit = CustomsOfficeTransit("AA", Some(dateTime))

      customsOfficeTransit.dateTimeFormatted mustBe Some("04/01/2020 12:25")
    }

    "return none when arrivalTime does not exist" in {

      val customsOfficeTransit = CustomsOfficeTransit("AA", None)

      customsOfficeTransit.dateTimeFormatted mustBe None
    }

  }

}
