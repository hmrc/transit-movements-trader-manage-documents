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

import com.github.tomakehurst.wiremock.client.WireMock.*
import itbase.{ItSpecBase, WireMockHelper}
import models.DepartureMessageType.DepartureNotification
import models.{DepartureMessageMetaData, DepartureMessages, Version}
import org.scalacheck.{Arbitrary, Gen}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.*

class DepartureMovementConnectorSpec extends ItSpecBase with WireMockHelper {

  implicit lazy val arbitraryHC: Arbitrary[HeaderCarrier] =
    Arbitrary(Gen.const(HeaderCarrier()))

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.common-transit-convention-traders.port" -> server.port
      )
      .build()

  private lazy val service: DepartureMovementConnector = app.injector.instanceOf[DepartureMovementConnector]

  implicit private val hc: HeaderCarrier = HeaderCarrier()

  private val version = Version("2.1")

  "getMessages" - {

    val url: String = s"/movements/departures/$departureId/messages"

    "must return messages for a given departure ID" in {

      val json = Json.parse("""
          |{
          |  "_links": {
          |    "self": {
          |      "href": "/customs/transits/movements/departures/6365135ba5e821ee/messages"
          |    },
          |    "departure": {
          |      "href": "/customs/transits/movements/departures/6365135ba5e821ee"
          |    }
          |  },
          |  "totalCount": 1,
          |  "messages": [
          |    {
          |      "_links": {
          |        "self": {
          |          "href": "/customs/transits/movements/departures/6365135ba5e821ee/message/634982098f02f00a"
          |        },
          |        "departure": {
          |          "href": "/customs/transits/movements/departures/6365135ba5e821ee"
          |        }
          |      },
          |      "id": "634982098f02f00a",
          |      "departureId": "6365135ba5e821ee",
          |      "received": "2022-11-10T15:32:51.000Z",
          |      "type": "IE015",
          |      "status": "Success"
          |    }
          |  ]
          |}
          |""".stripMargin)

      server.stubFor(
        get(urlEqualTo(url))
          .withHeader("Accept", equalTo("application/vnd.hmrc.2.1+json"))
          .willReturn(ok(json.toString))
      )

      val result = service.getMessages(departureId).futureValue

      result mustEqual DepartureMessages(
        List(
          DepartureMessageMetaData(
            id = "634982098f02f00a",
            received = LocalDateTime.of(2022: Int, 11: Int, 10: Int, 15: Int, 32: Int, 51: Int),
            messageType = DepartureNotification,
            path = "movements/departures/6365135ba5e821ee/message/634982098f02f00a"
          )
        )
      )
    }
  }

  "getMessage" - {

    val url: String = s"/movements/departures/$departureId/messages/$messageId/body"

    "must return message data" in {

      val xml: Node =
        <ncts:CC029C PhaseID="NCTS5.0" xmlns:ncts="http://ncts.dgtaxud.ec">
          <messageSender>token</messageSender>
        </ncts:CC029C>

      server.stubFor(
        get(urlEqualTo(url))
          .withHeader("Accept", equalTo("application/vnd.hmrc.2.1+xml"))
          .willReturn(ok(xml.toString()))
      )

      val result = service.getMessage(departureId, messageId, version).futureValue

      result mustEqual xml
    }
  }
}
