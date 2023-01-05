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

object SecurityConsigneeConverter {

  def convertToSecurityConsigneeVM(consignor: models.SecurityConsignee): viewmodels.SecurityConsignee =
    consignor match {
      case c: models.SecurityConsigneeWithEori =>
        viewmodels.SecurityConsignee(name = None, streetAndNumber = None, postCode = None, city = None, countryCode = None, eori = Some(c.eori))
      case c: models.SecurityConsigneeWithoutEori =>
        viewmodels.SecurityConsignee(
          name = Some(c.name),
          streetAndNumber = Some(c.streetAndNumber),
          postCode = Some(c.postCode),
          city = Some(c.city),
          countryCode = Some(c.countryCode),
          eori = None
        )
    }

}
