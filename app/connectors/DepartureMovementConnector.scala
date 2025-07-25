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
import models.{DepartureMessages, Version}
import org.apache.pekko.stream.Materializer
import play.api.http.HeaderNames.*
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Node

class DepartureMovementConnector @Inject() (
  override val config: AppConfig,
  override val http: HttpClientV2,
  implicit override val mat: Materializer,
  implicit override val ec: ExecutionContext
) extends MovementConnector {

  def getMessages(departureId: String)(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[DepartureMessages] = {
    val url = url"${config.commonTransitConventionTradersUrl}movements/departures/$departureId/messages"
    http
      .get(url)
      .setHeader(ACCEPT -> s"application/vnd.hmrc.2.1+json")
      .execute[DepartureMessages]
  }

  def getMessage(
    departureId: String,
    messageId: String,
    version: Version
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Node] =
    getMessage("departures", departureId, messageId, version)

}
