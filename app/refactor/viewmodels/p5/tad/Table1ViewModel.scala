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

package refactor.viewmodels.p5.tad

import generated.p5._
import refactor.viewmodels._
import refactor.viewmodels.p5._

case class Table1ViewModel(
  additionalDeclarationType: String,
  consignees: Seq[ConsigneeType04],
  consignors: Seq[ConsignorType03],
  declarationType: String,
  holderOfTransitProcedure: String,
  holderOfTransitProcedureIdentificationNumber: String,
  representative: String,
  representativeIdentificationNumber: String,
  lrn: String,
  carrierIdentificationNumber: String,
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
) {

  val consignee: String = consignees.map(_.asString.take200).separateEntities

  val consigneeIdentificationNumber: String = consignees.flatMap(_.identificationNumber.map(_.take20)).separateEntities

  val consignorIdentificationNumber: String = consignors.flatMap(_.identificationNumber.map(_.take20)).separateEntities

  val consignorContactPerson: String = consignors.flatMap(_.ContactPerson.map(_.asString.take100)).separateEntities
}

object Table1ViewModel {

  def apply(ie029: CC029CType): Table1ViewModel =
    new Table1ViewModel(
      additionalDeclarationType = ie029.TransitOperation.additionalDeclarationType.take10,
      consignees = ie029.Consignment.Consignee.map(Seq(_)).getOrElse(ie029.Consignment.HouseConsignment.flatMap(_.Consignee)),
      consignors = ie029.Consignment.Consignor.map(Seq(_)).getOrElse(ie029.Consignment.HouseConsignment.flatMap(_.Consignor.map(_.asConsignorType03))),
      declarationType = ie029.TransitOperation.declarationType,
      holderOfTransitProcedure = ie029.HolderOfTheTransitProcedure.asString,
      holderOfTransitProcedureIdentificationNumber = ie029.HolderOfTheTransitProcedure.identificationNumber.orElseBlank,
      representative = ie029.Representative.map(_.asString).orElseBlank,
      representativeIdentificationNumber = ie029.Representative.map(_.identificationNumber).orElseBlank,
      lrn = ie029.TransitOperation.LRN,
      carrierIdentificationNumber = ie029.Consignment.Carrier.map(_.identificationNumber).orElseBlank.take20,
      additionalSupplyChainActorRoles = ie029.Consignment.AdditionalSupplyChainActor.map(_.asString).separateEntities.takeN(30),
      additionalSupplyChainActorIdentificationNumbers = ie029.Consignment.AdditionalSupplyChainActor.map(_.identificationNumber).separateEntities.take20,
      departureTransportMeans = ie029.Consignment.DepartureTransportMeans.map(_.asString).toBeContinued.takeN(50),
      activeBorderTransportMeans = ie029.Consignment.ActiveBorderTransportMeans.map(_.asString).separateEntities.takeN(50),
      activeBorderTransportMeansConveyanceNumbers = ie029.Consignment.ActiveBorderTransportMeans.flatMap(_.conveyanceReferenceNumber).separateEntities,
      placeOfLoading = ie029.Consignment.PlaceOfLoading.map(_.asString).orElseBlank.take20,
      placeOfUnloading = ie029.Consignment.PlaceOfUnloading.map(_.asString).orElseBlank.take20,
      modeOfTransportAtBorder = ie029.Consignment.modeOfTransportAtTheBorder.orElseBlank.take20,
      inlandModeOfTransport = ie029.Consignment.inlandModeOfTransport.orElseBlank.take20,
      locationOfGoods = ie029.Consignment.LocationOfGoods.map(_.asString).orElseBlank.take100,
      locationOfGoodsContactPerson = ie029.Consignment.LocationOfGoods.flatMap(_.ContactPerson.map(_.asString)).orElseBlank.take100,
      specificCircumstanceIndicator = ie029.TransitOperation.specificCircumstanceIndicator.orElseBlank.take10,
      security = ie029.TransitOperation.security,
      tir = ie029.TransitOperation.TIRCarnetNumber.orElseBlank,
      totalGrossMass = ie029.Consignment.grossMass.toString(),
      totalItems = ie029.Consignment.HouseConsignment.map(_.ConsignmentItem.length).sum.toString,
      totalPackages = ie029.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.Packaging.flatMap(_.numberOfPackages))).sum.toString(),
      ucr = ie029.Consignment.referenceNumberUCR.orElseBlank
    )
}
