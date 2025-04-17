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

package viewmodels

import base.SpecBase
import generated.*
import generators.{IE015ScalaxbModelGenerators, ScalaxbModelGenerators}
import models.IE015
import org.scalacheck.Arbitrary.arbitrary
import scalaxb.XMLCalendar

import java.time.LocalDate
import javax.xml.datatype.XMLGregorianCalendar

trait DummyData extends IE015ScalaxbModelGenerators with ScalaxbModelGenerators {
  self: SpecBase =>

  private def calendar(date: String): XMLGregorianCalendar = XMLCalendar(date)

  lazy val cc015c: IE015 = {
    val generated = arbitrary[IE015].sample.value
    generated.copy(
      TransitOperation = generated.TransitOperation.copy(
        limitDate = Some(LocalDate.of(2022, 2, 3))
      ),
      Consignment = generated.Consignment.copy(
        HouseConsignment = Nil
      )
    )
  }

  lazy val cc029c: CC029CType = {
    import viewmodels.tad.*
    CC029CType(
      messageSequence1 = arbitrary[MESSAGESequence].sample.value,
      TransitOperation = new TransitOperation(
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
        new Authorisation(
          sequenceNumber = 1,
          typeValue = "C521",
          referenceNumber = "rn1"
        ),
        new Authorisation(
          sequenceNumber = 2,
          typeValue = "tv2",
          referenceNumber = "rn2"
        ),
        new Authorisation(
          sequenceNumber = 3,
          typeValue = "tv3",
          referenceNumber = "rn3"
        ),
        new Authorisation(
          sequenceNumber = 4,
          typeValue = "tv4",
          referenceNumber = "rn4"
        )
      ),
      CustomsOfficeOfDeparture = new CustomsOfficeOfDeparture(
        referenceNumber = "cood"
      ),
      CustomsOfficeOfDestinationDeclared = new CustomsOfficeOfDestinationDeclared(
        referenceNumber = "coodd"
      ),
      CustomsOfficeOfTransitDeclared = Seq(
        new CustomsOfficeOfTransitDeclared(
          sequenceNumber = 1,
          referenceNumber = "cootd1",
          arrivalDateAndTimeEstimated = Some(calendar("2010-02-03T08:45:00.000000"))
        ),
        new CustomsOfficeOfTransitDeclared(
          sequenceNumber = 2,
          referenceNumber = "cootd2",
          arrivalDateAndTimeEstimated = Some(calendar("2015-02-03T08:45:00.000000"))
        )
      ),
      CustomsOfficeOfExitForTransitDeclared = Seq(
        new CustomsOfficeOfExitForTransitDeclared(
          sequenceNumber = 1,
          referenceNumber = "cooeftd1"
        ),
        new CustomsOfficeOfExitForTransitDeclared(
          sequenceNumber = 2,
          referenceNumber = "cooeftd2"
        )
      ),
      HolderOfTheTransitProcedure = new HolderOfTheTransitProcedure(
        identificationNumber = Some("hin"),
        TIRHolderIdentificationNumber = Some("thin"),
        name = Some("hn"),
        Address = Some(
          new HolderOfTheTransitProcedureAddress(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        ),
        ContactPerson = Some(
          new HolderOfTheTransitProcedureContactPerson(
            name = "cp",
            phoneNumber = "cptel",
            eMailAddress = Some("cpemail")
          )
        )
      ),
      Representative = Some(
        new Representative(
          identificationNumber = "rid",
          status = "rstatus",
          ContactPerson = Some(
            new RepresentativeContactPerson(
              name = "cp",
              phoneNumber = "cptel",
              eMailAddress = Some("cpemail")
            )
          )
        )
      ),
      ControlResult = Some(
        new ControlResult(
          code = "crcode",
          date = calendar("2017-02-03T08:45:00.000000"),
          controlledBy = "crctrl",
          text = Some("crtext")
        )
      ),
      Guarantee = Seq(
        new Guarantee(
          sequenceNumber = 1,
          guaranteeType = "5",
          otherGuaranteeReference = Some("ogr1"),
          GuaranteeReference = Seq(
            new GuaranteeReference(
              sequenceNumber = 1,
              GRN = Some("1grn1"),
              accessCode = Some("1ac1"),
              amountToBeCovered = Some(BigDecimal(11)),
              currency = Some("1c1")
            ),
            new GuaranteeReference(
              sequenceNumber = 2,
              GRN = Some("1grn2"),
              accessCode = Some("1ac2"),
              amountToBeCovered = Some(BigDecimal(12)),
              currency = Some("1c2")
            ),
            new GuaranteeReference(
              sequenceNumber = 3,
              GRN = Some("1grn3"),
              accessCode = Some("1ac3"),
              amountToBeCovered = Some(BigDecimal(13)),
              currency = Some("1c3")
            )
          )
        ),
        new Guarantee(
          sequenceNumber = 2,
          guaranteeType = "1",
          otherGuaranteeReference = Some("ogr2"),
          GuaranteeReference = Seq(
            new GuaranteeReference(
              sequenceNumber = 1,
              GRN = Some("2grn1"),
              accessCode = Some("2ac1"),
              amountToBeCovered = Some(BigDecimal(21)),
              currency = Some("2c1")
            ),
            new GuaranteeReference(
              sequenceNumber = 2,
              GRN = Some("2grn2"),
              accessCode = Some("2ac2"),
              amountToBeCovered = Some(BigDecimal(22)),
              currency = Some("2c2")
            )
          )
        )
      ),
      Consignment = new Consignment(
        countryOfDispatch = Some("c of dispatch"),
        countryOfDestination = Some("c of destination"),
        containerIndicator = Number1,
        inlandModeOfTransport = Some("imot"),
        modeOfTransportAtTheBorder = Some("motatb"),
        grossMass = BigDecimal(200),
        referenceNumberUCR = Some("ucr"),
        Carrier = Some(
          new Carrier(
            identificationNumber = "cin",
            ContactPerson = Some(
              new CarrierContactPerson(
                name = "ccp",
                phoneNumber = "ccptel",
                eMailAddress = Some("ccpemail")
              )
            )
          )
        ),
        Consignor = Some(
          new ConsignmentConsignor(
            identificationNumber = Some("in"),
            name = Some("name"),
            Address = Some(
              new ConsignmentConsignorAddress(
                streetAndNumber = "san",
                postcode = Some("pc"),
                city = "city",
                country = "country"
              )
            ),
            ContactPerson = Some(
              new ConsignmentConsignorContactPerson(
                name = "ccp",
                phoneNumber = "ccptel",
                eMailAddress = Some("ccpemail")
              )
            )
          )
        ),
        Consignee = Some(
          new Consignee(
            identificationNumber = Some("in"),
            name = Some("name"),
            Address = Some(
              new ConsigneeAddress(
                streetAndNumber = "san",
                postcode = Some("pc"),
                city = "city",
                country = "country"
              )
            )
          )
        ),
        AdditionalSupplyChainActor = Seq(
          new AdditionalSupplyChainActor(
            sequenceNumber = 1,
            role = "role1",
            identificationNumber = "id1"
          ),
          new AdditionalSupplyChainActor(
            sequenceNumber = 2,
            role = "role2",
            identificationNumber = "id2"
          )
        ),
        TransportEquipment = Seq(
          new TransportEquipment(
            sequenceNumber = 1,
            containerIdentificationNumber = Some("cin1"),
            numberOfSeals = BigInt(2),
            Seal = Seq(
              new Seal(
                sequenceNumber = 1,
                identifier = "sid1"
              ),
              new Seal(
                sequenceNumber = 2,
                identifier = "sid2"
              )
            ),
            GoodsReference = Seq(
              new GoodsReference(
                sequenceNumber = 1,
                declarationGoodsItemNumber = BigInt(1)
              ),
              new GoodsReference(
                sequenceNumber = 2,
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          )
        ),
        LocationOfGoods = Some(
          new LocationOfGoods(
            typeOfLocation = "tol",
            qualifierOfIdentification = "qoi",
            authorisationNumber = Some("an"),
            additionalIdentifier = Some("ai"),
            UNLocode = Some("unl"),
            CustomsOffice = Some(
              new LocationOfGoodsCustomsOffice(
                referenceNumber = "corn"
              )
            ),
            GNSS = Some(
              new GNSS(
                latitude = "lat",
                longitude = "lon"
              )
            ),
            EconomicOperator = Some(
              new EconomicOperator(
                identificationNumber = "eoin"
              )
            ),
            Address = Some(
              new LocationOfGoodsAddress(
                streetAndNumber = "san",
                postcode = Some("pc"),
                city = "city",
                country = "country"
              )
            ),
            PostcodeAddress = Some(
              new LocationOfGoodsPostcodeAddress(
                houseNumber = Some("hn"),
                postcode = "pc",
                country = "country"
              )
            ),
            ContactPerson = Some(
              new LocationOfGoodsContactPerson(
                name = "logcp",
                phoneNumber = "logcptel",
                eMailAddress = Some("logcpemail")
              )
            )
          )
        ),
        DepartureTransportMeans = Seq(
          new ConsignmentDepartureTransportMeans(
            sequenceNumber = 1,
            typeOfIdentification = Some("toi1"),
            identificationNumber = Some("in1"),
            nationality = Some("nat1")
          ),
          new ConsignmentDepartureTransportMeans(
            sequenceNumber = 2,
            typeOfIdentification = Some("toi2"),
            identificationNumber = Some("in2"),
            nationality = Some("nat2")
          ),
          new ConsignmentDepartureTransportMeans(
            sequenceNumber = 3,
            typeOfIdentification = Some("toi3"),
            identificationNumber = Some("in3"),
            nationality = Some("nat3")
          ),
          new ConsignmentDepartureTransportMeans(
            sequenceNumber = 4,
            typeOfIdentification = Some("toi4"),
            identificationNumber = Some("in4"),
            nationality = Some("nat4")
          )
        ),
        CountryOfRoutingOfConsignment = Seq(
          new CountryOfRoutingOfConsignment(
            sequenceNumber = 1,
            country = "corocc1"
          ),
          new CountryOfRoutingOfConsignment(
            sequenceNumber = 2,
            country = "corocc2"
          ),
          new CountryOfRoutingOfConsignment(
            sequenceNumber = 3,
            country = "corocc3"
          ),
          new CountryOfRoutingOfConsignment(
            sequenceNumber = 4,
            country = "corocc4"
          )
        ),
        ActiveBorderTransportMeans = Seq(
          new ConsignmentActiveBorderTransportMeans(
            sequenceNumber = 1,
            customsOfficeAtBorderReferenceNumber = Some("coabrn1"),
            typeOfIdentification = Some("toi1"),
            identificationNumber = Some("in1"),
            nationality = Some("nat1"),
            conveyanceReferenceNumber = Some("crn1")
          ),
          new ConsignmentActiveBorderTransportMeans(
            sequenceNumber = 2,
            customsOfficeAtBorderReferenceNumber = Some("coabrn2"),
            typeOfIdentification = Some("toi2"),
            identificationNumber = Some("in2"),
            nationality = Some("nat2"),
            conveyanceReferenceNumber = Some("crn2")
          ),
          new ConsignmentActiveBorderTransportMeans(
            sequenceNumber = 3,
            customsOfficeAtBorderReferenceNumber = Some("coabrn3"),
            typeOfIdentification = Some("toi3"),
            identificationNumber = Some("in3"),
            nationality = Some("nat3"),
            conveyanceReferenceNumber = Some("crn3")
          ),
          new ConsignmentActiveBorderTransportMeans(
            sequenceNumber = 4,
            customsOfficeAtBorderReferenceNumber = Some("coabrn4"),
            typeOfIdentification = Some("toi4"),
            identificationNumber = Some("in4"),
            nationality = Some("nat4"),
            conveyanceReferenceNumber = Some("crn4")
          )
        ),
        PlaceOfLoading = Some(
          new PlaceOfLoading(
            UNLocode = Some("polunl"),
            country = Some("polc"),
            location = Some("poll")
          )
        ),
        PlaceOfUnloading = Some(
          new PlaceOfUnloading(
            UNLocode = Some("pouunl"),
            country = Some("pouc"),
            location = Some("poul")
          )
        ),
        PreviousDocument = Seq(
          new ConsignmentPreviousDocument(
            sequenceNumber = 1,
            typeValue = "ptv1",
            referenceNumber = "prn1",
            complementOfInformation = Some("pcoi1")
          ),
          new ConsignmentPreviousDocument(
            sequenceNumber = 2,
            typeValue = "ptv2",
            referenceNumber = "prn2",
            complementOfInformation = Some("pcoi1")
          ),
          new ConsignmentPreviousDocument(
            sequenceNumber = 3,
            typeValue = "ptv3",
            referenceNumber = "prn3",
            complementOfInformation = Some("pcoi1")
          ),
          new ConsignmentPreviousDocument(
            sequenceNumber = 4,
            typeValue = "ptv4",
            referenceNumber = "prn4",
            complementOfInformation = Some("pcoi1")
          )
        ),
        SupportingDocument = Seq(
          new SupportingDocument(
            sequenceNumber = 1,
            typeValue = "stv1",
            referenceNumber = "srn1",
            documentLineItemNumber = Some(BigInt(1)),
            complementOfInformation = Some("scoi1")
          ),
          new SupportingDocument(
            sequenceNumber = 2,
            typeValue = "stv2",
            referenceNumber = "srn2",
            documentLineItemNumber = Some(BigInt(1)),
            complementOfInformation = Some("scoi1")
          ),
          new SupportingDocument(
            sequenceNumber = 3,
            typeValue = "stv3",
            referenceNumber = "srn3",
            documentLineItemNumber = Some(BigInt(1)),
            complementOfInformation = Some("scoi3")
          ),
          new SupportingDocument(
            sequenceNumber = 4,
            typeValue = "stv4",
            referenceNumber = "srn4",
            documentLineItemNumber = Some(BigInt(1)),
            complementOfInformation = Some("scoi4")
          )
        ),
        TransportDocument = Seq(
          new TransportDocument(
            sequenceNumber = 1,
            typeValue = "ttv1",
            referenceNumber = "trn1"
          ),
          new TransportDocument(
            sequenceNumber = 2,
            typeValue = "ttv2",
            referenceNumber = "trn2"
          ),
          new TransportDocument(
            sequenceNumber = 3,
            typeValue = "ttv3",
            referenceNumber = "trn3"
          ),
          new TransportDocument(
            sequenceNumber = 4,
            typeValue = "ttv4",
            referenceNumber = "trn4"
          )
        ),
        AdditionalReference = Seq(
          new ConsignmentAdditionalReference(
            sequenceNumber = 1,
            typeValue = "artv1",
            referenceNumber = Some("arrn1")
          ),
          new ConsignmentAdditionalReference(
            sequenceNumber = 2,
            typeValue = "artv2",
            referenceNumber = Some("arrn2")
          ),
          new ConsignmentAdditionalReference(
            sequenceNumber = 3,
            typeValue = "artv3",
            referenceNumber = Some("arrn3")
          ),
          new ConsignmentAdditionalReference(
            sequenceNumber = 4,
            typeValue = "artv4",
            referenceNumber = Some("arrn4")
          )
        ),
        AdditionalInformation = Seq(
          new AdditionalInformation(
            sequenceNumber = 1,
            code = "aic1",
            text = Some("ait1")
          ),
          new AdditionalInformation(
            sequenceNumber = 2,
            code = "aic2",
            text = Some("ait2")
          ),
          new AdditionalInformation(
            sequenceNumber = 3,
            code = "aic3",
            text = Some("ait3")
          ),
          new AdditionalInformation(
            sequenceNumber = 4,
            code = "aic4",
            text = Some("ait4")
          )
        ),
        TransportCharges = Some(
          new TransportCharges(
            methodOfPayment = "mop"
          )
        ),
        HouseConsignment = Seq(
          new HouseConsignment(
            sequenceNumber = 1,
            grossMass = BigDecimal(100),
            ConsignmentItem = Seq(
              new ConsignmentItem(
                goodsItemNumber = 1,
                declarationGoodsItemNumber = BigInt(1),
                countryOfDispatch = Some("c of dispatch"),
                countryOfDestination = Some("c of destination"),
                referenceNumberUCR = Some("ucr"),
                Packaging = Seq(
                  new Packaging(
                    sequenceNumber = 1,
                    typeOfPackages = "1top1",
                    numberOfPackages = Some(BigInt(11)),
                    shippingMarks = Some("1sm1")
                  ),
                  new Packaging(
                    sequenceNumber = 2,
                    typeOfPackages = "1top2",
                    numberOfPackages = Some(BigInt(12)),
                    shippingMarks = Some("1sm2")
                  )
                ),
                Commodity = new Commodity(
                  descriptionOfGoods = "dog",
                  cusCode = Some("cus"),
                  CommodityCode = Some(
                    new CommodityCode(
                      harmonizedSystemSubHeadingCode = "hsshc",
                      combinedNomenclatureCode = Some("cnc")
                    )
                  ),
                  DangerousGoods = Seq(
                    new DangerousGoods(
                      sequenceNumber = 1,
                      UNNumber = "unn1"
                    ),
                    new DangerousGoods(
                      sequenceNumber = 2,
                      UNNumber = "unn2"
                    )
                  ),
                  GoodsMeasure = new GoodsMeasure(
                    grossMass = Some(BigDecimal(200)),
                    netMass = Some(BigDecimal(100))
                  )
                )
              ),
              new ConsignmentItem(
                goodsItemNumber = 2,
                declarationGoodsItemNumber = BigInt(2),
                countryOfDispatch = Some("c of dispatch"),
                countryOfDestination = Some("c of destination"),
                referenceNumberUCR = Some("ucr"),
                Packaging = Seq(
                  new Packaging(
                    sequenceNumber = 1,
                    typeOfPackages = "2top1",
                    numberOfPackages = Some(BigInt(21)),
                    shippingMarks = Some("2sm1")
                  ),
                  new Packaging(
                    sequenceNumber = 2,
                    typeOfPackages = "2top2",
                    numberOfPackages = Some(BigInt(22)),
                    shippingMarks = Some("2sm2")
                  )
                ),
                Commodity = new Commodity(
                  descriptionOfGoods = "dog",
                  cusCode = Some("cus"),
                  CommodityCode = Some(
                    new CommodityCode(
                      harmonizedSystemSubHeadingCode = "hsshc",
                      combinedNomenclatureCode = Some("cnc")
                    )
                  ),
                  DangerousGoods = Seq(
                    new DangerousGoods(
                      sequenceNumber = 1,
                      UNNumber = "unn1"
                    ),
                    new DangerousGoods(
                      sequenceNumber = 2,
                      UNNumber = "unn2"
                    )
                  ),
                  GoodsMeasure = new GoodsMeasure(
                    grossMass = Some(BigDecimal(200)),
                    netMass = Some(BigDecimal(100))
                  )
                )
              )
            )
          )
        )
      )
    )
  }

  lazy val cc043c: CC043CType = {
    import viewmodels.unloadingpermission.*
    CC043CType(
      messageSequence1 = arbitrary[MESSAGESequence].sample.value,
      TransitOperation = new TransitOperation(
        MRN = "mrn",
        declarationType = Some("T"),
        declarationAcceptanceDate = None,
        security = "0",
        reducedDatasetIndicator = Number1
      ),
      CustomsOfficeOfDestinationActual = new CustomsOfficeOfDestinationActual(
        referenceNumber = "cooda"
      ),
      HolderOfTheTransitProcedure = Some(
        new HolderOfTheTransitProcedure(
          identificationNumber = Some("hin"),
          TIRHolderIdentificationNumber = Some("thin"),
          name = "hn",
          Address = new HolderOfTheTransitProcedureAddress(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        )
      ),
      TraderAtDestination = new TraderAtDestination(
        identificationNumber = "tad"
      ),
      CTLControl = Some(
        new CTLControl(
          continueUnloading = BigInt(5)
        )
      ),
      Consignment = Some(
        new Consignment(
          countryOfDestination = Some("cod"),
          containerIndicator = Number1,
          inlandModeOfTransport = Some("imot"),
          grossMass = Some(BigDecimal(200)),
          Consignor = Some(
            new ConsignmentConsignor(
              identificationNumber = Some("in"),
              name = Some("name"),
              Address = Some(
                new ConsignmentConsignorAddress(
                  streetAndNumber = "san",
                  postcode = Some("pc"),
                  city = "city",
                  country = "country"
                )
              )
            )
          ),
          Consignee = Some(
            new Consignee(
              identificationNumber = Some("in"),
              name = Some("name"),
              Address = Some(
                new ConsigneeAddress(
                  streetAndNumber = "san",
                  postcode = Some("pc"),
                  city = "city",
                  country = "country"
                )
              )
            )
          ),
          TransportEquipment = Seq(
            new TransportEquipment(
              sequenceNumber = 1,
              containerIdentificationNumber = Some("cin1"),
              numberOfSeals = BigInt(2),
              Seal = Seq(
                new Seal(
                  sequenceNumber = 1,
                  identifier = "sid1"
                ),
                new Seal(
                  sequenceNumber = 2,
                  identifier = "sid2"
                )
              ),
              GoodsReference = Seq(
                new GoodsReference(
                  sequenceNumber = 1,
                  declarationGoodsItemNumber = BigInt(1)
                ),
                new GoodsReference(
                  sequenceNumber = 2,
                  declarationGoodsItemNumber = BigInt(3)
                )
              )
            )
          ),
          DepartureTransportMeans = Seq(
            new DepartureTransportMeans(
              sequenceNumber = 1,
              typeOfIdentification = Some("toi1"),
              identificationNumber = Some("in1"),
              nationality = Some("nat1")
            ),
            new DepartureTransportMeans(
              sequenceNumber = 2,
              typeOfIdentification = Some("toi2"),
              identificationNumber = Some("in2"),
              nationality = Some("nat2")
            )
          ),
          PreviousDocument = Seq(
            new ConsignmentPreviousDocument(
              sequenceNumber = 1,
              typeValue = "ptv1",
              referenceNumber = "prn1",
              complementOfInformation = Some("pcoi1")
            ),
            new ConsignmentPreviousDocument(
              sequenceNumber = 2,
              typeValue = "ptv2",
              referenceNumber = "prn2",
              complementOfInformation = Some("pcoi1")
            )
          ),
          SupportingDocument = Seq(
            new SupportingDocument(
              sequenceNumber = 1,
              typeValue = "stv1",
              referenceNumber = "srn1",
              complementOfInformation = Some("scoi1")
            ),
            new SupportingDocument(
              sequenceNumber = 2,
              typeValue = "stv2",
              referenceNumber = "srn2",
              complementOfInformation = Some("scoi1")
            )
          ),
          TransportDocument = Seq(
            new TransportDocument(
              sequenceNumber = 1,
              typeValue = "ttv1",
              referenceNumber = "trn1"
            ),
            new TransportDocument(
              sequenceNumber = 2,
              typeValue = "ttv2",
              referenceNumber = "trn2"
            )
          ),
          AdditionalReference = Seq(
            new AdditionalReference(
              sequenceNumber = 1,
              typeValue = "artv1",
              referenceNumber = Some("arrn1")
            ),
            new AdditionalReference(
              sequenceNumber = 2,
              typeValue = "artv2",
              referenceNumber = Some("arrn2")
            )
          ),
          AdditionalInformation = Seq(
            new AdditionalInformation(
              sequenceNumber = 1,
              code = "aic1",
              text = Some("ait1")
            ),
            new AdditionalInformation(
              sequenceNumber = 2,
              code = "aic2",
              text = Some("ait2")
            )
          ),
          Incident = Nil,
          HouseConsignment = Seq(
            new HouseConsignment(
              sequenceNumber = 1,
              grossMass = BigDecimal(100),
              ConsignmentItem = Seq(
                new ConsignmentItem(
                  goodsItemNumber = 1,
                  declarationGoodsItemNumber = 1,
                  Commodity = new Commodity(
                    descriptionOfGoods = "dog1",
                    GoodsMeasure = new GoodsMeasure(
                      grossMass = None,
                      netMass = None
                    )
                  )
                ),
                ConsignmentItemType04(
                  goodsItemNumber = 2,
                  declarationGoodsItemNumber = 2,
                  Commodity = new Commodity(
                    descriptionOfGoods = "dog2",
                    GoodsMeasure = new GoodsMeasure(
                      grossMass = None,
                      netMass = None
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
  }

  lazy val houseConsignmentType03 = {
    import viewmodels.tad.*
    new HouseConsignment(
      sequenceNumber = 1,
      countryOfDispatch = None,
      grossMass = BigDecimal(1),
      referenceNumberUCR = None,
      securityIndicatorFromExportDeclaration = None,
      Consignor = Some(
        new HouseConsignmentConsignor(
          identificationNumber = Some("hc consignor in"),
          name = Some("hc consignor name"),
          Address = Some(
            new HouseConsignmentConsignorAddress(
              streetAndNumber = "san",
              postcode = Some("pc"),
              city = "city",
              country = "country"
            )
          ),
          ContactPerson = Some(
            new HouseConsignmentConsignorContactPerson(
              name = "cp",
              phoneNumber = "cptel",
              eMailAddress = Some("cpemail")
            )
          )
        )
      ),
      Consignee = Some(
        new Consignee(
          identificationNumber = Some("hc consignee in"),
          name = Some("hc consignee name"),
          Address = Some(
            new ConsigneeAddress(
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
        new HouseConsignmentDepartureTransportMeans(
          sequenceNumber = 1,
          typeOfIdentification = "toi1",
          identificationNumber = "in1",
          nationality = "nat1"
        ),
        new HouseConsignmentDepartureTransportMeans(
          sequenceNumber = 2,
          typeOfIdentification = "toi2",
          identificationNumber = "in2",
          nationality = "nat2"
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
  }

  lazy val consignmentItemType03 = {
    import viewmodels.tad.*
    new ConsignmentItem(
      goodsItemNumber = 1,
      declarationGoodsItemNumber = BigInt(1),
      declarationType = Some("T"),
      countryOfDispatch = Some("c of dispatch"),
      countryOfDestination = Some("c of destination"),
      referenceNumberUCR = Some("ucr"),
      AdditionalSupplyChainActor = Seq(
        new AdditionalSupplyChainActor(
          sequenceNumber = 1,
          role = "role1",
          identificationNumber = "id1"
        ),
        new AdditionalSupplyChainActor(
          sequenceNumber = 2,
          role = "role2",
          identificationNumber = "id2"
        ),
        new AdditionalSupplyChainActor(
          sequenceNumber = 3,
          role = "role3",
          identificationNumber = "id3"
        ),
        new AdditionalSupplyChainActor(
          sequenceNumber = 4,
          role = "role4",
          identificationNumber = "id4"
        )
      ),
      Commodity = new Commodity(
        descriptionOfGoods = "dog",
        cusCode = Some("cus"),
        CommodityCode = Some(
          new CommodityCode(
            harmonizedSystemSubHeadingCode = "hsshc",
            combinedNomenclatureCode = Some("cnc")
          )
        ),
        DangerousGoods = Seq(
          new DangerousGoods(
            sequenceNumber = 1,
            UNNumber = "unn1"
          ),
          new DangerousGoods(
            sequenceNumber = 2,
            UNNumber = "unn2"
          )
        ),
        GoodsMeasure = new GoodsMeasure(
          grossMass = Some(BigDecimal(200)),
          netMass = Some(BigDecimal(100))
        )
      ),
      Packaging = Seq(
        new Packaging(
          sequenceNumber = 1,
          typeOfPackages = "top1",
          numberOfPackages = Some(BigInt(100)),
          shippingMarks = Some("sm1")
        ),
        new Packaging(
          sequenceNumber = 2,
          typeOfPackages = "top2",
          numberOfPackages = Some(BigInt(200)),
          shippingMarks = Some("sm2")
        )
      ),
      PreviousDocument = Seq(
        new ConsignmentItemPreviousDocument(
          sequenceNumber = 1,
          typeValue = "tv1",
          referenceNumber = "rn1",
          goodsItemNumber = Some(BigInt(1)),
          typeOfPackages = Some("top1"),
          numberOfPackages = Some(BigInt(11)),
          measurementUnitAndQualifier = Some("muaq1"),
          quantity = Some(BigDecimal(10)),
          complementOfInformation = Some("pcoi1")
        ),
        new ConsignmentItemPreviousDocument(
          sequenceNumber = 2,
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
        new SupportingDocument(
          sequenceNumber = 1,
          typeValue = "stv1",
          referenceNumber = "srn1",
          documentLineItemNumber = Some(BigInt(11)),
          complementOfInformation = Some("scoi1")
        ),
        new SupportingDocument(
          sequenceNumber = 2,
          typeValue = "stv2",
          referenceNumber = "srn2",
          documentLineItemNumber = Some(BigInt(22)),
          complementOfInformation = Some("scoi2")
        )
      ),
      AdditionalReference = Seq(
        new ConsignmentItemAdditionalReference(
          sequenceNumber = 1,
          typeValue = "artv1",
          referenceNumber = Some("arrn1")
        ),
        new ConsignmentItemAdditionalReference(
          sequenceNumber = 2,
          typeValue = "artv2",
          referenceNumber = Some("arrn2")
        )
      ),
      AdditionalInformation = Seq(
        new AdditionalInformation(
          sequenceNumber = 1,
          code = "aic1",
          text = Some("ait1")
        ),
        new AdditionalInformation(
          sequenceNumber = 2,
          code = "aic2",
          text = Some("ait2")
        )
      )
    )
  }
}
