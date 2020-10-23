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

package services.pdf

import generators.TadViewModelGenerators
import generators.ViewmodelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import viewmodels.PermissionToStartUnloading

class TransitAccompanyingDocumentPdfGeneratorSpec extends FreeSpec with MustMatchers with GuiceOneAppPerSuite with ViewmodelGenerators {

  //TODO: Don't need this, only creating a single view model

  private lazy val service: TransitAccompanyingDocumentPdfGenerator = app.injector.instanceOf[TransitAccompanyingDocumentPdfGenerator]

  "TransitAccompanyingDocumentPdfGenerator" - {

    "return pdf" in {

      val viewModel = arbitrary[PermissionToStartUnloading].sample.get

      service.generate(viewModel) mustBe an[Array[Byte]]

    }

  }

}
