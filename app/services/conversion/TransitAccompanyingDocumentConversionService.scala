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

import cats.data.Validated.Invalid
import javax.inject.Inject
import services.ReferenceDataService
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier
import cats.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class TransitAccompanyingDocumentConversionService @Inject()(referenceData: ReferenceDataService) {

  def toViewModel(transitAccompanyingDocument: models.TransitAccompanyingDocument)(
    implicit ec: ExecutionContext,
    hc: HeaderCarrier): Future[ValidationResult[viewmodels.PermissionToStartUnloading]] = {

    val countriesFuture      = referenceData.countries()
    val additionalInfoFuture = referenceData.additionalInformation()
    val kindsOfPackageFuture = referenceData.kindsOfPackage()
    val documentTypesFuture  = referenceData.documentTypes()

    //TODO: ref data will be needed below when we're building out the view model
    //    val transportMode  = referenceData.transportMode()
    //    val controlResultCode = referenceData.controlResult()
    //    val previousDocumentTypesFuture  = referenceData.previousDocumentTypes()
    //    val sensitiveGoodsCodeFuture = referenceData.sensitiveGoodsCode()
    //    val specialMentionsCodeFuture = referenceData.specialMentions()

    for {
      countriesResult      <- countriesFuture
      additionalInfoResult <- additionalInfoFuture
      kindsOfPackageResult <- kindsOfPackageFuture
      documentTypesResult  <- documentTypesFuture
    } yield {
      (
        countriesResult,
        additionalInfoResult,
        kindsOfPackageResult,
        documentTypesResult
      ).mapN(
          (countries, additionalInfo, kindsOfPackage, documentTypes) =>
            TransitAccompanyingDocumentConverter.toViewModel(transitAccompanyingDocument, countries, additionalInfo, kindsOfPackage, documentTypes)
        )
        .fold(
          errors => Invalid(errors),
          result => result
        )
    }
//
//    for {
//      countriesResult <- countriesFuture
//    } yield {
//      (
//        countriesResult,
//      ).map(
//          (countries) => TransitAccompanyingDocumentConverter.toViewModel(transitAccompanyingDocument, countries)
//        )
//        .fold(
//          errors => Invalid(errors),
//          result => result
//        )
//    }
  }
}
