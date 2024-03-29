/*
 * Copyright 2023 HM Revenue & Customs
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

import models.ControlResult
import models.GuaranteeDetails
import models.GuaranteeReference
import models.Itinerary
import models.PreviousAdministrativeReference
import models.SensitiveGoodsInformation
import models.reference._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import utils.FormattedDate
import viewmodels._

import java.time.LocalDate
import java.time.LocalDateTime

trait ViewModelGenerators extends GeneratorHelpers with ReferenceModelGenerators {

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

  implicit lazy val arbitraryTADSpecialMentions: Arbitrary[models.SpecialMention] =
    Arbitrary {
      for {
        additionalInformation      <- Gen.option(string)
        additionalInformationCoded <- stringWithMaxLength(5)
        exportFromEC               <- arbitrary[Option[Boolean]]
        exportFromCountry          <- Gen.option(string)
      } yield models.SpecialMention(additionalInformation, additionalInformationCoded, exportFromEC, exportFromCountry)
    }

  implicit lazy val arbitrarySpecialMention: Arbitrary[SpecialMention] =
    Arbitrary {
      for {
        additionalInformation <- arbitrary[AdditionalInformation]
        specialMentions       <- arbitrary[models.SpecialMention]
      } yield SpecialMention(additionalInformation, specialMentions)
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
        name                   <- stringWithMaxLength(35)
        streetAndNumber        <- stringWithMaxLength(35)
        streetAndNumberTrimmed <- stringWithMaxLength(35)
        postCode               <- stringWithMaxLength(9)
        city                   <- stringWithMaxLength(35)
        country                <- arbitrary[Country]
        eori                   <- Gen.option(stringWithMaxLength(17))
      } yield Consignee(name, streetAndNumber, streetAndNumberTrimmed, postCode, city, country, eori)
    }

  implicit lazy val arbitraryConsignor: Arbitrary[Consignor] =
    Arbitrary {

      for {
        name                   <- stringWithMaxLength(35)
        streetAndNumber        <- stringWithMaxLength(35)
        streetAndNumberTrimmed <- stringWithMaxLength(35)
        postCode               <- stringWithMaxLength(9)
        city                   <- stringWithMaxLength(35)
        country                <- arbitrary[Country]
        eori                   <- Gen.option(stringWithMaxLength(17))
      } yield Consignor(name, streetAndNumber, streetAndNumberTrimmed, postCode, city, country, eori)
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
        tir             <- Gen.option(stringWithMaxLength(17))
      } yield Principal(name, streetAndNumber, streetAndNumber, postCode, city, country, eori, tir)
    }

  implicit lazy val arbitrarySensitiveGoodsInformation: Arbitrary[SensitiveGoodsInformation] =
    Arbitrary {
      for {
        goodsCode <- Gen.option(stringWithMaxLength(2))
        quantity  <- Gen.choose(1, 99999)
      } yield SensitiveGoodsInformation(goodsCode, quantity)
    }

  implicit lazy val arbitraryPreviousAdministrativeReference: Arbitrary[PreviousAdministrativeReference] =
    Arbitrary {
      for {
        docType <- Gen.alphaNumStr
        docRef  <- Gen.alphaNumStr
        comInf  <- Gen.option(Gen.alphaNumStr)
      } yield PreviousAdministrativeReference(documentType = docType, documentReference = docRef, complimentOfInfo = comInf)
    }

  implicit lazy val arbitraryPreviousDocumentType: Arbitrary[PreviousDocumentType] =
    Arbitrary {
      for {
        prevDocTypes <- arbitrary[PreviousDocumentTypes]
        prevAdminRef <- arbitrary[PreviousAdministrativeReference]
      } yield PreviousDocumentType(prevDocTypes, prevAdminRef)
    }

  implicit lazy val arbitraryGoodsItem: Arbitrary[GoodsItem] =
    Arbitrary {
      for {
        itemNumber                <- Gen.choose(1, 99999)
        commodityCode             <- Gen.option(stringWithMaxLength(22))
        declarationType           <- Gen.option(arbitrary[String](arbitraryDeclarationType))
        description               <- stringWithMaxLength(280)
        grossMass                 <- Gen.option(Gen.choose(0.0, 99999999.999).map(BigDecimal(_)))
        netMass                   <- Gen.option(Gen.choose(0.0, 99999999.999).map(BigDecimal(_)))
        countryOfDispatch         <- Gen.option(arbitrary[Country])
        countryOfDestination      <- Gen.option(arbitrary[Country])
        methodOfPayment           <- Gen.option(stringWithMaxLength(1))
        commercialReferenceNumber <- Gen.option(stringWithMaxLength(17))
        unDangerGoodsCode         <- Gen.option(stringWithMaxLength(4))
        producedDocuments         <- listWithMaxSize(arbitrary[ProducedDocument], 9)
        previousDocuments         <- listWithMaxSize(arbitrary[PreviousDocumentType], 9)
        specialMentions           <- listWithMaxSize(arbitrary[SpecialMention], 9)
        consignor                 <- Gen.option(arbitrary[Consignor])
        consignee                 <- Gen.option(arbitrary[Consignee])
        containers                <- listWithMaxSize(stringWithMaxLength(17), 9)
        packages                  <- nonEmptyListWithMaxSize(arbitrary[Package], 9)
        sensitiveGoodsInformation <- listWithMaxSize(arbitrary[SensitiveGoodsInformation], 9)
        securityConsignor         <- Gen.option(arbitrary[SecurityConsignor])
        securityConsignee         <- Gen.option(arbitrary[SecurityConsignee])
      } yield GoodsItem(
        itemNumber.toString,
        commodityCode,
        declarationType,
        description,
        grossMass,
        netMass,
        countryOfDispatch,
        countryOfDestination,
        methodOfPayment,
        commercialReferenceNumber,
        unDangerGoodsCode,
        producedDocuments,
        previousDocuments,
        specialMentions,
        consignor,
        consignee,
        containers,
        packages,
        sensitiveGoodsInformation,
        securityConsignor,
        securityConsignee
      )
    }

  implicit lazy val arbitraryPermissionToStartUnloading: Arbitrary[PermissionToStartUnloading] =
    Arbitrary {

      for {
        mrn                   <- stringWithMaxLength(17)
        declarationType       <- arbitrary[String](arbitraryDeclarationType)
        countryOfDispatch     <- Gen.option(arbitrary[Country])
        countryOfDestination  <- Gen.option(arbitrary[Country])
        transportId           <- Gen.option(stringWithMaxLength(27))
        transportCountry      <- Gen.option(arbitrary[Country])
        acceptanceDate        <- Gen.option(datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now))
        acceptanceDateTrimmed <- Gen.option(stringWithMaxLength(8))
        numberOfItems         <- Gen.choose(1, 99999)
        numberOfPackages      <- Gen.option(Gen.choose(1, 9999999))
        grossMass             <- Gen.choose(0.0, 99999999.999).map(BigDecimal(_))
        principal             <- arbitrary[Principal]
        consignor             <- Gen.option(arbitrary[Consignor])
        consignee             <- Gen.option(arbitrary[Consignee])
        traderAtDestination   <- Gen.option(arbitrary[TraderAtDestination])
        departureOffice       <- stringWithMaxLength(8)
        presentationOffice    <- Gen.option(stringWithMaxLength(8))
        seals                 <- listWithMaxSize(stringWithMaxLength(20), 9)
        goodsItems            <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 9)
      } yield PermissionToStartUnloading(
        mrn,
        declarationType,
        countryOfDispatch,
        countryOfDestination,
        transportId,
        transportCountry,
        acceptanceDate,
        acceptanceDateTrimmed,
        numberOfItems,
        numberOfPackages,
        grossMass,
        principal,
        consignor,
        consignee,
        traderAtDestination,
        departureOffice,
        departureOffice,
        presentationOffice,
        seals,
        goodsItems
      )
    }

  implicit lazy val arbitraryFormattedDate: Arbitrary[FormattedDate] = Arbitrary {
    for {
      date <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
    } yield FormattedDate(date)
  }

  implicit lazy val arbitraryCustomsOffice: Arbitrary[CustomsOffice] = Arbitrary {
    for {
      id          <- stringWithMaxLength(6)
      description <- Gen.option(stringWithMaxLength(23))
      countryId   <- nonEmptyString
    } yield CustomsOffice(id, description, countryId)
  }

  implicit lazy val arbitraryCustomsOfficeWithOptionalDate: Arbitrary[CustomsOfficeWithOptionalDate] = Arbitrary {
    for {
      officeCode   <- arbitrary[CustomsOffice]
      optionalDate <- Gen.some(dateTimeBetween(LocalDateTime.of(1900, 1, 1, 0, 0), LocalDateTime.now))
    } yield CustomsOfficeWithOptionalDate(officeCode, optionalDate)
  }

  lazy val arbitraryCustomsOfficeWithoutOptionalDate: Arbitrary[CustomsOfficeWithOptionalDate] = Arbitrary {
    for {
      officeCode   <- arbitrary[CustomsOffice]
      optionalDate <- Gen.some(dateTimeBetween(LocalDateTime.of(1900, 1, 1, 0, 0), LocalDateTime.now))
    } yield CustomsOfficeWithOptionalDate(officeCode, optionalDate)
  }

  implicit lazy val arbitraryGuaranteeReference: Arbitrary[GuaranteeReference] = Arbitrary {
    for {
      guaranteeRef      <- Gen.option(stringWithMaxLength(23))
      otherGuaranteeRef <- stringWithMaxLength(23)
      notValidForEc     <- Gen.option(arbitrary[Boolean])
      notValidForOther  <- listWithMaxSize(stringWithMaxLength(24), 4)
    } yield GuaranteeReference(guaranteeRef, if (guaranteeRef.isDefined) None else Some(otherGuaranteeRef), notValidForEc, notValidForOther)
  }

  implicit lazy val arbitraryGuaranteeDetails: Arbitrary[GuaranteeDetails] = Arbitrary {
    for {
      guaranteeType <- Gen.oneOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "A")
      guaranteeRefs <- nonEmptyListWithMaxSize(arbitrary[GuaranteeReference], 5)
    } yield GuaranteeDetails(guaranteeType, guaranteeRefs.toList)
  }

  implicit lazy val arbitraryControlResult: Arbitrary[ControlResult] = Arbitrary {
    for {
      code <- stringWithMaxLength(23)
      date <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
    } yield ControlResult(code, date)
  }

  implicit lazy val arbitraryControlResultViewModel: Arbitrary[viewmodels.ControlResult] = Arbitrary {
    for {
      result <- arbitrary[ControlResult]
      data   <- arbitrary[ControlResultData]
    } yield viewmodels.ControlResult(data, result)
  }

  implicit lazy val arbitraryReturnCopiesCustomsOffice: Arbitrary[ReturnCopiesCustomsOffice] =
    Arbitrary {
      for {
        name            <- stringWithMaxLength(35)
        streetAndNumber <- stringWithMaxLength(35)
        postCode        <- stringWithMaxLength(9)
        city            <- stringWithMaxLength(35)
        countryCode     <- arbitrary[Country]
      } yield ReturnCopiesCustomsOffice(name, streetAndNumber, postCode, city, countryCode)
    }

  implicit lazy val arbitraryTransitAccompanyingDocument: Arbitrary[TransitAccompanyingDocumentPDF] =
    Arbitrary {
      for {
        mrn                       <- stringWithMaxLength(17)
        declarationType           <- arbitrary[String](arbitraryDeclarationType)
        countryOfDispatch         <- Gen.option(arbitrary[Country])
        countryOfDestination      <- Gen.option(arbitrary[Country])
        transportId               <- Gen.option(stringWithMaxLength(27))
        transportCountry          <- Gen.option(arbitrary[Country])
        acceptanceDate            <- Gen.option(arbitrary[FormattedDate])
        numberOfItems             <- Gen.choose(1, 99999)
        numberOfPackages          <- Gen.option(Gen.choose(1, 9999999))
        grossMass                 <- Gen.choose(0.0, 99999999.999).map(BigDecimal(_))
        printBindingItinerary     <- arbitrary[Boolean]
        authId                    <- Gen.option(stringWithMaxLength(12))
        copyType                  <- arbitrary[Boolean]
        principal                 <- arbitrary[Principal]
        consignor                 <- Gen.option(arbitrary[Consignor])
        consignee                 <- Gen.option(arbitrary[Consignee])
        departureOffice           <- arbitrary[CustomsOfficeWithOptionalDate](arbitraryCustomsOfficeWithoutOptionalDate)
        destinationOffice         <- arbitrary[CustomsOfficeWithOptionalDate](arbitraryCustomsOfficeWithoutOptionalDate)
        cusOfficesOfTransit       <- listWithMaxSize(arbitrary[CustomsOfficeWithOptionalDate], 5)
        guaranteeDetails          <- nonEmptyListWithMaxSize(arbitrary[GuaranteeDetails], 5)
        seals                     <- listWithMaxSize(stringWithMaxLength(20), 9)
        returnCopiesCustomsOffice <- Gen.option(arbitrary[ReturnCopiesCustomsOffice])
        controlResult             <- Gen.option(arbitrary[viewmodels.ControlResult])
        goodsItems                <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 9)
      } yield TransitAccompanyingDocumentPDF(
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
        printBindingItinerary,
        authId,
        copyType,
        principal,
        consignor,
        consignee,
        departureOffice,
        destinationOffice,
        cusOfficesOfTransit,
        guaranteeDetails.toList,
        seals,
        returnCopiesCustomsOffice,
        controlResult,
        goodsItems
      )
    }

  implicit lazy val arbitraryItinerary: Arbitrary[Itinerary] =
    Arbitrary {
      for {
        country <- Gen.pick(2, 'A' to 'Z')
      } yield Itinerary(country.mkString)
    }

  implicit lazy val arbitraryGoodsItemSecurityConsignee: Arbitrary[SecurityConsignee] =
    Arbitrary {
      for {
        name            <- Gen.option(stringWithMaxLength(35))
        streetAndNumber <- Gen.option(stringWithMaxLength(35))
        postCode        <- Gen.option(stringWithMaxLength(9))
        city            <- Gen.option(stringWithMaxLength(35))
        country         <- Gen.option(stringWithMaxLength(2))
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield SecurityConsignee(name, streetAndNumber, postCode, city, country, eori)
    }

  implicit lazy val arbitraryGoodsItemSecurityConsignor: Arbitrary[SecurityConsignor] =
    Arbitrary {
      for {
        name            <- Gen.option(stringWithMaxLength(35))
        streetAndNumber <- Gen.option(stringWithMaxLength(35))
        postCode        <- Gen.option(stringWithMaxLength(9))
        city            <- Gen.option(stringWithMaxLength(35))
        country         <- Gen.option(stringWithMaxLength(2))
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield SecurityConsignor(name, streetAndNumber, postCode, city, country, eori)
    }

  implicit lazy val arbitrarySafetyAndSecurityCarrier: Arbitrary[SafetyAndSecurityCarrier] =
    Arbitrary {
      for {
        name            <- Gen.option(stringWithMaxLength(35))
        streetAndNumber <- Gen.option(stringWithMaxLength(35))
        postCode        <- Gen.option(stringWithMaxLength(9))
        city            <- Gen.option(stringWithMaxLength(35))
        country         <- Gen.option(stringWithMaxLength(2))
        eori            <- Gen.option(stringWithMaxLength(17))
      } yield SafetyAndSecurityCarrier(name, streetAndNumber, postCode, city, country, eori)
    }

  implicit lazy val arbitraryTransitSecurityAccompanyingDocument: Arbitrary[TransitSecurityAccompanyingDocumentPDF] =
    Arbitrary {
      for {
        mrn                               <- stringWithMaxLength(17)
        declarationType                   <- arbitrary[String](arbitraryDeclarationType)
        countryOfDispatch                 <- Gen.option(arbitrary[Country])
        countryOfDestination              <- Gen.option(arbitrary[Country])
        transportId                       <- Gen.option(stringWithMaxLength(27))
        transportCountry                  <- Gen.option(arbitrary[Country])
        acceptanceDate                    <- Gen.option(arbitrary[FormattedDate])
        numberOfItems                     <- Gen.choose(1, 99999)
        numberOfPackages                  <- Gen.option(Gen.choose(1, 9999999))
        grossMass                         <- Gen.choose(0.0, 99999999.999).map(BigDecimal(_))
        printBindingItinerary             <- arbitrary[Boolean]
        authId                            <- Gen.option(stringWithMaxLength(12))
        copyType                          <- arbitrary[Boolean]
        circumstanceIndicator             <- Gen.option(stringWithMaxLength(1))
        methodOfPayment                   <- Gen.option(stringWithMaxLength(1))
        identityOfTransportCrossingBorder <- Gen.option(stringWithMaxLength(27))
        nationalityOfTransportAtBorder    <- Gen.option(stringWithMaxLength(2))
        transportModeAtBorder             <- Gen.option(stringWithMaxLength(2))
        agreedLocationOfGoodsCode         <- Gen.option(stringWithMaxLength(17))
        placeOfLoadingCode                <- Gen.option(stringWithMaxLength(17))
        placeOfUnLoadingCode              <- Gen.option(stringWithMaxLength(17))
        conveyanceReferenceNumber         <- Gen.option(stringWithMaxLength(17))
        security                          <- Gen.option(1)
        commercialReferenceNumber         <- Gen.option(stringWithMaxLength(12))
        principal                         <- arbitrary[Principal]
        consignor                         <- Gen.option(arbitrary[Consignor])
        consignee                         <- Gen.option(arbitrary[Consignee])
        departureOffice                   <- arbitrary[CustomsOfficeWithOptionalDate](arbitraryCustomsOfficeWithoutOptionalDate)
        destinationOffice                 <- arbitrary[CustomsOfficeWithOptionalDate](arbitraryCustomsOfficeWithoutOptionalDate)
        cusOfficesOfTransit               <- listWithMaxSize(arbitrary[CustomsOfficeWithOptionalDate], 5)
        guaranteeDetails                  <- nonEmptyListWithMaxSize(arbitrary[GuaranteeDetails], 5)
        seals                             <- listWithMaxSize(stringWithMaxLength(20), 9)
        returnCopiesCustomsOffice         <- Gen.option(arbitrary[ReturnCopiesCustomsOffice])
        controlResult                     <- Gen.option(arbitrary[viewmodels.ControlResult])
        goodsItems                        <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 9)
        itineraries                       <- listWithMaxSize(arbitrary[Itinerary], 9)
        safetyAndSecurityCarrier          <- Gen.option(arbitrary[SafetyAndSecurityCarrier])
        safetyAndSecurityConsignor        <- Gen.option(arbitrary[SecurityConsignor])
        safetyAndSecurityConsignee        <- Gen.option(arbitrary[SecurityConsignee])
      } yield TransitSecurityAccompanyingDocumentPDF(
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
        printBindingItinerary,
        authId,
        copyType,
        circumstanceIndicator,
        security,
        commercialReferenceNumber,
        methodOfPayment,
        identityOfTransportCrossingBorder,
        nationalityOfTransportAtBorder,
        transportModeAtBorder,
        agreedLocationOfGoodsCode,
        placeOfLoadingCode,
        placeOfUnLoadingCode,
        conveyanceReferenceNumber,
        principal,
        consignor,
        consignee,
        departureOffice,
        destinationOffice,
        cusOfficesOfTransit,
        guaranteeDetails.toList,
        seals,
        returnCopiesCustomsOffice,
        controlResult,
        goodsItems,
        itineraries,
        safetyAndSecurityCarrier,
        safetyAndSecurityConsignor,
        safetyAndSecurityConsignee
      )
    }
}
