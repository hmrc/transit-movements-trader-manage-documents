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

package generators
import models.DeclarationType
import models.reference.Country
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import viewmodels.tad.TransitAccompanyingDocument

trait TadViewModelGenerators extends ModelGenerators {

  implicit lazy val arbitraryTransitAccompanyingDocument: Arbitrary[TransitAccompanyingDocument] =
    Arbitrary {
      for {
        localReferenceNumber <- stringWithMaxLength(22)
        declarationType      <- arbitrary[DeclarationType]
        countryOfDispatch    <- Gen.option(arbitrary[Country])
        countryOfDestination <- Gen.option(arbitrary[Country])

      } yield
        TransitAccompanyingDocument(
          localReferenceNumber,
          declarationType: DeclarationType,
          countryOfDispatch,
          countryOfDestination
        )
    }
}
