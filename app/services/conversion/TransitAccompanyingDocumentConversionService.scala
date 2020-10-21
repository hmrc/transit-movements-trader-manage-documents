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
import javax.inject.Inject
import services.ReferenceDataService
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class TransitAccompanyingDocumentConversionService @Inject()(referenceData: ReferenceDataService) {

  def toViewModel(transitAccompanyingDocument: models.TransitAccompanyingDocument)(
    implicit ec: ExecutionContext,
    hc: HeaderCarrier): Future[ValidationResult[viewmodels.tad.TransitAccompanyingDocument]] =
    ???
//    val countriesFuture      = referenceData.countries()
//    val additionalInfoFuture = referenceData.additionalInformation()
//    val kindsOfPackageFuture = referenceData.kindsOfPackage()
//    val documentTypesFuture  = referenceData.documentTypes() // CL013. Document Type (Common)

//    val transportMode  = referenceData.transportMode() //CL018. Transport mode
//    val controlResultCode = referenceData.controlResultCode() //CL047 Control result // CONRESERS.ConResCodERS16
//
//    val previousDocumentTypesFuture  = referenceData.previousDocumentTypes()/// CL014. Previous document type (Common)
//    val sensitiveGoodsCodeFuture = referenceData.sensitiveGoodsCode() //CL064
//
//    val specialMentionsCodeFuture = referenceData.specialMentions() //CL039

  // TODO: Don't think we need guarantee type (uses the code that is pass in message)
  // val guaranteeType = referenceData.guaranteeType() //CL051 // GUAGUA.GuaTypGUA1

  //TODO: Could use the countries ref data for this
  // val guaranteeManagementCountries = referenceData.guaranteeManagementCountries() //CL071. Country Codes (Guarantee Management - non EC) // GUAGUA.NotValForOthConPLIM2

}
