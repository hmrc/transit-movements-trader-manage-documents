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

import generated.*
import viewmodels.*

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
  authorisations: String,
  containerIndicator: String,
  reducedDatasetIndicator: String
)

object Table2ViewModel {

  def apply(ie029: CC029CType): Table2ViewModel = {

    def combine(consignmentLevel: CUSTOM_ConsignmentType04 => Seq[String], houseConsignmentLevel: Seq[CUSTOM_HouseConsignmentType03] => Seq[String]): String =
      Seq(consignmentLevel(ie029.Consignment), houseConsignmentLevel(ie029.Consignment.HouseConsignment)).flatten.take3(_.semiColonSeparate)

    def combineWithSampling(consignmentLevel: CUSTOM_ConsignmentType04 => Seq[String],
                            houseConsignmentLevel: Seq[CUSTOM_HouseConsignmentType03] => Seq[String]
    ): String =
      Seq(consignmentLevel(ie029.Consignment), houseConsignmentLevel(ie029.Consignment.HouseConsignment)).flatten.takeSample

    new Table2ViewModel(
      transportEquipment = ie029.Consignment.TransportEquipment.map(_.asPostTransitionString).takeSample.adjustFor2WideLines,
      seals = ie029.Consignment.TransportEquipment
        .filter(
          _.Seal.nonEmpty
        )
        .map(
          te => (te.sequenceNumber, te.Seal)
        )
        .map(
          (teSeqNo, seals) => s"""$teSeqNo/${seals.map(_.identifier).firstAndLast("-")}"""
        )
        .takeSample,
      goodsReferences = ie029.Consignment.TransportEquipment
        .flatMap(_.GoodsReference)
        .map(
          goodsReference => s"${goodsReference.sequenceNumber},${goodsReference.declarationGoodsItemNumber}"
        )
        .take3(_.semiColonSeparate),
      previousDocuments = combineWithSampling(
        _.PreviousDocument.map(_.asSlashSeparatedString),
        _.flatMap(_.PreviousDocument).map(_.asSlashSeparatedString)
      ).adjustFor2WideLines,
      transportDocuments = combineWithSampling(
        _.TransportDocument.map(_.asSlashSeparatedString),
        _.flatMap(_.TransportDocument).map(_.asSlashSeparatedString)
      ).adjustFor2WideLines,
      supportingDocuments = combineWithSampling(
        _.SupportingDocument.map(_.asSlashSeparatedString),
        _.flatMap(_.SupportingDocument).map(_.asSlashSeparatedString)
      ).adjustFor2WideLines,
      additionalReferences = ie029.Consignment.AdditionalReference.map(_.asSlashSeparatedString).takeSample.take90,
      transportCharges = combine(
        _.TransportCharges.map(_.asString).toSeq,
        _.flatMap(_.TransportCharges).map(_.asString)
      ).take20,
      additionalInformation = ie029.Consignment.AdditionalInformation.map(_.asSlashSeparatedString).takeSample.take90,
      guarantees = ie029.Guarantee.map(_.asString).takeSample.adjustFor2WideLines,
      authorisations = ie029.Authorisation.map(_.asSlashSeparatedString).takeSample,
      containerIndicator = ie029.Consignment.containerIndicator.toString,
      reducedDatasetIndicator = ie029.TransitOperation.reducedDatasetIndicator.toString
    )
  }
}
