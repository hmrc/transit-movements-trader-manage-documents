/*
 * Copyright 2021 HM Revenue & Customs
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

package models.reference

import play.api.libs.functional.syntax._
import play.api.libs.json._

final case class TransportMode(code: String, description: String) extends CodedReferenceData

object TransportMode {

  implicit val writes: Writes[TransportMode] = (
    (JsPath \ "code").write[String] and
      (JsPath \ "description").write[String]
  )(unlift(TransportMode.unapply))

  implicit val reads: Reads[TransportMode] = (
    (JsPath \ "code").read[String](readString) and
      (JsPath \ "description").read[String]
  )(TransportMode.apply _)
}
