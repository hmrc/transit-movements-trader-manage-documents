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

package refactor.viewmodels.p5.tad

import base.SpecBase
import refactor.viewmodels.DummyData

class Table2ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table2ViewModel(cc029c)

    "transportEquipment" in {
      result.transportEquipment mustBe "1, cin1, 2, 1:10...2:20"
    }

    "seals" in {
      result.seals mustBe "1,[sid1]...2,[sid2]"
    }

    "previousDocuments" in {
      result.previousDocuments mustBe "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi1"
    }

    "transportDocuments" in {
      result.transportDocuments mustBe "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "supportingDocuments" in {
      result.supportingDocuments mustBe "1, stv1, srn1, 1, scoi1; 2, stv2, srn2, 1, scoi1"
    }

    "additionalReferences" in {
      result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "transportCharges" in {
      result.transportCharges mustBe "mop"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2"
    }

    "guarantees" in {
      result.guarantees mustBe "1, g1, ogr1, 1, 1grn1, 1ac1, 11.0, 1c1, 2, 1grn2, 1ac2, 12.0, 1c2; 2, g2, ogr2, 1, 2grn1, 2ac1, 2..."
    }

    "authorisations" in {
      result.authorisations mustBe "1, tv1, rn1..."
    }
  }
}
