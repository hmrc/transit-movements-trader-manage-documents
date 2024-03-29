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

import javax.inject.Inject
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class UnloadingPermissionConversionService @Inject() (referenceData: ReferenceDataConnector) {

  def toViewModel(
    permission: models.PermissionToStartUnloading
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[viewmodels.PermissionToStartUnloading]] = {

    val countriesFuture      = referenceData.countries()
    val additionalInfoFuture = referenceData.additionalInformation()
    val kindsOfPackageFuture = referenceData.kindsOfPackage()
    val documentTypesFuture  = referenceData.documentTypes()

    for {
      countriesResult      <- countriesFuture
      additionalInfoResult <- additionalInfoFuture
      kindsOfPackageResult <- kindsOfPackageFuture
      documentTypesResult  <- documentTypesFuture
    } yield (
      countriesResult,
      additionalInfoResult,
      kindsOfPackageResult,
      documentTypesResult
    ).mapN(
      (countries, additionalInfo, kindsOfPackage, documentTypes) =>
        UnloadingPermissionConverter.toViewModel(permission, countries, additionalInfo, kindsOfPackage, documentTypes)
    ).fold(
      errors => Invalid(errors),
      result => result
    )
  }
}
