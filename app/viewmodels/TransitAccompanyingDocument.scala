/*
 * Copyright 2020 HM Revenue & Customs
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

final case class TransitAccompanyingDocument(
  movementReferenceNumber: String,
  declarationType: DeclarationType,
  singleCountryOfDispatch: Option[Country],
  singleCountryOfDestination: Option[Country],
  transportIdentity: Option[String],
  transportCountry: Option[Country],
  numberOfItems: Int,
  numberOfPackages: Int,
  grossMass: BigDecimal,
  principal: Principal,
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  departureOffice: String,
  departureOfficeTrimmed: String,
  seals: Seq[String],
  goodsItems: NonEmptyList[GoodsItem]
) extends TransitDocument {

  val documentHeading = TransitAccompanyingDocumentHeading
}
