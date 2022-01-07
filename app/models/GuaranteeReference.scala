/*
 * Copyright 2022 HM Revenue & Customs
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

import cats.implicits.catsSyntaxTuple4Semigroupal
import com.lucidchart.open.xtract.XmlReader.strictReadSeq
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class GuaranteeReference(
  guaranteeRef: Option[String],
  otherGuaranteeRef: Option[String],
  notValidForEc: Option[Boolean],
  notValidForOther: Seq[String]
)

object GuaranteeReference {
  implicit lazy val format: OFormat[GuaranteeReference] = Json.format[GuaranteeReference]

  implicit val xmlReads: XmlReader[GuaranteeReference] = (
    (__ \ "GuaRefNumGRNREF1").read[String].optional,
    (__ \ "OthGuaRefREF4").read[String].optional,
    (__ \ "VALLIMECVLE" \ "NotValForECVLE1").read[Boolean].optional,
    (__ \ "VALLIMNONECLIM" \ "NotValForOthConPLIM2").read(strictReadSeq[String])
  ).mapN(GuaranteeReference.apply)
}
