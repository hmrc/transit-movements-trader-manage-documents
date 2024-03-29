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

package services

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.reference.Country
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class ReturnCopiesCustomsOfficeConverterSpec extends AnyFreeSpec with Matchers with ValidatedMatchers with ValidatedValues {

  private val countries = Seq(Country("a", "country 1"), Country("b", "country 2"))

  "toViewModel" - {

    "must return a view model when the country code is found in the reference data" in {

      val consignee = models.ReturnCopiesCustomsOffice("name", "street longer than 32 characters...", "postCode", "city", countries.head.code)

      val result = ReturnCopiesCustomsOfficeConverter.toViewModel(consignee, "path", countries)

      result.valid.value mustEqual viewmodels.ReturnCopiesCustomsOffice("name", "street longer than 32 charact***", "postCode", "city", countries.head)
    }

    "must return Country Not Found when the country code cannot be found in the reference data" in {

      val consignee = models.ReturnCopiesCustomsOffice("name", "street longer than 32 characters...", "postCode", "city", "invalid code")

      val result = ReturnCopiesCustomsOfficeConverter.toViewModel(consignee, "path", countries)

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.countryCode", "invalid code"))
    }
  }
}
