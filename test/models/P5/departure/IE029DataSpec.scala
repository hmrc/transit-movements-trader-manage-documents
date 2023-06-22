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

import base.SpecBase
import generators.ModelGenerators
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class IE029DataSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with SpecBase {

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

      IE029Data(departureMessageData).customsOfficeOfTransitDeclared mustBe "seq001, ref001, -999999999-01-01T00:00"
      IE029Data(departureMessageData).customsOfficeOfExitForTransitDeclared mustBe "seq001, ref001"
      IE029Data(departureMessageData).countryOfDestination mustBe "GB"
      IE029Data(departureMessageData).countryOfDispatch mustBe "GER"
      IE029Data(departureMessageData).customsOfficeOfDeparture mustBe "Ref001"
      IE029Data(departureMessageData).customsOfficeOfDestinationDeclared mustBe "Ref001"
      IE029Data(departureMessageData).countryOfRoutingOfConsignment mustBe "Seqnum12243, GB"
    }
  }

}
