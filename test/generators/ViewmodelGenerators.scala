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

import java.time.LocalDate

import models.DeclarationType
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import viewmodels._

trait ViewmodelGenerators extends GeneratorHelpers with ReferenceModelGenerators {

  implicit lazy val arbitraryBulkPackage: Arbitrary[BulkPackage] =
    Arbitrary {

      for {
        kindOfPackage   <- arbitrary[KindOfPackage]
        marksAndNumbers <- Gen.option(stringWithMaxLength(42))
      } yield BulkPackage(kindOfPackage, marksAndNumbers)
    }

  implicit lazy val arbitraryUnpackedPackage: Arbitrary[UnpackedPackage] =
    Arbitrary {

      for {
        kindOfPackage   <- arbitrary[KindOfPackage]
        numberOfPieces  <- Gen.choose(1, 99999)
        marksAndNumbers <- Gen.option(stringWithMaxLength(42))
      } yield UnpackedPackage(kindOfPackage, numberOfPieces, marksAndNumbers)
    }

  implicit lazy val arbitraryRegularPackage: Arbitrary[RegularPackage] =
    Arbitrary {

      for {
        kindOfPackage    <- arbitrary[KindOfPackage]
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
        documentType <- arbitrary[DocumentType]
        reference    <- Gen.option(stringWithMaxLength(35))
        complement   <- Gen.option(stringWithMaxLength(26))
      } yield ProducedDocument(documentType, reference, complement)
    }

  implicit lazy val arbitrarySpecialMentionEc: Arbitrary[SpecialMentionEc] =
    Arbitrary {

      arbitrary[AdditionalInformation].map(SpecialMentionEc)
    }

  implicit lazy val arbitrarySpecialMentionNonEc: Arbitrary[SpecialMentionNonEc] =
    Arbitrary {

      for {
        additionalInfo <- arbitrary[AdditionalInformation]
        exportCountry  <- arbitrary[Country]
      } yield SpecialMentionNonEc(additionalInfo, exportCountry)
    }

  implicit lazy val arbitrarySpecialMentionNoCountry: Arbitrary[SpecialMentionNoCountry] =
    Arbitrary {

      arbitrary[AdditionalInformation].map(SpecialMentionNoCountry)
    }

  implicit lazy val arbitrarySpecialMention: Arbitrary[SpecialMention] =
    Arbitrary {

      Gen.oneOf(arbitrary[SpecialMentionEc], arbitrary[SpecialMentionNonEc], arbitrary[SpecialMentionNoCountry])
    }

  implicit lazy val arbitraryTraderAtDestinationWithEori: Arbitrary[TraderAtDestinationWithEori] =
    Arbitrary {

      for {
        eori            <- stringWithMaxLength(17)
        name            <- Gen.option(stringWithMaxLength(35))
        streetAndNumber <- Gen.option(stringWithMaxLength(35))
        postCode        <- Gen.option(stringWithMaxLength(9))
        city            <- Gen.option(stringWithMaxLength(35))
        countryCode     <- Gen.option(arbitrary[Country])
      } yield TraderAtDestinationWithEori(eori, name, streetAndNumber, postCode, city, countryCode)
    }

  implicit lazy val arbitraryTraderAtDestinationWithoutEori: Arbitrary[TraderAtDestinationWithoutEori] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        countryCode     <- arbitrary[Country]
      } yield TraderAtDestinationWithoutEori(name, streetAndNumber, postCode, city, countryCode)
    }

  implicit lazy val arbitraryTraderAtDestination: Arbitrary[TraderAtDestination] =
    Arbitrary {

      Gen.oneOf(arbitrary[TraderAtDestinationWithEori], arbitrary[TraderAtDestinationWithoutEori])
    }

  implicit lazy val arbitraryConsignee: Arbitrary[Consignee] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        country         <- arbitrary[Country]
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield Consignee(name, streetAndNumber, postCode, city, country, eori)
    }

  implicit lazy val arbitraryConsignor: Arbitrary[Consignor] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        country         <- arbitrary[Country]
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield Consignor(name, streetAndNumber, postCode, city, country, eori)
    }

  implicit lazy val arbitraryPrincipal: Arbitrary[Principal] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        country         <- arbitrary[Country]
        eori            <- Gen.option(stringWithMaxLength(17))
        tin             <- Gen.option(stringWithMaxLength(17))
      } yield Principal(name, streetAndNumber, postCode, city, country, eori, tin)
    }

  implicit lazy val arbitraryDeclarationType: Arbitrary[DeclarationType] =
    Arbitrary {

      Gen.oneOf(DeclarationType.values)
    }

  implicit lazy val arbitrarySensitiveGoodsInformation: Arbitrary[SensitiveGoodsInformation] =
    Arbitrary {
      for {
        goodsCode <- Gen.option(stringWithMaxLength(2))
        quantity  <- Gen.choose(1, 99999)
      } yield SensitiveGoodsInformation(goodsCode, quantity)
    }

  implicit lazy val arbitraryGoodsItem: Arbitrary[GoodsItem] =
    Arbitrary {

      for {
        itemNumber                <- Gen.choose(1, 99999)
        commodityCode             <- Gen.option(stringWithMaxLength(22))
        declarationType           <- Gen.option(arbitrary[DeclarationType])
        description               <- stringWithMaxLength(280)
        grossMass                 <- Gen.option(Gen.choose(0.0, 99999999.999).map(BigDecimal(_)))
        netMass                   <- Gen.option(Gen.choose(0.0, 99999999.999).map(BigDecimal(_)))
        countryOfDispatch         <- arbitrary[Country]
        countryOfDestination      <- arbitrary[Country]
        producedDocuments         <- listWithMaxSize(9, arbitrary[ProducedDocument])
        specialMentions           <- listWithMaxSize(9, arbitrary[SpecialMention])
        consignor                 <- Gen.option(arbitrary[Consignor])
        consignee                 <- Gen.option(arbitrary[Consignee])
        containers                <- listWithMaxSize(9, stringWithMaxLength(17))
        packages                  <- nonEmptyListWithMaxSize(9, arbitrary[Package])
        sensitiveGoodsInformation <- listWithMaxSize(9, arbitrary[SensitiveGoodsInformation])
      } yield
        GoodsItem(
          itemNumber,
          commodityCode,
          declarationType,
          description,
          grossMass,
          netMass,
          countryOfDispatch,
          countryOfDestination,
          producedDocuments,
          specialMentions,
          consignor,
          consignee,
          containers,
          packages,
          sensitiveGoodsInformation
        )
    }

  implicit lazy val arbitraryPermissionToStartUnloading: Arbitrary[PermissionToStartUnloading] =
    Arbitrary {

      for {
        mrn                 <- stringWithMaxLength(17) // TODO: Introduce MRN model
        declarationType     <- arbitrary[DeclarationType]
        transportId         <- Gen.option(stringWithMaxLength(27))
        transportCountry    <- Gen.option(arbitrary[Country])
        acceptanceDate      <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
        numberOfItems       <- Gen.choose(1, 99999)
        numberOfPackages    <- Gen.choose(1, 9999999)
        grossMass           <- Gen.choose(0.0, 99999999.999).map(BigDecimal(_))
        principal           <- arbitrary[Principal]
        traderAtDestination <- arbitrary[TraderAtDestination]
        presentationOffice  <- stringWithMaxLength(8)
        seals               <- listWithMaxSize(9, stringWithMaxLength(20))
        goodsItems          <- nonEmptyListWithMaxSize(9, arbitrary[GoodsItem])
      } yield
        PermissionToStartUnloading(
          mrn,
          declarationType,
          transportId,
          transportCountry,
          acceptanceDate,
          numberOfItems,
          numberOfPackages,
          grossMass,
          principal,
          traderAtDestination,
          presentationOffice,
          seals,
          goodsItems
        )
    }
}
