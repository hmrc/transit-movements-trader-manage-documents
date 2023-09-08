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
    ie029Data.data.consignment.houseConsignments.flatMap(_.consignor.map(_.contactPerson.map(_.toString))).flatten.mkString("; ")

  val consignorContactPerson = ie029Data.data.consignment.consignor.flatMap(_.contactPerson) match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consignorContactPersonAtHouseOfConsignment)
  }

  private val consignorAtHouseOfConsignment: String = ie029Data.data.consignment.houseConsignments.map(_.consignor.getOrElse("")).mkString(";")

  val consignor = ie029Data.data.consignment.consignor match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consignorAtHouseOfConsignment)
  }

  private val consigneeeAtHouseOfConsignment: String = ie029Data.data.consignment.houseConsignments.map(_.consignee.getOrElse("")).mkString(";")

  val consignee: String = ie029Data.data.consignment.consignee match {
    case Some(value) => truncate(100, value.toString)
    case None        => truncate(100, consigneeeAtHouseOfConsignment)
  }
  val declarationType: String           = truncate(10, ie029Data.data.transitOperation.declarationType.toString)
  val additionalDeclarationType: String = truncate(10, ie029Data.data.transitOperation.additionalDeclarationType)
  val sci: String                       = truncate(10, ie029Data.data.transitOperation.specificCircumstanceIndicator.getOrElse(""))
  val mrn: String                       = truncate(20, ie029Data.data.transitOperation.MRN)

  val consigneeIdentificationNumber: String = ie029Data.data.consignment.consignee match {
    case Some(Consignee(Some(identificationNumber), _, _, _)) => truncate(20, identificationNumber)
    case None                                                 => "TODO get multiple consignor identification numbers"
  }

  val consignorIdentificationNumber: String = ie029Data.data.consignment.consignor match {
    case Some(Consignor(Some(identificationNumber), _, _, _)) => truncate(20, identificationNumber)
    case None                                                 => "TODO get multiple consignor identification numbers"
  }
  val totalItems: Int    = ie029Data.data.consignment.totalItems
  val totalPackages: Int = ie029Data.data.consignment.totalPackages

  val totalGrossMass = ie029Data.data.consignment.grossMass

  val security: String                                     = truncate(20, ie029Data.data.transitOperation.security)
  val holderOfTransitProcedure: String                     = truncate(150, ie029Data.data.holderOfTheTransitProcedure.toString)
  val holderOfTransitProcedureIdentificationNumber: String = truncate(20, ie029Data.data.holderOfTheTransitProcedure.identificationNumber.getOrElse(""))

  val representative: String = ie029Data.data.representative.toString

  val representativeIdentificationNumber: String = truncate(20, ie029Data.data.representative.identificationNumber.getOrElse(""))
  val lrn: String                                = truncate(20, ie029Data.data.transitOperation.LRN)
  val tir: String                                = truncate(20, ie029Data.data.transitOperation.TIRCarnetNumber.getOrElse(""))

  val carrierIdentificationNumber: String = truncate(20, ie029Data.data.consignment.carrier.map(_.identificationNumber).getOrElse(""))

  val additionalSupplyChainActorRoles: String = truncate(30, ie029Data.data.consignment.additionalSupplyChainActorsRole.getOrElse(""))

  val additionalSupplyChainActorIdentificationNumbers: String =
    truncate(20, ie029Data.data.consignment.additionalSupplyChainActorIdentificationNumbers.getOrElse(""))

  val departureTransportMeans: String    = truncate(50, ie029Data.data.consignment.departureTransportMeansDisplay.getOrElse(""))
  val ucr: String                        = truncate(20, ie029Data.data.consignment.referenceNumberUCR.getOrElse(""))
  val activeBorderTransportMeans: String = truncate(50, ie029Data.data.consignment.activeBorderTransportMeansDisplay.getOrElse(""))

  val activeBorderTransportMeansConveyanceNumbers: String = truncate(30, ie029Data.data.consignment.activeBorderTransportMeansConveyanceNumbers.getOrElse(""))

  val placeOfLoading: String = truncate(20, ie029Data.data.consignment.placeOfLoading.map(_.toString).getOrElse(""))

  val placeOfUnloading: String = truncate(20, ie029Data.data.consignment.placeOfUnloading.map(_.toString).getOrElse(""))

  val inlandModeOfTransport: String = truncate(20, ie029Data.data.consignment.inlandModeOfTransport.getOrElse(""))

  val modeOfTransportAtBorder: String = truncate(20, ie029Data.data.consignment.modeOfTransportAtTheBorder.getOrElse(""))

  val locationOfGoods: String = truncate(100, ie029Data.data.consignment.locationOfGoods.map(_.toString).getOrElse(""))

  val locationOfGoodsContactPerson: String = truncate(100, ie029Data.data.consignment.locationOfGoods.flatMap(_.contactPerson.map(_.toString)).getOrElse(""))

}

case class Table2ViewModel(implicit ie029Data: IE029Data) {

  val transportEquipment: String = truncate(50, ie029Data.data.consignment.transportEquipmentDisplay.getOrElse(""))
  val seals: String              = truncate(50, ie029Data.data.consignment.sealsString.getOrElse(""))

  private val previousDocumentAtConsignment: String      = ie029Data.data.consignment.document.previousDocument.getOrElse("")
  private val previousDocumentAtHouseConsignment: String = ie029Data.data.consignment.houseConsignments.flatMap(_.previousDocumentInHC).mkString("")
  val previousDocument: String                           = truncate(100, houseConsignmentAppender(previousDocumentAtConsignment, previousDocumentAtHouseConsignment))

  private val supportingDocumentAtConsignment: String      = ie029Data.data.consignment.document.supportingDocument.getOrElse("")
  private val supportingDocumentAtHouseConsignment: String = ie029Data.data.consignment.houseConsignments.flatMap(_.supportingDocumentInHC).mkString("")
  val supportingDocument: String                           = truncate(100, houseConsignmentAppender(supportingDocumentAtConsignment, supportingDocumentAtHouseConsignment))

  private val transportDocumentAtConsignment      = ie029Data.data.consignment.document.transportDocument.getOrElse("")
  private val transportDocumentAtHouseConsignment = ie029Data.data.consignment.houseConsignments.flatMap(_.transportDocumentInHC).mkString("")
  val transportDocument: String                   = truncate(100, houseConsignmentAppender(transportDocumentAtConsignment, transportDocumentAtHouseConsignment))

  private val additionalInformationAtConsignment: String      = ie029Data.data.consignment.additionalInformationDisplay.getOrElse("")
  private val additionalInformationAtHouseConsignment: String = ie029Data.data.consignment.houseConsignments.flatMap(_.additionalInformationInHC).mkString("")
  val additionalInformation: String                           = truncate(100, houseConsignmentAppender(additionalInformationAtConsignment, additionalInformationAtHouseConsignment))

  private val additionalReferenceAtConsignment: String      = ie029Data.data.consignment.additionalReferenceDisplay.getOrElse("")
  private val additionalReferenceAtHouseConsignment: String = ie029Data.data.consignment.houseConsignments.flatMap(_.additionalReferenceInHC).mkString("")
  val additionalReference: String                           = truncate(100, houseConsignmentAppender(additionalReferenceAtConsignment, additionalReferenceAtHouseConsignment))

  private val transportChargesAtConsignment: String      = ie029Data.data.consignment.transportChargesDisplay.getOrElse("")
  private val transportChargesAtHouseConsignment: String = ie029Data.data.consignment.houseConsignments.flatMap(_.transportChargesInHC).mkString("")
  val transportCharges: String                           = truncate(20, houseConsignmentAppender(transportChargesAtConsignment, transportChargesAtHouseConsignment))

  val guarantee: String     = truncate(100, ie029Data.data.guaranteeDisplay.getOrElse(""))
  val authorisation: String = truncate(100, ie029Data.data.authorisationDisplay.getOrElse(""))

}

case class Table3ViewModel(implicit ie029Data: IE029Data)

case class Table4ViewModel(implicit ie029Data: IE029Data) {

  val countryOfRoutingOfConsignment: String = truncate(30, ie029Data.data.consignment.countryOfRoutingOfConsignmentDisplay.getOrElse(""))

  val customsOfficeOfTransitDeclared: String = truncate(60, ie029Data.data.customsOfficeOfTransitDeclaredDisplay.getOrElse(""))

  val customsOfficeOfExitForTransitDeclared: String = truncate(60, ie029Data.data.customsOfficeOfExitForTransitDeclaredDisplay.getOrElse(""))

  val customsOfficeOfDeparture: String = truncate(10, ie029Data.data.customsOfficeOfDepartureDisplay)

  val customsOfficeOfDestinationDeclared: String = truncate(10, ie029Data.data.customsOfficeOfDestinationDeclaredDisplay)

  val countryOfDispatch: String = truncate(10, ie029Data.data.consignment.countryOfDispatch.getOrElse(""))

  private val referenceNumberUCRAtConsignment: String      = ie029Data.data.consignment.referenceNumberUCR.getOrElse("")
  private val referenceNumberUCRAtHouseConsignment: String = ie029Data.data.consignment.houseConsignments.map(_.referenceNumberUCR.getOrElse("")).mkString(",")
  val referenceNumberUCR: String                           = truncate(20, houseConsignmentAppender(referenceNumberUCRAtConsignment, referenceNumberUCRAtHouseConsignment))

  val countryOfDestination: String = truncate(10, ie029Data.data.consignment.countryOfDestination.getOrElse(""))

}
