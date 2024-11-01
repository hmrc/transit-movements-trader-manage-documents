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
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.client.HttpClientV2

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.Node

class UnloadingPermissionConnector @Inject() (config: AppConfig, http: HttpClientV2) extends MovementConnector(config, http) {

  def getMessage(
    arrivalId: String,
    messageId: String,
    phase: Phase
  )(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Node] =
    getMessage("arrivals", arrivalId, messageId, phase)

}