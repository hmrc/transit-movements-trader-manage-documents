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

package refactor.viewmodels.p5

import base.SpecBase
import generated.p5._
import generators.ScalaxbModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import refactor.viewmodels.DummyData

class RichSpec extends SpecBase with DummyData with ScalaCheckPropertyChecks with ScalaxbModelGenerators {

  "RichCC029CType" - {
    "must roll down" - {
      "when transport charges defined at consignment level" in {
        forAll(arbitrary[TransportChargesType]) {
          transportCharges =>
            val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(TransportCharges = Some(transportCharges)))
            val result = data.rollDown
            result.consignmentItems.foreach {
              _.TransportCharges.value mustBe transportCharges
            }
        }
      }

      "when UCR defined at consignment level" in {
        forAll(nonEmptyString) {
          referenceNumberUCR =>
            val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(referenceNumberUCR = Some(referenceNumberUCR)))
            val result = data.rollDown
            result.consignmentItems.foreach {
              _.referenceNumberUCR.value mustBe referenceNumberUCR
            }
        }
      }

      "when country of dispatch defined at consignment level" in {
        forAll(nonEmptyString) {
          countryOfDispatch =>
            val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(countryOfDispatch = Some(countryOfDispatch)))
            val result = data.rollDown
            result.consignmentItems.foreach {
              _.countryOfDispatch.value mustBe countryOfDispatch
            }
        }
      }

      "when country of destination defined at consignment level" in {
        forAll(nonEmptyString) {
          countryOfDestination =>
            val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(countryOfDestination = Some(countryOfDestination)))
            val result = data.rollDown
            result.consignmentItems.foreach {
              _.countryOfDestination.value mustBe countryOfDestination
            }
        }
      }
    }

    "must not roll down" - {
      "when transport charges undefined at consignment level" in {
        val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(TransportCharges = None))
        val result = data.rollDown
        result.consignmentItems.zipWithIndex.foreach {
          case (ci, i) =>
            ci.TransportCharges mustBe
              cc029c.Consignment.HouseConsignment.flatMap(_.ConsignmentItem).apply(i).TransportCharges
        }
      }

      "when UCR undefined at consignment level" in {
        val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(referenceNumberUCR = None))
        val result = data.rollDown
        result.consignmentItems.zipWithIndex.foreach {
          case (ci, i) =>
            ci.referenceNumberUCR mustBe
              cc029c.Consignment.HouseConsignment.flatMap(_.ConsignmentItem).apply(i).referenceNumberUCR
        }
      }

      "when country of dispatch undefined at consignment level" in {
        val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(countryOfDispatch = None))
        val result = data.rollDown
        result.consignmentItems.zipWithIndex.foreach {
          case (ci, i) =>
            ci.countryOfDispatch mustBe
              cc029c.Consignment.HouseConsignment.flatMap(_.ConsignmentItem).apply(i).countryOfDispatch
        }
      }

      "when country of destination undefined at consignment level" in {
        val data   = cc029c.copy(Consignment = cc029c.Consignment.copy(countryOfDestination = None))
        val result = data.rollDown
        result.consignmentItems.zipWithIndex.foreach {
          case (ci, i) =>
            ci.countryOfDestination mustBe
              cc029c.Consignment.HouseConsignment.flatMap(_.ConsignmentItem).apply(i).countryOfDestination
        }
      }
    }
  }
}
