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

class TransportEquipmentSpec extends SpecBase {

  private val transportEquipment = TransportEquipment(None, 0, None, None)

  "seals" - {
    "must display seals as string" - {
      "when no seals" in {
        val seals  = Nil
        val result = transportEquipment.copy(Seal = Some(seals)).seals
        result mustBe ""
      }

      "when one seal" in {
        val seals = List(
          Seal("1", "Seal 1")
        )
        val result = transportEquipment.copy(Seal = Some(seals)).seals
        result mustBe "1,[Seal 1]"
      }

      "when two seals" in {
        val seals = List(
          Seal("1", "Seal 1"),
          Seal("2", "Seal 2")
        )
        val result = transportEquipment.copy(Seal = Some(seals)).seals
        result mustBe "1,[Seal 1]...2,[Seal 2]"
      }

      "when multiple seals" in {
        val seals = List(
          Seal("1", "Seal 1"),
          Seal("2", "Seal 2"),
          Seal("3", "Seal 3")
        )
        val result = transportEquipment.copy(Seal = Some(seals)).seals
        result mustBe "1,[Seal 1]...3,[Seal 3]"
      }
    }
  }

}
