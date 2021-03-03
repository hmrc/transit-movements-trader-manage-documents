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

import cats.data.NonEmptyList
import cats.data.Validated.Valid
import cats.implicits._
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import services._
import utils.FormattedDate
import utils.ShortenedString
import utils.StringTransformer._

object TransitAccompanyingDocumentConverter extends Converter with Helpers {

  def toViewModel(mrn: String,
                  transitAccompanyingDocument: models.TransitAccompanyingDocument,
                  countries: Seq[Country],
                  additionalInfo: Seq[AdditionalInformation],
                  kindsOfPackage: Seq[KindOfPackage],
                  documentTypes: Seq[DocumentType]): ValidationResult[viewmodels.TransitAccompanyingDocumentPDF] =
    (
      convertCountryOfDispatch(transitAccompanyingDocument.countryOfDispatch, countries),
      convertCountryOfDestination(transitAccompanyingDocument.countryOfDestination, countries),
      PrincipalConverter.toViewModel(transitAccompanyingDocument.principal, "principal", countries),
      convertTransportCountry(transitAccompanyingDocument.transportCountry, countries),
      convertGoodsItems(transitAccompanyingDocument.goodsItems, countries, additionalInfo, kindsOfPackage, documentTypes),
      convertConsignor(transitAccompanyingDocument.consignor, countries),
      convertConsignee(transitAccompanyingDocument.consignee, countries)
    ).mapN(
      (dispatch, destination, principal, transportCountry, goodsItems, consignor, consignee) =>
        viewmodels.TransitAccompanyingDocumentPDF(
          mrn,
          transitAccompanyingDocument.declarationType,
          dispatch,
          destination,
          transitAccompanyingDocument.transportIdentity,
          transportCountry,
          Some(FormattedDate(transitAccompanyingDocument.acceptanceDate)),
          None,
          transitAccompanyingDocument.numberOfItems,
          transitAccompanyingDocument.numberOfPackages,
          transitAccompanyingDocument.grossMass,
          transitAccompanyingDocument.printBindingItinerary,
          transitAccompanyingDocument.authId,
          principal,
          consignor,
          consignee,
          None,
          ShortenedString(transitAccompanyingDocument.departureOffice, 45, "***"),
          ShortenedString(transitAccompanyingDocument.destinationOffice, 45, "***"),
          None,
          transitAccompanyingDocument.seals,
          transitAccompanyingDocument.controlResult,
          goodsItems
      )
    )

}
