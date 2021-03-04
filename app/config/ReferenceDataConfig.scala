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

package config

import javax.inject.Inject
import models.Service
import play.api.Configuration

class ReferenceDataConfig @Inject()(config: Configuration) {

  private val service = config.get[Service]("microservice.services.transit-movements-trader-reference-data")

  private val baseUrl = s"$service/transit-movements-trader-reference-data"

  val countriesUrl: String             = s"$baseUrl/countries-full-list"
  val kindsOfPackageUrl: String        = s"$baseUrl/kinds-of-package"
  val documentTypesUrl: String         = s"$baseUrl/document-types"
  val additionalInformationUrl: String = s"$baseUrl/additional-information"

  val transportModeUrl: String         = s"$baseUrl/transport-mode"
  val controlResultUrl: String         = s"$baseUrl/control-result"
  val previousDocumentTypesUrl: String = s"$baseUrl/previous-document-types"
  val sensitiveGoodsCodeUrl: String    = s"$baseUrl/sensitive-goods-code"
  lazy val customsOfficeUrl: String    = s"$baseUrl/customs-office/"

}
