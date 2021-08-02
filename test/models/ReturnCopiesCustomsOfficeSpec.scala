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

package models

import com.lucidchart.open.xtract.EmptyError
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.XPath
import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.Utility.trim

class ReturnCopiesCustomsOfficeSpec extends FreeSpec with ScalaCheckPropertyChecks with ModelGenerators with MustMatchers with OptionValues {

  "ReturnCopiesCustomsOffice" - {
    "must read xml as ReturnCopiesCustomsOffice model" in {

      val input: ReturnCopiesCustomsOffice = arbitrary[ReturnCopiesCustomsOffice].sample.value
      val inputXml                         = <CUSOFFRETCOPOCP>
      <CusOffNamOCP2>{input.customsOfficeName}</CusOffNamOCP2>
      <StrAndNumOCP3>{input.streetAndNumber}</StrAndNumOCP3>
      <PosCodOCP6>{input.postCode}</PosCodOCP6>
      <CitOCP7>{input.city}</CitOCP7>
      <CouOCP4>{input.countryCode}</CouOCP4>
      </CUSOFFRETCOPOCP>

      XmlReader.of[ReturnCopiesCustomsOffice].read(trim(inputXml)).toOption mustBe Some(input)
    }

    "must return error when mandatory fields missing" in {

      val input: ReturnCopiesCustomsOffice = arbitrary[ReturnCopiesCustomsOffice].sample.value
      val inputXml                         = <CUSOFFRETCOPOCP>
      <CusOffNamOCP2>{input.customsOfficeName}</CusOffNamOCP2>
      <CitOCP7>{input.city}</CitOCP7>
      <CouOCP4>{input.countryCode}</CouOCP4>
      </CUSOFFRETCOPOCP>

      XmlReader.of[ReturnCopiesCustomsOffice].read(trim(inputXml)) mustBe ParseFailure(
        List(EmptyError(XPath \ "StrAndNumOCP3"), EmptyError(XPath \ "PosCodOCP6"))
      )
    }
  }

}
