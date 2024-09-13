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

package viewmodels.tad.transition

import base.SpecBase
import generated.rfc37.PreviousDocumentType06
import viewmodels.DummyData

class Table2ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val consignmentItemViewModel: Option[ConsignmentItemViewModel] = None
    val result                                                     = Table2ViewModel(cc029c, consignmentItemViewModel)

    "numberOfItems" in {
      result.numberOfItems mustBe 2
    }

    "grossMass" in {
      result.grossMass mustBe "200.0"
    }

    "transportEquipment" in {
      result.transportEquipment mustBe "1, cin1"
    }

    "printBindingItinerary" in {
      result.printBindingItinerary mustBe true
    }

    "consignmentItemViewModel" in {
      result.consignmentItemViewModel mustBe consignmentItemViewModel
    }

    "consignmentPreviousDocuments" in {
      result.consignmentPreviousDocuments mustBe "1, ptv1, prn1, pcoi1..."
    }

    "consignmentPreviousDocuments with long text" in {
      val result = Table2ViewModel(
        cc029c.copy(Consignment =
          cc029c.Consignment.copy(PreviousDocument =
            Seq(
              PreviousDocumentType06(
                sequenceNumber = "1",
                typeValue = "ptv1",
                referenceNumber = "AbcdefghijklmnopqrstuvwxyzAbcdefghijklmnopqrstuvwxyz",
                complementOfInformation = Some("pcoi1")
              )
            )
          )
        ),
        consignmentItemViewModel
      )
      result.consignmentPreviousDocuments mustBe "1, ptv1, AbcdefghijklmnopqrstuvwxyzAbcdefghijkl..."
    }

    "consignmentSupportingDocuments" in {
      result.consignmentSupportingDocuments mustBe "1, stv1, srn1, 1, scoi1..."
    }

    "consignmentTransportDocuments" in {
      result.consignmentTransportDocuments mustBe "1, ttv1, trn1..."
    }

    "consignmentAdditionalInformation" in {
      result.consignmentAdditionalInformation mustBe "1, aic1, ait1..."
    }

    "consignmentAdditionalReference" in {
      result.consignmentAdditionalReference mustBe "1, artv1, arrn1..."
    }

    "authorisation" in {
      result.authorisation mustBe "1, C521, rn1..."
    }
  }
}
