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

import play.api.libs.json.Reads
import play.api.libs.json.__

sealed trait Message {
  val data: MessageData
}

case class IE029(data: IE029MessageData) extends Message

object IE029 {
  implicit val reads: Reads[IE029] = (__ \ "body" \ "n1:CC029C").read[IE029MessageData].map(IE029.apply)
}

case class IE015(data: IE015MessageData) extends Message

object IE015 {
  implicit val reads: Reads[IE015] = (__ \ "body" \ "n1:CC015C").read[IE015MessageData].map(IE015.apply)
}
