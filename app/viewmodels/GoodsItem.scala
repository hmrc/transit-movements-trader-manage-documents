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

package viewmodels

import cats.data.NonEmptyList
import models.DeclarationType
import models.reference.Country
import models.SensitiveGoodsInformation

final case class GoodsItem(
  itemNumber: Int,
  commodityCode: Option[String],
  declarationType: Option[DeclarationType],
  description: String,
  grossMass: Option[BigDecimal],
  netMass: Option[BigDecimal],
  countryOfDispatch: Option[Country],
  countryOfDestination: Option[Country],
  producedDocuments: Seq[ProducedDocument],
  previousDocumentTypes: Seq[PreviousDocumentType],
  specialMentions: Seq[SpecialMention],
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  containers: Seq[String],
  packages: NonEmptyList[Package],
  sensitiveGoodsInformation: Seq[SensitiveGoodsInformation]
)
