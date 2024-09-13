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

package viewmodels.tad.posttransition

import generated.p5._
import viewmodels._

case class Table2ViewModel(
  transportEquipment: String,
  seals: String,
  goodsReferences: String,
  previousDocuments: String,
  transportDocuments: String,
  supportingDocuments: String,
  additionalReferences: String,
  transportCharges: String,
  additionalInformation: String,
  guarantees: String,
  authorisations: String
)

object Table2ViewModel {

  def apply(ie029: CC029CType): Table2ViewModel = {

    def combine(consignmentLevel: ConsignmentType04 => Seq[String], houseConsignmentLevel: Seq[HouseConsignmentType03] => Seq[String]): String =
      Seq(consignmentLevel(ie029.Consignment), houseConsignmentLevel(ie029.Consignment.HouseConsignment)).flatten.take3(_.semiColonSeparate)

    new Table2ViewModel(
      transportEquipment = ie029.Consignment.TransportEquipment.map(_.asStringWithoutGoodsRef).take3(_.semiColonSeparate).adjustFor2WideLines,
      seals = ie029.Consignment.TransportEquipment
        .flatMap(_.Seal)
        .map(
          seal => s"${seal.sequenceNumber}/${seal.identifier}"
        )
        .firstAndLast(";"),
      goodsReferences = ie029.Consignment.TransportEquipment
        .flatMap(_.GoodsReference)
        .map(
          goodsReference => s"${goodsReference.sequenceNumber},${goodsReference.declarationGoodsItemNumber}"
        )
        .take3(_.semiColonSeparate),
      previousDocuments = combine(
        _.PreviousDocument.map(_.asString),
        _.flatMap(_.PreviousDocument).map(_.asString)
      ).adjustFor2WideLines,
      transportDocuments = combine(
        _.TransportDocument.map(_.asString),
        _.flatMap(_.TransportDocument).map(_.asString)
      ).adjustFor2WideLines,
      supportingDocuments = combine(
        _.SupportingDocument.map(_.asString),
        _.flatMap(_.SupportingDocument).map(_.asString)
      ).adjustFor2WideLines,
      additionalReferences = ie029.Consignment.AdditionalReference.map(_.asString).take3(_.semiColonSeparate).take90,
      transportCharges = combine(
        _.TransportCharges.map(_.asString).toSeq,
        _.flatMap(_.TransportCharges).map(_.asString)
      ).take20,
      additionalInformation = ie029.Consignment.AdditionalInformation.map(_.asString).take3(_.semiColonSeparate).take90,
      guarantees = ie029.Guarantee.map(_.asString).take2(_.semiColonSeparate).adjustFor2WideLines,
      authorisations = ie029.Authorisation.map(_.asString).take3(_.semiColonSeparate)
    )
  }
}
