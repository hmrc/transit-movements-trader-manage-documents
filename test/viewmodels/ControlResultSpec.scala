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

package viewmodels

import models.reference.ControlResultData
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

import java.time.LocalDate

class ControlResultSpec extends AnyFreeSpec with Matchers {

  "displayName" - {
    "return the code if the description is empty" in {
      ControlResult(
        ControlResultData("1234", ""),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      ).displayName mustBe "1234"
    }
    "return the description if the description exists" in {
      ControlResult(
        ControlResultData("1234", "A Description"),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      ).displayName mustBe "A Description"
    }
  }

  "isDescriptionAvailable" - {
    "return the false if the description is empty" in {
      ControlResult(
        ControlResultData("1234", ""),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      ).isDescriptionAvailable mustBe false
    }
    "return the true if the description exists" in {
      ControlResult(
        ControlResultData("1234", "A Description"),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      ).isDescriptionAvailable mustBe true
    }
  }

  "isSimplifiedMovement" - {
    "return the false if the code is not A3" in {
      ControlResult(
        ControlResultData("1234", ""),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      ).isSimplifiedMovement mustBe false
    }
    "return the true if the code is A3" in {
      ControlResult(
        ControlResultData("A3", "A Description"),
        models.ControlResult("A3", LocalDate.of(2020, 2, 2))
      ).isSimplifiedMovement mustBe true
    }
  }

  "formattedDate" - {
    "return the date as a string" in {
      ControlResult(
        ControlResultData("1234", ""),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      ).formattedDate mustBe "02/02/2020"
    }
  }
}
