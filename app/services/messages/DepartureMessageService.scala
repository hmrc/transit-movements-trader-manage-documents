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
import generated.{CC029CType, Generated_CC029CTypeFormat}
import models.DepartureMessageType.DepartureNotification
import models.IE015
import scalaxb.`package`.fromXML
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.RichCC029CType

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DepartureMessageService @Inject() (connector: DepartureMovementConnector) {

  def getReleaseForTransitNotification(
    departureId: String,
    messageId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CC029CType] =
    connector
      .getMessage(departureId, messageId)
      .map(fromXML(_))

  def getDeclarationData(
    departureId: String,
    messageId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[IE015] =
    connector
      .getMessage(departureId, messageId)
      .map(IE015.reads)

  def getIE015MessageId(
    departureId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[String]] =
    connector
      .getMessages(departureId)
      .map(_.find(DepartureNotification).map(_.id))
}
