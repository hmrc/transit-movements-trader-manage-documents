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

import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

trait ReferenceModelGenerators extends ModelGenerators {

  implicit lazy val arbitraryCountry: Arbitrary[Country] =
    Arbitrary {
      for {
        state       <- Gen.oneOf(Gen.const("valid"), Gen.const("invalid"))
        code        <- stringWithMaxLength(2).map(_.toUpperCase)
        description <- stringWithMaxLength(50)
      } yield Country(state, code, description)
    }

  implicit lazy val arbitraryKindOfPackage: Arbitrary[KindOfPackage] =
    Arbitrary {
      for {
        code        <- stringWithMaxLength(2).map(_.toUpperCase)
        description <- stringWithMaxLength(50)
      } yield KindOfPackage(code, description)
    }

  implicit lazy val arbitraryDocumentType: Arbitrary[DocumentType] =
    Arbitrary {
      for {
        code              <- stringWithMaxLength(5)
        description       <- stringWithMaxLength(50)
        transportDocument <- arbitrary[Boolean]
      } yield DocumentType(code, description, transportDocument)
    }

  implicit lazy val arbitraryAdditionalInformation: Arbitrary[AdditionalInformation] =
    Arbitrary {
      for {
        code        <- stringWithMaxLength(5)
        description <- stringWithMaxLength(50)
      } yield AdditionalInformation(code, description)
    }
}