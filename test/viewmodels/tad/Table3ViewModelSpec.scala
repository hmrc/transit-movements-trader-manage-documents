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

package viewmodels.tad

import base.SpecBase
import viewmodels.DummyData

class Table3ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val ie029 = cc029c.copy(Consignment =
      cc029c.Consignment.copy(TransportEquipment =
        Seq(
          new TransportEquipment(
            sequenceNumber = 1,
            containerIdentificationNumber = Some("cin1"),
            numberOfSeals = BigInt(2),
            Seal = Seq(
              new Seal(
                sequenceNumber = 1,
                identifier = "sid1"
              ),
              new Seal(
                sequenceNumber = 2,
                identifier = "sid2"
              )
            ),
            GoodsReference = Seq(
              new GoodsReference(
                sequenceNumber = 1,
                declarationGoodsItemNumber = BigInt(1)
              ),
              new GoodsReference(
                sequenceNumber = 2,
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          ),
          new TransportEquipment(
            sequenceNumber = 2,
            containerIdentificationNumber = Some("cin2"),
            numberOfSeals = BigInt(3),
            Seal = Seq(
              new Seal(
                sequenceNumber = 3,
                identifier = "sid3"
              ),
              new Seal(
                sequenceNumber = 4,
                identifier = "sid4"
              ),
              new Seal(
                sequenceNumber = 5,
                identifier = "sid5"
              )
            ),
            GoodsReference = Seq(
              new GoodsReference(
                sequenceNumber = 1,
                declarationGoodsItemNumber = BigInt(1)
              ),
              new GoodsReference(
                sequenceNumber = 2,
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          ),
          new TransportEquipment(
            sequenceNumber = 3,
            containerIdentificationNumber = Some("cin3"),
            numberOfSeals = BigInt(3),
            Seal = Seq(
              new Seal(
                sequenceNumber = 6,
                identifier = "sid6"
              ),
              new Seal(
                sequenceNumber = 7,
                identifier = "sid7"
              ),
              new Seal(
                sequenceNumber = 8,
                identifier = "sid8"
              )
            ),
            GoodsReference = Seq(
              new GoodsReference(
                sequenceNumber = 1,
                declarationGoodsItemNumber = BigInt(1)
              ),
              new GoodsReference(
                sequenceNumber = 2,
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          ),
          new TransportEquipment(
            sequenceNumber = 4,
            containerIdentificationNumber = Some("cin4"),
            numberOfSeals = BigInt(2),
            Seal = Seq(
              new Seal(
                sequenceNumber = 9,
                identifier = "sid9"
              ),
              new Seal(
                sequenceNumber = 10,
                identifier = "sid10"
              )
            ),
            GoodsReference = Seq(
              new GoodsReference(
                sequenceNumber = 1,
                declarationGoodsItemNumber = BigInt(1)
              ),
              new GoodsReference(
                sequenceNumber = 2,
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          )
        )
      )
    )

    val result = Table3ViewModel(ie029)

    "containerIdentification" in {
      result.containerIdentification mustEqual "cin1; cin2; cin3..."
    }
  }
}
