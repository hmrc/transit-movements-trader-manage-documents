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

import play.api.libs.functional.syntax._
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
                        consignor: Option[Consignor],
                        consignee: Option[Consignee],
                        additionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
                        transportEquipment: Option[List[TransportEquipment]],
                        locationOfGoods: Option[LocationOfGoods],
                        departureTransportMeans: Option[List[DepartureTransportMeans]],
                        countryOfRoutingOfConsignment: Option[List[CountryOfRoutingOfConsignment]],
                        activeBorderTransportMeans: Option[List[ActiveBorderTransportMeans]],
                        placeOfLoading: Option[PlaceOfLoading],
                        placeOfUnloading: Option[PlaceOfUnloading],
                        document: Document,
                        additionalReference: Option[List[AdditionalReference]],
                        additionalInformation: Option[List[AdditionalInformation]],
                        transportCharges: Option[TransportCharges],
                        houseConsignments: Seq[HouseConsignment]
) {

  val totalPackages: Int = houseConsignments.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalPackages
  )

  val totalItems: Int = houseConsignments.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalItems
  )

  val additionalSupplyChainActorsRole: Option[String] = additionalSupplyChainActor.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalSupplyChainActorIdentificationNumbers: Option[String] = additionalSupplyChainActor.map (
    _.map(_.identificationNumber).mkString("; ")
    )

  val departureTransportMeansDisplay: Option[String] = departureTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val departureTransportMeansIdentity: Option[String] = departureTransportMeans.map(
    _.map(_.typeOfIdentification).mkString("; ")
  )

  val transportEquipmentDisplay: Option[String] = transportEquipment.map(
    _.map(_.toString).mkString("; ")
  )

  val seals: Option[String] = transportEquipment.map(
    _.map(_.seal).mkString("; ")
  )

  val additionalInformationDisplay: Option[String] = additionalInformation.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalReferenceDisplay: Option[String] = additionalReference.map(
    _.map(_.toString).mkString("; ")
  )

  val transportChargesDisplay: Option[String] = transportCharges.map(_.toString)

  val activeBorderTransportMeansDisplay: Option[String] = activeBorderTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val countryOfRoutingOfConsignmentDisplay: Option[String] = countryOfRoutingOfConsignment.map(
    _.map(_.toString).mkString("; ")
  )

  val activeBorderTransportMeansConveyanceNumbers: Option[String] = activeBorderTransportMeans.map(
    _.map(_.conveyanceReferenceNumberToString).mkString("; ")
  )

  val consignmentItems: Seq[ConsignmentItem] = houseConsignments.foldLeft(Seq.empty[ConsignmentItem]) {
    (acc, house) => acc ++ house.consignmentItem.map(_.copy(Consignor = house.consignor, DepartureTransportMeans = departureTransportMeans))
  }

}

object Consignment {

  implicit val consignmentReads: Reads[Consignment] = new Reads[Consignment] {

    override def reads(json: JsValue): JsResult[Consignment] =
      JsSuccess(
        Consignment(
          (json \ "grossMass").as[Double],
          (json \ "inlandModeOfTransport").asOpt[String],
          (json \ "modeOfTransportAtTheBorder").asOpt[String],
          (json \ "countryOfDispatch").asOpt[String],
          (json \ "countryOfDestination").asOpt[String],
          (json \ "referenceNumberUCR").asOpt[String],
          (json \ "consignor").asOpt[Consignor],
          (json \ "consignee").asOpt[Consignee],
          (json \ "carrier").asOpt[Carrier],
          (json \ "document").as[Document],
          (json \ "locationOfGoods").asOpt[LocationOfGoods],
          (json \ "additionalSupplyChainActor").asOpt[List[AdditionalSupplyChainActor]],
          (json \ "departureTransportMeans").asOpt[List[DepartureTransportMeans]],
          (json \ "transportEquipment").asOpt[List[TransportEquipment]],
          (json \ "activeBorderTransportMeans").asOpt[List[ActiveBorderTransportMeans]],
          (json \ "placeOfLoading").asOpt[PlaceOfLoading],
          (json \ "placeOfUnloading").asOpt[PlaceOfUnloading],
          (json \ "houseConsignment").as[Seq[HouseConsignment]],
          (json \ "additionalInformation").asOpt[List[AdditionalInformation]],
          (json \ "additionalReference").asOpt[List[AdditionalReference]],
          (json \ "transportCharges").asOpt[TransportCharges],
          (json \ "countryOfRoutingOfConsignment").asOpt[List[CountryOfRoutingOfConsignment]]
        )
      )
  }

  implicit val itemWrites: Writes[Item] = new Writes[Item] {

    override def writes(item: Item): JsValue =
      Json.obj(
        "declarationGoodsItemNumber" -> item.declarationGoodsItemNumber,
        "goodsItemNumber" -> item.goodsItemNumber,
        "packaging" -> item.goodsItemNumber,
        "consignor" -> item.goodsItemNumber,
        "consignee" -> item.goodsItemNumber,
        "referenceNumberUcr" -> item.goodsItemNumber,
        "transportCharges" -> item.goodsItemNumber,
        "countryOfDispatch" -> item.goodsItemNumber,
        "countryOfDestination" -> item.goodsItemNumber,
        "declarationType" -> item.goodsItemNumber,
        "additionalSupplyChainActor" -> item.goodsItemNumber,
        "commodityCode" -> item.goodsItemNumber,
        "departureTransportMeans" -> item.goodsItemNumber,
        "dangerousGoods" -> item.goodsItemNumber,
        "cusCode" -> item.goodsItemNumber,
        "descriptionOfGoods" -> item.goodsItemNumber,
        "previousDocument" -> item.goodsItemNumber,
        "supportingDocument" -> item.goodsItemNumber,
        "transportDocument" -> item.goodsItemNumber,
        "additionalReference" -> item.goodsItemNumber,
        "additionalInformation" -> item.goodsItemNumber,
        "grossMass" -> item.goodsItemNumber,
        "netMass" -> item.goodsItemNumber
      )
  }

}
