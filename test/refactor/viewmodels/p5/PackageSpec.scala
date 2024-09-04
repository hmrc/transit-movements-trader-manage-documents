/*
 * Copyright 2024 HM Revenue & Customs
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

package refactor.viewmodels.p5

import base.SpecBase
import generated.p5.TransportChargesType
import org.scalacheck.Arbitrary.arbitrary

class PackageSpec extends SpecBase {

  "withLineBreak" - {
    "must add space after the limit is exceeded" in {
      withLineBreak("abcdefghijk", 10) mustBe "abcdefghij k"
    }

    "must not add space if it has already spaces even the limit is exceeded" in {
      withLineBreak("abcde fghijk", 10) mustBe "abcde fghijk"
    }

    "must use 65% of the limit for upper cases letter" in {
      withLineBreak("ABCDEFGHIJK", 10) mustBe "ABCDEF GHIJK"

      withLineBreak("abcdefghijklmnopqrstuvwxyz", 10) mustBe "abcdefghij klmnopqrst uvwxyz"
      // all upper case => space after 6th letter (10 * 0.65 = 6)
      withLineBreak("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10) mustBe "ABCDEF GHIJKL MNOPQR STUVWX YZ"

      // half lower case half upper case (50%)
      // all lower case limit = 10 => all upper case limit is 6 (10 * 0.65 = 6)
      // (50% * 10) + 50% * 6 = 8 => new limit
      withLineBreak("AbCdEfGhIjKlMnOpQrStUvWxYz", 10) mustBe "AbCdEfGh IjKlMnOp QrStUvWx Yz"
    }
  }
}
