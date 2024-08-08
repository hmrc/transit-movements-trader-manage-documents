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

package refactor.viewmodels.p5.tad

import base.SpecBase
import generated.p5._
import refactor.viewmodels.DummyData

class Table2ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val cc029cP5 = cc029c.copy(Consignment =
      cc029c.Consignment.copy(TransportEquipment =
        Seq(
          TransportEquipmentType05(
            sequenceNumber = "1",
            containerIdentificationNumber = Some("cin1"),
            numberOfSeals = BigInt(2),
            Seal = Seq(
              SealType04(
                sequenceNumber = "1",
                identifier = "sid1"
              ),
              SealType04(
                sequenceNumber = "2",
                identifier = "sid2"
              )
            ),
            GoodsReference = Seq(
              GoodsReferenceType02(
                sequenceNumber = "1",
                declarationGoodsItemNumber = BigInt(1)
              )
            )
          ),
          TransportEquipmentType05(
            sequenceNumber = "2",
            containerIdentificationNumber = Some("cin2"),
            numberOfSeals = BigInt(3),
            Seal = Seq(
              SealType04(
                sequenceNumber = "3",
                identifier = "sid3"
              ),
              SealType04(
                sequenceNumber = "4",
                identifier = "sid4"
              ),
              SealType04(
                sequenceNumber = "5",
                identifier = "sid5"
              )
            ),
            GoodsReference = Seq(
              GoodsReferenceType02(
                sequenceNumber = "2",
                declarationGoodsItemNumber = BigInt(2)
              )
            )
          ),
          TransportEquipmentType05(
            sequenceNumber = "3",
            containerIdentificationNumber = Some("cin3"),
            numberOfSeals = BigInt(3),
            Seal = Seq(
              SealType04(
                sequenceNumber = "6",
                identifier = "sid6"
              ),
              SealType04(
                sequenceNumber = "7",
                identifier = "sid7"
              ),
              SealType04(
                sequenceNumber = "8",
                identifier = "sid8"
              )
            ),
            GoodsReference = Seq(
              GoodsReferenceType02(
                sequenceNumber = "3",
                declarationGoodsItemNumber = BigInt(3)
              )
            )
          ),
          TransportEquipmentType05(
            sequenceNumber = "4",
            containerIdentificationNumber = Some("cin4"),
            numberOfSeals = BigInt(2),
            Seal = Seq(
              SealType04(
                sequenceNumber = "9",
                identifier = "sid9"
              ),
              SealType04(
                sequenceNumber = "10",
                identifier = "sid10"
              )
            ),
            GoodsReference = Seq(
              GoodsReferenceType02(
                sequenceNumber = "4",
                declarationGoodsItemNumber = BigInt(4)
              )
            )
          )
        )
      )
    )

    val result = Table2ViewModel(cc029cP5)

    "transportEquipment" in {
      result.transportEquipment mustBe "1, cin1, 2, 1:1; 2, cin2, 3, 2:2; 3, cin3, 3, 3..."
    }

    "seals" in {
      result.seals mustBe "1/sid1;10/sid10"
    }

    "goodsReference" in {
      result.goodsReference mustBe "1,1; 2,2; 3,3..."
    }

    "previousDocuments" in {
      result.previousDocuments mustBe "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi1; 3, ptv3, prn3, pcoi1..."
    }

    "transportDocuments" in {
      result.transportDocuments mustBe "1, ttv1, trn1; 2, ttv2, trn2; 3, ttv3, trn3..."
    }

    "supportingDocuments" in {
      result.supportingDocuments mustBe "1, stv1, srn1, 1, scoi1; 2, stv2, srn2, 1, scoi1; 3, stv3, srn3, 1, scoi3..."
    }

    "additionalReferences" in {
      result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2; 3, artv3, arrn3..."
    }

    "transportCharges" in {
      result.transportCharges mustBe "mop"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2; 3, aic3, ait3..."
    }

    "guarantees" - {
      def guaranteeReference(j: Int): Int => GuaranteeReferenceType01 = i =>
        GuaranteeReferenceType01(
          sequenceNumber = j.toString,
          GRN = Some(s"${i}grn$j"),
          accessCode = Some(s"${i}ac$j"),
          amountToBeCovered = Some(BigDecimal(s"$i$j")),
          currency = Some(s"${i}c$j")
        )

      def guarantee(i: Int, guaranteeReferences: Seq[Int => GuaranteeReferenceType01]): GuaranteeType03 =
        GuaranteeType03(
          sequenceNumber = i.toString,
          guaranteeType = s"g$i",
          otherGuaranteeReference = Some(s"ogr$i"),
          GuaranteeReference = guaranteeReferences.map(_(i))
        )

      "when 1 guarantee with 1 guarantee reference" in {
        val data = cc029c.copy(
          Guarantee = Seq(
            guarantee(
              1,
              Seq(
                guaranteeReference(1)
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1, ogr1"
      }

      "when 1 guarantee with 2 guarantee reference" in {
        val data = cc029c.copy(
          Guarantee = Seq(
            guarantee(
              1,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2)
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1; 2, 1grn2, 12.0, 1c2, ogr1"
      }

      "when 1 guarantee with 3 guarantee reference" in {
        val data = cc029c.copy(
          Guarantee = Seq(
            guarantee(
              1,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2),
                guaranteeReference(3)
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1; 2, 1grn2, 12.0, 1c2..., ogr1"
      }

      "when 2 guarantees with 3 guarantee references" in {
        val data = cc029c.copy(
          Guarantee = Seq(
            guarantee(
              1,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2),
                guaranteeReference(3)
              )
            ),
            guarantee(
              2,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2),
                guaranteeReference(3)
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1; 2, 1grn2, 12.0, 1c2..., ogr1; 2, g2, 1, 2grn1, 21.0, 2c1; 2, 2grn2, 22.0, 2c2..., ogr2"
      }

      "when 3 guarantees with 3 guarantee references" in {
        val data = cc029c.copy(
          Guarantee = Seq(
            guarantee(
              1,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2),
                guaranteeReference(3)
              )
            ),
            guarantee(
              2,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2),
                guaranteeReference(3)
              )
            ),
            guarantee(
              3,
              Seq(
                guaranteeReference(1),
                guaranteeReference(2),
                guaranteeReference(3)
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1; 2, 1grn2, 12.0, 1c2..., ogr1; 2, g2, 1, 2grn1, 21.0, 2c1; 2, 2grn2, 22.0, 2c2..., ogr2..."
      }
    }

    "authorisations" in {
      result.authorisations mustBe "1, C521, rn1; 2, tv2, rn2; 3, tv3, rn3..."
    }
  }
}
