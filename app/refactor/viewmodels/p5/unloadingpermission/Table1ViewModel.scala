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

package refactor.viewmodels.p5.unloadingpermission

import generated.p5._
import refactor.viewmodels._
import refactor.viewmodels.p5._

case class Table1ViewModel(
  consignorIdentificationNumber: String,
  consignor: String,
  consigneeIdentificationNumber: String,
  consignee: String,
  holderOfTransitID: String,
  holderOfTransit: String,
  declarationType: String,
  totalItems: String,
  totalPackages: String,
  totalGrossMass: String,
  tir: String,
  security: String,
  inlandModeOfTransport: String,
  departureTransportMeans: Seq[String],
  container: String,
  transportEquipment: String,
  seals: String,
  previousDocument: String,
  transportDocument: String,
  supportingDocument: String,
  additionalReference: String,
  additionalInformation: String,
  customsOfficeOfDestination: String,
  countryOfDestination: String
)

object Table1ViewModel {

  def apply(ie043: CC043CType): Table1ViewModel =
    new Table1ViewModel(
      consignorIdentificationNumber = ie043.Consignment.flatMap(_.Consignor.flatMap(_.identificationNumber)).orElseBlank,
      consignor = ie043.Consignment.flatMap(_.Consignor.map(_.asString)).orElseBlank,
      consigneeIdentificationNumber = ie043.Consignment.flatMap(_.Consignee.flatMap(_.identificationNumber)).orElseBlank,
      consignee = ie043.Consignment.flatMap(_.Consignee.map(_.asString)).orElseBlank,
      holderOfTransitID = ie043.HolderOfTheTransitProcedure.flatMap(_.identificationNumber).orElseBlank,
      holderOfTransit = ie043.HolderOfTheTransitProcedure.map(_.asString).orElseBlank,
      declarationType = ie043.TransitOperation.declarationType.orElseBlank,
      totalItems = ie043.numberOfItems.toString,
      totalPackages = ie043.numberOfPackages.toString(),
      totalGrossMass = ie043.Consignment.flatMap(_.grossMass).getOrElse(BigDecimal(0)).toString(),
      tir = ie043.HolderOfTheTransitProcedure.flatMap(_.TIRHolderIdentificationNumber).orElseBlank,
      security = ie043.TransitOperation.security,
      inlandModeOfTransport = ie043.Consignment.flatMap(_.inlandModeOfTransport).orElseBlank,
      departureTransportMeans = ie043.Consignment.fold[Seq[String]](Nil)(_.DepartureTransportMeans.map(_.asString)),
      container = ie043.Consignment.map(_.containerIndicator.asString).orElseBlank,
      transportEquipment = ie043.Consignment.fold[Seq[String]](Nil)(_.TransportEquipment.map(_.asString)).semiColonSeparate,
      seals = ie043.Consignment.fold[Seq[String]](Nil)(_.TransportEquipment.flatMap(_.Seal).map(_.asString)).semiColonSeparate,
      previousDocument = ie043.Consignment.fold[Seq[String]](Nil)(_.PreviousDocument.map(_.asString)).semiColonSeparate,
      transportDocument = ie043.Consignment.fold[Seq[String]](Nil)(_.TransportDocument.map(_.asString)).semiColonSeparate,
      supportingDocument = ie043.Consignment.fold[Seq[String]](Nil)(_.SupportingDocument.map(_.asString)).semiColonSeparate,
      additionalReference = ie043.Consignment.fold[Seq[String]](Nil)(_.AdditionalReference.map(_.asString)).semiColonSeparate,
      additionalInformation = ie043.Consignment.fold[Seq[String]](Nil)(_.AdditionalInformation.map(_.asString)).semiColonSeparate,
      customsOfficeOfDestination = ie043.CustomsOfficeOfDestinationActual.referenceNumber,
      countryOfDestination = ie043.Consignment.flatMap(_.countryOfDestination).orElseBlank
    )
}
