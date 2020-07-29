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

import models.reference.AdditionalInformation
import models.reference.Country

trait SpecialMention {
  val additionalInformationCoded: AdditionalInformation
  val country: Option[String]
}

final case class SpecialMentionEc(additionalInformationCoded: AdditionalInformation) extends SpecialMention {
  val country = Some("EC")
}

final case class SpecialMentionNonEc(additionalInformationCoded: AdditionalInformation, exportFromCountry: Country) extends SpecialMention {
  val country = Some(exportFromCountry.description)
}

final case class SpecialMentionNoCountry(additionalInformationCoded: AdditionalInformation) extends SpecialMention {
  val country = None
}
