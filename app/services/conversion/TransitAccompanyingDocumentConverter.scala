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

package services.conversion

import cats.data.NonEmptyList
import cats.data.Validated.Valid
import cats.implicits._
import models.P5.departure.IE015
import models.P5.departure.IE029
import models.P5.departure.IE029MessageData
import models._
import models.reference._
import services._
import utils.FormattedDate
import viewmodels.CustomsOfficeWithOptionalDate
import viewmodels.PreviousDocumentType
import viewmodels.TransitAccompanyingDocumentP5TransitionPDF

object TransitAccompanyingDocumentConverter extends Converter with ConversionHelpers {

  def toViewModel(
    transitAccompanyingDocument: models.ReleaseForTransit,
    countries: Seq[Country],
    additionalInfo: Seq[reference.AdditionalInformation],
    kindsOfPackage: Seq[KindOfPackage],
    documentTypes: Seq[DocumentType],
    departureOffice: CustomsOfficeWithOptionalDate,
    destinationOffice: CustomsOfficeWithOptionalDate,
    transitOffices: Seq[CustomsOfficeWithOptionalDate],
    previousDocumentTypes: Seq[PreviousDocumentTypes],
    controlResult: Option[viewmodels.ControlResult]
  ): ValidationResult[viewmodels.TransitAccompanyingDocumentPDF] =
    (
      convertCountryOfDispatch(transitAccompanyingDocument.header.countryOfDispatch, countries),
      convertCountryOfDestination(transitAccompanyingDocument.header.countryOfDestination, countries),
      PrincipalConverter.toViewModel(transitAccompanyingDocument.principal, "principal", countries),
      convertTransportCountry(transitAccompanyingDocument.header.transportCountry, countries),
      convertGoodsItems(transitAccompanyingDocument.goodsItems, countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes),
      convertConsignor(transitAccompanyingDocument.consignor, countries),
      convertConsignee(transitAccompanyingDocument.consignee, countries),
      convertReturnCopiesCustomsOffice(transitAccompanyingDocument.returnCopiesCustomsOffice, countries)
    ).mapN(
      (dispatch, destination, principal, transportCountry, goodsItems, consignor, consignee, returnCopiesCustomsOffice) =>
        viewmodels.TransitAccompanyingDocumentPDF(
          movementReferenceNumber = transitAccompanyingDocument.header.movementReferenceNumber,
          declarationType = transitAccompanyingDocument.header.declarationType,
          singleCountryOfDispatch = dispatch,
          singleCountryOfDestination = destination,
          transportIdentity = transitAccompanyingDocument.header.transportIdentity,
          transportCountry = transportCountry,
          acceptanceDate = Some(FormattedDate(transitAccompanyingDocument.header.acceptanceDate)),
          numberOfItems = transitAccompanyingDocument.header.numberOfItems,
          numberOfPackages = transitAccompanyingDocument.header.numberOfPackages,
          grossMass = transitAccompanyingDocument.header.grossMass,
          printBindingItinerary = transitAccompanyingDocument.header.printBindingItinerary,
          authId = transitAccompanyingDocument.header.authId,
          copyType = transitAccompanyingDocument.header.returnCopy,
          principal = principal,
          consignor = consignor,
          consignee = consignee,
          departureOffice = departureOffice,
          destinationOffice = destinationOffice,
          customsOfficeOfTransit = transitOffices,
          guaranteeDetails = transitAccompanyingDocument.guaranteeDetails.toList,
          seals = transitAccompanyingDocument.seals,
          returnCopiesCustomsOffice = returnCopiesCustomsOffice,
          controlResult = controlResult,
          goodsItems = goodsItems
        )
    )

  // TODO pass ref data values here along with main model (IE029Data)

  def fromP5ToViewModel(
    ie029: IE029,
    ie015: IE015,
    countries: Seq[Country]
  ): ValidationResult[TransitAccompanyingDocumentP5TransitionPDF] = {

    ie029.data match {
      case IE029MessageData(
            transitOperation,
            authorisation,
            officeOfDeparture,
            officeOfDestination,
            officesOfTransit,
            officeOfExit,
            holderOfTransit,
            representative,
            controlResult,
            guarantee,
            consignment
          ) =>
        def intStringToBool(intString: String) = intString match {
          case "1" => true
          case _   => false
        }

        Valid(
          viewmodels.TransitAccompanyingDocumentP5TransitionPDF(
            movementReferenceNumber = s"${transitOperation.MRN},${transitOperation.LRN}",      // P5
            declarationType = transitOperation.declarationType,                                // P5
            singleCountryOfDispatch = consignment.countryOfDispatch.map(Country(_, "")),       // P5
            singleCountryOfDestination = consignment.countryOfDestination.map(Country(_, "")), // P5
            transportIdentity = consignment.departureTransportMeansIdentity,                   // P5
            transportCountry = countries
              .find(
                country =>
                  country.code == consignment.DepartureTransportMeans
                    .map {
                      departureTransportMeans =>
                        departureTransportMeans.head.nationality
                    }
                    .getOrElse("")
              ),
            limitDate = ie015.data.TransitOperation.limitDate.getOrElse(""),
            acceptanceDate = transitOperation.declarationAcceptanceDate.map(FormattedDate(_)), // P5
            numberOfItems = consignment.totalItems,
            numberOfPackages = Some(consignment.totalPackages),
            grossMass = consignment.grossMass,                                          // P5
            printBindingItinerary = intStringToBool(transitOperation.bindingItinerary), // P5
            authId = ie029.data.authorisationDisplay,                                   // P5
            goodsReference = consignment.TransportEquipment.flatMap(_.head.GoodsReference).getOrElse(Seq.empty),
            copyType = false,
            holderOfTransitProcedure = holderOfTransit,
            consignor = consignment.Consignor, // P5
            consignee = consignment.Consignee, // P5
            departureOffice = officeOfDeparture,
            destinationOffice = officeOfDestination,
            customsOfficeOfTransit = officesOfTransit.getOrElse(Seq.empty),
            previousDocuments = consignment.PreviousDocument.getOrElse(Seq.empty),
            transportDocuments = consignment.TransportDocument.getOrElse(Seq.empty),
            supportingDocuments = consignment.SupportingDocument.getOrElse(Seq.empty),
            additionalInformation = consignment.AdditionalInformation.getOrElse(Seq.empty),
            additionalReferences = consignment.AdditionalReference.getOrElse(Seq.empty),
            guaranteeType = guarantee.map(_.head.guaranteeType.getOrElse("")).get,
            seals = Seq.empty,
            returnCopiesCustomsOffice = None,
            controlResult = None,
            goodsItems = {
              val goodsItemList = consignment.consignmentItems.map {
                consignmentItem =>
                  viewmodels.GoodsItemP5Transition(
                    itemNumber = s"${consignmentItem.goodsItemNumber}/${consignmentItem.declarationGoodsItemNumber}",
                    commodityCode = Some(consignmentItem.commodityCode),
                    declarationType = DeclarationType.values.find(
                      declarationType => consignmentItem.declarationType.getOrElse("") == declarationType.toString
                    ),
                    description = consignmentItem.descriptionOfGoods,
                    grossMass = Some(BigDecimal(consignmentItem.grossMass)),
                    netMass = Some(BigDecimal(consignmentItem.netMass)),
                    countryOfDispatch = countries.find(
                      country => country.code == consignmentItem.countryOfDispatch.getOrElse("")
                    ),
                    countryOfDestination = countries.find(
                      country => country.code == consignmentItem.countryOfDestination.getOrElse("")
                    ),
                    methodOfPayment = consignmentItem.TransportCharges.map(
                      transportCharge => transportCharge.toString
                    ),
                    commercialReferenceNumber = None,
                    unDangerGoodsCode = None,
                    transportDocuments = consignmentItem.TransportDocument.getOrElse(Seq.empty),
                    supportingDocuments = consignmentItem.SupportingDocument.getOrElse(Seq.empty),
                    previousDocuments = consignmentItem.PreviousDocument.getOrElse(Seq.empty),
                    additionalInformation = consignmentItem.AdditionalInformation.getOrElse(Seq.empty),
                    additionalReferences = consignmentItem.AdditionalReference.getOrElse(Seq.empty),
                    consignor = None,
                    consignee = consignmentItem.Consignee,
                    containers = consignment.TransportEquipment
                      .map(
                        transportEquipments =>
                          transportEquipments.map(
                            transportEquipment => transportEquipment.display
                          )
                      )
                      .getOrElse(Seq.empty),
                    packages = consignmentItem.Packaging,
                    sensitiveGoodsInformation = Seq.empty,
                    securityConsignor = None,
                    securityConsignee = None
                  )
              }.toList
              NonEmptyList.fromList(goodsItemList).get
            }
          )
        )
    }

  }

}
