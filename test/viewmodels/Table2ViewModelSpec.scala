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

import base.DepartureData
import base.SpecBase
import models.P5.departure.IE029
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import viewmodels.P5._

class Table2ViewModelSpec extends AnyFreeSpec with Matchers with SpecBase with DepartureData {

  val ie029Data: IE029          = IE029(ie029MessageData)
  val viewModel: TableViewModel = TableViewModel()(ie029Data)

  "table2ViewModelSpec" - {

    "return comma seperated transportEquipment information" in {
      viewModel.table2ViewModel.transportEquipment mustBe "12123, Container-ID-1, 8, 1232:3"
    }

    "return comma seperated seals information" in {
      viewModel.table2ViewModel.seals mustBe "1232,[ID10012]"
    }

    "return comma seperated seal information and semi-colon seperated seals" in {
      val multipleSealsDM        = ie029MessageData.copy(Consignment = consignmentWithMultipleSeals)
      val ie029DataMultupleSeals = IE029(multipleSealsDM)
      val viewModelMultiSeals    = TableViewModel()(ie029DataMultupleSeals)
      viewModelMultiSeals.table2ViewModel.seals mustBe "1232,[ID10012]...456,[ID10045]"
    }
    "return comma seperated previousDocument information" in {
      viewModel.table2ViewModel.previousDocument mustBe "Document-1, Type-1, Reference-1, C1; Document-2, Type-2, Reference-2, C1"
    }

    "return comma seperated supportingDocument information" in {
      viewModel.table2ViewModel.supportingDocument mustBe "Document-1,Type-1,Reference-1,5,C1; Document-2,Type-2,Reference-2,5,C1"
    }

    "return transportDocument information" in {
      viewModel.table2ViewModel.transportDocument mustBe "Document-1,Type-1,Reference-1; Document-2,Type-2,Reference-2"
    }

    "return additionalInformation information" in {
      viewModel.table2ViewModel.additionalInformation mustBe "Document-1, Type-1, Reference-1; Document-2, Type-2, Reference-2"
    }

    "return additionalReference information" in {
      viewModel.table2ViewModel.additionalReference mustBe "Document-1,Type-1,Reference-1; Document-2,Type-2,Reference-2"
    }

    "return transportCharges information" in {
      viewModel.table2ViewModel.transportCharges mustBe "payPal; payPal"
    }

    "return comma seperated guarantee information" in {
      viewModel.table2ViewModel.guarantee mustBe "SEQNum-1, G, otherGuaranteeReference, SEQNum-1, GRN-1, Access-code-1, 123456.1212, GBP"
    }

    "return comma seperated authorisation information" in {
      viewModel.table2ViewModel.authorisation mustBe "SEQNum-1, Auth-Type, Reference-Numb-1"
    }
  }
}
