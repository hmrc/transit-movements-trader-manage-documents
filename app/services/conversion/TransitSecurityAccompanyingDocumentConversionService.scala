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

import cats.data.Validated.Invalid
import cats.implicits._
import connectors.ReferenceDataConnector
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.CustomsOfficeWithOptionalDate

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class TransitSecurityAccompanyingDocumentConversionService @Inject()(referenceData: ReferenceDataConnector) {

  def toViewModel(transitAccompanyingDocument: models.ReleaseForTransit,
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[viewmodels.TransitAccompanyingDocumentPDF]] = {

    val countriesFuture             = referenceData.countries()
    val additionalInfoFuture        = referenceData.additionalInformation()
    val kindsOfPackageFuture        = referenceData.kindsOfPackage()
    val documentTypesFuture         = referenceData.documentTypes()
    val previousDocumentTypesFuture = referenceData.previousDocumentTypes()

    lazy val controlResultFuture = Future.sequence(transitAccompanyingDocument.controlResult.toList.map(code =>
      referenceData.controlResultByCode(code.conResCodERS16).map(crd => viewmodels.ControlResult(crd, code))))

    lazy val departureOfficeFuture = referenceData
      .customsOfficeSearch(transitAccompanyingDocument.departureOffice)
      .map(office => CustomsOfficeWithOptionalDate(office, None))

    lazy val destinationOfficeFuture = referenceData
      .customsOfficeSearch(transitAccompanyingDocument.destinationOffice)
      .map(office => CustomsOfficeWithOptionalDate(office, None))

    lazy val transitOfficeFuture = Future.sequence(
      transitAccompanyingDocument.customsOfficeOfTransit.map(
        office =>
          referenceData
            .customsOfficeSearch(office.reference)
            .map(customsOffice => CustomsOfficeWithOptionalDate(customsOffice, office.arrivalTime, maxLength = 18))
      )
    )

    for {
      countriesResult        <- countriesFuture
      additionalInfoResult   <- additionalInfoFuture
      kindsOfPackageResult   <- kindsOfPackageFuture
      documentTypesResult    <- documentTypesFuture
      previousDocumentResult <- previousDocumentTypesFuture
      departureOffice        <- departureOfficeFuture
      destinationOffice      <- destinationOfficeFuture
      transitOffice          <- transitOfficeFuture
      controlResult          <- controlResultFuture
    } yield {
      (
        countriesResult,
        additionalInfoResult,
        kindsOfPackageResult,
        documentTypesResult,
        previousDocumentResult,
      ).mapN(
          (countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes) =>
            TransitAccompanyingDocumentConverter.toViewModel(
              transitAccompanyingDocument,
              countries,
              additionalInfo,
              kindsOfPackage,
              documentTypes,
              departureOffice,
              destinationOffice,
              transitOffice,
              previousDocumentTypes,
              controlResult.headOption
          )
        )
        .fold(
          errors => Invalid(errors),
          result => result
        )
    }
  }
}
