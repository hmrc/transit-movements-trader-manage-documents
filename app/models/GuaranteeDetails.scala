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

package models

import cats.implicits.catsSyntaxTuple2Semigroupal
import cats.implicits.catsSyntaxTuple5Semigroupal
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.XmlReader.strictReadSeq
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class GuaranteeDetails(
  guaranteeType: String,
  guaranteeReference: Seq[GuaranteeReference]
)

object GuaranteeDetails {
  implicit lazy val format: OFormat[GuaranteeDetails] = Json.format[GuaranteeDetails]

  implicit val xmlReads: XmlReader[GuaranteeDetails] = (
    (__ \ "GuaTypGUA1").read[String],
    (__ \ "GUAREFREF").read(strictReadSeq[GuaranteeReference])
  ).mapN(GuaranteeDetails.apply)
}
