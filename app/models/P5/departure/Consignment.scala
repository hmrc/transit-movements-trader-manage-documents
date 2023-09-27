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

import play.api.libs.json._

case class Consignment(
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  containerIndicator: String,
  inlandModeOfTransport: Option[String],
  modeOfTransportAtTheBorder: Option[String],
  grossMass: Double,
  referenceNumberUCR: Option[String],
  carrier: Option[Carrier],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  AdditionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  TransportEquipment: Option[List[TransportEquipment]],
  LocationOfGoods: Option[LocationOfGoods],
  DepartureTransportMeans: Option[List[DepartureTransportMeans]],
  CountryOfRoutingOfConsignment: Option[List[CountryOfRoutingOfConsignment]],
  ActiveBorderTransportMeans: Option[List[ActiveBorderTransportMeans]],
  PlaceOfLoading: Option[PlaceOfLoading],
  PlaceOfUnloading: Option[PlaceOfUnloading],
  PreviousDocument: Option[List[PreviousDocument]],
  TransportDocument: Option[List[TransportDocument]],
  SupportingDocument: Option[List[SupportingDocument]],
  AdditionalReference: Option[List[AdditionalReference]],
  AdditionalInformation: Option[List[AdditionalInformation]],
  TransportCharges: Option[TransportCharges],
  HouseConsignment: Seq[HouseConsignment]
) {

  val totalPackages: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignment) => total + houseConsignment.totalPackages
  )

  val totalItems: Int = HouseConsignment.foldLeft(0)(
    (total, HouseConsignment) => total + HouseConsignment.totalItems
  )

  val additionalSupplyChainActorsRole: Option[String] = AdditionalSupplyChainActor.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalSupplyChainActorIdentificationNumbers: Option[String] = AdditionalSupplyChainActor.map(
    _.map(_.identificationNumber).mkString("; ")
  )

  val departureTransportMeansIdentity: Option[String] =
    DepartureTransportMeans.map {
      departureTransportMeansList =>
        departureTransportMeansList.length match {
          case 1 => s"${departureTransportMeansList.head.toString}"
          case _ => s"${departureTransportMeansList.head.toString}..."
        }
    }

  val transportEquipmentDisplay: Option[String] = TransportEquipment.map(
    _.map(_.toString).mkString("; ")
  )

  val sealsString: Option[String] = TransportEquipment.map(
    _.flatMap(_.sealsList).mkString("...")
  )

  val additionalInformationDisplay: Option[String] = AdditionalInformation.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalReferenceDisplay: Option[String] = AdditionalReference.map(
    _.map(_.toString).mkString("; ")
  )

  val transportChargesDisplay: Option[String] = TransportCharges.map(_.toString)

  val activeBorderTransportMeansDisplay: Option[String] = ActiveBorderTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val countryOfRoutingOfConsignmentDisplay: Option[String] = CountryOfRoutingOfConsignment.map(
    _.map(_.toString).mkString("; ")
  )

  val activeBorderTransportMeansConveyanceNumbers: Option[String] = ActiveBorderTransportMeans.map(
    _.map(_.conveyanceReferenceNumberToString).mkString("; ")
  )

  val previousDocument: Option[String] = PreviousDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val supportingDocument: Option[String] = SupportingDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val transportDocument: Option[String] = TransportDocument.map(
    _.map(_.toString).mkString("; ")
  )

//  val seals: Seq[String] = TransportEquipment.getOrElse(Nil).map(_.seals).showAllSeal

  val consignmentItems: Seq[ConsignmentItem] = HouseConsignment.foldLeft(Seq.empty[ConsignmentItem]) {
    (acc, house) =>
      acc ++ house.ConsignmentItem.map(
        _.copy(
          referenceNumberUCR = referenceNumberUCR
        )
      )
  }

}

object Consignment {

  implicit val consignmentReads: Reads[Consignment] = new Reads[Consignment] {

    override def reads(json: JsValue): JsResult[Consignment] =
      JsSuccess(
        Consignment(
          (json \ "countryOfDispatch").asOpt[String],
          (json \ "countryOfDestination").asOpt[String],
          (json \ "containerIndicator").as[String],
          (json \ "inlandModeOfTransport").asOpt[String],
          (json \ "modeOfTransportAtTheBorder").asOpt[String],
          (json \ "grossMass").as[Double],
          (json \ "referenceNumberUCR").asOpt[String],
          (json \ "carrier").asOpt[Carrier],
          (json \ "Consignor").asOpt[Consignor],
          (json \ "Consignee").asOpt[Consignee],
          (json \ "AdditionalSupplyChainActor").asOpt[List[AdditionalSupplyChainActor]],
          (json \ "TransportEquipment").asOpt[List[TransportEquipment]],
          (json \ "LocationOfGoods").asOpt[LocationOfGoods],
          (json \ "DepartureTransportMeans").asOpt[List[DepartureTransportMeans]],
          (json \ "CountryOfRoutingOfConsignment").asOpt[List[CountryOfRoutingOfConsignment]],
          (json \ "ActiveBorderTransportMeans").asOpt[List[ActiveBorderTransportMeans]],
          (json \ "PlaceOfLoading").asOpt[PlaceOfLoading],
          (json \ "PlaceOfUnloading").asOpt[PlaceOfUnloading],
          (json \ "PreviousDocument").asOpt[List[PreviousDocument]],
          (json \ "TransportDocument").asOpt[List[TransportDocument]],
          (json \ "SupportingDocument").asOpt[List[SupportingDocument]],
          (json \ "AdditionalReference").asOpt[List[AdditionalReference]],
          (json \ "AdditionalInformation").asOpt[List[AdditionalInformation]],
          (json \ "TransportCharges").asOpt[TransportCharges],
          (json \ "HouseConsignment").as[Seq[HouseConsignment]]
        )
      )
  }

  implicit val consignmentWrites: Writes[Consignment] = new Writes[Consignment] {

    override def writes(consignment: Consignment): JsValue =
      Json.obj(
        "countryOfDispatch"             -> consignment.countryOfDispatch,
        "countryOfDestination"          -> consignment.countryOfDestination,
        "containerIndicator"            -> consignment.containerIndicator,
        "inlandModeOfTransport"         -> consignment.inlandModeOfTransport,
        "modeOfTransportAtTheBorder"    -> consignment.modeOfTransportAtTheBorder,
        "grossMass"                     -> consignment.grossMass,
        "referenceNumberUCR"            -> consignment.referenceNumberUCR,
        "carrier"                       -> consignment.carrier,
        "Consignor"                     -> consignment.Consignor,
        "Consignee"                     -> consignment.Consignee,
        "AdditionalSupplyChainActor"    -> consignment.AdditionalSupplyChainActor,
        "TransportEquipment"            -> consignment.TransportEquipment,
        "LocationOfGoods"               -> consignment.LocationOfGoods,
        "DepartureTransportMeans"       -> consignment.DepartureTransportMeans,
        "CountryOfRoutingOfConsignment" -> consignment.CountryOfRoutingOfConsignment,
        "ActiveBorderTransportMeans"    -> consignment.ActiveBorderTransportMeans,
        "PlaceOfLoading"                -> consignment.PlaceOfLoading,
        "PlaceOfUnloading"              -> consignment.PlaceOfUnloading,
        "PreviousDocument"              -> consignment.PreviousDocument,
        "TransportDocument"             -> consignment.TransportDocument,
        "SupportingDocument"            -> consignment.SupportingDocument,
        "AdditionalReference"           -> consignment.AdditionalReference,
        "AdditionalInformation"         -> consignment.AdditionalInformation,
        "TransportCharges"              -> consignment.TransportCharges,
        "HouseConsignment"              -> consignment.HouseConsignment
      )
  }

}
