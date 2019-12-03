/*
 * Copyright 2019 HM Revenue & Customs
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

import play.api.libs.json.{Json, OWrites, Reads}

final case class GoodsItem(
                            itemNumber: Int,
                            commodityCode: Option[String],
                            declarationType: Option[DeclarationType],
                            description: String,
                            grossMass: Option[BigDecimal],
                            netMass: Option[BigDecimal],
                            countryOfDispatch: String,
                            countryOfDestination: String,
                            producedDocuments: Seq[ProducedDocument],
                            specialMentions: Seq[SpecialMention],
                            consignor: Option[Consignor],
                            consignee: Option[Consignee],
                            containers: Seq[String],
                            packages: Seq[Package]
                          )
object GoodsItem {

  implicit lazy val reads: Reads[GoodsItem] =
    Json.reads[GoodsItem]

  implicit lazy val writes: OWrites[GoodsItem] =
    Json.writes[GoodsItem]
}
