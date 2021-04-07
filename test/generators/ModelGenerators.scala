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

package generators

import models._
import models.reference._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import viewmodels.PreviousDocumentType

import java.time.LocalDate
import java.time.LocalDateTime

trait ModelGenerators extends GeneratorHelpers {

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

  protected val countrySpecificCodes      = Seq("DG0", "DG1")
  protected val countrySpecificCodeGen    = Gen.oneOf(countrySpecificCodes)
  protected val nonCountrySpecificCodeGen = stringWithMaxLength(5).suchThat(!countrySpecificCodes.contains(_))

  implicit lazy val arbitraryTraderAtDestinationWithEori: Arbitrary[TraderAtDestinationWithEori] =
    Arbitrary {

      for {
        eori            <- stringWithMaxLength(17)
        name            <- Gen.option(stringWithMaxLength(35))
        streetAndNumber <- Gen.option(stringWithMaxLength(35))
        postCode        <- Gen.option(stringWithMaxLength(9))
        city            <- Gen.option(stringWithMaxLength(35))
        countryCode     <- Gen.option(stringWithMaxLength(2))
      } yield TraderAtDestinationWithEori(eori, name, streetAndNumber, postCode, city, countryCode)
    }

  implicit lazy val arbitraryTraderAtDestinationWithoutEori: Arbitrary[TraderAtDestinationWithoutEori] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        countryCode     <- stringWithMaxLength(2)
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
        country         <- stringWithMaxLength(2)
        nadLanguageCode <- Gen.option(stringWithMaxLength(2))
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield Consignee(name, streetAndNumber, postCode, city, country, nadLanguageCode, eori)
    }

  implicit lazy val arbitraryConsignor: Arbitrary[Consignor] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        country         <- stringWithMaxLength(2)
        nadLanguageCode <- Gen.option(stringWithMaxLength(2))
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield Consignor(name, streetAndNumber, postCode, city, country, nadLanguageCode, eori)
    }

  implicit lazy val arbitraryPrincipal: Arbitrary[Principal] =
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        country         <- stringWithMaxLength(2)
        eori            <- Gen.option(stringWithMaxLength(17))
        tir             <- Gen.option(stringWithMaxLength(17))
      } yield Principal(name, streetAndNumber, postCode, city, country, eori, tir)
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
        countryOfDispatch         <- Gen.option(stringWithMaxLength(2))
        countryOfDestination      <- Gen.option(stringWithMaxLength(2))
        producedDocuments         <- listWithMaxSize(2, arbitrary[ProducedDocument])
        previousAdminRef          <- listWithMaxSize(2, arbitrary[PreviousAdministrativeReference])
        specialMentions           <- listWithMaxSize(2, arbitrary[SpecialMention])
        consignor                 <- Gen.option(arbitrary[Consignor])
        consignee                 <- Gen.option(arbitrary[Consignee])
        containers                <- listWithMaxSize(2, stringWithMaxLength(17))
        packages                  <- nonEmptyListWithMaxSize(2, arbitrary[Package])
        sensitiveGoodsInformation <- listWithMaxSize(2, arbitrary[SensitiveGoodsInformation])
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
          previousAdminRef,
          producedDocuments,
          specialMentions,
          consignor,
          consignee,
          containers,
          packages,
          sensitiveGoodsInformation
        )
    }

  implicit lazy val arbitraryPermissionToContinueUnloading: Arbitrary[PermissionToContinueUnloading] =
    Arbitrary {

      for {
        mrn                <- stringWithMaxLength(17)
        continue           <- Gen.choose(1, 9)
        presentationOffice <- stringWithMaxLength(8)
        trader             <- arbitrary[TraderAtDestination]
      } yield PermissionToContinueUnloading(mrn, continue, presentationOffice, trader)
    }

  implicit lazy val arbitraryPermissionToStartUnloading: Arbitrary[PermissionToStartUnloading] =
    Arbitrary {

      for {
        mrn                  <- stringWithMaxLength(17)
        declarationType      <- arbitrary[DeclarationType]
        countryOfDispatch    <- Gen.option(stringWithMaxLength(2))
        countryOfDestination <- Gen.option(stringWithMaxLength(2))
        transportId          <- Gen.option(stringWithMaxLength(27))
        transportCountry     <- Gen.option(stringWithMaxLength(2))
        acceptanceDate       <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
        numberOfItems        <- Gen.choose(1, 99999)
        numberOfPackages     <- Gen.option(Gen.choose(1, 9999999))
        grossMass            <- Gen.choose(0.0, 99999999.999).map(BigDecimal(_))
        principal            <- arbitrary[Principal]
        consignor            <- Gen.option(arbitrary[Consignor])
        consignee            <- Gen.option(arbitrary[Consignee])
        traderAtDestination  <- arbitrary[TraderAtDestination]
        departureOffice      <- stringWithMaxLength(8)
        presentationOffice   <- stringWithMaxLength(8)
        seals                <- listWithMaxSize(2, stringWithMaxLength(20))
        goodsItems           <- nonEmptyListWithMaxSize(2, arbitrary[GoodsItem])
      } yield
        PermissionToStartUnloading(
          mrn,
          declarationType,
          countryOfDispatch,
          countryOfDestination,
          transportId,
          transportCountry,
          acceptanceDate,
          numberOfItems,
          numberOfPackages,
          grossMass,
          principal,
          consignor,
          consignee,
          traderAtDestination,
          departureOffice,
          presentationOffice,
          seals,
          goodsItems
        )
    }

  implicit lazy val arbitraryCustomsOfficeOfTransit: Arbitrary[CustomsOfficeOfTransit] = Arbitrary {
    for {
      reference <- stringWithMaxLength(25)
      dateTime  <- Gen.option(dateTimeBetween(LocalDateTime.of(1970, 1, 1, 0, 0), LocalDateTime.now()))
    } yield CustomsOfficeOfTransit(reference, dateTime)
  }

  implicit lazy val arbitraryGuaranteeReference: Arbitrary[GuaranteeReference] = Arbitrary {
    for {
      guaranteeRef      <- Gen.option(stringWithMaxLength(23))
      otherGuaranteeRef <- stringWithMaxLength(23)
      notValidForEc     <- Gen.option(arbitrary[Boolean])
      notValidForOther  <- listWithMaxSize(4, stringWithMaxLength(24))
    } yield GuaranteeReference(guaranteeRef, if (guaranteeRef.isDefined) None else Some(otherGuaranteeRef), notValidForEc, notValidForOther)
  }

  implicit lazy val arbitraryGuaranteeDetails: Arbitrary[GuaranteeDetails] = Arbitrary {
    for {
      guaranteeType <- Gen.oneOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "A")
      guaranteeRefs <- nonEmptyListWithMaxSize(5, arbitrary[GuaranteeReference])
    } yield GuaranteeDetails(guaranteeType, guaranteeRefs.toList)
  }

  implicit lazy val arbitraryControlResult: Arbitrary[ControlResult] = Arbitrary {
    for {
      code <- stringWithMaxLength(23)
      date <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
    } yield ControlResult(code, date)
  }

  implicit lazy val arbitraryTransitAccompanyingDocument: Arbitrary[ReleaseForTransit] =
    Arbitrary {
      for {
        mrn                       <- stringWithMaxLength(22)
        declarationType           <- arbitrary[DeclarationType]
        countryOfDispatch         <- Gen.option(stringWithMaxLength(2))
        countryOfDestination      <- Gen.option(stringWithMaxLength(2))
        transportId               <- Gen.option(stringWithMaxLength(27))
        transportCountry          <- Gen.option(stringWithMaxLength(2))
        acceptanceDate            <- datesBetween(LocalDate.of(1970, 1, 1), LocalDate.now())
        numberOfItems             <- Gen.choose(1, 99999)
        numberOfPackages          <- Gen.option(Gen.choose(1, 9999999))
        grossMass                 <- Gen.choose(0.0, 99999999.999).map(BigDecimal(_))
        bindingItinerary          <- arbitrary[Boolean]
        authId                    <- Gen.option(stringWithMaxLength(25))
        returnCopy                <- arbitrary[Boolean]
        principal                 <- arbitrary[Principal]
        consignor                 <- Gen.option(arbitrary[Consignor])
        consignee                 <- Gen.option(arbitrary[Consignee])
        customsOffOfTransit       <- listWithMaxSize(9, arbitrary[CustomsOfficeOfTransit])
        guaranteeDetails          <- nonEmptyListWithMaxSize(9, arbitrary[GuaranteeDetails])
        departureOffice           <- stringWithMaxLength(8)
        destinationOffice         <- stringWithMaxLength(8)
        returnCopiesCustomsOffice <- Gen.option(arbitrary[ReturnCopiesCustomsOffice])
        controlResult             <- Gen.option(arbitrary[ControlResult])
        seals                     <- listWithMaxSize(2, stringWithMaxLength(20))
        goodsItems                <- nonEmptyListWithMaxSize(2, arbitrary[GoodsItem])
      } yield
        ReleaseForTransit(
          mrn,
          declarationType,
          countryOfDispatch,
          countryOfDestination,
          transportId,
          transportCountry,
          acceptanceDate,
          numberOfItems,
          numberOfPackages,
          grossMass,
          bindingItinerary,
          authId,
          returnCopy,
          principal,
          consignor,
          consignee,
          customsOffOfTransit,
          guaranteeDetails,
          departureOffice,
          destinationOffice,
          returnCopiesCustomsOffice,
          controlResult,
          seals,
          goodsItems
        )
    }

  implicit lazy val arbitraryCountry: Arbitrary[Country] = {
    Arbitrary {
      for {
        state       <- arbitrary[String]
        code        <- Gen.pick(2, 'A' to 'Z')
        description <- arbitrary[String]
      } yield Country(state, code.mkString, description)
    }
  }

  implicit lazy val arbitraryTraderAtDestinationWithEoriViewModel: Arbitrary[viewmodels.TraderAtDestinationWithEori] = {
    Arbitrary {
      for {
        eori            <- stringWithMaxLength(17)
        name            <- Gen.option(stringWithMaxLength(35))
        streetAndNumber <- Gen.option(stringWithMaxLength(35))
        postCode        <- Gen.option(stringWithMaxLength(9))
        city            <- Gen.option(stringWithMaxLength(35))
        countryCode     <- Gen.option(arbitrary[Country])
      } yield viewmodels.TraderAtDestinationWithEori(eori, name, streetAndNumber, postCode, city, countryCode)
    }
  }

  implicit lazy val arbitraryTraderAtDestinationWithoutEoriViewModel: Arbitrary[viewmodels.TraderAtDestinationWithoutEori] = {
    Arbitrary {

      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        country         <- arbitrary[Country]
      } yield viewmodels.TraderAtDestinationWithoutEori(name, streetAndNumber, postCode, city, country)
    }
  }

  implicit lazy val arbitraryTraderAtDestinationViewModel: Arbitrary[viewmodels.TraderAtDestination] = {
    Arbitrary {
      Gen.oneOf[viewmodels.TraderAtDestination](
        arbitrary[viewmodels.TraderAtDestinationWithEori],
        arbitrary[viewmodels.TraderAtDestinationWithoutEori]
      )
    }
  }

  implicit lazy val arbitraryDocumentType: Arbitrary[DocumentType] = {
    Arbitrary {
      for {
        code              <- arbitrary[String]
        description       <- arbitrary[String]
        transportDocument <- arbitrary[Boolean]
      } yield DocumentType(code, description, transportDocument)
    }
  }

  implicit lazy val arbitraryProducedDocumentViewModel: Arbitrary[viewmodels.ProducedDocument] = {
    Arbitrary {

      for {
        documentType <- arbitrary[DocumentType]
        reference    <- Gen.option(stringWithMaxLength(35))
        complement   <- Gen.option(stringWithMaxLength(26))
      } yield viewmodels.ProducedDocument(documentType, reference, complement)
    }
  }

  implicit lazy val arbitraryAdditionalInformationViewModel: Arbitrary[AdditionalInformation] = {
    Arbitrary {
      for {
        code        <- arbitrary[String]
        description <- arbitrary[String]
      } yield AdditionalInformation(code, description)
    }
  }

  implicit lazy val arbitraryTADSpecialMentions: Arbitrary[models.SpecialMention] = {
    Arbitrary {
      for {
        additionalInformation      <- Gen.option(stringWithMaxLength(10))
        additionalInformationCoded <- stringWithMaxLength(5)
        exportFromEC               <- arbitrary[Option[Boolean]]
        exportFromCountry          <- Gen.option(stringWithMaxLength(2))
      } yield models.SpecialMention(additionalInformation, additionalInformationCoded, exportFromEC, exportFromCountry)
    }
  }

  implicit lazy val arbitraryTADSpecialMention: Arbitrary[viewmodels.SpecialMention] =
    Arbitrary {
      for {
        additionalInformation <- arbitrary[AdditionalInformation]
        specialMentions       <- arbitrary[models.SpecialMention]
        country               <- arbitrary[Option[Country]]
      } yield viewmodels.SpecialMention(additionalInformation, specialMentions)
    }

  implicit lazy val arbitraryConsigneeViewModel: Arbitrary[viewmodels.Consignee] =
    Arbitrary {

      for {
        name                   <- stringWithMaxLength(35)
        streetAndNumber        <- stringWithMaxLength(35)
        streetAndNumberTrimmed <- stringWithMaxLength(35)
        postCode               <- stringWithMaxLength(9)
        city                   <- stringWithMaxLength(35)
        country                <- arbitrary[Country]
        eori                   <- Gen.option(stringWithMaxLength(17))
      } yield viewmodels.Consignee(name, streetAndNumber, streetAndNumberTrimmed, postCode, city, country, eori)
    }

  implicit lazy val arbitraryConsignorViewModel: Arbitrary[viewmodels.Consignor] =
    Arbitrary {

      for {
        name                   <- stringWithMaxLength(35)
        streetAndNumber        <- stringWithMaxLength(35)
        streetAndNumberTrimmed <- stringWithMaxLength(35)
        postCode               <- stringWithMaxLength(9)
        city                   <- stringWithMaxLength(35)
        country                <- arbitrary[Country]
        eori                   <- Gen.option(stringWithMaxLength(17))
      } yield viewmodels.Consignor(name, streetAndNumber, streetAndNumberTrimmed, postCode, city, country, eori)
    }

  implicit lazy val arbitraryKindOfPackage: Arbitrary[KindOfPackage] = {
    Arbitrary {
      for {
        code        <- arbitrary[String]
        description <- arbitrary[String]
      } yield KindOfPackage(code, description)
    }
  }

  implicit lazy val arbitraryBulkPackageViewModel: Arbitrary[viewmodels.BulkPackage] =
    Arbitrary {

      for {
        kindOfPackage   <- arbitrary[KindOfPackage]
        marksAndNumbers <- Gen.option(stringWithMaxLength(42))
      } yield viewmodels.BulkPackage(kindOfPackage, marksAndNumbers)
    }

  implicit lazy val arbitraryUnpackedPackageViewModel: Arbitrary[viewmodels.UnpackedPackage] =
    Arbitrary {

      for {
        kindOfPackage   <- arbitrary[KindOfPackage]
        numberOfPieces  <- Gen.choose(1, 99999)
        marksAndNumbers <- Gen.option(stringWithMaxLength(42))
      } yield viewmodels.UnpackedPackage(kindOfPackage, numberOfPieces, marksAndNumbers)
    }

  implicit lazy val arbitraryRegularPackageViewModel: Arbitrary[viewmodels.RegularPackage] =
    Arbitrary {

      for {
        kindOfPackage    <- arbitrary[KindOfPackage]
        numberOfPackages <- Gen.choose(0, 99999)
        marksAndNumbers  <- stringWithMaxLength(42)
      } yield viewmodels.RegularPackage(kindOfPackage, numberOfPackages, marksAndNumbers)
    }

  implicit lazy val arbitraryPackageViewModel: Arbitrary[viewmodels.Package] =
    Arbitrary {
      Gen.oneOf(arbitrary[viewmodels.BulkPackage], arbitrary[viewmodels.UnpackedPackage], arbitrary[viewmodels.RegularPackage])
    }

  implicit lazy val arbitraryPreviousDocumentTypes: Arbitrary[PreviousDocumentTypes] =
    Arbitrary {
      for {
        code        <- stringWithMaxLength(6)
        description <- stringWithMaxLength(50)
      } yield PreviousDocumentTypes(code, description)
    }

  implicit lazy val arbitraryPreviousAdministrativeReference: Arbitrary[PreviousAdministrativeReference] = {
    Arbitrary {
      for {
        docType <- Gen.alphaNumStr
        docRef  <- Gen.alphaNumStr
        comInf  <- Gen.option(Gen.alphaNumStr)
      } yield PreviousAdministrativeReference(documentType = docType, documentReference = docRef, complimentOfInfo = comInf)
    }
  }

  implicit lazy val arbitraryPreviousDocumentType: Arbitrary[PreviousDocumentType] =
    Arbitrary {
      for {
        prevDocTypes <- arbitrary[PreviousDocumentTypes]
        prevAdminRef <- arbitrary[PreviousAdministrativeReference]
      } yield PreviousDocumentType(prevDocTypes, prevAdminRef)
    }

  implicit lazy val arbitraryGoodsItemViewModel: Arbitrary[viewmodels.GoodsItem] = {
    Arbitrary {

      for {
        itemNumber                <- Gen.choose(1, 99999)
        commodityCode             <- Gen.option(stringWithMaxLength(22))
        declarationType           <- Gen.option(arbitrary[DeclarationType])
        description               <- stringWithMaxLength(280)
        grossMass                 <- Gen.option(Gen.choose(0.0, 99999999.999).map(BigDecimal(_)))
        netMass                   <- Gen.option(Gen.choose(0.0, 99999999.999).map(BigDecimal(_)))
        countryOfDispatch         <- Gen.option(arbitrary[Country])
        countryOfDestination      <- Gen.option(arbitrary[Country])
        producedDocuments         <- listWithMaxSize(2, arbitrary[viewmodels.ProducedDocument])
        previousDocTypes          <- listWithMaxSize(2, arbitrary[viewmodels.PreviousDocumentType])
        specialMentions           <- listWithMaxSize(2, arbitrary[viewmodels.SpecialMention])
        consignor                 <- Gen.option(arbitrary[viewmodels.Consignor])
        consignee                 <- Gen.option(arbitrary[viewmodels.Consignee])
        containers                <- listWithMaxSize(2, stringWithMaxLength(17))
        packages                  <- nonEmptyListWithMaxSize(2, arbitrary[viewmodels.Package])
        sensitiveGoodsInformation <- listWithMaxSize(2, arbitrary[SensitiveGoodsInformation])
      } yield
        viewmodels.GoodsItem(
          itemNumber,
          commodityCode,
          declarationType,
          description,
          grossMass,
          netMass,
          countryOfDispatch,
          countryOfDestination,
          producedDocuments,
          previousDocTypes,
          specialMentions,
          consignor,
          consignee,
          containers,
          packages,
          sensitiveGoodsInformation
        )
    }
  }

  implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
    datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
  }

  implicit lazy val arbitraryReturnCopiesCustomsOffice: Arbitrary[ReturnCopiesCustomsOffice] =
    Arbitrary {
      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        countryCode     <- stringWithMaxLength(2)
      } yield ReturnCopiesCustomsOffice(name, streetAndNumber, postCode, city, countryCode)
    }
}
