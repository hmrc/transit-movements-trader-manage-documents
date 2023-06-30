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

import viewmodels.P5._
import base.SpecBase
import models.P5.departure.IE029Data
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class Table1ViewModelSpec extends AnyFreeSpec with Matchers with SpecBase {

  val ie029Data = IE029Data(departureMessageData)
  val viewModel = TableViewModel()(ie029Data)

  "Table1ViewModelSpec" - {

    "return comma seperated consignor information" in {
      viewModel.table1ViewModel.consignor mustBe "Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
    }

    "return comma seperated consignor contact person information" in {
      viewModel.table1ViewModel.consignorContactPerson mustBe "Contact Person Name, 123456, a@a.com"
    }

    "return comma seperated consignee information" in {
      viewModel.table1ViewModel.consignee mustBe "Consignee Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4, Contact Person Na..."
    }

    "return declarationType information" in {
      viewModel.table1ViewModel.declarationType mustBe "T1"
    }

    "return additionalDeclarationType information" in {
      viewModel.table1ViewModel.additionalDeclarationType mustBe "T2F"
    }

    "return sci information" in {
      viewModel.table1ViewModel.sci mustBe "SCI"
    }

    "return mrn information" in {
      viewModel.table1ViewModel.mrn mustBe "MRN"
    }

    "return consigneeIdentificationNumber information" in {
      viewModel.table1ViewModel.consigneeIdentificationNumber mustBe "idnum1"
    }

    "return consignorIdentificationNumber information" in {
      viewModel.table1ViewModel.consignorIdentificationNumber mustBe "idnum1"
    }

    "return totalItems information" in {
      viewModel.table1ViewModel.totalItems mustBe 2
    }

    "return totalPackages information" in {
      viewModel.table1ViewModel.totalPackages mustBe 10
    }

    "return totalGrossMass information" in {
      viewModel.table1ViewModel.totalGrossMass mustBe 1.0
    }

    "return security information" in {
      viewModel.table1ViewModel.security mustBe "sec"
    }

    "return  comma seperated holderOfTransitProcedure information" in {
      viewModel.table1ViewModel.holderOfTransitProcedure mustBe "TIRID1, Bob, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
    }

    "return holderOfTransitProcedureIdentificationNumber information" in {
      viewModel.table1ViewModel.holderOfTransitProcedureIdentificationNumber mustBe "id1"
    }

    "return representative information" in {
      viewModel.table1ViewModel.representative mustBe "Status-1"
    }

    "return representativeIdentificationNumber information" in {
      viewModel.table1ViewModel.representativeIdentificationNumber mustBe "ID1"
    }

    "return lrn information" in {
      viewModel.table1ViewModel.lrn mustBe "LRN"
    }

    "return tir information" in {
      viewModel.table1ViewModel.tir mustBe "TIR"
    }

    "return  carrierIdentificationNumber information" in {
      viewModel.table1ViewModel.carrierIdentificationNumber mustBe "idnum1"
    }

    "return additionalSupplyChainActorRoles information" in {
      viewModel.table1ViewModel.additionalSupplyChainActorRoles mustBe "Actor-Role"
    }

    "return additionalSupplyChainActorIdentificationNumbers information" in {
      viewModel.table1ViewModel.additionalSupplyChainActorIdentificationNumbers mustBe "ID001"
    }

    "return comma seperated departureTransportMeans information" in {
      viewModel.table1ViewModel.departureTransportMeans mustBe "Actor-Role, ID001, Nationality"
    }

    "return ucr information" in {
      viewModel.table1ViewModel.ucr mustBe "UCR001"
    }

    "return activeBorderTransportMeans information" in {
      viewModel.table1ViewModel.activeBorderTransportMeans mustBe "GB0001, T1, ID001, GB"
    }

    "return  activeBorderTransportMeansConveyanceNumbers information" in {
      viewModel.table1ViewModel.activeBorderTransportMeansConveyanceNumbers mustBe "conveyReferenceNumber-1"
    }

    "return comma seperated placeOfLoading information" in {
      viewModel.table1ViewModel.placeOfLoading mustBe "LoCoCode-1, GB, L1"
    }

    "return placeOfUnloading information" in {
      viewModel.table1ViewModel.placeOfUnloading mustBe "UnLoCoCode-1, GB, L1"
    }

    "return inlandModeOfTransport information" in {
      viewModel.table1ViewModel.inlandModeOfTransport mustBe "T1"
    }

    "return modeOfTransportAtBorder information" in {
      viewModel.table1ViewModel.modeOfTransportAtBorder mustBe "Road"
    }

    "return locationOfGoods information" in {
      viewModel.table1ViewModel.locationOfGoods mustBe "Warehouse, qualifierIdentifier-num-1, 1212, LNCODE1, Reference1, 1232, 1234, EconomicOperator-1, ..."
    }

    "return  comma seperated locationOfGoodsContactPerson information" in {
      viewModel.table1ViewModel.locationOfGoodsContactPerson mustBe "Contact Person Name, 123456, a@a.com"
    }
  }
}
