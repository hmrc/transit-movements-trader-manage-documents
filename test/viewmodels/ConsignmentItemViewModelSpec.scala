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
import models.P5.departure.IE029Data
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import viewmodels.P5._

class ConsignmentItemViewModelSpec extends AnyFreeSpec with Matchers with SpecBase {

  val ie029Data = IE029Data(departureMessageData)
  val viewModel = ConsignmentItemViewModel()(ie029Data)

  "ConsignmentItemViewModelSpec" - {

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("T1")
    }

    "should return countryOfDispatchString information" in {
      viewModel.consignmentItem.map(_.countryOfDispatchString) mustBe List("GER")
    }

    "should return countryOfDestinationString information" in {
      viewModel.consignmentItem.map(_.countryOfDestinationString) mustBe List("GB")
    }

    "should return consignor information" in {
      viewModel.consignmentItem.map(_.consignor) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return consignee information" in {
      viewModel.consignmentItem.map(_.consignee) mustBe List(
        "Consignee Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4, Contact Person Name, 123456, a@a.com"
      )
    }

    "should return goodsItemNumberString information" in {
      viewModel.consignmentItem.map(_.goodsItemNumberString) mustBe List("123545")
    }

    "should return declarationGoodsItemNumberString information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("T1")
    }

    "should return packaging information" in {
      viewModel.consignmentItem.map(_.packaging) mustBe List("5, Plastic, rubberStamp")
    }

    "should return referenceNumberUCRString information" in {
      viewModel.consignmentItem.map(_.referenceNumberUCRString) mustBe List("ref1")
    }

    "should return transportCharges information" in {
      viewModel.consignmentItem.map(_.transportCharges) mustBe List("payPal")
    }

    "should return additionalSupplyChainActor information" in {
      viewModel.consignmentItem.map(_.additionalSupplyChainActor) mustBe List("Actor-Role")
    }

    "should return commodityCode information" in {
      viewModel.consignmentItem.map(_.commodityCode) mustBe List("SHC1, NOMC1")
    }

    "should return departureTransportMeans information" in {
      viewModel.consignmentItem.map(_.departureTransportMeans) mustBe List("Actor-Role, ID001, Nationality")
    }

    "should return dangerousGoods information" in {
      viewModel.consignmentItem.map(_.dangerousGoods) mustBe List("seq1, UNNumber1")
    }

    "should return cusCode information" in {
      viewModel.consignmentItem.map(_.cusCode) mustBe List("CUSTCODE1")
    }

    "should return descriptionOfGoods information" in {
      viewModel.consignmentItem.map(_.descriptionOfGoods) mustBe List("Tiles")
    }

    "should return previousDocumentString information" in {
      viewModel.consignmentItem.map(_.previousDocumentString) mustBe List("Document-1, Type-1, Reference-1, C1")
    }

    "should return supportingDocumentString information" in {
      viewModel.consignmentItem.map(_.supportingDocumentString) mustBe List("Document-1, Type-1, Reference-1, 5, C1")
    }

    "should return transportDocumentString information" in {
      viewModel.consignmentItem.map(_.transportDocumentString) mustBe List("Document-1, Type-1, Reference-1")
    }

    "should return additionalReferenceString information" in {
      viewModel.consignmentItem.map(_.additionalReferenceString) mustBe List("Document-1, Type-1, Reference-1")
    }

    "should return grossMass information" in {
      viewModel.consignmentItem.map(_.grossMass) mustBe List("1.2")
    }

    "should return netMass information" in {
      viewModel.consignmentItem.map(_.netMass) mustBe List("1.4")
    }
    "should return consignorId information" in {
      viewModel.consignmentItem.map(_.consignorId) mustBe List("idnum1")
    }
    "should return consigneeId information" in {
      viewModel.consignmentItem.map(_.consigneeId) mustBe List("idnum1")
    }
    "should return supplyChainActorId information" in {
      viewModel.consignmentItem.map(_.supplyChainActorId) mustBe List("ID001")
    }
    "should return totalPackages information" in {
      viewModel.consignmentItem.map(_.totalPackages) mustBe List(5)
    }
    "should return packagesType information" in {
      viewModel.consignmentItem.map(_.packagesType) mustBe List("5, Plastic, rubberStamp")
    }

  }
}
