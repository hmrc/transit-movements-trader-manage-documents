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

  def toViewModel(releaseForTransit: models.ReleaseForTransit,
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[viewmodels.TransitSecurityAccompanyingDocumentPDF]] = {

    val countriesFuture              = referenceData.countries()
    val additionalInfoFuture         = referenceData.additionalInformation()
    val kindsOfPackageFuture         = referenceData.kindsOfPackage()
    val documentTypesFuture          = referenceData.documentTypes()
    val previousDocumentTypesFuture  = referenceData.previousDocumentTypes()
    val circumstanceIndicatorsFuture = referenceData.circumstanceIndicators()

    lazy val controlResultFuture = Future.sequence(releaseForTransit.controlResult.toList.map(code =>
      referenceData.controlResultByCode(code.conResCodERS16).map(crd => viewmodels.ControlResult(crd, code))))

    lazy val departureOfficeFuture = referenceData
      .customsOfficeSearch(releaseForTransit.departureOffice)
      .map(office => CustomsOfficeWithOptionalDate(office, None))

    lazy val destinationOfficeFuture = referenceData
      .customsOfficeSearch(releaseForTransit.destinationOffice)
      .map(office => CustomsOfficeWithOptionalDate(office, None))

    lazy val transitOfficeFuture = Future.sequence(
      releaseForTransit.customsOfficeOfTransit.map(
        office =>
          referenceData
            .customsOfficeSearch(office.reference)
            .map(customsOffice => CustomsOfficeWithOptionalDate(customsOffice, office.arrivalTime, maxLength = 32))
      )
    )

    for {
      countriesResult              <- countriesFuture
      additionalInfoResult         <- additionalInfoFuture
      kindsOfPackageResult         <- kindsOfPackageFuture
      documentTypesResult          <- documentTypesFuture
      previousDocumentResult       <- previousDocumentTypesFuture
      departureOffice              <- departureOfficeFuture
      destinationOffice            <- destinationOfficeFuture
      transitOffice                <- transitOfficeFuture
      controlResult                <- controlResultFuture
      circumstanceIndicatorsResult <- circumstanceIndicatorsFuture
    } yield {
      (
        countriesResult,
        additionalInfoResult,
        kindsOfPackageResult,
        documentTypesResult,
        previousDocumentResult,
        circumstanceIndicatorsResult
      ).mapN(
          (countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes, circumstanceIndicators) =>
            TransitSecurityAccompanyingDocumentConverter.toViewModel(
              releaseForTransit,
              countries,
              additionalInfo,
              kindsOfPackage,
              documentTypes,
              departureOffice,
              destinationOffice,
              transitOffice,
              previousDocumentTypes,
              controlResult.headOption,
              circumstanceIndicators
          )
        )
        .fold(
          errors => Invalid(errors),
          result => result
        )
    }
  }
}
