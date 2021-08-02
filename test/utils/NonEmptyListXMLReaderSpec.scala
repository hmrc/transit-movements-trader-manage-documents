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

package utils

import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import utils.NonEmptyListXMLReader._

import scala.xml.NodeSeq

class NonEmptyListXMLReaderSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "NonEmptyListXMLReader" - {

    "must deserialize" in {

      forAll(arbitrary[List[String]].suchThat(_.nonEmpty)) {
        list =>
          val xml =
            list.map {
              value =>
                <testChild>{value}</testChild>
            }

          val result = XmlReader.of(xmlNonEmptyListReads[String]).read(xml).toOption.value

          result.toList mustBe list
      }
    }

    "must fail to deserialise if empty" in {

      val xml = NodeSeq.Empty

      val result = XmlReader.of(xmlNonEmptyListReads[String]).read(xml)

      result mustBe an[ParseFailure]
    }
  }

}
