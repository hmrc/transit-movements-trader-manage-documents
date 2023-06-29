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

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class HouseConsignment(
  ConsignmentItem: Seq[ConsignmentItem],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  PreviousDocument: Option[List[PreviousDocument]],
  TransportDocument: Option[List[TransportDocument]],
  SupportingDocument: Option[List[SupportingDocument]]
) {

  val totalPackages: Int = ConsignmentItem.foldLeft(0)(
    (total, item) => total + item.totalPackages
  )

  val totalItems: Int = ConsignmentItem.length

  val previousDocumentInHC: Option[String] = PreviousDocument.map(_.map(_.toString)).map(_.mkString("; "))

  val supportingDocumentInHC: Option[String] = SupportingDocument.map(_.map(_.toString).mkString("; "))

  val transportDocumentInHC: Option[String] = TransportDocument.map(_.map(_.toString).mkString("; "))
}

object HouseConsignment {
  implicit val formats: OFormat[HouseConsignment] = Json.format[HouseConsignment]
}
