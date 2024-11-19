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

import generated._

import java.text.SimpleDateFormat
import javax.xml.datatype.XMLGregorianCalendar
import scala.annotation.tailrec

package object viewmodels {

  implicit class RichString(value: String) {

    private val Ellipsis = "..."

    // approximate values since we don't use a fixed width font
    private val NarrowLineCharLength      = 45
    private val NarrowLineBlankCharLength = 80 // a narrow line with only spaces need approx. 80 characters
    private val WideLineCharLength        = NarrowLineCharLength * 2
    private val WideLineBlankCharLength   = NarrowLineBlankCharLength * 2 // a narrow line with only spaces need approx. 160 characters

    def takeN(n: Int, chars: String = Ellipsis): String =
      if (value.length > n) {
        value.take(n - 3) + chars
      } else {
        value
      }

    def appendPeriod: String = value match {
      case v if v.nonEmpty && v.endsWith("...") => v
      case x if x.nonEmpty                      => s"$x."
      case _                                    => value
    }
    def adjustFor2NarrowLines: String = adjustForNNarrowLines(2)
    def adjustFor3NarrowLines: String = adjustForNNarrowLines(3)

    def adjustFor2WideLines: String = adjustForNWideLines(2)

    private def adjustForNNarrowLines(n: Int): String =
      adjustForNLines(n, NarrowLineCharLength, NarrowLineBlankCharLength)

    private def adjustForNWideLines(n: Int): String =
      adjustForNLines(n, WideLineCharLength, WideLineBlankCharLength)

    private def adjustForNLines(n: Int, lineLength: Int, blankLineLength: Int): String = {
      val charLength = lineLength * n
      if (value.length >= charLength) {
        value.take(charLength - 3) + Ellipsis
      } else {
        pad(n, lineLength, blankLineLength)
      }
    }

    private def pad(lines: Int, lineLength: Int, blankLineLength: Int): String = {
      @tailrec
      def rec(i: Int): String =
        if (i == lines) {
          value
        } else {
          if (value.length < lineLength * i) {
            value + (" " * blankLineLength * (lines - i))
          } else {
            rec(i + 1)
          }
        }

      rec(1)
    }

    def take10: String  = takeN(10)
    def take20: String  = takeN(20)
    def take30: String  = takeN(30)
    def take40: String  = takeN(40)
    def take45: String  = takeN(45)
    def take50: String  = takeN(50)
    def take60: String  = takeN(60)
    def take90: String  = takeN(90)
    def take100: String = takeN(100)
    def take200: String = takeN(200)
  }

  implicit class RichStringSeq(value: Seq[String]) {
    def commaSeparate: String     = value.mkString(", ")
    def slashSeparate: String     = value.mkString("/")
    def semiColonSeparate: String = value.mkString("; ")
    def dashSeparate: String      = value.mkString(" - ")
    def ellipsisSeparate: String  = value.mkString("...")

    def toBeContinued(stringIfEmpty: String = ""): String = value.toList match {
      case Nil         => stringIfEmpty
      case head :: Nil => head
      case head :: _   => s"$head..."
    }

    def firstAndLast(separator: String = "..."): String = {
      val (headOption, lastOption) = value match {
        case head :: tail => (Some(head), tail.lastOption)
        case _            => (None, None)
      }
      Seq(headOption, lastOption).flatten.mkString(separator)
    }

    def addDefaultIfEmpty(): Seq[String] = if (value.isEmpty) Seq("--") else value

    private def takeN(n: Int)(f: Seq[String] => String): String =
      if (value.length > n) {
        f(value.take(n)) + "..."
      } else {
        f(value.take(n))
      }

    def take1: String                           = takeN(1)(_.mkString)
    def take2(f: Seq[String] => String): String = takeN(2)(f)
    def take3(f: Seq[String] => String): String = takeN(3)(f)
  }

  implicit class RichOptionTSeq[T](value: Seq[Option[T]]) {

    def takeFirstIfAllTheSame: Option[T] = value match {
      case head :: tail if tail.forall(_ == head) => head
      case _                                      => None
    }
  }

  implicit class RichStringOption(value: Option[String]) {
    def orElseBlank: String = value.getOrElse("")

    def orElse2Dashes: String = value.getOrElse("--")
    def orElse3Dashes: String = value.getOrElse("---")
  }

  implicit class RichXMLGregorianCalendar(value: XMLGregorianCalendar) {

    private def format(pattern: String): String = {
      val date      = value.toGregorianCalendar.getTime
      val formatter = new SimpleDateFormat(pattern)
      formatter.format(date)
    }

    def dateAndTimeString: String = format("dd/MM/yyyy HH:mm")
    def dateString: String        = format("dd/MM/yyyy")
    def limitDateString: String   = format("yyyy-MM-dd")
  }

  implicit class RichFlag(value: Flag) {
    def asString: String = value.toString

    def toBoolean: Boolean = value match {
      case Number0 => false
      case Number1 => true
    }
  }

  implicit class RichBigDecimal(value: BigDecimal) {
    def asString: String = value.toDouble.toString
  }

  implicit class RichCC029CType(value: CC029CType) {

    /** In the IE015 submission we roll up the following to the consignment level if they are the same across all items:
      *   - transport charges
      *   - UCR
      *   - country of dispatch
      *   - country of destination
      *
      * Therefore we need to roll these back down here.
      */
    def rollDown: CC029CType = {
      val houseConsignments = value.Consignment.HouseConsignment
        .map {
          houseConsignment =>
            val consignmentItems = houseConsignment.ConsignmentItem
              .rollDown(value.Consignment.TransportCharges)(
                TransportCharges => _.copy(TransportCharges = Some(TransportCharges))
              )
              .rollDown(value.Consignment.referenceNumberUCR)(
                referenceNumberUCR => _.copy(referenceNumberUCR = Some(referenceNumberUCR))
              )
              .rollDown(value.Consignment.countryOfDispatch)(
                countryOfDispatch => _.copy(countryOfDispatch = Some(countryOfDispatch))
              )
              .rollDown(value.Consignment.countryOfDestination)(
                countryOfDestination => _.copy(countryOfDestination = Some(countryOfDestination))
              )
            houseConsignment.copy(ConsignmentItem = consignmentItems)
        }
      val consignment = value.Consignment.copy(HouseConsignment = houseConsignments)
      value.copy(Consignment = consignment)
    }

    val consignmentItems: Seq[CUSTOM_ConsignmentItemType03] =
      value.Consignment.HouseConsignment
        .flatMap(_.ConsignmentItem)

    val numberOfItems: Int = consignmentItems.size

    val numberOfPackages: BigInt = consignmentItems.flatMap(_.Packaging.flatMap(_.numberOfPackages)).sum
  }

  implicit class RichConsignmentItems(consignmentItems: Seq[CUSTOM_ConsignmentItemType03]) {

    def rollDown[T](value: Option[T])(rollDown: T => CUSTOM_ConsignmentItemType03 => CUSTOM_ConsignmentItemType03): Seq[CUSTOM_ConsignmentItemType03] =
      value match {
        case Some(t) => consignmentItems.map(rollDown(t))
        case None    => consignmentItems
      }
  }

  implicit class RichCC043CType(value: CC043CType) {

    private val consignmentItems: Seq[CUSTOM_ConsignmentItemType04] =
      value.Consignment.fold[Seq[CUSTOM_ConsignmentItemType04]](Nil)(_.HouseConsignment.flatMap(_.ConsignmentItem))

    val numberOfItems: Int = consignmentItems.size

    val numberOfPackages: BigInt = consignmentItems.flatMap(_.Packaging.flatMap(_.numberOfPackages)).sum
  }

  implicit class RichPackagingType02(value: PackagingType02) {

    private val values = Seq(
      Some(value.typeOfPackages),
      value.numberOfPackages.map(_.toString()),
      value.shippingMarks
    ).flatten

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate

    def asTransitionString: String = Seq(
      Some(value.typeOfPackages),
      value.numberOfPackages.map(_.toString())
    ).flatten.commaSeparate

    def asP4String: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeOfPackages),
      value.numberOfPackages.map(_.toString()),
      value.shippingMarks
    ).flatten.commaSeparate

  }

  implicit class RichPreviousDocumentType03(value: PreviousDocumentType03) {

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

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate

    def asP4String: String = Seq(
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.dashSeparate
  }

  implicit class RichPreviousDocumentType04(value: PreviousDocumentType04) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.goodsItemNumber.map(_.toString),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichPreviousDocumentType06(value: PreviousDocumentType06) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichPreviousDocumentType07(value: PreviousDocumentType07) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate
  }

  implicit class RichSupportingDocumentType02(value: SupportingDocumentType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.complementOfInformation
    ).flatten.commaSeparate
  }

  implicit class RichSupportingDocumentType06(value: SupportingDocumentType06) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      Some(value.referenceNumber),
      value.documentLineItemNumber.map(_.toString),
      value.complementOfInformation
    ).flatten

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate
  }

  implicit class RichTransportDocumentType02(value: TransportDocumentType02) {

    private val values = Seq(
      value.sequenceNumber.toString,
      value.typeValue,
      value.referenceNumber
    )

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate
  }

  implicit class RichAdditionalSupplyChainActorType(value: AdditionalSupplyChainActorType) {

    def asString: String = value.role
  }

  implicit class RichAdditionalInformationType02(value: AdditionalInformationType02) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.code),
      value.text
    ).flatten

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate
  }

  implicit class RichCommodityCodeType05(value: CommodityCodeType05) {

    def asString: String = Seq(
      Some(value.harmonizedSystemSubHeadingCode),
      value.combinedNomenclatureCode
    ).flatten.commaSeparate
  }

  implicit class RichDangerousGoodsType01(value: DangerousGoodsType01) {

    def asString: String = Seq(
      value.sequenceNumber.toString,
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

    private val values = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate
  }

  implicit class RichConsignorType03(value: ConsignorType03) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString)
    ).flatten.slashSeparate
  }

  implicit class RichConsignorType04(value: ConsignorType04) {

    def asString: String = Seq(
      value.name,
      value.Address.map(_.asString),
      value.ContactPerson.map(_.asString)
    ).flatten.slashSeparate

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
      Some(value.phoneNumber)
    ).flatten.slashSeparate
  }

  implicit class RichContactPersonType02(value: ContactPersonType02) {

    def asString: String = Seq(
      Some(value.name),
      Some(value.phoneNumber)
    ).flatten.commaSeparate
  }

  implicit class RichAdditionalReferenceType02(value: AdditionalReferenceType02) {

    private val values = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate
  }

  implicit class RichAdditionalReferenceType03(value: AdditionalReferenceType03) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.typeValue),
      value.referenceNumber
    ).flatten.commaSeparate
  }

  implicit class RichHolderOfTheTransitProcedureType05(value: HolderOfTheTransitProcedureType05) {

    def asString: String = Seq(
      value.TIRHolderIdentificationNumber,
      value.name,
      value.Address.map(_.asString)
    ).flatten.slashSeparate
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

    private val values = Seq(
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    )

    def asString: String = values.commaSeparate

    def asSlashSeparatedString: String = values.slashSeparate

    def asP4String: String = Seq(
      value.sequenceNumber.toString,
      value.typeOfIdentification,
      value.identificationNumber
    ).commaSeparate
  }

  implicit class RichCustomDepartureTransportMeansType02(value: CUSTOM_DepartureTransportMeansType02) {

    def asString: String = Seq(
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    ).flatten.commaSeparate

    def asP4String: String = Seq(
      Some(value.sequenceNumber.toString),
      value.typeOfIdentification,
      value.identificationNumber
    ).flatten.commaSeparate
  }

  implicit class RichConsignmentItemType04(value: CUSTOM_ConsignmentItemType04) {

    // TODO check if we can use N/A for absent optional values
    def asString: String = Seq(
      Some(value.goodsItemNumber.toString),
      Some(value.declarationGoodsItemNumber.toString),
      value.Commodity.CommodityCode.flatMap(_.combinedNomenclatureCode),
      value.Commodity.CommodityCode.map(_.harmonizedSystemSubHeadingCode),
      value.Commodity.GoodsMeasure.flatMap(
        gm => gm.netMass.map(_.toString)
      ),
      value.Commodity.GoodsMeasure.map(_.grossMass.toString),
      Some(value.Commodity.descriptionOfGoods)
    ).flatten.commaSeparate

  }

  implicit class RichActiveBorderTransportMeansType01(value: CUSTOM_ActiveBorderTransportMeansType01) {

    def asString: String = Seq(
      value.customsOfficeAtBorderReferenceNumber,
      value.typeOfIdentification,
      value.identificationNumber,
      value.nationality
    ).flatten.commaSeparate
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
      Some(value.sequenceNumber.toString),
      value.containerIdentificationNumber,
      Some(value.numberOfSeals.toString),
      Some(value.GoodsReference.map(_.asString).firstAndLast())
    ).flatten.commaSeparate

    def asStringWithoutGoodsRef: String = Seq(
      Some(value.sequenceNumber.toString),
      value.containerIdentificationNumber,
      Some(value.numberOfSeals.toString)
    ).flatten.commaSeparate

    def asP4String: String = Seq(
      Some(value.sequenceNumber.toString),
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
      Some(value.sequenceNumber.toString),
      Some(value.guaranteeType),
      Some(value.GuaranteeReference.map(_.asString).take2(_.semiColonSeparate)),
      value.otherGuaranteeReference
    ).flatten.commaSeparate
  }

  implicit class RichGuaranteeReferenceType01(value: CUSTOM_GuaranteeReferenceType01) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      value.GRN,
      value.amountToBeCovered.map(_.asString),
      value.currency
    ).flatten.commaSeparate
  }

  implicit class RichAuthorisationType02(value: AuthorisationType02) {

    def asString: String = Seq(
      value.sequenceNumber.toString,
      value.typeValue,
      value.referenceNumber
    ).commaSeparate
  }

  implicit class RichCountryOfRoutingOfConsignmentType01(value: CountryOfRoutingOfConsignmentType01) {

    def asString: String = Seq(
      value.sequenceNumber.toString,
      value.country
    ).commaSeparate
  }

  implicit class RichCustomsOfficeOfTransitDeclaredType04(value: CustomsOfficeOfTransitDeclaredType04) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
      Some(value.referenceNumber),
      value.arrivalDateAndTimeEstimated.map(_.dateAndTimeString)
    ).flatten.commaSeparate
  }

  implicit class RichCustomsOfficeOfExitForTransitDeclaredType02(value: CustomsOfficeOfExitForTransitDeclaredType02) {

    def asString: String = Seq(
      Some(value.sequenceNumber.toString),
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
