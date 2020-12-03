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

package services

import java.time.LocalDate

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class ControlResultConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  "toViewModel" - {

    "must return a view model when ControlResult exists" in {

      val date = LocalDate.now()

      val controlResult = models.ControlResult("A3", date)

      val result = ControlResultConverter.toViewModel(Some(controlResult))

      result.get mustEqual viewmodels.ControlResult("A3", date)

      result.get.isPrintable mustBe true
    }

    "must return None when ControlResult does not exist" in {

      val result = ControlResultConverter.toViewModel(None)

      result mustEqual None
    }
  }
}
