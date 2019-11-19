/*
 * Copyright 2019 HM Revenue & Customs
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

import models._
import net.ceedubs.ficus.readers.ArbitraryTypeReader
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen, Shrink}

trait ModelGenerators {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  def stringWithMaxLength(maxLength: Int): Gen[String] =
    for {
      length <- Gen.choose(1, maxLength)
      chars  <- Gen.listOfN(length, Gen.alphaNumChar)
    } yield chars.mkString

  implicit lazy val arbitraryBulkPackage: Arbitrary[BulkPackage] =
    Arbitrary {

      for {
        kindOfPackage   <- Gen.oneOf(BulkPackage.validCodes)
        marksAndNumbers <- Gen.option(stringWithMaxLength(42))
      } yield BulkPackage(kindOfPackage, marksAndNumbers)
    }

  implicit lazy val arbitraryUnpackedPackage: Arbitrary[UnpackedPackage] =
    Arbitrary {

      for {
        kindOfPackage   <- Gen.oneOf(UnpackedPackage.validCodes)
        numberOfPieces  <- Gen.choose(1, 99999)
        marksAndNumbers <- Gen.option(stringWithMaxLength(42))
      } yield UnpackedPackage(kindOfPackage, numberOfPieces, marksAndNumbers)
    }

  implicit lazy val arbitraryRegularPackage: Arbitrary[RegularPackage] =
    Arbitrary {

      for {
        kindOfPackage    <- stringWithMaxLength(3).suchThat(x => !BulkPackage.validCodes.contains(x) && !UnpackedPackage.validCodes.contains(x))
        numberOfPackages <- Gen.choose(0, 99999)
        marksAndNumbers  <- stringWithMaxLength(42)
      } yield RegularPackage(kindOfPackage, numberOfPackages, marksAndNumbers)
    }

  implicit lazy val arbitraryPackage: Arbitrary[Package] =
    Arbitrary {

      Gen.oneOf(arbitrary[BulkPackage], arbitrary[UnpackedPackage], arbitrary[RegularPackage])
    }

  implicit lazy val arbitraryProducedDocument: Arbitrary[ProducedDocument] =
    Arbitrary {

      for {
        documentType <- stringWithMaxLength(4)
        reference    <- Gen.option(stringWithMaxLength(35))
        complement   <- Gen.option(stringWithMaxLength(26))
      } yield ProducedDocument(documentType, reference, complement)
    }

  implicit lazy val arbitrarySpecialMentionEc: Arbitrary[SpecialMentionEc] =
    Arbitrary {

      stringWithMaxLength(5).map(SpecialMentionEc(_))
    }

  implicit lazy val arbitrarySpecialMentionNonEc: Arbitrary[SpecialMentionNonEc] =
    Arbitrary  {

      for {
        additionalInfo <- stringWithMaxLength(5)
        exportCountry  <- stringWithMaxLength(2)
      } yield SpecialMentionNonEc(additionalInfo, exportCountry)
    }

  implicit lazy val arbitrarySpecialMention: Arbitrary[SpecialMention] =
    Arbitrary {

      Gen.oneOf(arbitrary[SpecialMentionEc], arbitrary[SpecialMentionNonEc])
    }
}
