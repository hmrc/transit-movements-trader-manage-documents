/*
 * Copyright 2021 HM Revenue & Customs
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
import models.reference.Country
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class TraderConverterSpec extends AnyFreeSpec with Matchers with ValidatedMatchers with ValidatedValues {

  private val countries   = Seq(Country("a", "country 1"), Country("b", "country 2"))
  private val invalidCode = "invalid code"

  "toViewModel" - {

    "when given a trader with an EORI" - {

      "must return a view model when the trader does not have a country code" in {

        val trader = models.TraderAtDestinationWithEori("EORI", None, None, None, None, None)

        val result = TraderConverter.toViewModel(trader, "path", countries)

        result.valid.value mustEqual viewmodels.TraderAtDestinationWithEori("EORI", None, None, None, None, None)
      }

      "must return a view model when the country code is found in reference data" in {

        val trader = models.TraderAtDestinationWithEori("EORI", None, None, None, None, Some(countries.head.code))

        val result = TraderConverter.toViewModel(trader, "path", countries)

        result.valid.value mustEqual viewmodels.TraderAtDestinationWithEori("EORI", None, None, None, None, Some(countries.head))
      }

      "must return Invalid when the country code cannot be found in reference data" in {

        val trader = models.TraderAtDestinationWithEori("EORI", None, None, None, None, Some(invalidCode))

        val result = TraderConverter.toViewModel(trader, "path", countries)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.countryCode", invalidCode))
      }
    }

    "when given a trader without an EORI" - {

      "must return a view model when the country code is found in reference data" in {

        val trader = models.TraderAtDestinationWithoutEori("name", "street", "postCode", "city", countries.head.code)

        val result = TraderConverter.toViewModel(trader, "path", countries)

        result.valid.value mustEqual viewmodels.TraderAtDestinationWithoutEori("name", "street", "postCode", "city", countries.head)
      }

      "must return Invalid when the country code cannot be found in reference data" in {

        val trader = models.TraderAtDestinationWithoutEori("name", "street", "postCode", "city", invalidCode)

        val result = TraderConverter.toViewModel(trader, "path", countries)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.countryCode", invalidCode))
      }
    }
  }
}
