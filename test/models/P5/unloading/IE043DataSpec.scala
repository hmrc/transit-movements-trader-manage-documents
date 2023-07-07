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

package models.P5.unloading

import base.SpecBase
import base.UnloadingData

class IE043DataSpec extends SpecBase with UnloadingData {

  private val ie043Data = IE043Data(unloadingPermissionMessageData)

  "IE043Data" - {

    "must be able to return correct UPD Data for each fields" in {

      ie043Data.mrn mustBe "38VYQTYFU3T0KUTUM3"
      ie043Data.tir mustBe "tirId"
      ie043Data.customsOfficeOfDestination mustBe "GB000068"
      ie043Data.countryOfDestination mustBe "FR"
      ie043Data.totalGrossMass mustBe 1000.99
      ie043Data.totalPackages mustBe 3
      ie043Data.totalItems mustBe 1
      ie043Data.container mustBe "1"
      ie043Data.security mustBe "4"
      ie043Data.inlandModeOfTransport mustBe "2"
      ie043Data.declarationType mustBe "T1"
      ie043Data.consignee mustBe "Consignee Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      ie043Data.consigneeIdentificationNumber mustBe "idnum2"
      ie043Data.consignor mustBe "Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      ie043Data.consignorIdentificationNumber mustBe "idnum1"
      ie043Data.holderOfTransit mustBe "Travis, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      ie043Data.holderOfTransitID mustBe "id"
      ie043Data.departureTransportMeans mustBe "type1, id1, NG"
      ie043Data.transportEquipment mustBe "cin-2, 35, seq1:5"
      ie043Data.seals mustBe "1,[seal1]...2,[seal2]"
      ie043Data.previousDocument mustBe "768, ref1, 55"
      ie043Data.supportingDocument mustBe "764, ref2, 45"
      ie043Data.transportDocument mustBe "767, ref3"
      ie043Data.additionalInformation mustBe "32, additional ref text"
      ie043Data.additionalReference mustBe "4, ref4"
    }
  }

}
