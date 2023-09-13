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
  sequenceNumber: String,
  countryOfDispatch: Option[String],
  grossMass: Double,
  referenceNumberUCR: Option[String],
  securityIndicatorFromExport: Option[String],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  AdditionalSupplyChainActor: Option[AdditionalSupplyChainActor],
  DepartureTransportMeans: Option[DepartureTransportMeans],
  PreviousDocument: Option[List[PreviousDocument]],
  TransportDocument: Option[List[TransportDocument]],
  SupportingDocument: Option[List[SupportingDocument]],
  AdditionalReference: Option[List[AdditionalReference]],
  AdditionalInformation: Option[List[AdditionalInformation]],
  TransportCharges: Option[TransportCharges],
  ConsignmentItem: Seq[ConsignmentItem]
) {

  val totalItems: Int = ConsignmentItem.length

  val totalPackages: Int = ConsignmentItem.flatMap(_.Packaging.flatMap(_.numberOfPackages)).sum

  val previousDocumentInHC: Option[String] = PreviousDocument.map(_.showAll)

  val supportingDocumentInHC: Option[String] = SupportingDocument.map(_.showAll)

  val transportDocumentInHC: Option[String] = TransportDocument.map(_.showAll)

  val additionalInformationInHC: Option[String] = AdditionalInformation.map(_.showAll)

  val additionalReferenceInHC: Option[String] = AdditionalReference.map(_.showAll)

  val transportChargesInHC: Option[String] = TransportCharges.map(_.toString)
}

object HouseConsignment {
  implicit val formats: OFormat[HouseConsignment] = Json.format[HouseConsignment]
}
