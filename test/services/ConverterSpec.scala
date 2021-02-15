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
import models.reference.CodedReferenceData
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

case class ReferenceData(override val code: String) extends CodedReferenceData

class TestConverter extends Converter

class ConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val data = Seq(ReferenceData("a"), ReferenceData("b"))

  val converter = new TestConverter()

  "find reference data" - {

    "must return an item from the reference data" - {

      val result = converter.findReferenceData(data.head.code, data, "path")

      result.valid.value mustEqual data.head
    }

    "must return a not found error when the requested item is not in the reference data" in {

      val result = converter.findReferenceData("invalid code", data, "path")

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path", "invalid code"))
    }
  }
}
