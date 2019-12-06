/*
 * Copyright 2019 HM Revenue & Customs
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

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.{FreeSpec, MustMatchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, JsSuccess, Json}

class DeclarationTypeSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Declaration Type" -  {

    "must deserialise from `T-`" in {

      JsString("T-").validate[DeclarationType] mustEqual JsSuccess(DeclarationType.TMinus)
    }

    "must deserialise from `T1`" in {

      JsString("T1").validate[DeclarationType] mustEqual JsSuccess(DeclarationType.T1)
    }

    "must deserialise from `T2`" in {

      JsString("T2").validate[DeclarationType] mustEqual JsSuccess(DeclarationType.T2)
    }

    "must deserialise from `T2F`" in {

      JsString("T2F").validate[DeclarationType] mustEqual JsSuccess(DeclarationType.T2F)
    }

    "must deserialise from `T2SM`" in {

      JsString("T2SM").validate[DeclarationType] mustEqual JsSuccess(DeclarationType.T2SM)
    }

    "must deserialise from `TIR`" in {

      JsString("TIR").validate[DeclarationType] mustEqual JsSuccess(DeclarationType.TIR)
    }

    "must fail to deserialise from an invalid value" in {

      JsString("Invalid").validate[DeclarationType] mustBe a[JsError]
    }

    "must serialise to `T-`" in {

      Json.toJson(DeclarationType.TMinus: DeclarationType) mustEqual JsString("T-")
    }

    "must serialise to `T1`" in {

      Json.toJson(DeclarationType.T1: DeclarationType) mustEqual JsString("T1")
    }

    "must serialise to `T2`" in {

      Json.toJson(DeclarationType.T2: DeclarationType) mustEqual JsString("T2")
    }

    "must serialise to `T2F`" in {

      Json.toJson(DeclarationType.T2F: DeclarationType) mustEqual JsString("T2F")
    }

    "must serialise to `T2SM`" in {

      Json.toJson(DeclarationType.T2SM: DeclarationType) mustEqual JsString("T2SM")
    }

    "must serialise to `TIR`" in {

      Json.toJson(DeclarationType.TIR: DeclarationType) mustEqual JsString("TIR")
    }
  }
}
