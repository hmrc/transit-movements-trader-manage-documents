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

package services.P5

import connectors.DepartureMovementP5Connector
import generated.p5.CC015CType
import generated.p5.CC029CType
import models.NamespaceBinding
import models.P5.departure.DepartureMessageType.DepartureNotification
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class DepartureMessageP5Service @Inject() (connector: DepartureMovementP5Connector) extends MessageP5Service {

  def getReleaseForTransitNotification(
    departureId: String,
    messageId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CC029CType] =
    getMessage[CC029CType](departureId, messageId)

  def getDeclarationData(
    departureId: String,
    messageId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CC015CType] =
    getMessage[CC015CType](departureId, messageId)

  def getIE015MessageId(
    departureId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[String]] =
    connector
      .getMessages(departureId)
      .map(_.find(DepartureNotification).map(_.id))

  private def getMessage[T](
    departureId: String,
    messageId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, namespaceBinding: NamespaceBinding[T]): Future[T] =
    formatResponse(connector.getMessage(departureId, messageId))
}
