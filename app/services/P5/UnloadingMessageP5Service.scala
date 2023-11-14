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

import connectors.UnloadingPermissionP5Connector
import generated.p5.CC043CType
import models.NamespaceBinding
import scalaxb.`package`.fromXML
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class UnloadingMessageP5Service @Inject() (connector: UnloadingPermissionP5Connector) {

  def getUnloadingPermissionNotification(arrivalId: String, messageId: String)(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[CC043CType] =
    getMessage[CC043CType](arrivalId, messageId)

  // TODO - refactor this along with DepartureMessageP5Service.getMessage to call a single connector endpoint
  private def getMessage[T](
    arrivalId: String,
    messageId: String
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, namespaceBinding: NamespaceBinding[T]): Future[T] =
    connector.getMessage(arrivalId, messageId).map(_.body).map {
      nodeSeq =>
        fromXML(nodeSeq, namespaceBinding.stack)(namespaceBinding.format)
    }
}
