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
import models.reference.AdditionalInformation
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class SpecialMentionConverterSpec extends AnyFreeSpec with Matchers with ValidatedMatchers with ValidatedValues {

  private val additionalInfo = Seq(AdditionalInformation("a", "info 1"), AdditionalInformation("b", "info 2"))

  "toViewModel" - {
    "convert the correct value" in {
      val specialModel = models.SpecialMention(
        additionalInformation = Some("Some Reference"),
        additionalInformationCoded = "a",
        exportFromEC = Some(false),
        exportFromCountry = None
      )
      SpecialMentionConverter.toViewModel(
        specialMention = specialModel,
        path = "somePath",
        additionalInfo = additionalInfo
      ) mustBe Valid(
        viewmodels.SpecialMention(AdditionalInformation("a", "info 1"), specialMention = specialModel)
      )
    }
    "invalid if additional information not found" in {
      val specialModel = models.SpecialMention(
        additionalInformation = Some("Some Reference"),
        additionalInformationCoded = "x",
        exportFromEC = Some(false),
        exportFromCountry = None
      )

      val result = SpecialMentionConverter.toViewModel(
        specialMention = specialModel,
        path = "somePath",
        additionalInfo = additionalInfo
      )

      val expectedErrors = Seq(
        ReferenceDataNotFound("somePath.additionalInformationCoded", "x")
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }
  }
}
