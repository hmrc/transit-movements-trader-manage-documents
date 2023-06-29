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

  "IE043Data" - {

    "must be able to return correct UPD Data for each fields" in {

      IE043Data(unloadingPermissionMessageData).mrn mustBe "38VYQTYFU3T0KUTUM3"
      IE043Data(unloadingPermissionMessageData).tir mustBe "tirId"
      IE043Data(unloadingPermissionMessageData).customsOfficeOfDestination mustBe "GB000068"
      IE043Data(unloadingPermissionMessageData).countryOfDestination mustBe "FR"
      IE043Data(unloadingPermissionMessageData).totalGrossMass mustBe 1000.99
      IE043Data(unloadingPermissionMessageData).totalPackages mustBe 3
      IE043Data(unloadingPermissionMessageData).totalItems mustBe 1
      IE043Data(unloadingPermissionMessageData).container mustBe "1"
      IE043Data(unloadingPermissionMessageData).security mustBe "4"
      IE043Data(unloadingPermissionMessageData).inlandModeOfTransport mustBe "2"
      IE043Data(unloadingPermissionMessageData).declarationType mustBe "T1"
      IE043Data(
        unloadingPermissionMessageData
      ).consignee mustBe "Consignee Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      IE043Data(unloadingPermissionMessageData).consigneeIdentificationNumber mustBe "idnum2"
      IE043Data(
        unloadingPermissionMessageData
      ).consignor mustBe "Consignor Name, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      IE043Data(unloadingPermissionMessageData).consignorIdentificationNumber mustBe "idnum1"
      IE043Data(unloadingPermissionMessageData).holderOfTransit mustBe "Travis, Address Line 1, Address Line 2, Address Line 3, Address Line 4"
      IE043Data(unloadingPermissionMessageData).holderOfTransitID mustBe "id"
      IE043Data(unloadingPermissionMessageData).departureTransportMeans mustBe "type1, id1, NG"
      IE043Data(unloadingPermissionMessageData).transportEquipment mustBe "cin-2, 35, seq1:5"
      IE043Data(unloadingPermissionMessageData).seals mustBe "1,[seal1]...2,[seal2]"
      IE043Data(unloadingPermissionMessageData).previousDocument mustBe "768, ref1, 55"
      IE043Data(unloadingPermissionMessageData).supportingDocument mustBe "764, ref2, 45"
      IE043Data(unloadingPermissionMessageData).transportDocument mustBe "767, ref3"
      IE043Data(unloadingPermissionMessageData).additionalInformation mustBe "32, additional ref text"
      IE043Data(unloadingPermissionMessageData).additionalReference mustBe "4, ref4"
    }
  }

}
