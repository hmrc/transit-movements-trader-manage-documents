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

package viewmodels.tad.transition

import base.SpecBase
import generated.CC029CType
import generators.ScalaxbModelGenerators
import models.IE015
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class TadPdfViewModelSpec extends SpecBase with ScalaCheckPropertyChecks with ScalaxbModelGenerators {

  "when security is 0" - {
    "there should be no security view model" in {
      forAll(arbitrary[IE015], arbitrary[CC029CType]) {
        (ie015, ie029) =>
          val ie029WithNoSecurity = ie029.copy(TransitOperation = ie029.TransitOperation.copy(security = "0"))
          val viewModel           = TadPdfViewModel(ie015, ie029WithNoSecurity)
          viewModel.securityViewModel must not be defined
      }
    }
  }

  "when security is not 0" - {
    "there should be a security view model" in {
      forAll(arbitrary[IE015], arbitrary[CC029CType], Gen.oneOf("1", "2", "3")) {
        (ie015, ie029, security) =>
          val ie029WithNoSecurity = ie029.copy(TransitOperation = ie029.TransitOperation.copy(security = security))
          val viewModel           = TadPdfViewModel(ie015, ie029WithNoSecurity)
          viewModel.securityViewModel must be(defined)
      }
    }
  }
}
