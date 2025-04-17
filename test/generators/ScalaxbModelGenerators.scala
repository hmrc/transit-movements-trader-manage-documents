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

import generated.*
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import scalaxb.XMLCalendar

import java.time.{LocalDate, LocalDateTime}
import javax.xml.datatype.XMLGregorianCalendar

trait ScalaxbModelGenerators extends GeneratorHelpers {

  implicit lazy val arbitraryFlag: Arbitrary[Flag] =
    Arbitrary {
      for {
        bool <- arbitrary[Boolean]
      } yield if (bool) Number1 else Number0
    }

  implicit lazy val arbitraryMESSAGESequence: Arbitrary[MESSAGESequence] =
    Arbitrary {
      for {
        messageSender          <- nonEmptyString
        messageRecipient       <- nonEmptyString
        preparationDateAndTime <- arbitrary[XMLGregorianCalendar]
        messageIdentification  <- nonEmptyString
        messageType            <- arbitrary[MessageTypes]
      } yield MESSAGESequence(
        messageSender = messageSender,
        messageRecipient = messageRecipient,
        preparationDateAndTime = preparationDateAndTime,
        messageIdentification = messageIdentification,
        messageType = messageType,
        correlationIdentifier = None
      )
    }

  implicit lazy val arbitraryMessageTypes: Arbitrary[MessageTypes] =
    Arbitrary {
      Gen.oneOf(MessageTypes.values)
    }

  implicit lazy val arbitraryXMLGregorianCalendar: Arbitrary[XMLGregorianCalendar] =
    Arbitrary {
      XMLCalendar(LocalDateTime.now().toString)
    }
}

trait IE029ScalaxbModelGenerators extends ScalaxbModelGenerators {

  import viewmodels.tad.*

  implicit lazy val arbitraryCC029CType: Arbitrary[CC029CType] =
    Arbitrary {
      for {
        messageSequence1                   <- arbitrary[MESSAGESequence]
        transitOperation                   <- arbitrary[TransitOperation]
        customsOfficeOfDeparture           <- arbitrary[CustomsOfficeOfDeparture]
        customsOfficeOfDestinationDeclared <- arbitrary[CustomsOfficeOfDestinationDeclared]
        holderOfTheTransitProcedure        <- arbitrary[HolderOfTheTransitProcedure]
        consignment                        <- arbitrary[Consignment]
      } yield CC029CType(
        messageSequence1 = messageSequence1,
        TransitOperation = transitOperation,
        Authorisation = Nil,
        CustomsOfficeOfDeparture = customsOfficeOfDeparture,
        CustomsOfficeOfDestinationDeclared = customsOfficeOfDestinationDeclared,
        CustomsOfficeOfTransitDeclared = Nil,
        CustomsOfficeOfExitForTransitDeclared = Nil,
        HolderOfTheTransitProcedure = holderOfTheTransitProcedure,
        Representative = None,
        ControlResult = None,
        Guarantee = Nil,
        Consignment = consignment,
        attributes = Map.empty
      )
    }

  implicit lazy val arbitraryConsignment: Arbitrary[Consignment] =
    Arbitrary {
      for {
        containerIndicator <- arbitrary[Flag]
        grossMass          <- arbitrary[BigDecimal]
      } yield new Consignment(
        countryOfDispatch = None,
        countryOfDestination = None,
        containerIndicator = containerIndicator,
        inlandModeOfTransport = None,
        modeOfTransportAtTheBorder = None,
        grossMass = grossMass,
        referenceNumberUCR = None,
        Carrier = None,
        Consignor = None,
        Consignee = None,
        AdditionalSupplyChainActor = Nil,
        TransportEquipment = Nil,
        LocationOfGoods = None,
        DepartureTransportMeans = Nil,
        CountryOfRoutingOfConsignment = Nil,
        ActiveBorderTransportMeans = Nil,
        PlaceOfLoading = None,
        PlaceOfUnloading = None,
        PreviousDocument = Nil,
        SupportingDocument = Nil,
        TransportDocument = Nil,
        AdditionalReference = Nil,
        AdditionalInformation = Nil,
        TransportCharges = None,
        HouseConsignment = Nil
      )
    }

  implicit lazy val arbitraryHouseConsignment: Arbitrary[HouseConsignment] =
    Arbitrary {
      for {
        sequenceNumber <- arbitrary[BigInt]
        grossMass      <- arbitrary[BigDecimal]
      } yield new HouseConsignment(
        sequenceNumber = sequenceNumber,
        countryOfDispatch = None,
        grossMass = grossMass,
        referenceNumberUCR = None,
        securityIndicatorFromExportDeclaration = None,
        Consignor = None,
        Consignee = None,
        AdditionalSupplyChainActor = Nil,
        DepartureTransportMeans = Nil,
        PreviousDocument = Nil,
        SupportingDocument = Nil,
        TransportDocument = Nil,
        AdditionalReference = Nil,
        AdditionalInformation = Nil,
        TransportCharges = None,
        ConsignmentItem = Nil
      )
    }

  implicit lazy val arbitraryConsignmentItem: Arbitrary[ConsignmentItem] =
    Arbitrary {
      for {
        goodsItemNumber            <- arbitrary[BigInt]
        declarationGoodsItemNumber <- arbitrary[BigInt]
        commodity                  <- arbitrary[Commodity]
      } yield new ConsignmentItem(
        goodsItemNumber = goodsItemNumber,
        declarationGoodsItemNumber = declarationGoodsItemNumber,
        declarationType = None,
        countryOfDispatch = None,
        countryOfDestination = None,
        referenceNumberUCR = None,
        AdditionalSupplyChainActor = Nil,
        Commodity = commodity,
        Packaging = Nil,
        PreviousDocument = Nil,
        SupportingDocument = Nil,
        AdditionalReference = Nil,
        AdditionalInformation = Nil
      )
    }

  implicit lazy val arbitraryTransportEquipment: Arbitrary[TransportEquipment] =
    Arbitrary {
      for {
        sequenceNumber                <- arbitrary[BigInt]
        containerIdentificationNumber <- Gen.option(nonEmptyString)
        numberOfSeals                 <- arbitrary[BigInt]
      } yield new TransportEquipment(
        sequenceNumber = sequenceNumber,
        containerIdentificationNumber = containerIdentificationNumber,
        numberOfSeals = numberOfSeals,
        Seal = Nil,
        GoodsReference = Nil
      )
    }

  implicit lazy val arbitraryHolderOfTheTransitProcedure: Arbitrary[HolderOfTheTransitProcedure] =
    Arbitrary {
      for {
        identificationNumber          <- Gen.option(nonEmptyString)
        tirHolderIdentificationNumber <- Gen.option(nonEmptyString)
        name                          <- Gen.option(nonEmptyString)
      } yield new HolderOfTheTransitProcedure(
        identificationNumber = identificationNumber,
        TIRHolderIdentificationNumber = tirHolderIdentificationNumber,
        name = name,
        Address = None,
        ContactPerson = None
      )
    }

  implicit lazy val arbitraryCommodity: Arbitrary[Commodity] =
    Arbitrary {
      for {
        descriptionOfGoods <- nonEmptyString
        goodsMeasure       <- arbitrary[GoodsMeasure]
      } yield new Commodity(
        descriptionOfGoods = descriptionOfGoods,
        cusCode = None,
        CommodityCode = None,
        DangerousGoods = Nil,
        GoodsMeasure = goodsMeasure
      )
    }

  implicit lazy val arbitraryTransitOperation: Arbitrary[TransitOperation] =
    Arbitrary {
      for {
        lrn                       <- nonEmptyString
        mrn                       <- nonEmptyString
        declarationType           <- nonEmptyString
        additionalDeclarationType <- nonEmptyString
        declarationAcceptanceDate <- arbitrary[XMLGregorianCalendar]
        releaseDate               <- arbitrary[XMLGregorianCalendar]
        security                  <- nonEmptyString
        reducedDatasetIndicator   <- arbitrary[Flag]
        bindingItinerary          <- arbitrary[Flag]
      } yield new TransitOperation(
        LRN = lrn,
        MRN = mrn,
        declarationType = declarationType,
        additionalDeclarationType = additionalDeclarationType,
        TIRCarnetNumber = None,
        declarationAcceptanceDate = declarationAcceptanceDate,
        releaseDate = releaseDate,
        security = security,
        reducedDatasetIndicator = reducedDatasetIndicator,
        specificCircumstanceIndicator = None,
        communicationLanguageAtDeparture = None,
        bindingItinerary = bindingItinerary
      )
    }

  implicit lazy val arbitraryGoodsMeasure: Arbitrary[GoodsMeasure] =
    Arbitrary {
      for {
        grossMass <- Gen.option(arbitrary[BigDecimal])
        netMass   <- Gen.option(arbitrary[BigDecimal])
      } yield new GoodsMeasure(
        grossMass = grossMass,
        netMass = netMass
      )
    }

  implicit lazy val arbitraryCustomsOfficeOfDeparture: Arbitrary[CustomsOfficeOfDeparture] =
    Arbitrary {
      for {
        referenceNumber <- nonEmptyString
      } yield new CustomsOfficeOfDeparture(
        referenceNumber = referenceNumber
      )
    }

  implicit lazy val arbitraryCustomsOfficeOfDestinationDeclared: Arbitrary[CustomsOfficeOfDestinationDeclared] =
    Arbitrary {
      for {
        referenceNumber <- nonEmptyString
      } yield new CustomsOfficeOfDestinationDeclared(
        referenceNumber = referenceNumber
      )
    }
}

trait IE043ScalaxbModelGenerators extends ScalaxbModelGenerators {

  import viewmodels.unloadingpermission.*

  implicit lazy val arbitraryCC043CType: Arbitrary[CC043CType] =
    Arbitrary {
      for {
        messageSequence1                 <- arbitrary[MESSAGESequence]
        transitOperation                 <- arbitrary[TransitOperation]
        customsOfficeOfDestinationActual <- arbitrary[CustomsOfficeOfDestinationActual]
        traderAtDestination              <- arbitrary[TraderAtDestination]
      } yield CC043CType(
        messageSequence1 = messageSequence1,
        TransitOperation = transitOperation,
        CustomsOfficeOfDestinationActual = customsOfficeOfDestinationActual,
        HolderOfTheTransitProcedure = None,
        TraderAtDestination = traderAtDestination,
        CTLControl = None,
        Consignment = None,
        attributes = Map.empty
      )
    }

  implicit lazy val arbitraryConsignmentItem: Arbitrary[ConsignmentItem] =
    Arbitrary {
      for {
        goodsItemNumber            <- arbitrary[BigInt]
        declarationGoodsItemNumber <- arbitrary[BigInt]
        commodity                  <- arbitrary[Commodity]
      } yield new ConsignmentItem(
        goodsItemNumber = goodsItemNumber,
        declarationGoodsItemNumber = declarationGoodsItemNumber,
        declarationType = None,
        countryOfDestination = None,
        Consignee = None,
        Commodity = commodity,
        Packaging = Nil,
        PreviousDocument = Nil,
        SupportingDocument = Nil,
        TransportDocument = Nil,
        AdditionalReference = Nil,
        AdditionalInformation = Nil
      )
    }

  implicit lazy val arbitraryCustomsOfficeOfDestinationActuak: Arbitrary[CustomsOfficeOfDestinationActual] =
    Arbitrary {
      for {
        referenceNumber <- nonEmptyString
      } yield new CustomsOfficeOfDestinationActual(
        referenceNumber = referenceNumber
      )
    }

  implicit lazy val arbitraryCommodity: Arbitrary[Commodity] =
    Arbitrary {
      for {
        descriptionOfGoods <- nonEmptyString
        goodsMeasure       <- arbitrary[GoodsMeasure]
      } yield new Commodity(
        descriptionOfGoods = descriptionOfGoods,
        cusCode = None,
        CommodityCode = None,
        DangerousGoods = Nil,
        GoodsMeasure = goodsMeasure
      )
    }

  implicit lazy val arbitraryGoodsMeasure: Arbitrary[GoodsMeasure] =
    Arbitrary {
      for {
        grossMass <- Gen.option(arbitrary[BigDecimal])
        netMass   <- Gen.option(arbitrary[BigDecimal])
      } yield new GoodsMeasure(
        grossMass = grossMass,
        netMass = netMass
      )
    }

  implicit lazy val arbitraryTraderAtDestination: Arbitrary[TraderAtDestination] =
    Arbitrary {
      for {
        identificationNumber <- nonEmptyString
      } yield new TraderAtDestination(
        identificationNumber = identificationNumber
      )
    }

  implicit lazy val arbitraryTransitOperation: Arbitrary[TransitOperation] =
    Arbitrary {
      for {
        mrn                     <- nonEmptyString
        security                <- nonEmptyString
        reducedDatasetIndicator <- arbitrary[Flag]
      } yield new TransitOperation(
        MRN = mrn,
        declarationType = None,
        declarationAcceptanceDate = None,
        security = security,
        reducedDatasetIndicator = reducedDatasetIndicator
      )
    }
}

trait IE015ScalaxbModelGenerators extends ScalaxbModelGenerators {

  import models.IE015
  import models.IE015.*

  implicit lazy val arbitraryIE015: Arbitrary[IE015] = {
    implicit lazy val arbitraryTransitOperation: Arbitrary[TransitOperation] =
      Arbitrary {
        for {
          limitDate <- Gen.option(arbitrary[LocalDate])
        } yield TransitOperation(
          limitDate = limitDate
        )
      }

    implicit lazy val arbitraryConsignment: Arbitrary[Consignment] =
      Arbitrary {
        for {
          houseConsignments <- listWithMaxSize[HouseConsignment]()
        } yield Consignment(
          HouseConsignment = houseConsignments
        )
      }

    implicit lazy val arbitraryHouseConsignment: Arbitrary[HouseConsignment] =
      Arbitrary {
        for {
          consignmentItems <- listWithMaxSize[ConsignmentItem]()
        } yield HouseConsignment(
          ConsignmentItem = consignmentItems
        )
      }

    implicit lazy val arbitraryConsignmentItem: Arbitrary[ConsignmentItem] =
      Arbitrary {
        for {
          declarationGoodsItemNumber <- arbitrary[BigInt]
          commodity                  <- arbitrary[Commodity]
        } yield ConsignmentItem(
          declarationGoodsItemNumber = declarationGoodsItemNumber,
          Commodity = commodity
        )
      }

    implicit lazy val arbitraryCommodity: Arbitrary[Commodity] =
      Arbitrary {
        for {
          goodsMeasure <- Gen.option(arbitrary[GoodsMeasure])
        } yield Commodity(
          GoodsMeasure = goodsMeasure
        )
      }

    implicit lazy val arbitraryGoodsMeasure: Arbitrary[GoodsMeasure] =
      Arbitrary {
        for {
          supplementaryUnits <- Gen.option(arbitrary[BigDecimal])
        } yield GoodsMeasure(
          supplementaryUnits = supplementaryUnits
        )
      }

    Arbitrary {
      for {
        transitOperation <- arbitrary[TransitOperation]
        consignment      <- arbitrary[Consignment]
      } yield IE015(
        TransitOperation = transitOperation,
        Consignment = consignment
      )
    }
  }
}
