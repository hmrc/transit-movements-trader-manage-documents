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

package models

import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.NodeSeq

class ProducedDocumentSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "ProducedDocument" - {

    "XML" - {

      "must deserialise to ProducedDocument" in {

        forAll(arbitrary[ProducedDocument]) {
          producedDocument =>
            val xml =
              <PRODOCDC2>
                <DocTypDC21>{producedDocument.documentType}</DocTypDC21>
                {
                producedDocument.reference.fold(NodeSeq.Empty) {
                  reference =>
                    <DocRefDC23>{reference}</DocRefDC23>
                } ++
                  producedDocument.complementOfInformation.fold(NodeSeq.Empty) {
                    information =>
                      <ComOfInfDC25>{information}</ComOfInfDC25>
                  }
              }
              </PRODOCDC2>

            val result = XmlReader.of[ProducedDocument].read(xml).toOption.value

            result mustBe producedDocument
        }
      }

      "must fail to deserialise when a mandatory field is missing" in {

        val xml = <PRODOCDC2></PRODOCDC2>

        val result = XmlReader.of[ProducedDocument].read(xml).toOption

        result mustBe None
      }
    }
  }

}
