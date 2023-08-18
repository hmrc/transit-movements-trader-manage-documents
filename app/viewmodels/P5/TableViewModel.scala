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

import models.P5.departure.Consignee
import models.P5.departure.Consignor
import models.P5.departure.IE029Data
import viewmodels.P5.TableViewModel.houseConsignmentAppender
import viewmodels.P5.TableViewModel.truncate

object TableViewModel {

  def truncate(maxLength: Int, input: String): String =
    if (input.length > maxLength) {
      input.take(maxLength - 3) + "..."
    } else input

  def houseConsignmentAppender(fieldAtConsignment: String, fieldAtHouseConsignment: String, appender: String = ";"): String =
    if (fieldAtConsignment.isEmpty) {
      fieldAtHouseConsignment
    } else if (fieldAtHouseConsignment.isEmpty) {
      fieldAtConsignment
    } else {
      s"$fieldAtConsignment$appender $fieldAtHouseConsignment"
    }

}

case class TableViewModel(implicit ie029Data: IE029Data) {

  val table1ViewModel = Table1ViewModel.apply()
  val table2ViewModel = Table2ViewModel.apply()
  val table3ViewModel = Table3ViewModel.apply()
  val table4ViewModel = Table4ViewModel.apply()

}

case class Table1ViewModel(implicit ie029Data: IE029Data) {

  private val consignorContactPersonAtHouseOfConsignment: String =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.Consignor.map(_.ContactPerson.map(_.toString))).flatten.mkString("; ")

  val consignorContactPerson = ie029Data.data.Consignment.Consignor.flatMap(_.ContactPerson) match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consignorContactPersonAtHouseOfConsignment)
  }

  private val consignorAtHouseOfConsignment: String = ie029Data.data.Consignment.HouseConsignment.map(_.Consignor.getOrElse("")).mkString(";")

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consignorAtHouseOfConsignment)
  }

  private val consigneeeAtHouseOfConsignment: String = ie029Data.data.Consignment.HouseConsignment.map(_.Consignee.getOrElse("")).mkString(";")

  val consignee: String = ie029Data.data.Consignment.Consignee match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consigneeeAtHouseOfConsignment)
  }
  val declarationType: String           = truncate(10, ie029Data.data.TransitOperation.declarationType.toString)
  val additionalDeclarationType: String = truncate(10, ie029Data.data.TransitOperation.additionalDeclarationType)
  val sci: String                       = truncate(10, ie029Data.data.TransitOperation.specificCircumstanceIndicator.getOrElse(""))
  val mrn: String                       = truncate(20, ie029Data.data.TransitOperation.MRN)

  val consigneeIdentificationNumber: String = ie029Data.data.Consignment.Consignee match {
    case Some(Consignee(Some(identificationNumber), _, _, _)) => truncate(20, identificationNumber)
    case None                                                 => "TODO get multiple consignor identification numbers"
  }

  val consignorIdentificationNumber: String = ie029Data.data.Consignment.Consignor match {
    case Some(Consignor(Some(identificationNumber), _, _, _)) => truncate(20, identificationNumber)
    case None                                                 => "TODO get multiple consignor identification numbers"
  }
  val totalItems: Int    = ie029Data.data.Consignment.totalItems
  val totalPackages: Int = ie029Data.data.Consignment.totalPackages

  val totalGrossMass = ie029Data.data.Consignment.grossMass

  val security: String                                     = truncate(20, ie029Data.data.TransitOperation.security)
  val holderOfTransitProcedure: String                     = truncate(150, ie029Data.data.HolderOfTheTransitProcedure.toString)
  val holderOfTransitProcedureIdentificationNumber: String = truncate(20, ie029Data.data.HolderOfTheTransitProcedure.identificationNumber.getOrElse(""))

  val representative: String = ie029Data.data.Representative.toString

  val representativeIdentificationNumber: String = truncate(20, ie029Data.data.Representative.identificationNumber.getOrElse(""))
  val lrn: String                                = truncate(20, ie029Data.data.TransitOperation.LRN)
  val tir: String                                = truncate(20, ie029Data.data.TransitOperation.TIRCarnetNumber.getOrElse(""))

  val carrierIdentificationNumber: String = truncate(20, ie029Data.data.Consignment.Carrier.map(_.identificationNumber).getOrElse(""))

  val additionalSupplyChainActorRoles: String = truncate(30, ie029Data.data.Consignment.additionalSupplyChainActorsRole.getOrElse(""))

  val additionalSupplyChainActorIdentificationNumbers: String =
    truncate(20, ie029Data.data.Consignment.additionalSupplyChainActorIdentificationNumbers.getOrElse(""))

  val departureTransportMeans: String    = truncate(50, ie029Data.data.Consignment.departureTransportMeans.getOrElse(""))
  val ucr: String                        = truncate(20, ie029Data.data.Consignment.referenceNumberUCR.getOrElse(""))
  val activeBorderTransportMeans: String = truncate(50, ie029Data.data.Consignment.activeBorderTransportMeans.getOrElse(""))

  val activeBorderTransportMeansConveyanceNumbers: String = truncate(30, ie029Data.data.Consignment.activeBorderTransportMeansConveyanceNumbers.getOrElse(""))

  val placeOfLoading: String = truncate(20, ie029Data.data.Consignment.PlaceOfLoading.map(_.toString).getOrElse(""))

  val placeOfUnloading: String = truncate(20, ie029Data.data.Consignment.PlaceOfUnloading.map(_.toString).getOrElse(""))

  val inlandModeOfTransport: String = truncate(20, ie029Data.data.Consignment.inlandModeOfTransport.getOrElse(""))

  val modeOfTransportAtBorder: String = truncate(20, ie029Data.data.Consignment.modeOfTransportAtTheBorder.getOrElse(""))

  val locationOfGoods: String = truncate(100, ie029Data.data.Consignment.LocationOfGoods.map(_.toString).getOrElse(""))

  val locationOfGoodsContactPerson: String = truncate(100, ie029Data.data.Consignment.LocationOfGoods.flatMap(_.ContactPerson.map(_.toString)).getOrElse(""))

}

case class Table2ViewModel(implicit ie029Data: IE029Data) {

  val transportEquipment: String = truncate(50, ie029Data.data.Consignment.transportEquipment.getOrElse(""))
  val seals: String              = truncate(50, ie029Data.data.Consignment.seals.getOrElse(""))

  private val previousDocumentAtConsignment: String      = ie029Data.data.Consignment.Document.previousDocument.getOrElse("")
  private val previousDocumentAtHouseConsignment: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.previousDocumentInHC).mkString("")
  val previousDocument: String                           = truncate(100, houseConsignmentAppender(previousDocumentAtConsignment, previousDocumentAtHouseConsignment))

  private val supportingDocumentAtConsignment: String      = ie029Data.data.Consignment.Document.supportingDocument.getOrElse("")
  private val supportingDocumentAtHouseConsignment: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.supportingDocumentInHC).mkString("")
  val supportingDocument: String                           = truncate(100, houseConsignmentAppender(supportingDocumentAtConsignment, supportingDocumentAtHouseConsignment))

  private val transportDocumentAtConsignment      = ie029Data.data.Consignment.Document.transportDocument.getOrElse("")
  private val transportDocumentAtHouseConsignment = ie029Data.data.Consignment.HouseConsignment.flatMap(_.transportDocumentInHC).mkString("")
  val transportDocument: String                   = truncate(100, houseConsignmentAppender(transportDocumentAtConsignment, transportDocumentAtHouseConsignment))

  private val additionalInformationAtConsignment: String      = ie029Data.data.Consignment.additionalInformation.getOrElse("")
  private val additionalInformationAtHouseConsignment: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.additionalInformationInHC).mkString("")
  val additionalInformation: String                           = truncate(100, houseConsignmentAppender(additionalInformationAtConsignment, additionalInformationAtHouseConsignment))

  private val additionalReferenceAtConsignment: String      = ie029Data.data.Consignment.additionalReference.getOrElse("")
  private val additionalReferenceAtHouseConsignment: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.additionalReferenceInHC).mkString("")
  val additionalReference: String                           = truncate(100, houseConsignmentAppender(additionalReferenceAtConsignment, additionalReferenceAtHouseConsignment))

  private val transportChargesAtConsignment: String      = ie029Data.data.Consignment.transportCharges.getOrElse("")
  private val transportChargesAtHouseConsignment: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.transportChargesInHC).mkString("")
  val transportCharges: String                           = truncate(20, houseConsignmentAppender(transportChargesAtConsignment, transportChargesAtHouseConsignment))

  val guarantee: String     = truncate(100, ie029Data.data.guarantee.getOrElse(""))
  val authorisation: String = truncate(100, ie029Data.data.authorisation.getOrElse(""))

}

case class Table3ViewModel(implicit ie029Data: IE029Data)

case class Table4ViewModel(implicit ie029Data: IE029Data) {

  val countryOfRoutingOfConsignment: String = truncate(30, ie029Data.data.Consignment.countryOfRoutingOfConsignment.getOrElse(""))

  val customsOfficeOfTransitDeclared: String = truncate(60, ie029Data.data.customsOfficeOfTransitDeclared.getOrElse(""))

  val customsOfficeOfExitForTransitDeclared: String = truncate(60, ie029Data.data.customsOfficeOfExitForTransitDeclared.getOrElse(""))

  val customsOfficeOfDeparture: String = truncate(10, ie029Data.data.customsOfficeOfDeparture)

  val customsOfficeOfDestinationDeclared: String = truncate(10, ie029Data.data.customsOfficeOfDestinationDeclared)

  val countryOfDispatch: String = truncate(10, ie029Data.data.Consignment.countryOfDispatch.getOrElse(""))

  private val referenceNumberUCRAtConsignment: String      = ie029Data.data.Consignment.referenceNumberUCR.getOrElse("")
  private val referenceNumberUCRAtHouseConsignment: String = ie029Data.data.Consignment.HouseConsignment.map(_.referenceNumberUCR.getOrElse("")).mkString(",")
  val referenceNumberUCR: String                           = truncate(20, houseConsignmentAppender(referenceNumberUCRAtConsignment, referenceNumberUCRAtHouseConsignment))

  val countryOfDestination: String = truncate(10, ie029Data.data.Consignment.countryOfDestination.getOrElse(""))

}
