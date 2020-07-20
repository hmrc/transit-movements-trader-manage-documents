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

import cats.data.Validated._
import cats.implicits._
import models.reference.AdditionalInformation
import models.reference.Country

object SpecialMentionConverter extends Converter {

  private def specialMentionEcToViewModel(specialMention: models.SpecialMentionEc,
                                          path: String,
                                          additionalInfo: Seq[AdditionalInformation]): ValidationResult[viewmodels.SpecialMentionEc] =
    findReferenceData(specialMention.additionalInformationCoded, additionalInfo, s"$path.additionalInformationCoded")
      .map(viewmodels.SpecialMentionEc)

  private def specialMentionNonEcToViewModel(specialMention: models.SpecialMentionNonEc,
                                             path: String,
                                             additionalInfo: Seq[AdditionalInformation],
                                             countries: Seq[Country]): ValidationResult[viewmodels.SpecialMentionNonEc] =
    (
      findReferenceData(specialMention.additionalInformationCoded, additionalInfo, s"$path.additionalInformationCoded"),
      findReferenceData(specialMention.exportFromCountry, countries, s"$path.countryCode")
    ).mapN(viewmodels.SpecialMentionNonEc)

  private def specialMentionNoCountryToViewModel(specialMention: models.SpecialMentionNoCountry,
                                                 path: String,
                                                 additionalInfo: Seq[AdditionalInformation]): ValidationResult[viewmodels.SpecialMentionNoCountry] =
    findReferenceData(specialMention.additionalInformationCoded, additionalInfo, s"$path.additionalInformationCoded")
      .map(viewmodels.SpecialMentionNoCountry)

  def toViewModel(specialMention: models.SpecialMention,
                  path: String,
                  additionalInfo: Seq[AdditionalInformation],
                  countries: Seq[Country]): ValidationResult[viewmodels.SpecialMention] =
    specialMention match {
      case sm: models.SpecialMentionEc        => specialMentionEcToViewModel(sm, path, additionalInfo)
      case sm: models.SpecialMentionNonEc     => specialMentionNonEcToViewModel(sm, path, additionalInfo, countries)
      case sm: models.SpecialMentionNoCountry => specialMentionNoCountryToViewModel(sm, path, additionalInfo)
    }
}
