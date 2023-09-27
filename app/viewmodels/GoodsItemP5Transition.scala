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

package viewmodels

import cats.data.NonEmptyList
import models.P5.departure.AdditionalReference
import models.P5.departure.Packaging
import models.P5.departure.PreviousDocument
import models.P5.departure.SupportingDocument
import models.P5.departure.TransportDocument
import models.DeclarationType
import models.SensitiveGoodsInformation
import models.reference.AdditionalInformation
import models.reference.Country

final case class GoodsItemP5Transition(
  itemNumber: String,
  commodityCode: Option[String],
  declarationType: Option[String],
  description: String,
  grossMass: Option[BigDecimal],
  netMass: Option[BigDecimal],
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  methodOfPayment: Option[String],
  commercialReferenceNumber: Option[String],
  unDangerGoodsCode: Option[String],
  transportDocuments: Seq[TransportDocument],
  supportingDocuments: Seq[SupportingDocument],
  previousDocuments: Seq[PreviousDocument],
  additionalInformation: Seq[models.P5.departure.AdditionalInformation],
  additionalReferences: Seq[AdditionalReference],
  consignor: Option[models.P5.departure.Consignor],
  consignee: Option[models.P5.departure.Consignee],
  containers: Seq[String],
  packages: Seq[Packaging],
  sensitiveGoodsInformation: Seq[SensitiveGoodsInformation],
  securityConsignor: Option[SecurityConsignor],
  securityConsignee: Option[SecurityConsignee]
)
