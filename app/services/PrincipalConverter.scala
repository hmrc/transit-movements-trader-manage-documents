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

package services

import models.reference.Country
import utils.StringTransformer._

object PrincipalConverter extends Converter {

  def toViewModel(p: models.Principal, path: String, countries: Seq[Country]): ValidationResult[viewmodels.Principal] =
    findReferenceData(p.countryCode, countries, s"$path.countryCode")
      .map(viewmodels.Principal(p.name, p.streetAndNumber, p.streetAndNumber.shorten(32)("***"), p.postCode, p.city, _, p.eori, p.tir))
}
