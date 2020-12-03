/*
 * Copyright 2020 HM Revenue & Customs
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

package services

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import generators.ModelGenerators
import models.CustomsOfficeTransit
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class CustomsOfficeTransitConverterSpec
    extends FreeSpec
    with MustMatchers
    with ScalaCheckPropertyChecks
    with ValidatedMatchers
    with ValidatedValues
    with ModelGenerators {

  private val genTransitAccompanyingDocumentData = for {
    customsOfficeTransit1 <- arbitrary[CustomsOfficeTransit]
    customsOfficeTransit2 <- arbitrary[CustomsOfficeTransit]
    customsOfficeTransit3 <- arbitrary[CustomsOfficeTransit]
    customsOfficeTransit4 <- arbitrary[CustomsOfficeTransit]
    customsOfficeTransit5 <- arbitrary[CustomsOfficeTransit]
    customsOfficeTransit6 <- arbitrary[CustomsOfficeTransit]
    customsOfficeTransit7 <- arbitrary[CustomsOfficeTransit]
  } yield
    (customsOfficeTransit1,
     customsOfficeTransit2,
     customsOfficeTransit3,
     customsOfficeTransit4,
     customsOfficeTransit5,
     customsOfficeTransit6,
     customsOfficeTransit7)

  "toViewModel" - {

    "must return a view model when CustomsOfficeTransit exists" in {

      forAll(arbitrary[CustomsOfficeTransit]) {
        customsOfficeTransit =>
          val result = CustomsOfficeTransitConverter.toViewModel(Seq(customsOfficeTransit))

          result mustEqual Seq(viewmodels.CustomsOfficeTransit(customsOfficeTransit.referenceNumber, customsOfficeTransit.arrivalTime))
      }
    }

    "must limit to 6 when more than 6 exist records exist" in {

      forAll(genTransitAccompanyingDocumentData) {
        case (customsOfficeTransit1,
              customsOfficeTransit2,
              customsOfficeTransit3,
              customsOfficeTransit4,
              customsOfficeTransit5,
              customsOfficeTransit6,
              customsOfficeTransit7) =>
          val result = CustomsOfficeTransitConverter.toViewModel(
            Seq(
              customsOfficeTransit1,
              customsOfficeTransit2,
              customsOfficeTransit3,
              customsOfficeTransit4,
              customsOfficeTransit5,
              customsOfficeTransit6,
              customsOfficeTransit7
            ))

          result mustEqual Seq(
            viewmodels.CustomsOfficeTransit(customsOfficeTransit1.referenceNumber, customsOfficeTransit1.arrivalTime),
            viewmodels.CustomsOfficeTransit(customsOfficeTransit2.referenceNumber, customsOfficeTransit2.arrivalTime),
            viewmodels.CustomsOfficeTransit(customsOfficeTransit3.referenceNumber, customsOfficeTransit3.arrivalTime),
            viewmodels.CustomsOfficeTransit(customsOfficeTransit4.referenceNumber, customsOfficeTransit4.arrivalTime),
            viewmodels.CustomsOfficeTransit(customsOfficeTransit5.referenceNumber, customsOfficeTransit5.arrivalTime),
            viewmodels.CustomsOfficeTransit(customsOfficeTransit6.referenceNumber, customsOfficeTransit6.arrivalTime)
          )
      }
    }

    "must return None when CustomsOfficeTransit does not exist" in {

      val result = ControlResultConverter.toViewModel(None)

      result mustEqual None
    }
  }
}
