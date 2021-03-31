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

package services

import cats.data.Validated.Valid
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.TADSpecialMention
import models.reference.AdditionalInformation
import models.reference.Country
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class TADSpecialMentionConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val additionalInfo = Seq(AdditionalInformation("a", "info 1"), AdditionalInformation("b", "info 2"))
  private val invalidCode    = "invalid code"

  "toViewModel" - {
    "convert the correct value" in {
      val specialModel = models.TADSpecialMention(
        additionalInformation = Some("Some Reference"),
        additionalInformationCoded = Some("a"),
        exportFromEC = Some(false),
        exportFromCountry = None
      )
      TADSpecialMentionConverter.toViewModel(
        specialMention = specialModel,
        path = "somePath",
        additionalInfo = additionalInfo
      ) mustBe Valid(
        viewmodels.TADSpecialMention(AdditionalInformation("a", "info 1"), specialMention = specialModel)
      )
    }
    "invalid if additional information not found" in {}
  }
}
