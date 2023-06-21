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
import play.api.libs.json.OWrites
import play.api.libs.json.Reads
import play.api.libs.json.__

case class IE029Data(data: DepartureMessageData) {

  // TODO move this to viewmodel

  val lrn: String = data.TransitOperation.LRN

  val mrn: String = data.TransitOperation.MRN

  val tir: String = data.TransitOperation.TIRCarnetNumber.getOrElse("")

  val sci: String = data.TransitOperation.specificCircumstanceIndicator.getOrElse("")

  val ucr: String = data.Consignment.referenceNumberUCR.getOrElse("")

  val totalGrossMass: Double = data.Consignment.grossMass

  val totalPackages: Int = data.Consignment.totalPackages

  val totalItems: Int = data.Consignment.totalItems

  val security: String = data.TransitOperation.security

  val inlandModeOfTransport: String = data.Consignment.inlandModeOfTransport.getOrElse("")

  val modeOfTransportAtBorder: String = data.Consignment.modeOfTransportAtTheBorder.getOrElse("")

  val declarationType: String = data.TransitOperation.declarationType

  val additionalDeclarationType: String = data.TransitOperation.additionalDeclarationType

  val consignee: String = data.Consignment.Consignee match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignee"
  }

  val consigneeIdentificationNumber: String = data.Consignment.Consignee match {
    case Some(Consignee(Some(identificationNumber), _, _, _)) => identificationNumber
    case None                                                 => "TODO get multiple consignor identification numbers"
  }

  val consignor: String = data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val consignorIdentificationNumber: String = data.Consignment.Consignor match {
    case Some(Consignor(Some(identificationNumber), _, _, _)) => identificationNumber
    case None                                                 => "TODO get multiple consignor identification numbers"
  }

  val holderOfTransitProcedure: String = data.HolderOfTheTransitProcedure.toString

  val holderOfTransitProcedureIdentificationNumber: String = data.HolderOfTheTransitProcedure.identificationNumber.getOrElse("")

  val representative: String = data.Representative.toString

  val representativeIdentificationNumber: String = data.Representative.identificationNumber.getOrElse("")

  val carrierIdentificationNumber: String = data.Consignment.Carrier.map(_.identificationNumber).getOrElse("")

  val additionalSupplyChainActorRoles: String = data.Consignment.additionalSupplyChainActorsRole.getOrElse("")

  val additionalSupplyChainActorIdentificationNumbers: String = data.Consignment.additionalSupplyChainActorIdentificationNumbers.getOrElse("")

  val departureTransportMeans: String = data.Consignment.departureTransportMeans.getOrElse("")

  val transportEquipment: String                    = data.Consignment.transportEquipment.getOrElse("")
  val seals: String                                 = data.Consignment.seals.getOrElse("")
  val previousDocument: String                      = data.Consignment.previousDocument.getOrElse("")
  val supportingDocument: String                    = data.Consignment.supportingDocument.getOrElse("")
  val transportDocument: String                     = data.Consignment.transportDocument.getOrElse("")
  val additionalInformation: String                 = data.Consignment.additionalInformation.getOrElse("")
  val additionalReference: String                   = data.Consignment.additionalReference.getOrElse("")
  val transportCharges: String                      = data.Consignment.transportCharges.getOrElse("")
  val guarantee: String                             = data.guarantee.getOrElse("")
  val customsOfficeOfTransitDeclared: String        = data.customsOfficeOfTransitDeclared.getOrElse("")
  val customsOfficeOfExitForTransitDeclared: String = data.customsOfficeOfExitForTransitDeclared.getOrElse("")
  val customsOfficeOfDeparture: String              = data.customsOfficeOfDeparture
  val customsOfficeOfDestinationDeclared: String    = data.customsOfficeOfDestinationDeclared
  val authorisation: String                         = data.authorisation.getOrElse("")
  val activeBorderTransportMeans: String            = data.Consignment.activeBorderTransportMeans.getOrElse("")
  val countryOfRoutingOfConsignment: String         = data.Consignment.countryOfRoutingOfConsignment.getOrElse("")

  val activeBorderTransportMeansConveyanceNumbers: String = data.Consignment.activeBorderTransportMeansConveyanceNumbers.getOrElse("")

  val placeOfLoading: String = data.Consignment.PlaceOfLoading.map(_.toString).getOrElse("")

  val placeOfUnloading: String = data.Consignment.PlaceOfUnloading.map(_.toString).getOrElse("")

  val locationOfGoods: String = data.Consignment.LocationOfGoods.map(_.toString).getOrElse("")

  val locationOfGoodsContactPerson: String = data.Consignment.LocationOfGoods.flatMap(_.ContactPerson.map(_.toString)).getOrElse("")
  val countryOfDispatch: String            = ""
  val countryOfDestination: String         = ""

}

object IE029Data {
  implicit val reads: Reads[IE029Data]    = (__ \ "body" \ "n1:CC029C").read[DepartureMessageData].map(IE029Data.apply)
  implicit val writes: OWrites[IE029Data] = Json.writes[IE029Data]
}
