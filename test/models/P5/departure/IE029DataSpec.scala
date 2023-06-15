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

package models.P5.departure

import generators.ModelGenerators
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class IE029DataSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators {

  val transitOperation: TransitOperation =
    TransitOperation("MRN", "LRN", "T1", "T2F", "sec", Some("TIR"), Some("SCI"))

  val address: Address = Address("Address Line 1", Some("Address Line 2"), "Address Line 3", "Address Line 4")
  val contactPerson: ContactPerson = ContactPerson("Contact Person Name", "123456", Some("a@a.com"))

  val holderOfTheTransitProcedure: HolderOfTransitProcedure =
    HolderOfTransitProcedure(Some("id1"), Some("TIRID1"), Some("Bob"), Some(address), Some(contactPerson))

  val representative: Representative = Representative(Some("ID1"), Some("Status-1"), Some(contactPerson))

  val consignor = Consignor(Some("idnum1"), Some("Consignor Name"), Some(address), Some(contactPerson))
  val consignee = Consignee(Some("idnum1"), Some("Consignee Name"), Some(address), Some(contactPerson))
  val carrier = Carrier("idnum1", Some(contactPerson))

  val authorisation: Authorisation = Authorisation(Some("SEQNum-1"), Some("Auth-Type"), Some("Reference-Numb-1"))
  val guaranteeReference: GuaranteeReference = GuaranteeReference(Some("SEQNum-1"), Some("GRN-1"), Some("Access-code-1"), Some(123456.1212), Some("GBP"))
  val guarantee: Guarantee = Guarantee(Some("SEQNum-1"), Some("SomeGuaranteeType"), Some("otherGuaranteeReference"), Some(List(guaranteeReference)))
  val postcodeAddress = PostcodeAddress(Some("house1"), "BR", "UK")
  val economicOperator = EconomicOperator("EconomicOperator-1")
  val customsOffice = CustomsOffice("Reference1")
  val gnss = GNSS("1232", "1234")
  val seal = Seal(Some("1232"), Some("ID10012"))
  val goodsReference = GoodsReference(Some("1232"), Some(1234))

  val locationOfGoods = LocationOfGoods(
    "Warehouse",
    "qualifierIdentifier-num-1",
    Some("1212"),
    Some("ID0001"),
    Some("LNCODE1"),
    Some(customsOffice),
    Some(gnss),
    Some(economicOperator),
    Some(address),
    Some(postcodeAddress),
    Some(contactPerson)
  )

  val additionalSupplyChainActor = AdditionalSupplyChainActor("Actor-Role", "ID001")
  val departureTransportMeans = DepartureTransportMeans("Actor-Role", "ID001", Some("Nationality"))
  val transportEquipment = TransportEquipment("12123", Some("Container-ID-1"), 8, Some(List(seal)), Some(List(goodsReference)))
  val activeBorderTransportMeans = ActiveBorderTransportMeans("GB0001", "T1", "ID001", "GB", Some("conveyReferenceNumber-1"))
  val placeOfLoading = PlaceOfLoading(Some("LoCoCode-1"), Some("GB"), Some("L1"))
  val placeOfUnLoading = PlaceOfUnloading(Some("UnLoCoCode-1"), Some("GB"), Some("L1"))
  val houseConsignment = HouseConsignment(Seq(ConsignmentItem(Seq(Packaging(Some(5))))))
  val previousDocument = PreviousDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"), Some("C1"))
  val transportDocument = TransportDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val additionalReference = AdditionalReference(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val additionalInformation = AdditionalInformation(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val supportingDocument = SupportingDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"), Some(5), Some("C1"))

  val consigmment: Consignment = Consignment(
    1.0,
    Some("T1"),
    Some("Road"),
    Some("UCR001"),
    Some(consignor),
    Some(consignee),
    Some(carrier),
    Some(locationOfGoods),
    Some(List(additionalSupplyChainActor)),
    Some(List(departureTransportMeans)),
    Some(List(transportEquipment)),
    Some(List(activeBorderTransportMeans)),
    Some(placeOfLoading),
    Some(placeOfUnLoading),
    Seq(houseConsignment),
    Some(List(previousDocument)),
    Some(List(transportDocument)),
    Some(List(supportingDocument)),
    Some(List(additionalInformation)),
    Some(List(additionalReference)),
    Some(TransportCharges(Some("payPal")))
  )

  val departureMessageData: DepartureMessageData =
    DepartureMessageData(transitOperation, holderOfTheTransitProcedure, representative, consigmment, Some(List(guarantee)), Some(List(authorisation)))

  "IE029Data" - {

    "must be able to return correct TAD Data for each fields" in {

      IE029Data(departureMessageData).lrn mustBe "LRN"
      IE029Data(departureMessageData).mrn mustBe "MRN"
      IE029Data(departureMessageData).tir mustBe "TIR"
      IE029Data(departureMessageData).sci mustBe "SCI"
      IE029Data(departureMessageData).ucr mustBe "UCR001"
      IE029Data(departureMessageData).totalGrossMass mustBe 1.0
      IE029Data(departureMessageData).totalPackages mustBe 5
      IE029Data(departureMessageData).totalItems mustBe 1

      IE029Data(departureMessageData).security mustBe "sec"
      IE029Data(departureMessageData).inlandModeOfTransport mustBe "T1"
      IE029Data(departureMessageData).modeOfTransportAtBorder mustBe "Road"
      IE029Data(departureMessageData).declarationType mustBe "T1"
      IE029Data(departureMessageData).additionalDeclarationType mustBe "T2F"
      IE029Data(
        departureMessageData
      ).consignee mustBe "Consignee Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4, Contact Person Name, 123456, a@a.com"
      IE029Data(departureMessageData).consigneeIdentificationNumber mustBe "idnum1"
      IE029Data(
        departureMessageData
      ).consignor mustBe "Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4, Contact Person Name, 123456, a@a.com"
      IE029Data(departureMessageData).consignorIdentificationNumber mustBe "idnum1"
      IE029Data(departureMessageData).holderOfTransitProcedure mustBe "TIRID1, Bob, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      IE029Data(departureMessageData).holderOfTransitProcedureIdentificationNumber mustBe "id1"
      IE029Data(departureMessageData).representative mustBe "Status-1"
      IE029Data(departureMessageData).carrierIdentificationNumber mustBe "idnum1"
      IE029Data(departureMessageData).additionalSupplyChainActorRoles mustBe "Actor-Role"
      IE029Data(departureMessageData).additionalSupplyChainActorIdentificationNumbers mustBe "ID001"
      IE029Data(departureMessageData).departureTransportMeans mustBe "Actor-Role, ID001, Nationality"
      IE029Data(departureMessageData).transportEquipment mustBe "12123, Container-ID-1, 8, 1232:1234"
      IE029Data(departureMessageData).seals mustBe "1232:ID10012"
      IE029Data(departureMessageData).previousDocument mustBe "Document-1, Type-1, Reference-1, C1"
      IE029Data(departureMessageData).supportingDocument mustBe "Document-1, Type-1, Reference-1, 5, C1"
      IE029Data(departureMessageData).transportDocument mustBe "Document-1, Type-1, Reference-1"
      IE029Data(departureMessageData).additionalInformation mustBe "Document-1, Type-1, Reference-1"
      IE029Data(departureMessageData).additionalReference mustBe "Document-1, Type-1, Reference-1"
      IE029Data(departureMessageData).transportCharges mustBe "payPal"
      IE029Data(departureMessageData).guarantee mustBe "SEQNum-1, SomeGuaranteeType, otherGuaranteeReference, SEQNum-1, GRN-1, Access-code-1, 123456.1212, GBP"
      IE029Data(departureMessageData).authorisation mustBe "SEQNum-1, Auth-Type, Reference-Numb-1"
      IE029Data(departureMessageData).activeBorderTransportMeans mustBe "GB0001, T1, ID001, GB"
      IE029Data(departureMessageData).activeBorderTransportMeansConveyanceNumbers mustBe "conveyReferenceNumber-1"
      IE029Data(departureMessageData).placeOfLoading mustBe "LoCoCode-1, GB, L1"
      IE029Data(departureMessageData).placeOfUnloading mustBe "UnLoCoCode-1, GB, L1"
      IE029Data(
        departureMessageData
      ).locationOfGoods mustBe "Warehouse, qualifierIdentifier-num-1, 1212, LNCODE1, Reference1, 1232, 1234, EconomicOperator-1, Address Line 1, Address Line 2, Address Line 3, Address Line 4, house1, BR, UK"
      IE029Data(departureMessageData).locationOfGoodsContactPerson mustBe "Contact Person Name, 123456, a@a.com"
    }
  }

}
