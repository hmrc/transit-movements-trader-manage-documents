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
import generated.p5.PackagingType02
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
          numberOfPackages = BigInt(100),
          description = "top1"
        ),
        PackageViewModel(
          numberOfPackages = BigInt(200),
          description = "top2"
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
      result.previousDocuments mustBe Seq("tv1 - rn1 - pcoi1", "tv2 - rn2 - pcoi2")
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

  "PackageViewModel" - {
    "must format as string" - {
      "when 0 packages" in {
        val pvm = PackageViewModel(
          `package` = PackagingType02(
            sequenceNumber = "1",
            typeOfPackages = "top",
            numberOfPackages = Some(BigInt(0)),
            shippingMarks = Some("sm")
          )
        )

        pvm.toString mustBe "top"
      }

      "when more than 0 packages" in {
        val pvm = PackageViewModel(
          `package` = PackagingType02(
            sequenceNumber = "1",
            typeOfPackages = "top",
            numberOfPackages = Some(BigInt(10)),
            shippingMarks = Some("sm")
          )
        )

        pvm.toString mustBe "10 - top"
      }
    }
  }
}
