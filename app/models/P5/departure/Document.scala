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

package models.P5.departure

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Document(
  PreviousDocument: Option[List[PreviousDocument]],
  TransportDocument: Option[List[TransportDocument]],
  SupportingDocument: Option[List[SupportingDocument]]
) {

  val previousDocument: Option[String] = PreviousDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val supportingDocument: Option[String] = SupportingDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val transportDocument: Option[String] = TransportDocument.map(
    _.map(_.toString).mkString("; ")
  )
}

object Document {

  implicit val reads: Reads[Document] = (
    (JsPath \ "PreviousDocument").readNullable[List[PreviousDocument]] and
      (JsPath \ "TransportDocument").readNullable[List[TransportDocument]] and
      (JsPath \ "SupportingDocument").readNullable[List[SupportingDocument]]
  )(Document.apply(_, _, _))

  implicit val writes: Writes[Document] = Json.writes[Document]

}
