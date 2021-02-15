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

package services.pdf

import generators.ViewmodelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import viewmodels.PermissionToStartUnloading

class UnloadingPermissionPdfGeneratorSpec extends FreeSpec with MustMatchers with GuiceOneAppPerSuite with ViewmodelGenerators {

  private lazy val service: UnloadingPermissionPdfGenerator = app.injector.instanceOf[UnloadingPermissionPdfGenerator]

  "UnloadingPermissionPdfGenerator" - {

    "return pdf" in {

      val unloadingPermissionObject = arbitrary[PermissionToStartUnloading]

      val unloadingPermission = unloadingPermissionObject.sample.get

      service.generate(unloadingPermission) mustBe an[Array[Byte]]

    }

  }

}
