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
import models.Phase
import play.api.Logging
import play.api.http.HeaderNames._
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpReadsTry
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.http.StringContextOps
import uk.gov.hmrc.http.client.HttpClientV2

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.Node
import scala.xml.XML

class MovementConnector(config: AppConfig, http: HttpClientV2) extends HttpReadsTry with Logging {

  def getMessage(
    movements: String,
    movementId: String,
    messageId: String
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Node] = {
    val url = url"${config.commonTransitConventionTradersUrl}movements/$movements/$movementId/messages/$messageId/body"
    http
      .get(url)
      .setHeader(ACCEPT -> s"application/vnd.hmrc.${Phase.version}+xml")
      .execute[HttpResponse]
      .map(_.body)
      .map(XML.loadString)
  }

}
