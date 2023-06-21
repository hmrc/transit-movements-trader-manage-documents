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

import models.P5.departure.TransportEquipment.sealToString
import play.api.libs.functional.syntax._
import play.api.libs.json._

//case class Consignment(value: JsObject)
case class Consignment(
  grossMass: Double,
  inlandModeOfTransport: Option[String],
  modeOfTransportAtTheBorder: Option[String],
  countryOfDispatch: Option[String],
  referenceNumberUCR: Option[String],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  Carrier: Option[Carrier],
  LocationOfGoods: Option[LocationOfGoods],
  AdditionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  DepartureTransportMeans: Option[List[DepartureTransportMeans]],
  TransportEquipment: Option[List[TransportEquipment]],
  ActiveBorderTransportMeans: Option[List[ActiveBorderTransportMeans]],
  PlaceOfLoading: Option[PlaceOfLoading],
  PlaceOfUnloading: Option[PlaceOfUnloading],
  HouseConsignment: Seq[HouseConsignment],
  Document: Seq[Document],
  AdditionalInformation: Option[List[AdditionalInformation]],
  AdditionalReference: Option[List[AdditionalReference]],
  TransportCharges: Option[TransportCharges],
  CountryOfRoutingOfConsignment: Option[List[CountryOfRoutingOfConsignment]]
) {

  val totalPackages: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalPackages
  )

  val totalItems: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalItems
  )

  val additionalSupplyChainActorsRole: Option[String] = AdditionalSupplyChainActor.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalSupplyChainActorIdentificationNumbers: Option[String] = AdditionalSupplyChainActor.map {
    _.map(_.identificationNumber).mkString("; ")
  }

  val departureTransportMeans: Option[String] = DepartureTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val transportEquipment: Option[String] = TransportEquipment.map(
    _.map(_.toString).mkString("; ")
  )

  val seals: Option[String] = TransportEquipment.map(
    _.map(
      x => sealToString(x.Seal)
    ).mkString("; ")
  )

  val additionalInformation: Option[String] = AdditionalInformation.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalReference: Option[String] = AdditionalReference.map(
    _.map(_.toString).mkString("; ")
  )

  val transportCharges: Option[String] = TransportCharges.map(_.toString)

  val activeBorderTransportMeans: Option[String] = ActiveBorderTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val countryOfRoutingOfConsignment: Option[String] = CountryOfRoutingOfConsignment.map(
    _.map(_.toString).mkString("; ")
  )

  val activeBorderTransportMeansConveyanceNumbers: Option[String] = ActiveBorderTransportMeans.map(
    _.map(_.conveyanceReferenceNumberToString).mkString("; ")
  )
}

case class Document(
  PreviousDocument: Option[List[PreviousDocument]],
  TransportDocument: Option[List[TransportDocument]],
  SupportingDocument: Option[List[SupportingDocument]]
) {

  val previousDocument: Option[String] = PreviousDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val supportingDocument: Option[String] = SupportingDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val transportDocument: Option[String] = TransportDocument.map(
    _.map(_.toString).mkString("; ")
  )
}

object Consignment {

  implicit val reads: Reads[Consignment] = (
    (JsPath \ "grossMass").readNullable[Double] and
      (JsPath \ "inlandModeOfTransport").readNullable[String] and
      (JsPath \ "modeOfTransportAtTheBorder").readNullable[String] and
      (JsPath \ "referenceNumberUCR").readNullable[String] and
      (JsPath \ "Consignor").readNullable[Consignor] and
      (JsPath \ "Consignee").readNullable[Consignee] and
      (JsPath \ "Carrier").readNullable[Carrier] and
      (JsPath \ "LocationOfGoods").readNullable[LocationOfGoods] and
      (JsPath \ "AdditionalSupplyChainActor").readNullable[List[AdditionalSupplyChainActor]] and
      (JsPath \ "DepartureTransportMeans").readNullable[List[DepartureTransportMeans]] and
      (JsPath \ "TransportEquipment").readNullable[List[TransportEquipment]] and
      (JsPath \ "ActiveBorderTransportMeans").readNullable[List[ActiveBorderTransportMeans]] and
      (JsPath \ "PlaceOfLoading").readNullable[PlaceOfLoading] and
      (JsPath \ "PlaceOfUnloading").readNullable[PlaceOfUnloading] and
      (JsPath \ "HouseConsignment").readNullable[Seq[HouseConsignment]] and
      (JsPath \ "DepartureTransportMeans").readNullable[DepartureTransportMeans] and
      (JsPath \ "PreviousDocument").readNullable[PreviousDocument] and
      (JsPath \ "SupportingDocument").readNullable[SupportingDocument] and
      (JsPath \ "TransportDocument").readNullable[TransportDocument] and
      (JsPath \ "AdditionalInformation").readNullable[AdditionalInformation] and
      (JsPath \ "AdditionalReference").readNullable[AdditionalReference] and
      (JsPath \ "TransportCharges").readNullable[TransportCharges]
  )(Consignment.apply _)
}
