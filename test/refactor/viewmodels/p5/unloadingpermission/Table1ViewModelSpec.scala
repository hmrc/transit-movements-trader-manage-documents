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

import base.SpecBase
import generated.p5._
import generators.ScalaxbModelGenerators
import org.scalacheck.Arbitrary.arbitrary

class Table1ViewModelSpec extends SpecBase with ScalaxbModelGenerators {

  private val data: CC043CType = CC043CType(
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
                declarationGoodsItemNumber = BigInt(10)
              ),
              GoodsReferenceType02(
                sequenceNumber = "2",
                declarationGoodsItemNumber = BigInt(20)
              )
            )
          )
        ),
        DepartureTransportMeans = Seq(
          DepartureTransportMeansType02(
            sequenceNumber = "1",
            typeOfIdentification = "toi1",
            identificationNumber = "in1",
            nationality = "nat1"
          ),
          DepartureTransportMeansType02(
            sequenceNumber = "2",
            typeOfIdentification = "toi2",
            identificationNumber = "in2",
            nationality = "nat2"
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

  "must map data to view model" - {

    val result = Table1ViewModel(data)

    "consignorIdentificationNumber" in {
      result.consignorIdentificationNumber mustBe "in"
    }

    "consignor" in {
      result.consignor mustBe "name, san, pc, city, country"
    }

    "consigneeIdentificationNumber" in {
      result.consigneeIdentificationNumber mustBe "in"
    }

    "consignee" in {
      result.consignee mustBe "name, san, pc, city, country"
    }

    "holderOfTransitID" in {
      result.holderOfTransitID mustBe "hin"
    }

    "holderOfTransit" in {
      result.holderOfTransit mustBe "hn, san, pc, city, country"
    }

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "totalItems" in {
      result.totalItems mustBe "2"
    }

    "totalPackages" in {
      result.totalPackages mustBe "0"
    }

    "totalGrossMass" in {
      result.totalGrossMass mustBe "200"
    }

    "tir" in {
      result.tir mustBe "thin"
    }

    "security" in {
      result.security mustBe "0"
    }

    "inlandModeOfTransport" in {
      result.inlandModeOfTransport mustBe "imot"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustBe "toi1, in1, nat1; toi2, in2, nat2"
    }

    "container" in {
      result.container mustBe "1"
    }

    "transportEquipment" in {
      result.transportEquipment mustBe "1, cin1, 2, 1:10...2:20"
    }

    "seals" in {
      result.seals mustBe "1,[sid1]; 2,[sid2]"
    }

    "previousDocument" in {
      result.previousDocument mustBe "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi1"
    }

    "transportDocument" in {
      result.transportDocument mustBe "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "supportingDocument" in {
      result.supportingDocument mustBe "1, stv1, srn1, scoi1; 2, stv2, srn2, scoi1"
    }

    "additionalReference" in {
      result.additionalReference mustBe "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2"
    }

    "customsOfficeOfDestination" in {
      result.customsOfficeOfDestination mustBe "cooda"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustBe "cod"
    }
  }
}
