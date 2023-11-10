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

package refactor.viewmodels

import generated.p5._

package object p5 {

  implicit class RichPackagingType02(value: PackagingType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      value.numberOfPackages.map(_.toString()),
      Some(value.typeOfPackages),
      value.shippingMarks
    ).flatten.commaSeparate
  }

  implicit class RichPreviousDocumentType03(value: PreviousDocumentType03) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.goodsItemNumber.map(_.toString),
      value.typeOfPackages,
      value.numberOfPackages.map(_.toString),
      value.measurementUnitAndQualifier,
      value.quantity.map(_.toString),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichSupportingDocumentType06(value: SupportingDocumentType06) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.documentLineItemNumber.map(_.toString),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichTransportDocumentType02(value: TransportDocumentType02) {

    def asString: String = Seq(
      value.sequenceNumber,
      value.typeValue,
      value.referenceNumber
    ).commaSeparate
  }

  implicit class RichAdditionalSupplyChainActorType(value: AdditionalSupplyChainActorType) {

    def asString: String = value.role
  }

  implicit class RichAdditionalInformationType02(value: AdditionalInformationType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.code),
      value.text
    ).flatten.commaSeparate
  }

  implicit class RichCommodityCodeType05(value: CommodityCodeType05) {

    def asString: String = Seq(
      Some(value.harmonizedSystemSubHeadingCode),
      value.combinedNomenclatureCode
    ).flatten.commaSeparate
  }

  implicit class RichDangerousGoodsType01(value: DangerousGoodsType01) {

    def asString: String = Seq(
      value.sequenceNumber,
      value.UNNumber
    ).commaSeparate
  }

  implicit class RichTransportChargesType(value: TransportChargesType) {

    def asString: String = value.methodOfPayment
  }

  implicit class RichAddressType07(value: AddressType07) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichAddressType09(value: AddressType09) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichConsigneeType03(value: ConsigneeType03) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsigneeType04(value: ConsigneeType04) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsignorType03(value: ConsignorType03) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString),
      value.ContactPerson.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichConsignorType04(value: ConsignorType04) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString),
      value.ContactPerson.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichContactPersonType01(value: ContactPersonType01) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber),
      value.eMailAddress
    ).flatten.commaSeparate
  }

  implicit class RichAdditionalReferenceType02(value: AdditionalReferenceType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten.commaSeparate
  }
}
