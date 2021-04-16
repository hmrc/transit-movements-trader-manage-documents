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

package services.conversion

import cats.implicits._
import models.reference._
import services._
import utils.FormattedDate
import viewmodels.CustomsOfficeWithOptionalDate

object TransitSecurityAccompanyingDocumentConverter extends Converter with ConversionHelpers {

  def toViewModel(releaseForTransit: models.ReleaseForTransit,
                  countries: Seq[Country],
                  additionalInfo: Seq[AdditionalInformation],
                  kindsOfPackage: Seq[KindOfPackage],
                  documentTypes: Seq[DocumentType],
                  departureOffice: CustomsOfficeWithOptionalDate,
                  destinationOffice: CustomsOfficeWithOptionalDate,
                  transitOffices: Seq[CustomsOfficeWithOptionalDate],
                  previousDocumentTypes: Seq[PreviousDocumentTypes],
                  controlResult: Option[viewmodels.ControlResult],
                  circumstanceIndicators: Seq[CircumstanceIndicator]): ValidationResult[viewmodels.TransitSecurityAccompanyingDocumentPDF] =
    (
      convertCountryOfDispatch(releaseForTransit.header.countryOfDispatch, countries),
      convertCountryOfDestination(releaseForTransit.header.countryOfDestination, countries),
      PrincipalConverter.toViewModel(releaseForTransit.principal, "principal", countries),
      convertTransportCountry(releaseForTransit.header.transportCountry, countries),
      convertGoodsItems(releaseForTransit.goodsItems, countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes),
      convertConsignor(releaseForTransit.consignor, countries),
      convertConsignee(releaseForTransit.consignee, countries),
      convertReturnCopiesCustomsOffice(releaseForTransit.returnCopiesCustomsOffice, countries),
      convertCircumstanceIndicator(releaseForTransit.header.circumstanceIndicator, circumstanceIndicators),
    ).mapN(
      (dispatch, destination, principal, transportCountry, goodsItems, consignor, consignee, returnCopiesCustomsOffice, circumstanceIndicator) =>
        viewmodels.TransitSecurityAccompanyingDocumentPDF(
          movementReferenceNumber = releaseForTransit.header.movementReferenceNumber,
          declarationType = releaseForTransit.header.declarationType,
          singleCountryOfDispatch = dispatch,
          singleCountryOfDestination = destination,
          transportIdentity = releaseForTransit.header.transportIdentity,
          transportCountry = transportCountry,
          acceptanceDate = Some(FormattedDate(releaseForTransit.header.acceptanceDate)),
          numberOfItems = releaseForTransit.header.numberOfItems,
          numberOfPackages = releaseForTransit.header.numberOfPackages,
          grossMass = releaseForTransit.header.grossMass,
          printBindingItinerary = releaseForTransit.header.printBindingItinerary,
          authId = releaseForTransit.header.authId,
          copyType = releaseForTransit.header.returnCopy,
          circumstanceIndicator = circumstanceIndicator,
          security = releaseForTransit.header.security,
          commercialReferenceNumber = releaseForTransit.header.commercialReferenceNumber,
          methodOfPayment = releaseForTransit.header.methodOfPayment,
          identityOfTransportAtBorder = releaseForTransit.header.identityOfTransportAtBorder,
          nationalityOfTransportAtBorder = releaseForTransit.header.nationalityOfTransportAtBorder,
          transportModeAtBorder = releaseForTransit.header.transportModeAtBorder,
          agreedLocationOfGoodsCode = releaseForTransit.header.agreedLocationOfGoodsCode,
          placeOfLoadingCode = releaseForTransit.header.placeOfLoadingCode,
          placeOfUnloadingCode = releaseForTransit.header.placeOfUnloadingCode,
          conveyanceReferenceNumber = releaseForTransit.header.conveyanceReferenceNumber,
          principal = principal,
          consignor = consignor,
          consignee = consignee,
          departureOffice = departureOffice,
          destinationOffice = destinationOffice,
          customsOfficeOfTransit = transitOffices,
          guaranteeDetails = releaseForTransit.guaranteeDetails.toList,
          seals = releaseForTransit.seals,
          returnCopiesCustomsOffice = returnCopiesCustomsOffice,
          controlResult = controlResult,
          goodsItems = goodsItems,
          itineraries = releaseForTransit.itineraries,
          safetyAndSecurityCarrier = releaseForTransit.safetyAndSecurityCarrier.map(SecurityCarrierConverter.convertToSafetyAndSecurityCarrierVM),
          safetyAndSecurityConsignor = releaseForTransit.safetyAndSecurityConsignor.map(SecurityConsignorConverter.convertToSecurityConsignorVM),
          safetyAndSecurityConsignee = releaseForTransit.safetyAndSecurityConsignee.map(SecurityConsigneeConverter.convertToSecurityConsigneeVM)
      )
    )

}
