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

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.reference.AdditionalInformation
import models.reference.Country
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class SpecialMentionConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val countries      = Seq(Country("valid", "a", "country 1"), Country("valid", "b", "country 2"))
  private val additionalInfo = Seq(AdditionalInformation("a", "info 1"), AdditionalInformation("b", "info 2"))
  private val invalidCode    = "invalid code"

  "toViewModel" - {

    "when given a Special Mention EC" - {

      "must return a view model when the additional information is found in reference data" in {

        val specialMention = models.SpecialMentionEc(additionalInfo.head.code)

        val result = SpecialMentionConverter.toViewModel(specialMention, "path", additionalInfo, countries)

        result.valid.value mustEqual viewmodels.SpecialMentionEc(additionalInfo.head)
      }

      "must return Invalid when the additional information code cannot be found in reference data" in {

        val specialMention = models.SpecialMentionEc(invalidCode)

        val result = SpecialMentionConverter.toViewModel(specialMention, "path", additionalInfo, countries)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.additionalInformationCoded", invalidCode))
      }
    }

    "when given a Special Mention Non EC" - {

      "must return a view model when the additional information and country code are found in reference data" in {

        val specialMention = models.SpecialMentionNonEc(additionalInfo.head.code, countries.head.code)

        val result = SpecialMentionConverter.toViewModel(specialMention, "path", additionalInfo, countries)

        result.valid.value mustEqual viewmodels.SpecialMentionNonEc(additionalInfo.head, countries.head)
      }

      "must return Invalid when the additional information code cannot be found in reference data" in {

        val specialMention = models.SpecialMentionNonEc(invalidCode, countries.head.code)

        val result = SpecialMentionConverter.toViewModel(specialMention, "path", additionalInfo, countries)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.additionalInformationCoded", invalidCode))
      }

      "must return Invalid when the country code cannot be found in reference data" in {

        val specialMention = models.SpecialMentionNonEc(additionalInfo.head.code, invalidCode)

        val result = SpecialMentionConverter.toViewModel(specialMention, "path", additionalInfo, countries)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.countryCode", invalidCode))
      }

      "must return Invalid when both the additional information code andthe country code cannot be found in reference data" in {

        val specialMention = models.SpecialMentionNonEc(invalidCode, invalidCode)

        val result = SpecialMentionConverter.toViewModel(specialMention, "path", additionalInfo, countries)

        val expectedErrors = Seq(
          ReferenceDataNotFound("path.additionalInformationCoded", invalidCode),
          ReferenceDataNotFound("path.countryCode", invalidCode)
        )

        result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
      }
    }
  }
}
