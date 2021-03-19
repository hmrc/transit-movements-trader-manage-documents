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

import models.reference.Country
import utils.StringTransformer._

object ReturnCopiesCustomsOfficeConverter extends Converter {

  def toViewModel(returnCopiesCustomsOffice: models.ReturnCopiesCustomsOffice,
                  path: String,
                  countries: Seq[Country]): ValidationResult[viewmodels.ReturnCopiesCustomsOffice] =
    findReferenceData(returnCopiesCustomsOffice.countryCode, countries, s"$path.countryCode")
      .map {
        country =>
          viewmodels.ReturnCopiesCustomsOffice(
            returnCopiesCustomsOffice.customsOfficeName,
            returnCopiesCustomsOffice.streetAndNumber.shorten(32)("***"),
            returnCopiesCustomsOffice.postCode,
            returnCopiesCustomsOffice.city,
            country
          )
      }
}
