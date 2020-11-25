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

package viewmodels
import generators.ViewmodelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class TransitAccompanyingDocumentSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ViewmodelGenerators with OptionValues {

  "must have document heading" in {

    val transitAccompanyingDocument = arbitrary[TransitAccompanyingDocument].sample.get

    transitAccompanyingDocument.documentHeading.title mustBe "TRANSIT - ACCOMPANYING DOCUMENT"
    transitAccompanyingDocument.documentHeading.isBold mustBe true
  }
}
