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

  implicit class RichCC029CType(value: CC029CType) {

    val consignmentItems: Seq[ConsignmentItemType03] = value.Consignment.HouseConsignment.flatMap(_.ConsignmentItem)

    val numberOfItems: Int = consignmentItems.size
  }

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

    def asP4String: String = Seq(
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.dashSeparate
  }

  implicit class RichPreviousDocumentType04(value: PreviousDocumentType04) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.goodsItemNumber,
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichPreviousDocumentType06(value: PreviousDocumentType06) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichPreviousDocumentType07(value: PreviousDocumentType07) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichSupportingDocumentType02(value: SupportingDocumentType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      Some(value.referenceNumber),
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

  implicit class RichAddressType02(value: AddressType02) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
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

  implicit class RichAddressType10(value: AddressType10) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichPostcodeAddressType01(value: PostcodeAddressType01) {

    def asString: String = Seq(
      value.houseNumber,
      Some(value.postcode),
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
      value.ContactPerson.map(_.asString) // TODO - since we print the contact person separately do we need this?
    ).flatten.commaSeparate
  }

  implicit class RichConsignorType04(value: ConsignorType04) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString),
      value.ContactPerson.map(_.asString)
    ).flatten.commaSeparate

    // TODO - refactor when you create generic types for duplicates
    def asConsignorType03: ConsignorType03 = ConsignorType03(
      value.identificationNumber,
      value.name,
      value.Address,
      value.ContactPerson
    )
  }

  implicit class RichConsignorType05(value: ConsignorType05) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichContactPersonType01(value: ContactPersonType01) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber),
      value.eMailAddress
    ).flatten.commaSeparate
  }

  implicit class RichContactPersonType02(value: ContactPersonType02) {

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

  implicit class RichAdditionalReferenceType03(value: AdditionalReferenceType03) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten.commaSeparate
  }

  implicit class RichHolderOfTheTransitProcedureType05(value: HolderOfTheTransitProcedureType05) {

    def asString: String = Seq(
      value.TIRHolderIdentificationNumber,
      value.name,
      value.Address.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichHolderOfTheTransitProcedureType06(value: HolderOfTheTransitProcedureType06) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.Address.asString)
    ).flatten.commaSeparate
  }

  implicit class RichRepresentativeType02(value: RepresentativeType02) {

    def asString: String = value.status
  }

  implicit class RichDepartureTransportMeansType02(value: DepartureTransportMeansType02) {

    def asString: String = Seq(
      value.sequenceNumber,
      value.typeOfIdentification,
      value.identificationNumber
    ).commaSeparate
  }

  implicit class RichActiveBorderTransportMeansType01(value: ActiveBorderTransportMeansType01) {

    def asString: String = Seq(
      value.customsOfficeAtBorderReferenceNumber,
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    ).commaSeparate
  }

  implicit class RichPlaceOfLoadingType02(value: PlaceOfLoadingType02) {

    def asString: String = Seq(
      value.UNLocode,
      value.country,
      value.location
    ).flatten.commaSeparate
  }

  implicit class RichPlaceOfUnloadingType02(value: PlaceOfUnloadingType02) {

    def asString: String = Seq(
      value.UNLocode,
      value.country,
      value.location
    ).flatten.commaSeparate
  }

  implicit class RichLocationOfGoodsType02(value: LocationOfGoodsType02) {

    def asString: String = Seq(
      Some(value.typeOfLocation),
      Some(value.qualifierOfIdentification),
      value.authorisationNumber,
      value.UNLocode,
      value.CustomsOffice.map(_.referenceNumber),
      value.GNSS.map(_.asString),
      value.EconomicOperator.map(_.identificationNumber),
      value.Address.map(_.asString),
      value.PostcodeAddress.map(_.asString)
    ).flatten.commaSeparate
  }

  implicit class RichGNSSType(value: GNSSType) {

    def asString: String = Seq(
      value.latitude,
      value.longitude
    ).commaSeparate
  }

  implicit class RichTransportEquipmentType05(value: TransportEquipmentType05) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      value.containerIdentificationNumber,
      Some(value.numberOfSeals.toString),
      Some(value.GoodsReference.map(_.asString).firstAndLast())
    ).flatten.commaSeparate

    def asP4String: String = Seq(
      Some(value.sequenceNumber),
      value.containerIdentificationNumber
    ).flatten.commaSeparate
  }

  implicit class RichGoodsReferenceType02(value: GoodsReferenceType02) {

    def asString: String = value match {
      case GoodsReferenceType02(sequenceNumber, declarationGoodsItemNumber) =>
        s"$sequenceNumber:${declarationGoodsItemNumber.toString()}"
    }
  }

  implicit class RichSealType04(value: SealType04) {

    def asString: String = value match {
      case SealType04(sequenceNumber, identifier) =>
        s"$sequenceNumber,[$identifier]"
    }
  }

  implicit class RichGuaranteeType03(value: GuaranteeType03) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.guaranteeType),
      value.otherGuaranteeReference,
      Some(value.GuaranteeReference.map(_.asString).commaSeparate)
    ).flatten.semiColonSeparate
  }

  implicit class RichGuaranteeReferenceType01(value: GuaranteeReferenceType01) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      value.GRN,
      value.accessCode,
      Some(value.amountToBeCovered.toString()),
      Some(value.currency)
    ).flatten.commaSeparate
  }

  implicit class RichAuthorisationType02(value: AuthorisationType02) {

    def asString: String = Seq(
      value.sequenceNumber,
      value.typeValue,
      value.referenceNumber
    ).commaSeparate
  }

  implicit class RichCountryOfRoutingOfConsignmentType01(value: CountryOfRoutingOfConsignmentType01) {

    def asString: String = Seq(
      value.sequenceNumber,
      value.country
    ).commaSeparate
  }

  implicit class RichCustomsOfficeOfTransitDeclaredType04(value: CustomsOfficeOfTransitDeclaredType04) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.referenceNumber),
      value.arrivalDateAndTimeEstimated.map(_.dateAndTimeString)
    ).flatten.commaSeparate
  }

  implicit class RichCustomsOfficeOfExitForTransitDeclaredType02(value: CustomsOfficeOfExitForTransitDeclaredType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber),
      Some(value.referenceNumber)
    ).flatten.commaSeparate
  }

  implicit class RichCustomsOfficeOfDepartureType03(value: CustomsOfficeOfDepartureType03) {

    def asString: String = value.referenceNumber
  }

  implicit class RichCustomsOfficeOfDestinationDeclaredType01(value: CustomsOfficeOfDestinationDeclaredType01) {

    def asString: String = value.referenceNumber
  }
}
