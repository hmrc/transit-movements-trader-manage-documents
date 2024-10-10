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

package services.messages

import connectors.DepartureMovementConnector
import generated.CC015CType
import generated.CC029CType
import generated.Generated_CC015CTypeFormat
import generated.Generated_CC029CTypeFormat
import models.DepartureMessageType.DepartureNotification
import models.Phase
import scalaxb.XMLFormat
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.RichCC029CType

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class DepartureMessageService @Inject() (connector: DepartureMovementConnector) extends MessageService {

  def getReleaseForTransitNotification(
    departureId: String,
    messageId: String,
    phase: Phase
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CC029CType] =
    getMessage[CC029CType](departureId, messageId, phase).map(_.rollDown)

  def getDeclarationData(
    departureId: String,
    messageId: String,
    phase: Phase
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CC015CType] =
    getMessage[CC015CType](departureId, messageId, phase)

  def getIE015MessageId(
    departureId: String,
    phase: Phase
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[String]] =
    connector
      .getMessages(departureId, phase)
      .map(_.find(DepartureNotification).map(_.id))

  private def getMessage[T](
    departureId: String,
    messageId: String,
    phase: Phase
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, format: XMLFormat[T]): Future[T] =
    formatResponse(connector.getMessage(departureId, messageId, phase))
}
