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

package views.tad.transition.components.table_2

import base.SpecBase
import play.api.test.Helpers._
import viewmodels.DummyData
import viewmodels.p4.tad.SecurityViewModel
import views.xml.tad.transition.components.security.table_2

class AuthorisationNumberSpec extends SpecBase with DummyData {

  "authorisation_number" - {
    val result = SecurityViewModel(cc029c)

    "render the template" in {
      val authNumber = result.locationOfGoods.authorisationNumber
      val html       = table_2.row1.authorisation_number(authNumber)
      contentAsString(html) must include(s"Authorisation number")
      contentAsString(html) must include(s"$authNumber")
    }

  }

}
