/*
 * Copyright 2025 HM Revenue & Customs
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

package viewmodels

import generated.*

package object unloadingpermission {

  type TransitOperation                   = TransitOperationType10 // TransitOperationType14
  type CustomsOfficeOfDestinationActual   = CustomsOfficeOfDestinationActualType03
  type HolderOfTheTransitProcedure        = HolderOfTheTransitProcedureType06
  type HolderOfTheTransitProcedureAddress = AddressType15
  type TraderAtDestination                = TraderAtDestinationType02 // TraderAtDestinationType03
  type CTLControl                         = CTLControlType
  type Consignment                        = ConsignmentType05 // CUSTOM_ConsignmentType05

  type ConsignmentConsignor        = ConsignorType04 // ConsignorType05
  type ConsignmentConsignorAddress = AddressType14 // ConsignorType05
  type Consignee                   = ConsigneeType05 // ConsigneeType04
  type ConsigneeAddress            = AddressType14 // ConsigneeType04
  type TransportEquipment          = TransportEquipmentType03 // TransportEquipmentType05
  type Seal                        = SealType01
  type GoodsReference              = GoodsReferenceType01 // GoodsReferenceType02
  type DepartureTransportMeans     = CUSTOM_DepartureTransportMeansType01 // CUSTOM_DepartureTransportMeansType02
  type ConsignmentPreviousDocument = PreviousDocumentType05 // PreviousDocumentType06
  type SupportingDocument          = SupportingDocumentType02
  type TransportDocument           = TransportDocumentType01 // TransportDocumentType02
  type AdditionalReference         = AdditionalReferenceType02 // AdditionalReferenceType03
  type AdditionalInformation       = AdditionalInformationType02
  type Incident                    = IncidentType03 // IncidentType04
  type HouseConsignment            = HouseConsignmentType04 // CUSTOM_HouseConsignmentType04

  type Consignor                               = ConsignorType05 // ConsignorType06
  type ConsignorAddress                        = AddressType14 // AddressType07
  type HouseConsignmentDepartureTransportMeans = DepartureTransportMeansType01 // DepartureTransportMeansType02
  type HouseConsignmentPreviousDocument        = PreviousDocumentType06 // PreviousDocumentType07
  type ConsignmentItem                         = ConsignmentItemType04 // CUSTOM_ConsignmentItemType04

  type ConsignmentItemConsignee           = ConsigneeType01 // ConsigneeType03
  type ConsignmentItemConsigneeAddress    = AddressType01 // AddressType09
  type Commodity                          = CommodityType09 // CUSTOM_CommodityType08
  type Packaging                          = PackagingType01 // PackagingType02
  type ConsignmentItemPreviousDocument    = PreviousDocumentType03 // PreviousDocumentType04
  type ConsignmentItemAdditionalReference = AdditionalReferenceType01 // AdditionalReferenceType02

  type CommodityCode  = CommodityCodeType05
  type DangerousGoods = DangerousGoodsType01
  type GoodsMeasure   = CUSTOM_GoodsMeasureType05 // CUSTOM_GoodsMeasureType03

  implicit class RichCC043CType(value: CC043CType) {

    private val consignmentItems: Seq[ConsignmentItemType04] =
      value.Consignment.fold[Seq[ConsignmentItemType04]](Nil)(_.HouseConsignment.flatMap(_.ConsignmentItem))

    val numberOfItems: Int = consignmentItems.size

    val numberOfPackages: BigInt = consignmentItems.flatMap(_.Packaging.flatMap(_.numberOfPackages)).sum
  }

  implicit class RichPackagingType01(value: PackagingType01) {

    private val values = Seq(
      Some(value.typeOfPackages),
      value.numberOfPackages.map(_.toString()),
      value.shippingMarks
    ).flatten

    def asString: String = values.commaSeparate
  }

  implicit class RichTransportDocumentType01(value: TransportDocumentType01) {

    private val values = Seq(
      value.sequenceNumber.toString,
      value.typeValue,
      value.referenceNumber
    )

    def asString: String = values.commaSeparate
  }

  implicit class RichAdditionalInformationType02(value: AdditionalInformationType02) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.code),
      value.text
    ).flatten

    def asString: String = values.commaSeparate
  }

  implicit class RichConsigneeType05(value: ConsigneeType05) {

    private val values = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten

    def asString: String = values.commaSeparate
  }

  implicit class RichAdditionalReferenceType01(value: AdditionalReferenceType01) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten

    def asString: String = values.commaSeparate
  }

  implicit class RichAdditionalReferenceType02(value: AdditionalReferenceType02) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten

    def asString: String = values.commaSeparate
  }

  implicit class RichTransportEquipment(value: TransportEquipment) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      value.containerIdentificationNumber,
      Some(value.numberOfSeals.toString),
      Some(value.GoodsReference.map(_.asString).firstAndLast())
    ).flatten.commaSeparate
  }

  implicit class RichGoodsReference(value: GoodsReference) {

    def asString: String =
      s"${value.sequenceNumber}:${value.declarationGoodsItemNumber.toString()}"
  }

  implicit class RichConsignor(value: Consignor) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsignorAddress(value: ConsignorAddress) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentItemPreviousDocument(value: ConsignmentItemPreviousDocument) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.goodsItemNumber.map(_.toString),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichHouseConsignmentPreviousDocument(value: HouseConsignmentPreviousDocument) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichSupportingDocument(value: SupportingDocument) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichHolderOfTheTransitProcedure(value: HolderOfTheTransitProcedure) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.Address.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentItemConsignee(value: ConsignmentItemConsignee) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentItemConsigneeAddress(value: ConsignmentItemConsigneeAddress) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichDangerousGoods(value: DangerousGoods) {

    def asString: String = Seq(
      value.sequenceNumber.toString,
      value.UNNumber
    ).commaSeparate
  }

  implicit class RichCommodityCode(value: CommodityCode) {

    def asString: String = Seq(
      Some(value.harmonizedSystemSubHeadingCode),
      value.combinedNomenclatureCode
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentConsignor(value: ConsignmentConsignor) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichSeal(value: Seal) {

    def asString: String =
      s"${value.sequenceNumber},[${value.identifier}]"
  }

  implicit class RichConsignmentPreviousDocument(value: ConsignmentPreviousDocument) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten

    def asString: String = values.commaSeparate
  }

  implicit class RichHolderOfTheTransitProcedureAddress(value: HolderOfTheTransitProcedureAddress) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }
}
