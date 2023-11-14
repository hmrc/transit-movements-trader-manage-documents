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

import models.NamespaceBinding
import scalaxb.`package`.fromXML

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

trait MessageP5Service {

  def formatResponse[T](messageF: Future[models.Message])(implicit ec: ExecutionContext, namespaceBinding: NamespaceBinding[T]): Future[T] =
    messageF.map(_.body).map {
      nodeSeq =>
        fromXML(nodeSeq, namespaceBinding.stack)(namespaceBinding.format)
    }

}
