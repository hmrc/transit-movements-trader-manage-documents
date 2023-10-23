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

import models.P5.departure._
import models.P5.unloading.CustomsOfficeOfDestinationActual
import models.P5.unloading.IE043Data
import models.P5.unloading.UnloadingMessageData
import models.P5.unloading.{HolderOfTransitProcedure => IE043HolderOfTransitProcedure}
import models.P5.unloading.{TransitOperation => IE043TransitOperation}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

import java.time.LocalDate

trait P5ModelGenerators extends GeneratorHelpers {

  implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
    datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
  }

  implicit lazy val arbitraryIE015TransitOperation: Arbitrary[IE015TransitOperation] =
    Arbitrary {
      for {
        limitDate <- Gen.option(nonEmptyString)
      } yield IE015TransitOperation(limitDate)
    }

  implicit lazy val arbitraryIE029TransitOperation: Arbitrary[IE029TransitOperation] =
    Arbitrary {
      for {
        lrn                              <- nonEmptyString
        mrn                              <- nonEmptyString
        declarationType                  <- arbitrary[String](arbitraryDeclarationType)
        additionalDeclarationType        <- nonEmptyString
        tirCarnetNumber                  <- Gen.option(nonEmptyString)
        declarationAcceptanceDate        <- arbitrary[LocalDate]
        releaseDate                      <- arbitrary[LocalDate]
        security                         <- nonEmptyString
        reducedDatasetIndicator          <- nonEmptyString
        specificCircumstanceIndicator    <- Gen.option(nonEmptyString)
        communicationLanguageAtDeparture <- Gen.option(nonEmptyString)
        bindingItinerary                 <- nonEmptyString
      } yield IE029TransitOperation(
        lrn,
        mrn,
        declarationType,
        additionalDeclarationType,
        tirCarnetNumber,
        declarationAcceptanceDate,
        releaseDate,
        security,
        reducedDatasetIndicator,
        specificCircumstanceIndicator,
        communicationLanguageAtDeparture,
        bindingItinerary
      )
    }

  implicit lazy val arbitraryIE015MessageData: Arbitrary[IE015MessageData] =
    Arbitrary {
      for {
        ie015TransitOperation <- arbitrary[IE015TransitOperation]
      } yield IE015MessageData(ie015TransitOperation)
    }

  implicit lazy val arbitraryAuthorisation: Arbitrary[Authorisation] =
    Arbitrary {
      for {
        sequenceNumber    <- nonEmptyString
        authorisationType <- nonEmptyString
        referenceNumber   <- nonEmptyString
      } yield Authorisation(sequenceNumber, authorisationType, referenceNumber)
    }

  implicit lazy val arbitraryCustomsOfficeOfDeparture: Arbitrary[CustomsOfficeOfDeparture] =
    Arbitrary {
      for {
        referenceNumber <- nonEmptyString
      } yield CustomsOfficeOfDeparture(referenceNumber)
    }

  implicit lazy val arbitraryCustomsOfficeOfDestinationDeclared: Arbitrary[CustomsOfficeOfDestinationDeclared] =
    Arbitrary {
      for {
        referenceNumber <- nonEmptyString
      } yield CustomsOfficeOfDestinationDeclared(referenceNumber)
    }

  implicit lazy val arbitraryCustomsOfficeOfTransitDeclared: Arbitrary[CustomsOfficeOfTransitDeclared] =
    Arbitrary {
      for {
        sequenceNumber  <- nonEmptyString
        referenceNumber <- nonEmptyString
      } yield CustomsOfficeOfTransitDeclared(sequenceNumber, referenceNumber, None)
    }

  implicit lazy val arbitraryAddress: Arbitrary[Address] =
    Arbitrary {
      for {
        streetAndNumber <- nonEmptyString
        postcode        <- Gen.option(nonEmptyString)
        city            <- nonEmptyString
        country         <- nonEmptyString
      } yield Address(streetAndNumber, postcode, city, country)
    }

  implicit lazy val arbitraryContactPerson: Arbitrary[ContactPerson] =
    Arbitrary {
      for {
        name         <- nonEmptyString
        phoneNumber  <- nonEmptyString
        eMailAddress <- Gen.option(nonEmptyString)
      } yield ContactPerson(name, phoneNumber, eMailAddress)
    }

  implicit lazy val arbitraryHolderOfTheTransitProcedure: Arbitrary[HolderOfTransitProcedure] =
    Arbitrary {
      for {
        identificationNumber          <- Gen.option(nonEmptyString)
        tirHolderIdentificationNumber <- Gen.option(nonEmptyString)
        name                          <- Gen.option(nonEmptyString)
        address                       <- Gen.option(arbitrary[Address])
        contactPerson                 <- Gen.option(arbitrary[ContactPerson])
      } yield HolderOfTransitProcedure(
        identificationNumber,
        tirHolderIdentificationNumber,
        name,
        address,
        contactPerson
      )
    }

  implicit lazy val arbitraryIE043HolderOfTheTransitProcedure: Arbitrary[IE043HolderOfTransitProcedure] =
    Arbitrary {
      for {
        name <- nonEmptyString
      } yield IE043HolderOfTransitProcedure(
        None,
        None,
        name,
        None
      )
    }

  implicit lazy val arbitraryP5GuaranteeReference: Arbitrary[GuaranteeReference] =
    Arbitrary {
      for {
        sequenceNumber    <- nonEmptyString
        grn               <- Gen.option(nonEmptyString)
        accessCode        <- Gen.option(nonEmptyString)
        amountToBeCovered <- arbitrary[Double]
        currency          <- nonEmptyString
      } yield GuaranteeReference(
        sequenceNumber,
        grn,
        accessCode,
        amountToBeCovered,
        currency
      )
    }

  implicit lazy val arbitraryGuarantee: Arbitrary[Guarantee] =
    Arbitrary {
      for {
        sequenceNumber          <- nonEmptyString
        guaranteeType           <- nonEmptyString
        otherGuaranteeReference <- Gen.option(nonEmptyString)
        guaranteeReferences     <- Gen.option(listWithMaxSize[GuaranteeReference]())
      } yield Guarantee(
        sequenceNumber,
        guaranteeType,
        otherGuaranteeReference,
        guaranteeReferences.map(_.toList)
      )
    }

  implicit lazy val arbitraryCommodity: Arbitrary[Commodity] =
    Arbitrary {
      for {
        descriptionOfGoods <- nonEmptyString
      } yield Commodity(
        descriptionOfGoods,
        None,
        None,
        None,
        None
      )
    }

  implicit lazy val arbitraryPackaging: Arbitrary[Packaging] =
    Arbitrary {
      for {
        sequenceNumber <- nonEmptyString
        typeOfPackages <- nonEmptyString
      } yield Packaging(
        sequenceNumber,
        None,
        typeOfPackages,
        None
      )
    }

  implicit lazy val arbitraryConsignmentItem: Arbitrary[ConsignmentItem] =
    Arbitrary {
      for {
        goodsItemNumber            <- nonEmptyString
        declarationGoodsItemNumber <- arbitrary[Int]
        commodity                  <- arbitrary[Commodity]
        packaging                  <- listWithMaxSize[Packaging]()
      } yield ConsignmentItem(
        goodsItemNumber,
        declarationGoodsItemNumber,
        None,
        None,
        None,
        None,
        None,
        None,
        commodity,
        packaging,
        None,
        None,
        None,
        None,
        None,
        None
      )
    }

  implicit lazy val arbitraryHouseConsignment: Arbitrary[HouseConsignment] =
    Arbitrary {
      for {
        sequenceNumber   <- nonEmptyString
        grossMass        <- arbitrary[Double]
        consignmentItems <- nonEmptyListWithMaxSize[ConsignmentItem]()
      } yield HouseConsignment(
        sequenceNumber,
        None,
        grossMass,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        consignmentItems.toList
      )
    }

  implicit lazy val arbitraryConsignment: Arbitrary[Consignment] =
    Arbitrary {
      for {
        containerIndicator <- nonEmptyString
        grossMass          <- arbitrary[Double]
        houseConsignments  <- listWithMaxSize[HouseConsignment]()
      } yield Consignment(
        None,
        None,
        containerIndicator,
        None,
        None,
        grossMass,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        None,
        houseConsignments
      )
    }

  implicit lazy val arbitraryIE029MessageData: Arbitrary[IE029MessageData] =
    Arbitrary {
      for {
        ie029TransitOperation              <- arbitrary[IE029TransitOperation]
        authorisations                     <- Gen.option(listWithMaxSize[Authorisation]())
        customsOfficeOfDeparture           <- arbitrary[CustomsOfficeOfDeparture]
        customsOfficeOfDestinationDeclared <- arbitrary[CustomsOfficeOfDestinationDeclared]
        holderOfTransitProcedure           <- arbitrary[HolderOfTransitProcedure]
        consignment                        <- arbitrary[Consignment]
        guarantees                         <- listWithMaxSize[Guarantee]()
      } yield IE029MessageData(
        ie029TransitOperation,
        authorisations.map(_.toList),
        customsOfficeOfDeparture,
        customsOfficeOfDestinationDeclared,
        None,
        None,
        holderOfTransitProcedure,
        None,
        None,
        guarantees.toList,
        consignment
      )
    }

  implicit lazy val arbitraryIE015: Arbitrary[IE015] =
    Arbitrary {
      for {
        ie015MessageData <- arbitrary[IE015MessageData]
      } yield IE015(ie015MessageData)
    }

  implicit lazy val arbitraryIE029: Arbitrary[IE029] =
    Arbitrary {
      for {
        ie029MessageData <- arbitrary[IE029MessageData]
      } yield IE029(ie029MessageData)
    }

  implicit lazy val arbitraryIE043TransitOperation: Arbitrary[IE043TransitOperation] =
    Arbitrary {
      for {
        mrn                     <- nonEmptyString
        security                <- nonEmptyString
        reducedDatasetIndicator <- nonEmptyString
      } yield IE043TransitOperation(
        mrn,
        None,
        None,
        security,
        reducedDatasetIndicator
      )
    }

  implicit lazy val arbitraryCustomsOfficeOfDestinationActual: Arbitrary[CustomsOfficeOfDestinationActual] =
    Arbitrary {
      for {
        referenceNumber <- nonEmptyString
      } yield CustomsOfficeOfDestinationActual(referenceNumber)
    }

  implicit lazy val arbitraryUnloadingMessageData: Arbitrary[UnloadingMessageData] =
    Arbitrary {
      for {
        ie043TransitOperation            <- arbitrary[IE043TransitOperation]
        customsOfficeOfDestinationActual <- arbitrary[CustomsOfficeOfDestinationActual]
        ie043HolderOfTransitProcedure    <- arbitrary[IE043HolderOfTransitProcedure]
      } yield UnloadingMessageData(
        ie043TransitOperation,
        customsOfficeOfDestinationActual,
        ie043HolderOfTransitProcedure,
        None
      )
    }

  implicit lazy val arbitraryIE043Data: Arbitrary[IE043Data] =
    Arbitrary {
      for {
        data <- arbitrary[UnloadingMessageData]
      } yield IE043Data(data)
    }
}
