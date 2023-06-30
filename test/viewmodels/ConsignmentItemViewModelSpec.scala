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
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return countryOfDispatchString information" in {
      viewModel.consignmentItem.map(_.countryOfDispatchString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.countryOfDestinationString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return consignor information" in {
      viewModel.consignmentItem.map(_.consignor) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }

    "should return declarationType information" in {
      viewModel.consignmentItem.map(_.declarationTypeString) mustBe List("Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4")
    }
  }
}
