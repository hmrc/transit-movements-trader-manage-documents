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

package models
import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.NodeSeq

class PreviousAdministrativeReferenceSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "PreviousAdministrativeReference" - {

    "must deserialise" in {
      forAll(arbitrary[PreviousAdministrativeReference]) {
        previousAdministrativeReference =>
          val xml =
            <PREADMREFAR2>
              <PreDocTypAR21>{previousAdministrativeReference.preDocTypAR21}</PreDocTypAR21>
              <PreDocRefAR26>{previousAdministrativeReference.preDocRefAR26}</PreDocRefAR26>
                {
                  previousAdministrativeReference.comOfInfAR29.fold(NodeSeq.Empty) {
                    information =>
                      <ComOfInfAR29>{information}</ComOfInfAR29>
                  }
                }
            </PREADMREFAR2>

          val result = XmlReader.of[PreviousAdministrativeReference].read(xml).toOption.value

          result mustBe previousAdministrativeReference
      }
    }
  }
}
