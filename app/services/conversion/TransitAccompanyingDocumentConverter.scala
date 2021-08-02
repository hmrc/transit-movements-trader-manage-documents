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

object TransitAccompanyingDocumentConverter extends Converter with ConversionHelpers {

  def toViewModel(
    transitAccompanyingDocument: models.ReleaseForTransit,
    countries: Seq[Country],
    additionalInfo: Seq[AdditionalInformation],
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

}
