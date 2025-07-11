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

package object tad {

  type TransitOperation                         = TransitOperationType08 // TransitOperationType12
  type Authorisation                            = AuthorisationType02
  type CustomsOfficeOfDeparture                 = CustomsOfficeOfDepartureType05 // CustomsOfficeOfDepartureType03
  type CustomsOfficeOfDestinationDeclared       = CustomsOfficeOfDestinationDeclaredType01
  type CustomsOfficeOfTransitDeclared           = CustomsOfficeOfTransitDeclaredType06 // CustomsOfficeOfTransitDeclaredType04
  type CustomsOfficeOfExitForTransitDeclared    = CustomsOfficeOfExitForTransitDeclaredType02
  type HolderOfTheTransitProcedure              = HolderOfTheTransitProcedureType23 // HolderOfTheTransitProcedureType05
  type HolderOfTheTransitProcedureAddress       = AddressType14 // AddressType07
  type HolderOfTheTransitProcedureContactPerson = ContactPersonType03 // AddressType07
  type Representative                           = RepresentativeType06 // RepresentativeType02
  type RepresentativeContactPerson              = ContactPersonType03
  type ControlResult                            = ControlResultType04 // ControlResultType02
  type Guarantee                                = GuaranteeType05 // GuaranteeType03
  type GuaranteeReference                       = CUSTOM_GuaranteeReferenceType03 // CUSTOM_GuaranteeReferenceType01
  type Consignment                              = ConsignmentType04 // CUSTOM_ConsignmentType04

  type Carrier                               = CarrierType06 // CarrierType03
  type CarrierContactPerson                  = ContactPersonType03
  type ConsignmentConsignor                  = ConsignorType03
  type ConsignmentConsignorAddress           = AddressType14
  type ConsignmentConsignorContactPerson     = ContactPersonType03 // ContactPersonType01
  type Consignee                             = ConsigneeType05 // ConsigneeType04
  type ConsigneeAddress                      = AddressType14
  type AdditionalSupplyChainActor            = AdditionalSupplyChainActorType01 // AdditionalSupplyChainActorType
  type TransportEquipment                    = TransportEquipmentType03 // TransportEquipmentType05
  type Seal                                  = SealType01 // SealType04
  type GoodsReference                        = GoodsReferenceType01
  type LocationOfGoods                       = LocationOfGoodsType04 // LocationOfGoodsType02
  type LocationOfGoodsCustomsOffice          = CustomsOfficeType02
  type LocationOfGoodsContactPerson          = ContactPersonType01 // ContactPersonType02
  type EconomicOperator                      = EconomicOperatorType02
  type LocationOfGoodsAddress                = AddressType06 // AddressType02
  type LocationOfGoodsPostcodeAddress        = PostcodeAddressType // PostcodeAddressType01
  type GNSS                                  = GNSSType
  type ConsignmentDepartureTransportMeans    = CUSTOM_DepartureTransportMeansType01 // CUSTOM_DepartureTransportMeansType02
  type CountryOfRoutingOfConsignment         = CountryOfRoutingOfConsignmentType02 // CountryOfRoutingOfConsignmentType01
  type ConsignmentActiveBorderTransportMeans = CUSTOM_ActiveBorderTransportMeansType03 // CUSTOM_ActiveBorderTransportMeansType01
  type PlaceOfLoading                        = PlaceOfLoadingType // PlaceOfLoadingType02
  type PlaceOfUnloading                      = PlaceOfUnloadingType // PlaceOfUnloadingType02
  type ConsignmentPreviousDocument           = PreviousDocumentType05 // PreviousDocumentType06
  type SupportingDocument                    = SupportingDocumentType03 // SupportingDocumentType03
  type TransportDocument                     = TransportDocumentType01 // TransportDocumentType02
  type ConsignmentAdditionalReference        = AdditionalReferenceType02 // AdditionalReferenceType03
  type AdditionalInformation                 = AdditionalInformationType02
  type TransportCharges                      = TransportChargesType01 // TransportChargesType
  type HouseConsignment                      = HouseConsignmentType03 // CUSTOM_HouseConsignmentType03

  type HouseConsignmentConsignor               = ConsignorType10 // ConsignorType04
  type HouseConsignmentConsignorAddress        = AddressType14
  type HouseConsignmentConsignorContactPerson  = ContactPersonType03
  type HouseConsignmentDepartureTransportMeans = DepartureTransportMeansType01 // DepartureTransportMeansType02
  type HouseConsignmentPreviousDocument        = PreviousDocumentType06 // PreviousDocumentType07
  type HouseConsignmentAdditionalReference     = AdditionalReferenceType02 // AdditionalReferenceType03
  type ConsignmentItem                         = ConsignmentItemType03 // CUSTOM_ConsignmentItemType03

  type Commodity                          = CommodityType09 // CUSTOM_CommodityType08
  type Packaging                          = PackagingType01 // PackagingType02
  type ConsignmentItemPreviousDocument    = PreviousDocumentType08 // PreviousDocumentType03
  type ConsignmentItemAdditionalReference = AdditionalReferenceType01 // AdditionalReferenceType02

  type CommodityCode  = CommodityCodeType05
  type DangerousGoods = DangerousGoodsType01
  type GoodsMeasure   = CUSTOM_GoodsMeasureType05 // CUSTOM_GoodsMeasureType03

  implicit class RichCC029CType(value: CC029CType) {

    val consignmentItems: Seq[ConsignmentItem] =
      value.Consignment.HouseConsignment
        .flatMap(_.ConsignmentItem)

    val numberOfItems: Int = consignmentItems.size

    val numberOfPackages: BigInt = consignmentItems.flatMap(_.Packaging.flatMap(_.numberOfPackages)).sum
  }

  implicit class RichAuthorisation(value: Authorisation) {

    private val values = Seq(
      value.sequenceNumber.toString,
      value.typeValue,
      value.referenceNumber
    )

    def asString: String = values.slashSeparate
  }

  implicit class RichCustomsOfficeOfDeparture(value: CustomsOfficeOfDeparture) {

    def asString: String = value.referenceNumber
  }

  implicit class RichCustomsOfficeOfDestinationDeclared(value: CustomsOfficeOfDestinationDeclared) {

    def asString: String = value.referenceNumber
  }

  implicit class RichCustomsOfficeOfTransitDeclared(value: CustomsOfficeOfTransitDeclared) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.referenceNumber),
      value.arrivalDateAndTimeEstimated.map(_.dateAndTimeString)
    ).flatten.commaSeparate
  }

  implicit class RichCustomsOfficeOfExitForTransitDeclared(value: CustomsOfficeOfExitForTransitDeclared) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.referenceNumber)
    ).flatten.commaSeparate
  }

  implicit class RichHolderOfTheTransitProcedure(value: HolderOfTheTransitProcedure) {

    def asString: String = Seq(
      value.TIRHolderIdentificationNumber,
      value.name,
      value.Address.map(_.asString)
    ).flatten.slashSeparate
  }

  implicit class RichRepresentative(value: Representative) {

    def asString: String = value.status
  }

  implicit class RichGuarantee(value: Guarantee) {

    def asString: String =
      Seq(
        Some(value.sequenceNumber.toString),
        Some(value.guaranteeType),
        Option.when(value.GuaranteeReference.nonEmpty)(value.GuaranteeReference.flatMap(_.asString).takeSampleWithoutPeriod),
        value.otherGuaranteeReference
      ).flatten.slashSeparate
  }

  implicit class RichConsignmentConsignor(value: ConsignmentConsignor) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.slashSeparate
  }

  implicit class RichConsignee(value: Consignee) {

    private val values = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichAdditionalSupplyChainActor(value: AdditionalSupplyChainActor) {

    def asString: String = Seq(value.sequenceNumber.toString, value.role).slashSeparate
  }

  implicit class RichTransportEquipment(value: TransportEquipment) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.containerIdentificationNumber.getOrElse("-")),
      if (value.numberOfSeals == 0) None else Some(value.numberOfSeals.toString),
      Some(value.GoodsReference.map(_.declarationGoodsItemNumber.toString).firstAndLast("-"))
    ).flatten.slashSeparate
  }

  implicit class RichLocationOfGoods(value: LocationOfGoods) {

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

  implicit class RichLocationOfGoodsAddress(value: LocationOfGoodsAddress) {

    def asString: String = Seq(
      Some(value.streetAndNumber),
      value.postcode,
      Some(value.city),
      Some(value.country)
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentDepartureTransportMeans(value: ConsignmentDepartureTransportMeans) {

    def asString: String = Seq(
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    ).flatten.slashSeparate
  }

  implicit class RichCountryOfRoutingOfConsignment(value: CountryOfRoutingOfConsignment) {

    private val values = Seq(
      value.sequenceNumber.toString,
      value.country
    )

    def asString: String = values.slashSeparate
  }

  implicit class RichConsignmentActiveBorderTransportMeans(value: ConsignmentActiveBorderTransportMeans) {

    private val values = Seq(
      value.customsOfficeAtBorderReferenceNumber,
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichPlaceOfLoading(value: PlaceOfLoading) {

    def asString: String = Seq(
      value.UNLocode,
      value.country,
      value.location
    ).flatten.commaSeparate
  }

  implicit class RichPlaceOfUnloading(value: PlaceOfUnloading) {

    def asString: String = Seq(
      value.UNLocode,
      value.country,
      value.location
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentPreviousDocument(value: ConsignmentPreviousDocument) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichConsignmentTransportDocument(value: TransportDocument) {

    private val values = Seq(
      value.sequenceNumber.toString,
      value.typeValue,
      value.referenceNumber
    )

    def asString: String = values.slashSeparate
  }

  implicit class RichSupportingDocument(value: SupportingDocument) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.documentLineItemNumber.map(_.toString),
      value.complementOfInformation
    ).flatten

    def asString: String = values.slashSeparate
  }

  // Covers Consignment, House consignment, and consignment item levels
  implicit class RichAdditionalInformation(value: AdditionalInformation) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.code),
      value.text
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichTransportCharges(value: TransportCharges) {

    def asString: String = value.methodOfPayment
  }

  implicit class RichHouseConsignmentDepartureTransportMeans(value: HouseConsignmentDepartureTransportMeans) {

    private val values = Seq(
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    )

    def asString: String = values.slashSeparate
  }

  implicit class RichHouseConsignmentAdditionalReference(value: HouseConsignmentAdditionalReference) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichPackaging(value: Packaging) {

    private val values = Seq(
      Some(value.typeOfPackages),
      value.numberOfPackages.map(_.toString()),
      value.shippingMarks
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichConsignmentItemPreviousDocument(value: ConsignmentItemPreviousDocument) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.goodsItemNumber.map(_.toString),
      value.typeOfPackages,
      value.numberOfPackages.map(_.toString),
      value.measurementUnitAndQualifier,
      value.quantity.map(_.toString),
      value.complementOfInformation
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichConsignmentItemAdditionalReference(value: ConsignmentItemAdditionalReference) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten

    def asString: String = values.slashSeparate
  }

  implicit class RichCommodityCode(value: CommodityCode) {

    def asString: String = Seq(
      Some(value.harmonizedSystemSubHeadingCode),
      value.combinedNomenclatureCode
    ).flatten.commaSeparate
  }

  implicit class RichDangerousGoods(value: DangerousGoods) {

    def asString: String = Seq(
      value.sequenceNumber.toString,
      value.UNNumber
    ).commaSeparate
  }

  implicit class RichGuaranteeReference(value: GuaranteeReference) {

    def asString: Option[String] = value.GRN
  }

  implicit class RichSeal(value: Seal) {

    def asString: String =
      s"${value.sequenceNumber},[${value.identifier}]"
  }

  implicit class RichGNSS(value: GNSS) {

    def asString: String = Seq(
      value.latitude,
      value.longitude
    ).commaSeparate
  }

  implicit class RichHouseConsignmentConsignor(value: HouseConsignmentConsignor) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString),
      value.ContactPerson.map(_.asString)
    ).flatten.slashSeparate

    def asConsignmentConsignor: ConsignmentConsignor = new ConsignmentConsignor(
      value.identificationNumber,
      value.name,
      value.Address,
      value.ContactPerson
    )
  }

  implicit class RichConsignorContactPerson(value: ConsignmentConsignorContactPerson) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber)
    ).flatten.slashSeparate
  }

  implicit class RichLocationOfGoodsContactPerson(value: LocationOfGoodsContactPerson) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber)
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

  implicit class RichPostcodeAddress(value: LocationOfGoodsPostcodeAddress) {

    def asString: String = Seq(
      value.houseNumber,
      Some(value.postcode),
      Some(value.country)
    ).flatten.commaSeparate
  }
}
