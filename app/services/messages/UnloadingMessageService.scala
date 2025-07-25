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

import connectors.UnloadingPermissionConnector
import generated.{CC043CType, Generated_CC043CTypeFormat}
import models.Version
import scalaxb.`package`.fromXML
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UnloadingMessageService @Inject() (connector: UnloadingPermissionConnector) {

  def getUnloadingPermissionNotification(
    arrivalId: String,
    messageId: String,
    version: Version
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CC043CType] =
    connector
      .getMessage(arrivalId, messageId, version)
      .map(fromXML(_))
}
