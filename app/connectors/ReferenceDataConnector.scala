/*
 * Copyright 2019 HM Revenue & Customs
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

package connectors

import javax.inject.Inject
import models.Service
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class ReferenceDataConnector @Inject()(
  config: Configuration,
  httpClient: HttpClient
) {

  private val service = config.get[Service]("microservice.services.transit-movements-trader-reference-data")

  private val baseUrl = s"$service/transit-movements-trader-reference-data"

  private val countriesUrl             = s"$baseUrl/countries-full-list"
  private val kindsOfPackageUrl        = s"$baseUrl/kinds-of-package"
  private val documentTypesUrl         = s"$baseUrl/document-types"
  private val additionalInformationUrl = s"$baseUrl/additional-information"

  def countries()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Seq[Country]] =
    httpClient.GET[Seq[Country]](countriesUrl)

  def kindsOfPackage()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Seq[KindOfPackage]] =
    httpClient.GET[Seq[KindOfPackage]](kindsOfPackageUrl)

  def documentTypes()(implicit ex: ExecutionContext, hc: HeaderCarrier): Future[Seq[DocumentType]] =
    httpClient.GET[Seq[DocumentType]](documentTypesUrl)

  def additionalInformation()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Seq[AdditionalInformation]] =
    httpClient.GET[Seq[AdditionalInformation]](additionalInformationUrl)
}
