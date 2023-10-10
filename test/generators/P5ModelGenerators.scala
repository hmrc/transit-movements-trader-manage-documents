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

import models.DeclarationType
import models.P5.departure._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

import java.time.LocalDate

trait P5ModelGenerators extends GeneratorHelpers {

  implicit lazy val arbitraryDeclarationType: Arbitrary[DeclarationType] =
    Arbitrary {
      Gen.oneOf(DeclarationType.values)
    }

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
        declarationType                  <- arbitrary[DeclarationType]
        additionalDeclarationType        <- nonEmptyString
        tirCarnetNumber                  <- Gen.option(nonEmptyString)
        declarationAcceptanceDate        <- Gen.option(arbitrary[LocalDate])
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
        sequenceNumber    <- Gen.option(nonEmptyString)
        authorisationType <- Gen.option(nonEmptyString)
        referenceNumber   <- Gen.option(nonEmptyString)
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

  implicit lazy val arbitraryIE029MessageData: Arbitrary[IE029MessageData] =
    Arbitrary {
      for {
        ie029TransitOperation              <- arbitrary[IE029TransitOperation]
        authorisations                     <- Gen.option(listWithMaxSize[Authorisation]().map(_.toList))
        customsOfficeOfDeparture           <- arbitrary[CustomsOfficeOfDeparture]
        customsOfficeOfDestinationDeclared <- arbitrary[CustomsOfficeOfDestinationDeclared]
        holderOfTransitProcedure           <- arbitrary[HolderOfTransitProcedure]
        consignment                        <- arbitrary[Consignment]
      } yield IE029MessageData(
        ie029TransitOperation,
        authorisations,
        customsOfficeOfDeparture,
        customsOfficeOfDestinationDeclared,
        None,
        None,
        holderOfTransitProcedure,
        None,
        None,
        None,
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
        ie015MessageData <- arbitrary[IE015MessageData]
      } yield IE015(ie015MessageData)
    }
}
