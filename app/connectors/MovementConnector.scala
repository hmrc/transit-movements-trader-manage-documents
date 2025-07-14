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
import models.Version
import org.apache.pekko.stream.Materializer
import org.apache.pekko.stream.scaladsl.{Source, StreamConverters}
import org.apache.pekko.util.ByteString
import play.api.Logging
import play.api.http.HeaderNames.*
import uk.gov.hmrc.http.client.{HttpClientV2, given}
import uk.gov.hmrc.http.{HeaderCarrier, HttpReadsTry, StringContextOps}

import javax.xml.parsers.{SAXParser, SAXParserFactory}
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.{Node, XML}

trait MovementConnector extends HttpReadsTry with Logging {

  val config: AppConfig
  val http: HttpClientV2
  implicit val mat: Materializer
  implicit val ec: ExecutionContext

  private val saxParser: SAXParser = {
    val saxParserFactory = SAXParserFactory.newInstance()
    saxParserFactory.newSAXParser()
  }

  def getMessage(
    movements: String,
    movementId: String,
    messageId: String,
    version: Version
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Node] = {
    val url = url"${config.commonTransitConventionTradersUrl}movements/$movements/$movementId/messages/$messageId/body"
    http
      .get(url)
      .setHeader(ACCEPT -> s"application/vnd.hmrc.$version+xml")
      .stream[Source[ByteString, ?]]
      .map(_.runWith(StreamConverters.asInputStream()))
      .map(XML.withSAXParser(saxParser).load(_))
  }

}
