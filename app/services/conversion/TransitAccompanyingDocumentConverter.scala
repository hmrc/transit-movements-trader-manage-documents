/*
 * Copyright 2020 HM Revenue & Customs
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
import cats.data.Validated.Valid
import models.reference.Country
import services.Converter
import services.ValidationResult

object TransitAccompanyingDocumentConverter extends Converter {

  def toViewModel(transitAccompanyingDocument: models.TransitAccompanyingDocument,
                  countries: Seq[Country]): ValidationResult[viewmodels.tad.TransitAccompanyingDocument] = {

    def convertCountryOfDispatch(maybeCountryOfDispatch: Option[String]): ValidationResult[Option[Country]] =
      maybeCountryOfDispatch match {
        case Some(countryOfDispatch) => findReferenceData(countryOfDispatch, countries, s"countryOfDispatch").map(x => Some(x))
        case None                    => Valid(None)
      }

    def convertCountryOfDestination(maybeCountryOfDestination: Option[String]): ValidationResult[Option[Country]] =
      maybeCountryOfDestination match {
        case Some(countryOfDestination) => findReferenceData(countryOfDestination, countries, s"countryOfDestination").map(x => Some(x))
        case None                       => Valid(None)
      }

    (
      convertCountryOfDispatch(transitAccompanyingDocument.countryOfDispatch),
      convertCountryOfDestination(transitAccompanyingDocument.countryOfDestination),
    ).mapN(
      (dispatch, destination) =>
        viewmodels.tad.TransitAccompanyingDocument(
          transitAccompanyingDocument.localReferenceNumber,
          transitAccompanyingDocument.declarationType,
          dispatch,
          destination
      )
    )

  }

}
