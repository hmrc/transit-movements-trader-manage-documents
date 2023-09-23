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

import base.SpecBase
import connectors.DepartureMovementP5Connector
import models.P5.departure.DepartureMessageType.DepartureNotification
import models.P5.departure.DepartureMessageMetaData
import models.P5.departure.DepartureMessages
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DepartureMessageP5ServiceSpec extends SpecBase with ScalaFutures {

  private val mockConnector = mock[DepartureMovementP5Connector]

  private val service = new DepartureMessageP5Service(mockConnector)

  private val departureId = "departureId"

  implicit private val hc: HeaderCarrier = new HeaderCarrier()

  "getIE015MessageId" - {
    "must return None" - {
      "when IE015 message not found" in {
        val messages = DepartureMessages(List.empty)

        when(mockConnector.getMessages(any())(any(), any()))
          .thenReturn(Future.successful(messages))

        val result = service.getIE015MessageId(departureId).futureValue

        result mustBe None
      }
    }

    "must return IE015 message ID" - {
      "when IE015 message found" in {
        val messageId = "ie015 id"
        val ie015     = DepartureMessageMetaData(messageId, LocalDateTime.now(), DepartureNotification, "")
        val messages  = DepartureMessages(List(ie015))

        when(mockConnector.getMessages(any())(any(), any()))
          .thenReturn(Future.successful(messages))

        val result = service.getIE015MessageId(departureId).futureValue

        result mustBe Some(messageId)
      }
    }
  }
}
