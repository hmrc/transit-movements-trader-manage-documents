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

package viewmodels.P5

import generated.p5.CC029CType
import generated.p5.ConsigneeType04
import generated.p5.ConsignorType03

trait TADViewModel extends Formatters {
  val ie029: CC029CType

  private val consignorContactPersonAtHouseOfConsignment: String =
    ie029.Consignment.HouseConsignment.flatMap(_.Consignor.map(_.ContactPerson.map(_.toString))).flatten.mkString("; ")

  val consignorContactPerson = ie029.Consignment.Consignor.flatMap(_.ContactPerson) match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consignorContactPersonAtHouseOfConsignment)
  }

  private val consignorAtHouseOfConsignment: String = ie029.Consignment.HouseConsignment.map(_.Consignor.getOrElse("")).mkString(";")

  val consignor = ie029.Consignment.Consignor match {
    case Some(value) => truncate(200, value.toString)
    case None        => truncate(200, consignorAtHouseOfConsignment)
  }

  private val consigneeeAtHouseOfConsignment: String = ie029.Consignment.HouseConsignment.map(_.Consignee.getOrElse("")).mkString(";")

  val consignee: String = ie029.Consignment.Consignee match {
    case Some(value) => truncate(200, value.toString)
    case None        => truncate(200, consigneeeAtHouseOfConsignment)
  }
  val declarationType: String           = truncate(10, ie029.TransitOperation.declarationType.toString)
  val additionalDeclarationType: String = truncate(10, ie029.TransitOperation.additionalDeclarationType)
  val sci: String                       = truncate(10, ie029.TransitOperation.specificCircumstanceIndicator.getOrElse(""))
  val mrn: String                       = truncate(20, ie029.TransitOperation.MRN)

  val consigneeIdentificationNumber: String = ie029.Consignment.Consignee match {
    case Some(ConsigneeType04(Some(identificationNumber), _, _)) => truncate(20, identificationNumber)
    case None                                                    => "TODO get multiple consignor identification numbers"
  }

  val consignorIdentificationNumber: String = ie029.Consignment.Consignor match {
    case Some(ConsignorType03(Some(identificationNumber), _, _, _)) => truncate(20, identificationNumber)
    case None                                                       => "TODO get multiple consignor identification numbers"
  }
  val totalItems: Int    = ie029.Consignment.HouseConsignment.flatMap(_.ConsignmentItem).length
  val totalPackages: Int = ie029.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.Packaging.flatMap(_.numberOfPackages))).sum.toInt

  val totalGrossMass = ie029.Consignment.grossMass

  val security: String                                     = truncate(20, ie029.TransitOperation.security)
  val holderOfTransitProcedure: String                     = truncate(150, ie029.HolderOfTheTransitProcedure.toString)
  val holderOfTransitProcedureIdentificationNumber: String = truncate(20, ie029.HolderOfTheTransitProcedure.identificationNumber.getOrElse(""))

  val representative: String = ie029.Representative.map(_.toString).getOrElse("")

  val representativeIdentificationNumber: String = truncate(20, ie029.Representative.map(_.identificationNumber).getOrElse(""))
  val lrn: String                                = truncate(20, ie029.TransitOperation.LRN)
  val tir: String                                = truncate(20, ie029.TransitOperation.TIRCarnetNumber.getOrElse(""))

//  val carrierIdentificationNumber: String = truncate(20, ie029.Consignment.carrier.map(_.identificationNumber).getOrElse(""))
//
//  val additionalSupplyChainActorRoles: String = truncate(30, ie029.Consignment.additionalSupplyChainActorsRole.getOrElse(""))
//
//  val additionalSupplyChainActorIdentificationNumbers: String =
//    truncate(20, ie029.Consignment.additionalSupplyChainActorIdentificationNumbers.getOrElse(""))
//
//  val departureTransportMeans: String = truncate(50, ie029.Consignment.departureTransportMeansIdentity.getOrElse(""))
//  val ucr: String = truncate(20, ie029.Consignment.referenceNumberUCR.getOrElse(""))
//  val activeBorderTransportMeans: String = truncate(50, ie029.Consignment.activeBorderTransportMeansDisplay.getOrElse(""))
//
//  val activeBorderTransportMeansConveyanceNumbers: String = truncate(30, ie029.Consignment.activeBorderTransportMeansConveyanceNumbers.getOrElse(""))

  val placeOfLoading: String = truncate(20, ie029.Consignment.PlaceOfLoading.map(_.toString).getOrElse(""))

  val placeOfUnloading: String = truncate(20, ie029.Consignment.PlaceOfUnloading.map(_.toString).getOrElse(""))

  val inlandModeOfTransport: String = truncate(20, ie029.Consignment.inlandModeOfTransport.getOrElse(""))

  val modeOfTransportAtBorder: String = truncate(20, ie029.Consignment.modeOfTransportAtTheBorder.getOrElse(""))

  val locationOfGoods: String = truncate(100, ie029.Consignment.LocationOfGoods.map(_.toString).getOrElse(""))

  val locationOfGoodsContactPerson: String = truncate(100, ie029.Consignment.LocationOfGoods.flatMap(_.ContactPerson.map(_.toString)).getOrElse(""))

}
