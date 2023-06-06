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
import models.P5.departure.DepartureNotificationMessage
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class DepartureMessageP5Service @Inject() (connector: DepartureMovementP5Connector) {

  def getDepartureNotificationMessage(departureId: String, messageId: String)(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[DepartureNotificationMessage] =
    for {
      getMRN           <- connector.getMRN(departureId)
      getDepartureData <- connector.getDepartureNotificationMessage(departureId, messageId)
    } yield DepartureNotificationMessage(getMRN, getDepartureData)

}
