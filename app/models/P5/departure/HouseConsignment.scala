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
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  additionalSupplyChainActor: Option[AdditionalSupplyChainActor],
  departureTransportMeans: Option[DepartureTransportMeans],
  previousDocument: Option[List[PreviousDocument]],
  transportDocument: Option[List[TransportDocument]],
  supportingDocument: Option[List[SupportingDocument]],
  additionalReference: Option[List[AdditionalReference]],
  additionalInformation: Option[List[AdditionalInformation]],
  transportCharges: Option[TransportCharges],
  consignmentItems: Seq[ConsignmentItem]
) {

  val totalItems: Int = consignmentItems.length

  val totalPackages: Int = consignmentItems.flatMap(_.packaging.flatMap(_.numberOfPackages)).sum

  val previousDocumentInHC: Option[String] = previousDocument.map(_.showAll)

  val supportingDocumentInHC: Option[String] = supportingDocument.map(_.showAll)

  val transportDocumentInHC: Option[String] = transportDocument.map(_.showAll)

  val additionalInformationInHC: Option[String] = additionalInformation.map(_.showAll)

  val additionalReferenceInHC: Option[String] = additionalReference.map(_.showAll)

  val transportChargesInHC: Option[String] = transportCharges.map(_.toString)
}

object HouseConsignment {
  implicit val formats: OFormat[HouseConsignment] = Json.format[HouseConsignment]
}
