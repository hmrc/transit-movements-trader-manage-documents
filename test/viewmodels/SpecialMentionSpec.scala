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

package viewmodels

import models.reference.AdditionalInformation
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues

class SpecialMentionSpec extends AnyFreeSpec with Matchers with OptionValues {

  "countryCodeToPrint" - {
    "return EU if only exportFromEC is set to true" in {
      SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(None, "1234", Some(true), None)
      ).countryCodeToPrint.value mustBe "EU"
    }
    "return EU and country code if exportFromEC is true and exportFromCountry is defined" in {
      SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(None, "1234", Some(true), Some("CC"))
      ).countryCodeToPrint.value mustBe "EU CC"
    }
    "return country code if exportFromEC is false and exportFromCountry is defined" in {
      SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(None, "1234", Some(false), Some("CC"))
      ).countryCodeToPrint.value mustBe "CC"
    }
    "return country code if exportFromEC is not defined and exportFromCountry is defined" in {
      SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(None, "1234", None, Some("CC"))
      ).countryCodeToPrint.value mustBe "CC"
    }
    "return none code if exportFromEC and exportFromCountry are not defined" in {
      SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(None, "1234", None, None)
      ).countryCodeToPrint mustBe None
    }

    "return none code if exportFromEC is false and exportFromCountry is not defined" in {
      SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(None, "1234", None, None)
      ).countryCodeToPrint mustBe None
    }
  }

}
