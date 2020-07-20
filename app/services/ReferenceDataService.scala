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

package services

import cats.implicits._
import config.ReferenceDataConfig
import javax.inject.Inject
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import play.api.http.Status
import play.api.libs.json.Reads
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpReads
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class ReferenceDataService @Inject()(
  config: ReferenceDataConfig,
  httpClient: HttpClient
) {

  private def referenceDataReads[A](typeOfData: String)(implicit ev: Reads[A]): HttpReads[ValidationResult[Seq[A]]] =
    (_, _, response: HttpResponse) =>
      response.status match {
        case Status.OK =>
          response.json
            .validate[Seq[A]]
            .fold(
              errors => JsonError(typeOfData, errors).invalidNec,
              result => result.validNec
            )
        case _ => ReferenceDataRetrievalError(typeOfData, response.status, response.body).invalidNec
    }

  def countries()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[Seq[Country]]] = {

    val reads = referenceDataReads[Country]("country")

    httpClient.GET[ValidationResult[Seq[Country]]](config.countriesUrl)(reads, implicitly, implicitly)
  }

  def kindsOfPackage()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[Seq[KindOfPackage]]] = {

    val reads = referenceDataReads[KindOfPackage]("kindOfPackage")

    httpClient.GET[ValidationResult[Seq[KindOfPackage]]](config.kindsOfPackageUrl)(reads, implicitly, implicitly)
  }

  def documentTypes()(implicit ex: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[Seq[DocumentType]]] = {

    val reads = referenceDataReads[DocumentType]("documentType")

    httpClient.GET[ValidationResult[Seq[DocumentType]]](config.documentTypesUrl)(reads, implicitly, implicitly)
  }

  def additionalInformation()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[ValidationResult[Seq[AdditionalInformation]]] = {

    val reads = referenceDataReads[AdditionalInformation]("additionalInformation")
    httpClient.GET[ValidationResult[Seq[AdditionalInformation]]](config.additionalInformationUrl)(reads, implicitly, implicitly)
  }
}
