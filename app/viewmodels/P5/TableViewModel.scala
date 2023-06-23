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

import models.P5.departure.CustomsOfficeOfDeparture
import models.P5.departure.Consignee
import models.P5.departure.Consignor
import models.P5.departure.IE029Data

case class TableViewModel(implicit ie029Data: IE029Data) {
  val table1ViewModel = Table1ViewModel.apply()
  val table2ViewModel = Table2ViewModel.apply()
  val table3ViewModel = Table3ViewModel.apply()
  val table4ViewModel = Table4ViewModel.apply()

}

case class Table1ViewModel(implicit ie029Data: IE029Data) {

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val consignee: String = ie029Data.data.Consignment.Consignee match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignee"
  }
  val declarationType: String           = ie029Data.data.TransitOperation.declarationType
  val additionalDeclarationType: String = ie029Data.data.TransitOperation.additionalDeclarationType
  val sci: String                       = ie029Data.data.TransitOperation.specificCircumstanceIndicator.getOrElse("")
  val mrn: String                       = ie029Data.data.TransitOperation.MRN

  val consigneeIdentificationNumber: String = ie029Data.data.Consignment.Consignee match {
    case Some(Consignee(Some(identificationNumber), _, _, _)) => identificationNumber
    case None                                                 => "TODO get multiple consignor identification numbers"
  }

  val consignorIdentificationNumber: String = ie029Data.data.Consignment.Consignor match {
    case Some(Consignor(Some(identificationNumber), _, _, _)) => identificationNumber
    case None                                                 => "TODO get multiple consignor identification numbers"
  }
  val totalItems: Int                                      = ie029Data.data.Consignment.totalItems
  val totalPackages: Int                                   = ie029Data.data.Consignment.totalPackages
  val totalGrossMass: Double                               = ie029Data.data.Consignment.grossMass
  val security: String                                     = ie029Data.data.TransitOperation.security
  val holderOfTransitProcedure: String                     = ie029Data.data.HolderOfTheTransitProcedure.toString
  val holderOfTransitProcedureIdentificationNumber: String = ie029Data.data.HolderOfTheTransitProcedure.identificationNumber.getOrElse("")

  val representative: String = ie029Data.data.Representative.toString

  val representativeIdentificationNumber: String = ie029Data.data.Representative.identificationNumber.getOrElse("")
  val lrn: String                                = ie029Data.data.TransitOperation.LRN
  val tir: String                                = ie029Data.data.TransitOperation.TIRCarnetNumber.getOrElse("")

  val carrierIdentificationNumber: String = ie029Data.data.Consignment.Carrier.map(_.identificationNumber).getOrElse("")

  val additionalSupplyChainActorRoles: String = ie029Data.data.Consignment.additionalSupplyChainActorsRole.getOrElse("")

  val additionalSupplyChainActorIdentificationNumbers: String = ie029Data.data.Consignment.additionalSupplyChainActorIdentificationNumbers.getOrElse("")

  val departureTransportMeans: String    = ie029Data.data.Consignment.departureTransportMeans.getOrElse("")
  val ucr: String                        = ie029Data.data.Consignment.referenceNumberUCR.getOrElse("")
  val activeBorderTransportMeans: String = ie029Data.data.Consignment.activeBorderTransportMeans.getOrElse("")

  val activeBorderTransportMeansConveyanceNumbers: String = ie029Data.data.Consignment.activeBorderTransportMeansConveyanceNumbers.getOrElse("")

  val placeOfLoading: String = ie029Data.data.Consignment.PlaceOfLoading.map(_.toString).getOrElse("")

  val placeOfUnloading: String = ie029Data.data.Consignment.PlaceOfUnloading.map(_.toString).getOrElse("")

  val inlandModeOfTransport: String = ie029Data.data.Consignment.inlandModeOfTransport.getOrElse("")

  val modeOfTransportAtBorder: String = ie029Data.data.Consignment.modeOfTransportAtTheBorder.getOrElse("")

  val locationOfGoods: String = ie029Data.data.Consignment.LocationOfGoods.map(_.toString).getOrElse("")

  val locationOfGoodsContactPerson: String = ie029Data.data.Consignment.LocationOfGoods.flatMap(_.ContactPerson.map(_.toString)).getOrElse("")

}

case class Table2ViewModel(implicit ie029Data: IE029Data) {

  val transportEquipment: String    = ie029Data.data.Consignment.transportEquipment.getOrElse("")
  val seals: String                 = ie029Data.data.Consignment.seals.getOrElse("")
  val previousDocument: String      = ie029Data.data.Consignment.Document.previousDocument.getOrElse("")
  val supportingDocument: String    = ie029Data.data.Consignment.Document.supportingDocument.getOrElse("")
  val transportDocument: String     = ie029Data.data.Consignment.Document.transportDocument.getOrElse("")
  val additionalInformation: String = ie029Data.data.Consignment.additionalInformation.getOrElse("")
  val additionalReference: String   = ie029Data.data.Consignment.additionalReference.getOrElse("")
  val transportCharges: String      = ie029Data.data.Consignment.transportCharges.getOrElse("")
  val guarantee: String             = ie029Data.data.guarantee.getOrElse("")
  val authorisation: String         = ie029Data.data.authorisation.getOrElse("")

}

case class Table3ViewModel(implicit ie029Data: IE029Data)

case class Table4ViewModel(implicit ie029Data: IE029Data) {

  val countryOfRoutingOfConsignment = ie029Data.data.Consignment.CountryOfRoutingOfConsignment match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val customsOfficeOfTransitDeclared = ie029Data.data.CustomsOfficeOfTransitDeclared match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val customsOfficeOfExitForTransitDeclared = ie029Data.data.CustomsOfficeOfExitForTransitDeclared match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val customsOfficeOfDeparture = ie029Data.data.CustomsOfficeOfDeparture.toString

  val customsOfficeOfDestinationDeclared = ie029Data.data.CustomsOfficeOfDestinationDeclared.toString

  val countryOfDispatch = ie029Data.data.Consignment.countryOfDispatch match {
    case Some(value) => value
    case None        => "TODO get multiple consignor"
  }

  val countryOfDestination = ie029Data.data.Consignment.countryOfDestination match {
    case Some(value) => value
    case None        => "TODO get multiple consignor"
  }

}
