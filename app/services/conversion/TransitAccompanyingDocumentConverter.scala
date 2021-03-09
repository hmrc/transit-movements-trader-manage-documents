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

  def toViewModel(transitAccompanyingDocument: models.TransitAccompanyingDocument,
                  countries: Seq[Country],
                  additionalInfo: Seq[AdditionalInformation],
                  kindsOfPackage: Seq[KindOfPackage],
                  documentTypes: Seq[DocumentType],
                  departureOffice: CustomsOfficeWithOptionalDate,
                  destinationOffice: CustomsOfficeWithOptionalDate,
                  transitOffices: Seq[CustomsOfficeWithOptionalDate],
                  previousDocumentTypes: Seq[PreviousDocumentTypes]): ValidationResult[viewmodels.TransitAccompanyingDocumentPDF] =
    (
      convertCountryOfDispatch(transitAccompanyingDocument.countryOfDispatch, countries),
      convertCountryOfDestination(transitAccompanyingDocument.countryOfDestination, countries),
      PrincipalConverter.toViewModel(transitAccompanyingDocument.principal, "principal", countries),
      convertTransportCountry(transitAccompanyingDocument.transportCountry, countries),
      convertGoodsItems(transitAccompanyingDocument.goodsItems, countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes),
      convertConsignor(transitAccompanyingDocument.consignor, countries),
      convertConsignee(transitAccompanyingDocument.consignee, countries),
    ).mapN(
      (dispatch, destination, principal, transportCountry, goodsItems, consignor, consignee) =>
        viewmodels.TransitAccompanyingDocumentPDF(
          transitAccompanyingDocument.movementReferenceNumber,
          transitAccompanyingDocument.declarationType,
          dispatch,
          destination,
          transitAccompanyingDocument.transportIdentity,
          transportCountry,
          Some(FormattedDate(transitAccompanyingDocument.acceptanceDate)),
          transitAccompanyingDocument.numberOfItems,
          transitAccompanyingDocument.numberOfPackages,
          transitAccompanyingDocument.grossMass,
          transitAccompanyingDocument.printBindingItinerary,
          transitAccompanyingDocument.authId,
          transitAccompanyingDocument.returnCopy,
          principal,
          consignor,
          consignee,
          departureOffice,
          destinationOffice,
          transitOffices,
          transitAccompanyingDocument.guaranteeDetails.toList,
          transitAccompanyingDocument.seals,
          transitAccompanyingDocument.controlResult,
          goodsItems
      )
    )

}
