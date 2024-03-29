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

package connectors

import config.AppConfig
import models.P5.departure.DepartureMessages
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.http.HttpReads.Implicits._

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.Node

class DepartureMovementP5Connector @Inject() (config: AppConfig, http: HttpClient) extends MovementConnector(config, http) {

  def getMessages(departureId: String)(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[DepartureMessages] = {

    val headers = hc.withExtraHeaders(("Accept", "application/vnd.hmrc.2.0+json"))

    val serviceUrl = s"${config.commonTransitConventionTradersUrl}movements/departures/$departureId/messages"

    http.GET[DepartureMessages](serviceUrl)(implicitly, headers, ec)
  }

  def getMessage(
    departureId: String,
    messageId: String
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Node] =
    getMessage("departures", departureId, messageId)

}
