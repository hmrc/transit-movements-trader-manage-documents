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

import base.SpecBase
import generated.p5._
import generators.ScalaxbModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import scalaxb.XMLCalendar

import javax.xml.datatype.XMLGregorianCalendar

trait DummyData extends ScalaxbModelGenerators {
  self: SpecBase =>

  private def calendar(date: String): XMLGregorianCalendar = XMLCalendar(date)

  lazy val cc015c: CC015CType = {
    val generated = arbitrary[CC015CType].sample.value
    generated.copy(TransitOperation = generated.TransitOperation.copy(limitDate = Some(calendar("2022-02-03T08:45:00.000000"))))
  }

  lazy val cc029c: CC029CType = CC029CType(
    messageSequence1 = arbitrary[MESSAGESequence].sample.value,
    TransitOperation = TransitOperationType12(
      LRN = "lrn",
      MRN = "mrn",
      declarationType = "T",
      additionalDeclarationType = "adt",
      TIRCarnetNumber = Some("tir"),
      declarationAcceptanceDate = calendar("1996-02-03T08:45:00.000000"),
      releaseDate = calendar("2023-02-03T08:45:00.000000"),
      security = "security",
      reducedDatasetIndicator = Number0,
      specificCircumstanceIndicator = Some("sci"),
      communicationLanguageAtDeparture = None,
      bindingItinerary = Number1
    ),
    Authorisation = Seq(
      AuthorisationType02(
        sequenceNumber = "1",
        typeValue = "C521",
        referenceNumber = "rn1"
      ),
      AuthorisationType02(
        sequenceNumber = "2",
        typeValue = "tv2",
        referenceNumber = "rn2"
      )
    ),
    CustomsOfficeOfDeparture = CustomsOfficeOfDepartureType03(
      referenceNumber = "cood"
    ),
    CustomsOfficeOfDestinationDeclared = CustomsOfficeOfDestinationDeclaredType01(
      referenceNumber = "coodd"
    ),
    CustomsOfficeOfTransitDeclared = Seq(
      CustomsOfficeOfTransitDeclaredType04(
        sequenceNumber = "1",
        referenceNumber = "cootd1",
        arrivalDateAndTimeEstimated = Some(calendar("2010-02-03T08:45:00.000000"))
      ),
      CustomsOfficeOfTransitDeclaredType04(
        sequenceNumber = "2",
        referenceNumber = "cootd2",
        arrivalDateAndTimeEstimated = Some(calendar("2015-02-03T08:45:00.000000"))
      )
    ),
    CustomsOfficeOfExitForTransitDeclared = Seq(
      CustomsOfficeOfExitForTransitDeclaredType02(
        sequenceNumber = "1",
        referenceNumber = "cooeftd1"
      ),
      CustomsOfficeOfExitForTransitDeclaredType02(
        sequenceNumber = "2",
        referenceNumber = "cooeftd2"
      )
    ),
    HolderOfTheTransitProcedure = HolderOfTheTransitProcedureType05(
      identificationNumber = Some("hin"),
      TIRHolderIdentificationNumber = Some("thin"),
      name = Some("hn"),
      Address = Some(
        AddressType07(
          streetAndNumber = "san",
          postcode = Some("pc"),
          city = "city",
          country = "country"
        )
      ),
      ContactPerson = Some(
        ContactPersonType01(
          name = "cp",
          phoneNumber = "cptel",
          eMailAddress = Some("cpemail")
        )
      )
    ),
    Representative = Some(
      RepresentativeType02(
        identificationNumber = "rid",
        status = "rstatus",
        ContactPerson = Some(
          ContactPersonType01(
            name = "cp",
            phoneNumber = "cptel",
            eMailAddress = Some("cpemail")
          )
        )
      )
    ),
    ControlResult = Some(
      ControlResultType02(
        code = "crcode",
        date = calendar("2017-02-03T08:45:00.000000"),
        controlledBy = "crctrl",
        text = Some("crtext")
      )
    ),
    Guarantee = Seq(
      GuaranteeType03(
        sequenceNumber = "1",
        guaranteeType = "g1",
        otherGuaranteeReference = Some("ogr1"),
        GuaranteeReference = Seq(
          GuaranteeReferenceType01(
            sequenceNumber = "1",
            GRN = Some("1grn1"),
            accessCode = Some("1ac1"),
            amountToBeCovered = Some(BigDecimal(11)),
            currency = Some("1c1")
          ),
          GuaranteeReferenceType01(
            sequenceNumber = "2",
            GRN = Some("1grn2"),
            accessCode = Some("1ac2"),
            amountToBeCovered = Some(BigDecimal(12)),
            currency = Some("1c2")
          )
        )
      ),
      GuaranteeType03(
        sequenceNumber = "2",
        guaranteeType = "g2",
        otherGuaranteeReference = Some("ogr2"),
        GuaranteeReference = Seq(
          GuaranteeReferenceType01(
            sequenceNumber = "1",
            GRN = Some("2grn1"),
            accessCode = Some("2ac1"),
            amountToBeCovered = Some(BigDecimal(21)),
            currency = Some("2c1")
          ),
          GuaranteeReferenceType01(
            sequenceNumber = "2",
            GRN = Some("2grn2"),
            accessCode = Some("2ac2"),
            amountToBeCovered = Some(BigDecimal(22)),
            currency = Some("2c2")
          )
        )
      )
    ),
    Consignment = ConsignmentType04(
      countryOfDispatch = Some("c of dispatch"),
      countryOfDestination = Some("c of destination"),
      containerIndicator = Some(Number1),
      inlandModeOfTransport = Some("imot"),
      modeOfTransportAtTheBorder = Some("motatb"),
      grossMass = BigDecimal(200),
      referenceNumberUCR = Some("ucr"),
      Carrier = Some(
        CarrierType03(
          identificationNumber = "cin",
          ContactPerson = Some(
            ContactPersonType01(
              name = "ccp",
              phoneNumber = "ccptel",
              eMailAddress = Some("ccpemail")
            )
          )
        )
      ),
      Consignor = Some(
        ConsignorType03(
          identificationNumber = Some("in"),
          name = Some("name"),
          Address = Some(
            AddressType07(
              streetAndNumber = "san",
              postcode = Some("pc"),
              city = "city",
              country = "country"
            )
          ),
          ContactPerson = Some(
            ContactPersonType01(
              name = "ccp",
              phoneNumber = "ccptel",
              eMailAddress = Some("ccpemail")
            )
          )
        )
      ),
      Consignee = Some(
        ConsigneeType04(
          identificationNumber = Some("in"),
          name = Some("name"),
          Address = Some(
            AddressType07(
              streetAndNumber = "san",
              postcode = Some("pc"),
              city = "city",
              country = "country"
            )
          )
        )
      ),
      AdditionalSupplyChainActor = Seq(
        AdditionalSupplyChainActorType(
          sequenceNumber = "1",
          role = "role1",
          identificationNumber = "id1"
        ),
        AdditionalSupplyChainActorType(
          sequenceNumber = "2",
          role = "role2",
          identificationNumber = "id2"
        )
      ),
      TransportEquipment = Seq(
        TransportEquipmentType05(
          sequenceNumber = "1",
          containerIdentificationNumber = Some("cin1"),
          numberOfSeals = BigInt(2),
          Seal = Seq(
            SealType04(
              sequenceNumber = "1",
              identifier = "sid1"
            ),
            SealType04(
              sequenceNumber = "2",
              identifier = "sid2"
            )
          ),
          GoodsReference = Seq(
            GoodsReferenceType02(
              sequenceNumber = "1",
              declarationGoodsItemNumber = BigInt(1)
            ),
            GoodsReferenceType02(
              sequenceNumber = "2",
              declarationGoodsItemNumber = BigInt(3)
            )
          )
        )
      ),
      LocationOfGoods = Some(
        LocationOfGoodsType02(
          typeOfLocation = "tol",
          qualifierOfIdentification = "qoi",
          authorisationNumber = Some("an"),
          additionalIdentifier = Some("ai"),
          UNLocode = Some("unl"),
          CustomsOffice = Some(
            CustomsOfficeType02(
              referenceNumber = "corn"
            )
          ),
          GNSS = Some(
            GNSSType(
              latitude = "lat",
              longitude = "lon"
            )
          ),
          EconomicOperator = Some(
            EconomicOperatorType01(
              identificationNumber = "eoin"
            )
          ),
          Address = Some(
            AddressType02(
              streetAndNumber = "san",
              postcode = Some("pc"),
              city = "city",
              country = "country"
            )
          ),
          PostcodeAddress = Some(
            PostcodeAddressType01(
              houseNumber = Some("hn"),
              postcode = "pc",
              country = "country"
            )
          ),
          ContactPerson = Some(
            ContactPersonType02(
              name = "logcp",
              phoneNumber = "logcptel",
              eMailAddress = Some("logcpemail")
            )
          )
        )
      ),
      DepartureTransportMeans = Seq(
        DepartureTransportMeansType02(
          sequenceNumber = "1",
          typeOfIdentification = Some("toi1"),
          identificationNumber = Some("in1"),
          nationality = Some("nat1")
        ),
        DepartureTransportMeansType02(
          sequenceNumber = "2",
          typeOfIdentification = Some("toi2"),
          identificationNumber = Some("in2"),
          nationality = Some("nat2")
        )
      ),
      CountryOfRoutingOfConsignment = Seq(
        CountryOfRoutingOfConsignmentType01(
          sequenceNumber = "1",
          country = "corocc1"
        ),
        CountryOfRoutingOfConsignmentType01(
          sequenceNumber = "2",
          country = "corocc2"
        )
      ),
      ActiveBorderTransportMeans = Seq(
        ActiveBorderTransportMeansType01(
          sequenceNumber = "1",
          customsOfficeAtBorderReferenceNumber = Some("coabrn1"),
          typeOfIdentification = Some("toi1"),
          identificationNumber = Some("in1"),
          nationality = Some("nat1"),
          conveyanceReferenceNumber = Some("crn1")
        ),
        ActiveBorderTransportMeansType01(
          sequenceNumber = "2",
          customsOfficeAtBorderReferenceNumber = Some("coabrn2"),
          typeOfIdentification = Some("toi2"),
          identificationNumber = Some("in2"),
          nationality = Some("nat2"),
          conveyanceReferenceNumber = Some("crn2")
        )
      ),
      PlaceOfLoading = Some(
        PlaceOfLoadingType02(
          UNLocode = Some("polunl"),
          country = Some("polc"),
          location = Some("poll")
        )
      ),
      PlaceOfUnloading = Some(
        PlaceOfUnloadingType02(
          UNLocode = Some("pouunl"),
          country = Some("pouc"),
          location = Some("poul")
        )
      ),
      PreviousDocument = Seq(
        PreviousDocumentType06(
          sequenceNumber = "1",
          typeValue = "ptv1",
          referenceNumber = "prn1",
          complementOfInformation = Some("pcoi1")
        ),
        PreviousDocumentType06(
          sequenceNumber = "2",
          typeValue = "ptv2",
          referenceNumber = "prn2",
          complementOfInformation = Some("pcoi1")
        )
      ),
      SupportingDocument = Seq(
        SupportingDocumentType06(
          sequenceNumber = "1",
          typeValue = "stv1",
          referenceNumber = "srn1",
          documentLineItemNumber = Some(BigInt(1)),
          complementOfInformation = Some("scoi1")
        ),
        SupportingDocumentType06(
          sequenceNumber = "2",
          typeValue = "stv2",
          referenceNumber = "srn2",
          documentLineItemNumber = Some(BigInt(1)),
          complementOfInformation = Some("scoi1")
        )
      ),
      TransportDocument = Seq(
        TransportDocumentType02(
          sequenceNumber = "1",
          typeValue = "ttv1",
          referenceNumber = "trn1"
        ),
        TransportDocumentType02(
          sequenceNumber = "2",
          typeValue = "ttv2",
          referenceNumber = "trn2"
        )
      ),
      AdditionalReference = Seq(
        AdditionalReferenceType03(
          sequenceNumber = "1",
          typeValue = "artv1",
          referenceNumber = Some("arrn1")
        ),
        AdditionalReferenceType03(
          sequenceNumber = "2",
          typeValue = "artv2",
          referenceNumber = Some("arrn2")
        )
      ),
      AdditionalInformation = Seq(
        AdditionalInformationType02(
          sequenceNumber = "1",
          code = "aic1",
          text = Some("ait1")
        ),
        AdditionalInformationType02(
          sequenceNumber = "2",
          code = "aic2",
          text = Some("ait2")
        )
      ),
      TransportCharges = Some(
        TransportChargesType(
          methodOfPayment = "mop"
        )
      ),
      HouseConsignment = Seq(
        HouseConsignmentType03(
          sequenceNumber = "1",
          grossMass = BigDecimal(100),
          ConsignmentItem = Seq(
            ConsignmentItemType03(
              goodsItemNumber = "1",
              declarationGoodsItemNumber = BigInt(1),
              countryOfDispatch = Some("c of dispatch"),
              countryOfDestination = Some("c of destination"),
              referenceNumberUCR = Some("ucr"),
              Packaging = Seq(
                PackagingType02(
                  sequenceNumber = "1",
                  typeOfPackages = "1top1",
                  numberOfPackages = Some(BigInt(11)),
                  shippingMarks = Some("1sm1")
                ),
                PackagingType02(
                  sequenceNumber = "2",
                  typeOfPackages = "1top2",
                  numberOfPackages = Some(BigInt(12)),
                  shippingMarks = Some("1sm2")
                )
              ),
              Commodity = CommodityType08(
                descriptionOfGoods = "dog",
                cusCode = Some("cus"),
                CommodityCode = Some(
                  CommodityCodeType05(
                    harmonizedSystemSubHeadingCode = "hsshc",
                    combinedNomenclatureCode = Some("cnc")
                  )
                ),
                DangerousGoods = Seq(
                  DangerousGoodsType01(
                    sequenceNumber = "1",
                    UNNumber = "unn1"
                  ),
                  DangerousGoodsType01(
                    sequenceNumber = "2",
                    UNNumber = "unn2"
                  )
                ),
                GoodsMeasure = Some(
                  GoodsMeasureType03(
                    grossMass = Some(BigDecimal(200)),
                    netMass = Some(BigDecimal(100))
                  )
                )
              ),
              TransportCharges = Some(
                TransportChargesType(
                  methodOfPayment = "mop"
                )
              )
            ),
            ConsignmentItemType03(
              goodsItemNumber = "2",
              declarationGoodsItemNumber = BigInt(2),
              countryOfDispatch = Some("c of dispatch"),
              countryOfDestination = Some("c of destination"),
              referenceNumberUCR = Some("ucr"),
              Packaging = Seq(
                PackagingType02(
                  sequenceNumber = "1",
                  typeOfPackages = "2top1",
                  numberOfPackages = Some(BigInt(21)),
                  shippingMarks = Some("2sm1")
                ),
                PackagingType02(
                  sequenceNumber = "2",
                  typeOfPackages = "2top2",
                  numberOfPackages = Some(BigInt(22)),
                  shippingMarks = Some("2sm2")
                )
              ),
              Commodity = CommodityType08(
                descriptionOfGoods = "dog",
                cusCode = Some("cus"),
                CommodityCode = Some(
                  CommodityCodeType05(
                    harmonizedSystemSubHeadingCode = "hsshc",
                    combinedNomenclatureCode = Some("cnc")
                  )
                ),
                DangerousGoods = Seq(
                  DangerousGoodsType01(
                    sequenceNumber = "1",
                    UNNumber = "unn1"
                  ),
                  DangerousGoodsType01(
                    sequenceNumber = "2",
                    UNNumber = "unn2"
                  )
                ),
                GoodsMeasure = Some(
                  GoodsMeasureType03(
                    grossMass = Some(BigDecimal(200)),
                    netMass = Some(BigDecimal(100))
                  )
                )
              ),
              TransportCharges = Some(
                TransportChargesType(
                  methodOfPayment = "mop"
                )
              )
            )
          )
        )
      )
    )
  )

  lazy val cc043c: CC043CType = CC043CType(
    messageSequence1 = arbitrary[MESSAGESequence].sample.value,
    TransitOperation = TransitOperationType14(
      MRN = "mrn",
      declarationType = Some("T"),
      declarationAcceptanceDate = None,
      security = "0",
      reducedDatasetIndicator = Number1
    ),
    CustomsOfficeOfDestinationActual = CustomsOfficeOfDestinationActualType03(
      referenceNumber = "cooda"
    ),
    HolderOfTheTransitProcedure = Some(
      HolderOfTheTransitProcedureType06(
        identificationNumber = Some("hin"),
        TIRHolderIdentificationNumber = Some("thin"),
        name = "hn",
        Address = AddressType10(
          streetAndNumber = "san",
          postcode = Some("pc"),
          city = "city",
          country = "country"
        )
      )
    ),
    TraderAtDestination = TraderAtDestinationType03(
      identificationNumber = "tad"
    ),
    CTLControl = Some(
      CTLControlType(
        continueUnloading = BigInt(5)
      )
    ),
    Consignment = Some(
      ConsignmentType05(
        countryOfDestination = Some("cod"),
        containerIndicator = Number1,
        inlandModeOfTransport = Some("imot"),
        grossMass = Some(BigDecimal(200)),
        Consignor = Some(
          ConsignorType05(
            identificationNumber = Some("in"),
            name = Some("name"),
            Address = Some(
              AddressType07(
                streetAndNumber = "san",
                postcode = Some("pc"),
                city = "city",
                country = "country"
              )
            )
          )
        ),
        Consignee = Some(
          ConsigneeType04(
            identificationNumber = Some("in"),
            name = Some("name"),
            Address = Some(
              AddressType07(
                streetAndNumber = "san",
                postcode = Some("pc"),
                city = "city",
                country = "country"
              )
            )
          )
        ),
        TransportEquipment = Seq(
          TransportEquipmentType05(
            sequenceNumber = "1",
            containerIdentificationNumber = Some("cin1"),
            numberOfSeals = BigInt(2),
            Seal = Seq(
              SealType04(
                sequenceNumber = "1",
                identifier = "sid1"
              ),
              SealType04(
                sequenceNumber = "2",
                identifier = "sid2"
              )
            ),
            GoodsReference = Seq(
              GoodsReferenceType02(
                sequenceNumber = "1",
                declarationGoodsItemNumber = BigInt(1)
              ),
              GoodsReferenceType02(
                sequenceNumber = "2",
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          )
        ),
        DepartureTransportMeans = Seq(
          DepartureTransportMeansType02(
            sequenceNumber = "1",
            typeOfIdentification = Some("toi1"),
            identificationNumber = Some("in1"),
            nationality = Some("nat1")
          ),
          DepartureTransportMeansType02(
            sequenceNumber = "2",
            typeOfIdentification = Some("toi2"),
            identificationNumber = Some("in2"),
            nationality = Some("nat2")
          )
        ),
        PreviousDocument = Seq(
          PreviousDocumentType06(
            sequenceNumber = "1",
            typeValue = "ptv1",
            referenceNumber = "prn1",
            complementOfInformation = Some("pcoi1")
          ),
          PreviousDocumentType06(
            sequenceNumber = "2",
            typeValue = "ptv2",
            referenceNumber = "prn2",
            complementOfInformation = Some("pcoi1")
          )
        ),
        SupportingDocument = Seq(
          SupportingDocumentType02(
            sequenceNumber = "1",
            typeValue = "stv1",
            referenceNumber = "srn1",
            complementOfInformation = Some("scoi1")
          ),
          SupportingDocumentType02(
            sequenceNumber = "2",
            typeValue = "stv2",
            referenceNumber = "srn2",
            complementOfInformation = Some("scoi1")
          )
        ),
        TransportDocument = Seq(
          TransportDocumentType02(
            sequenceNumber = "1",
            typeValue = "ttv1",
            referenceNumber = "trn1"
          ),
          TransportDocumentType02(
            sequenceNumber = "2",
            typeValue = "ttv2",
            referenceNumber = "trn2"
          )
        ),
        AdditionalReference = Seq(
          AdditionalReferenceType03(
            sequenceNumber = "1",
            typeValue = "artv1",
            referenceNumber = Some("arrn1")
          ),
          AdditionalReferenceType03(
            sequenceNumber = "2",
            typeValue = "artv2",
            referenceNumber = Some("arrn2")
          )
        ),
        AdditionalInformation = Seq(
          AdditionalInformationType02(
            sequenceNumber = "1",
            code = "aic1",
            text = Some("ait1")
          ),
          AdditionalInformationType02(
            sequenceNumber = "2",
            code = "aic2",
            text = Some("ait2")
          )
        ),
        Incident = Nil,
        HouseConsignment = Seq(
          HouseConsignmentType04(
            sequenceNumber = "1",
            grossMass = BigDecimal(100),
            ConsignmentItem = Seq(
              arbitrary[ConsignmentItemType04].sample.value,
              arbitrary[ConsignmentItemType04].sample.value
            )
          )
        )
      )
    )
  )

  lazy val houseConsignmentType03: HouseConsignmentType03 = HouseConsignmentType03(
    sequenceNumber = "1",
    countryOfDispatch = None,
    grossMass = BigDecimal(1),
    referenceNumberUCR = None,
    securityIndicatorFromExportDeclaration = None,
    Consignor = Some(
      ConsignorType04(
        identificationNumber = Some("hc consignor in"),
        name = Some("hc consignor name"),
        Address = Some(
          AddressType07(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        ),
        ContactPerson = Some(
          ContactPersonType01(
            name = "cp",
            phoneNumber = "cptel",
            eMailAddress = Some("cpemail")
          )
        )
      )
    ),
    Consignee = Some(
      ConsigneeType04(
        identificationNumber = Some("hc consignee in"),
        name = Some("hc consignee name"),
        Address = Some(
          AddressType07(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        )
      )
    ),
    AdditionalSupplyChainActor = Nil,
    DepartureTransportMeans = Seq(
      DepartureTransportMeansType02(
        sequenceNumber = "1",
        typeOfIdentification = Some("toi1"),
        identificationNumber = Some("in1"),
        nationality = Some("nat1")
      ),
      DepartureTransportMeansType02(
        sequenceNumber = "2",
        typeOfIdentification = Some("toi2"),
        identificationNumber = Some("in2"),
        nationality = Some("nat2")
      )
    ),
    PreviousDocument = Nil,
    SupportingDocument = Nil,
    TransportDocument = Nil,
    AdditionalReference = Nil,
    AdditionalInformation = Nil,
    TransportCharges = None,
    ConsignmentItem = Nil
  )

  lazy val consignmentItemType03: ConsignmentItemType03 = ConsignmentItemType03(
    goodsItemNumber = "gin",
    declarationGoodsItemNumber = BigInt(1),
    declarationType = Some("T"),
    countryOfDispatch = Some("c of dispatch"),
    countryOfDestination = Some("c of destination"),
    referenceNumberUCR = Some("ucr"),
    Consignee = Some(
      ConsigneeType03(
        identificationNumber = Some("in"),
        name = Some("name"),
        Address = Some(
          AddressType09(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        )
      )
    ),
    AdditionalSupplyChainActor = Seq(
      AdditionalSupplyChainActorType(
        sequenceNumber = "1",
        role = "role1",
        identificationNumber = "id1"
      ),
      AdditionalSupplyChainActorType(
        sequenceNumber = "2",
        role = "role2",
        identificationNumber = "id2"
      )
    ),
    Commodity = CommodityType08(
      descriptionOfGoods = "dog",
      cusCode = Some("cus"),
      CommodityCode = Some(
        CommodityCodeType05(
          harmonizedSystemSubHeadingCode = "hsshc",
          combinedNomenclatureCode = Some("cnc")
        )
      ),
      DangerousGoods = Seq(
        DangerousGoodsType01(
          sequenceNumber = "1",
          UNNumber = "unn1"
        ),
        DangerousGoodsType01(
          sequenceNumber = "2",
          UNNumber = "unn2"
        )
      ),
      GoodsMeasure = Some(
        GoodsMeasureType03(
          grossMass = Some(BigDecimal(200)),
          netMass = Some(BigDecimal(100))
        )
      )
    ),
    Packaging = Seq(
      PackagingType02(
        sequenceNumber = "1",
        typeOfPackages = "top1",
        numberOfPackages = Some(BigInt(100)),
        shippingMarks = Some("sm1")
      ),
      PackagingType02(
        sequenceNumber = "2",
        typeOfPackages = "top2",
        numberOfPackages = Some(BigInt(200)),
        shippingMarks = Some("sm2")
      )
    ),
    PreviousDocument = Seq(
      PreviousDocumentType03(
        sequenceNumber = "1",
        typeValue = "tv1",
        referenceNumber = "rn1",
        goodsItemNumber = Some(BigInt(1)),
        typeOfPackages = Some("top1"),
        numberOfPackages = Some(BigInt(11)),
        measurementUnitAndQualifier = Some("muaq1"),
        quantity = Some(BigDecimal(10)),
        complementOfInformation = Some("pcoi1")
      ),
      PreviousDocumentType03(
        sequenceNumber = "2",
        typeValue = "tv2",
        referenceNumber = "rn2",
        goodsItemNumber = Some(BigInt(2)),
        typeOfPackages = Some("top2"),
        numberOfPackages = Some(BigInt(22)),
        measurementUnitAndQualifier = Some("muaq2"),
        quantity = Some(BigDecimal(20)),
        complementOfInformation = Some("pcoi2")
      )
    ),
    SupportingDocument = Seq(
      SupportingDocumentType06(
        sequenceNumber = "1",
        typeValue = "stv1",
        referenceNumber = "srn1",
        documentLineItemNumber = Some(BigInt(11)),
        complementOfInformation = Some("scoi1")
      ),
      SupportingDocumentType06(
        sequenceNumber = "2",
        typeValue = "stv2",
        referenceNumber = "srn2",
        documentLineItemNumber = Some(BigInt(22)),
        complementOfInformation = Some("scoi2")
      )
    ),
    TransportDocument = Seq(
      TransportDocumentType02(
        sequenceNumber = "1",
        typeValue = "ttv1",
        referenceNumber = "trn1"
      ),
      TransportDocumentType02(
        sequenceNumber = "2",
        typeValue = "ttv2",
        referenceNumber = "trn2"
      )
    ),
    AdditionalReference = Seq(
      AdditionalReferenceType02(
        sequenceNumber = "1",
        typeValue = "artv1",
        referenceNumber = Some("arrn1")
      ),
      AdditionalReferenceType02(
        sequenceNumber = "2",
        typeValue = "artv2",
        referenceNumber = Some("arrn2")
      )
    ),
    AdditionalInformation = Seq(
      AdditionalInformationType02(
        sequenceNumber = "1",
        code = "aic1",
        text = Some("ait1")
      ),
      AdditionalInformationType02(
        sequenceNumber = "2",
        code = "aic2",
        text = Some("ait2")
      )
    ),
    TransportCharges = Some(
      TransportChargesType(
        methodOfPayment = "mop"
      )
    )
  )

}
