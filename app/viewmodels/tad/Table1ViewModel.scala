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

package viewmodels.tad

import generated._
import viewmodels._

case class Table1ViewModel(
  additionalDeclarationType: String,
  consignees: String,
  consigneeIdentificationNumbers: String,
  consignors: String,
  consignorIdentificationNumbers: String,
  consignorContactPersons: String,
  declarationType: String,
  holderOfTransitProcedure: String,
  holderOfTransitProcedureIdentificationNumber: String,
  hotPContactPerson: String,
  representative: String,
  representativeIdentificationNumber: String,
  representativeContactPerson: String,
  lrn: String,
  carrierIdentificationNumber: String,
  carrierContactPerson: String,
  additionalSupplyChainActorRoles: String,
  additionalSupplyChainActorIdentificationNumbers: String,
  departureTransportMeans: String,
  activeBorderTransportMeans: String,
  activeBorderTransportMeansConveyanceNumbers: String,
  placeOfLoading: String,
  placeOfUnloading: String,
  modeOfTransportAtBorder: String,
  inlandModeOfTransport: String,
  locationOfGoods: String,
  locationOfGoodsContactPerson: String,
  specificCircumstanceIndicator: String,
  security: String,
  tir: String,
  totalGrossMass: String,
  totalItems: String,
  totalPackages: String,
  ucr: String
)

object Table1ViewModel {

  def apply(ie029: CC029CType): Table1ViewModel = {

    val consignees = ie029.Consignment.Consignee.map(Seq(_)).getOrElse(ie029.Consignment.HouseConsignment.flatMap(_.Consignee))

    val consignors = ie029.Consignment.Consignor.map(Seq(_)).getOrElse(ie029.Consignment.HouseConsignment.flatMap(_.Consignor.map(_.asConsignmentConsignor)))

    new Table1ViewModel(
      additionalDeclarationType = ie029.TransitOperation.additionalDeclarationType.take10,
      consignees = consignees.map(_.asString).semiColonSeparate.appendPeriod.adjustFor2NarrowLines,
      consigneeIdentificationNumbers = consignees.flatMap(_.identificationNumber).semiColonSeparate.appendPeriod,
      consignors = consignors.map(_.asString).semiColonSeparate.appendPeriod.adjustFor3NarrowLines,
      consignorIdentificationNumbers = consignors.flatMap(_.identificationNumber).semiColonSeparate.appendPeriod,
      consignorContactPersons = consignors.flatMap(_.ContactPerson.map(_.asString)).semiColonSeparate.appendPeriod.adjustFor2NarrowLines,
      declarationType = ie029.TransitOperation.declarationType,
      holderOfTransitProcedure = ie029.HolderOfTheTransitProcedure.asString.appendPeriod.adjustFor3NarrowLines,
      holderOfTransitProcedureIdentificationNumber = ie029.HolderOfTheTransitProcedure.identificationNumber.orElseBlank.appendPeriod,
      hotPContactPerson = ie029.HolderOfTheTransitProcedure.ContactPerson.map(_.asString).orElseBlank.appendPeriod.adjustFor2NarrowLines,
      representative = ie029.Representative.map(_.asString).orElseBlank.appendPeriod,
      representativeIdentificationNumber = ie029.Representative.map(_.identificationNumber).orElseBlank.appendPeriod,
      representativeContactPerson = ie029.Representative.flatMap(_.ContactPerson.map(_.asString)).orElseBlank.appendPeriod.take45,
      lrn = ie029.TransitOperation.LRN,
      carrierIdentificationNumber = ie029.Consignment.Carrier.map(_.identificationNumber).orElseBlank.appendPeriod.take20,
      carrierContactPerson = ie029.Consignment.Carrier.flatMap(_.ContactPerson.map(_.asString)).orElseBlank.appendPeriod.take45,
      additionalSupplyChainActorRoles = ie029.Consignment.AdditionalSupplyChainActor.map(_.asString).takeSample,
      additionalSupplyChainActorIdentificationNumbers =
        ie029.Consignment.AdditionalSupplyChainActor.map(_.identificationNumber).takeSample.adjustFor2NarrowLines,
      departureTransportMeans = ie029.Consignment.DepartureTransportMeans.map(_.asString).takeSample.adjustFor2WideLines,
      activeBorderTransportMeans = ie029.Consignment.ActiveBorderTransportMeans.map(_.asString).takeSample.take90,
      activeBorderTransportMeansConveyanceNumbers =
        ie029.Consignment.ActiveBorderTransportMeans.flatMap(_.conveyanceReferenceNumber).takeSample.adjustFor2NarrowLines,
      placeOfLoading = ie029.Consignment.PlaceOfLoading.map(_.asString).orElseBlank.adjustFor2NarrowLines,
      placeOfUnloading = ie029.Consignment.PlaceOfUnloading.map(_.asString).orElseBlank.adjustFor2NarrowLines,
      modeOfTransportAtBorder = ie029.Consignment.modeOfTransportAtTheBorder.orElseBlank.take20,
      inlandModeOfTransport = ie029.Consignment.inlandModeOfTransport.orElseBlank.take20,
      locationOfGoods = ie029.Consignment.LocationOfGoods.map(_.asString).orElseBlank,
      locationOfGoodsContactPerson = ie029.Consignment.LocationOfGoods.flatMap(_.ContactPerson.map(_.asString)).orElseBlank.take100,
      specificCircumstanceIndicator = ie029.TransitOperation.specificCircumstanceIndicator.orElseBlank.take10,
      security = ie029.TransitOperation.security,
      tir = ie029.TransitOperation.TIRCarnetNumber.orElseBlank,
      totalGrossMass = ie029.Consignment.grossMass.asString,
      totalItems = ie029.numberOfItems.toString,
      totalPackages = ie029.numberOfPackages.toString(),
      ucr = ie029.Consignment.referenceNumberUCR.orElseBlank.take30
    )
  }
}
