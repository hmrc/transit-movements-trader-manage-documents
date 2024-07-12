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

package refactor.viewmodels.p4.tad

import base.SpecBase
import refactor.viewmodels.DummyData
import refactor.viewmodels.p4.tad.ConsignmentItemViewModel.PackageViewModel
import refactor.viewmodels.p4.tad.ConsignmentItemViewModel.SensitiveGoodsInformationViewModel

class ConsignmentItemViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(cc029c, consignmentItemType03)

    "itemNumber" in {
      result.itemNumber mustBe "gin/1"
    }

    "shippingMarks" in {
      result.shippingMarks mustBe Seq("sm1", "sm2")
    }

    "packages" in {
      result.packages mustBe Seq(
        PackageViewModel(
          sequenceNumber = "1",
          typeOfPackages = "top1",
          numberOfPackages = BigInt(100),
          shippingMarks = Some("sm1")
        ),
        PackageViewModel(
          sequenceNumber = "2",
          typeOfPackages = "top2",
          numberOfPackages = BigInt(200),
          shippingMarks = Some("sm2")
        )
      )
    }

    "containers" in {
      result.containers mustBe Seq("1, cin1")
    }

    "description" in {
      result.description mustBe "dog"
    }

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "commodityCode" in {
      result.commodityCode mustBe "hsshc, cnc"
    }

    "sensitiveGoodsInformation" in {
      result.sensitiveGoodsInformation mustBe Seq(SensitiveGoodsInformationViewModel("--", "--"))
    }

    "previousDocuments" in {
      result.previousDocuments mustBe Seq("1, tv1, rn1, 1, top1, 11, muaq1, 10, pcoi1", "2, tv2, rn2, 2, top2, 22, muaq2, 20, pcoi2")
    }

    "countryOfDispatch" in {
      result.countryOfDispatch mustBe "c of dispatch"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustBe "c of destination"
    }

    "grossMass" in {
      result.grossMass mustBe "200.0"
    }

    "netMass" in {
      result.netMass mustBe "100.0"
    }

    "specialMentions" in {
      result.specialMentions mustBe None
    }

    "producedDocuments" in {
      result.producedDocuments mustBe None
    }

    "consigneeViewModel" in {
      result.commodityCode mustBe "hsshc, cnc"
    }

    "consignorViewModel" in {
      result.consignorViewModel mustBe None
    }
  }
}
