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

import cats.data.Validated.Invalid
import cats.implicits._
import connectors.ReferenceDataConnector
import connectors.ReferenceDataP5Connector
import models.ControlResult
import models.reference.AdditionalInformation
import models.reference.CircumstanceIndicator
import models.reference.Country
import models.reference.CustomsOffice
import models.reference.DocumentType
import models.reference.KindOfPackage
import models.reference.PreviousDocumentTypes
import models.reference.SupportingDocumentTypes
import models.reference.TransportDocumentTypes
import models.P5.departure.IE029Data
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.CustomsOfficeWithOptionalDate
import viewmodels.TransitAccompanyingDocumentPDF
import java.time.LocalDate

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class TransitAccompanyingDocumentConversionService @Inject() (referenceData: ReferenceDataConnector, referenceDataP5: ReferenceDataP5Connector) {

  /*
   * The TAD/UL xsd files are identical, both documents share same structure
   * There's no point having separate templates under views
   * One view model can hold all the required data which can be used to build a document
   * Let each Converter handle what goes in the view model
   */
  def toViewModel(
    transitAccompanyingDocument: models.ReleaseForTransit
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[viewmodels.TransitAccompanyingDocumentPDF]] = {

    val countriesFuture             = referenceData.countries()
    val additionalInfoFuture        = referenceData.additionalInformation()
    val kindsOfPackageFuture        = referenceData.kindsOfPackage()
    val documentTypesFuture         = referenceData.documentTypes()
    val previousDocumentTypesFuture = referenceData.previousDocumentTypes()

    lazy val controlResultFuture = Future.sequence(
      transitAccompanyingDocument.controlResult.toList.map(
        code =>
          referenceData
            .controlResultByCode(code.conResCodERS16)
            .map(
              crd => viewmodels.ControlResult(crd, code)
            )
      )
    )

    lazy val departureOfficeFuture = referenceData
      .customsOfficeSearch(transitAccompanyingDocument.departureOffice)
      .map(
        office => CustomsOfficeWithOptionalDate(office, None)
      )

    lazy val destinationOfficeFuture = referenceData
      .customsOfficeSearch(transitAccompanyingDocument.destinationOffice)
      .map(
        office => CustomsOfficeWithOptionalDate(office, None)
      )

    lazy val transitOfficeFuture = Future.sequence(
      transitAccompanyingDocument.customsOfficeOfTransit.map(
        office =>
          referenceData
            .customsOfficeSearch(office.reference)
            .map(
              customsOffice => CustomsOfficeWithOptionalDate(customsOffice, office.arrivalTime, maxLength = 18)
            )
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
    } yield (
      countriesResult,
      additionalInfoResult,
      kindsOfPackageResult,
      documentTypesResult,
      previousDocumentResult
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
    ).fold(
      errors => Invalid(errors),
      result => result
    )
  }

  def fromP5ToViewModel(ie029: IE029Data)(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[TransitAccompanyingDocumentPDF]] = {

    val countriesFuture: Future[ValidationResult[Seq[Country]]] = referenceDataP5.getList[Seq[Country]]("CountryCodesForAddress")
    val additionalInfoFuture: Future[ValidationResult[Seq[AdditionalInformation]]] =
      referenceDataP5.getList[Seq[AdditionalInformation]]("AdditionalInformation")
    val kindsOfPackageFuture: Future[ValidationResult[Seq[KindOfPackage]]] = referenceDataP5.getList[Seq[KindOfPackage]]("KindOfPackages")
    val previousDocumentTypesFuture: Future[ValidationResult[Seq[PreviousDocumentTypes]]] =
      referenceDataP5.getList[Seq[PreviousDocumentTypes]]("PreviousDocumentType")
    val supportingDocumentTypesFuture: Future[ValidationResult[Seq[SupportingDocumentTypes]]] =
      referenceDataP5.getList[Seq[SupportingDocumentTypes]]("SupportingDocumentType")
    val transportDocumentTypesFuture: Future[ValidationResult[Seq[TransportDocumentTypes]]] =
      referenceDataP5.getList[Seq[TransportDocumentTypes]]("TransportDocumentType")
    val circumstanceIndicatorsFuture: Future[ValidationResult[Seq[CircumstanceIndicator]]] =
      referenceDataP5.getList[Seq[CircumstanceIndicator]]("SpecificCircumstanceIndicatorCode")
    val customsOfficeFuture: Future[ValidationResult[Seq[CustomsOffice]]] = referenceDataP5.getList[Seq[CustomsOffice]]("customsOffices")
//    val controlResultFuture: Future[ValidationResult[Seq[ControlResult]]] =
//      referenceDataP5.getList[Seq[ControlResult]]("controlResult")

    for {
      countryCodesForAddress  <- countriesFuture
      additionalInfo          <- additionalInfoFuture
      kindsOfPackage          <- kindsOfPackageFuture
      previousDocumentTypes   <- previousDocumentTypesFuture
      supportingDocumentTypes <- supportingDocumentTypesFuture
      transportDocumentTypes  <- transportDocumentTypesFuture
      circumstanceIndicators  <- circumstanceIndicatorsFuture
      customsOffice           <- customsOfficeFuture
//      controlResult <- controlResultFuture
    } yield (
      countryCodesForAddress,
      additionalInfo,
      kindsOfPackage,
      previousDocumentTypes,
      supportingDocumentTypes,
      transportDocumentTypes,
      circumstanceIndicators,
      customsOffice
//      controlResult
    )
      .mapN {
        (
          countriesForAddress,
          additionalInfo,
          kindsOfPackage,
          previousDocumentTypes,
          supportingDocumentTypes,
          transportDocumentTypes,
          circumstanceIndicators,
          customsOffice
//          controlResult
        ) =>
          TransitAccompanyingDocumentConverter.fromP5ToViewModel(
            ie029,
            countriesForAddress,
            additionalInfo,
            kindsOfPackage,
            previousDocumentTypes,
            supportingDocumentTypes,
            transportDocumentTypes,
            circumstanceIndicators,
            customsOffice,
            Seq.empty
          )
      }
      .fold(
        errors => Invalid(errors),
        result => result
      )

  }

}
