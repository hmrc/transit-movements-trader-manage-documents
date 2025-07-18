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

import com.github.tomakehurst.wiremock.client.WireMock.{equalTo, get, ok, urlEqualTo}
import itbase.{ItSpecBase, WireMockHelper}
import models.Version
import org.scalacheck.{Arbitrary, Gen}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Node

class UnloadingPermissionConnectorSpec extends ItSpecBase with WireMockHelper {

  implicit lazy val arbitraryHC: Arbitrary[HeaderCarrier] =
    Arbitrary(Gen.const(HeaderCarrier()))

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.common-transit-convention-traders.port" -> server.port
      )
      .build()

  private lazy val service: UnloadingPermissionConnector = app.injector.instanceOf[UnloadingPermissionConnector]

  implicit private val hc: HeaderCarrier = HeaderCarrier()

  private val version = Version("2.1")

  "getMessage" - {

    val url: String = s"/movements/arrivals/$arrivalId/messages/$messageId/body"

    "must return message data" in {

      val xml: Node =
        <ncts:CC043C PhaseID="NCTS5.0" xmlns:ncts="http://ncts.dgtaxud.ec">
          <messageSender>token</messageSender>
        </ncts:CC043C>

      server.stubFor(
        get(urlEqualTo(url))
          .withHeader("Accept", equalTo("application/vnd.hmrc.2.1+xml"))
          .willReturn(ok(xml.toString()))
      )

      val result = service.getMessage(arrivalId, messageId, version).futureValue

      result mustEqual xml
    }
  }
}
