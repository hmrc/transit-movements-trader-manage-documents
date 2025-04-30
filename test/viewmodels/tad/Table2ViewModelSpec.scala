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
import generated.*
import generators.IE029ScalaxbModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import viewmodels.DummyData

class Table2ViewModelSpec extends SpecBase with DummyData with ScalaCheckPropertyChecks with IE029ScalaxbModelGenerators {

  private val lineWithSpaces = " " * 160

  "must map data to view model" - {

    "transportEquipment" - {
      def seal(i: Int)(j: Int): Seal =
        new Seal(
          sequenceNumber = i,
          identifier = s"${i}sid$j"
        )

      /** @param i
        *   goods reference sequence number
        * @param j
        *   transport equipment sequence number
        * @param k
        *   number of goods references in this transport equipment
        * @return
        *   a GoodsReference
        */
      def goodsReference(i: Int)(j: Int)(k: Int): GoodsReference =
        new GoodsReference(
          sequenceNumber = i,
          declarationGoodsItemNumber = BigInt((k * (j - 1)) + i)
        )

      def transportEquipment(
        i: Int,
        containerIdentificationNumber: Int => Option[String],
        seals: Seq[Int => Seal],
        goodsReferences: Seq[Int => Int => GoodsReference]
      ): TransportEquipment =
        new TransportEquipment(
          sequenceNumber = i,
          containerIdentificationNumber = containerIdentificationNumber(i),
          numberOfSeals = seals.length,
          Seal = seals.map(_(i)),
          GoodsReference = goodsReferences.map(_(i)(goodsReferences.length))
        )

      "when 1 transport equipment and 1 declarationGoodsItemNumber" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/cin1/1/1." + lineWithSpaces
      }

      "when 1 transport equipment and no seals" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                i => Some(s"cin$i"),
                Seq(),
                Seq(
                  goodsReference(1)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/cin1/1." + lineWithSpaces
        result.seals mustBe ""
      }

      "when 1 transport equipment and no container identification number" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                _ => None,
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/-/1/1." + lineWithSpaces
      }

      "when 1 transport equipment and multiple declarationGoodsItemNumber" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2),
                  goodsReference(3)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/cin1/1/1-3." + lineWithSpaces
      }

      "when 3 transport equipment" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2),
                  goodsReference(3)
                )
              ),
              transportEquipment(
                2,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2),
                  goodsReference(3)
                )
              ),
              transportEquipment(
                3,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2),
                  goodsReference(3)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/cin1/1/1-3; 2/cin2/1/4-6; 3/cin3/1/7-9." + lineWithSpaces
      }

      "when more than 3 transport equipment" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              ),
              transportEquipment(
                2,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              ),
              transportEquipment(
                3,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              ),
              transportEquipment(
                4,
                i => Some(s"cin$i"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/cin1/1/1;...;4/cin4/1/4." + lineWithSpaces
      }

      "when transport equipment goes over 2 wide lines" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                i => Some(s"transport equipment $i container identification number"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2)
                )
              ),
              transportEquipment(
                2,
                i => Some(s"transport equipment $i container identification number"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2)
                )
              ),
              transportEquipment(
                3,
                i => Some(s"transport equipment $i container identification number"),
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1),
                  goodsReference(2)
                )
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportEquipment mustBe "1/transport equipment 1 container identification number/1/1-2; 2/transport equipment 2 container identification number/1/3-4; 3/transport equipment 3 container identification nu..."
      }
    }

    "seals" - {
      def seal(i: Int): Seal =
        new Seal(
          sequenceNumber = i,
          identifier = s"sid$i"
        )

      "when 1 transport equipment and 1 seal" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipment]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    sequenceNumber = 1,
                    Seal = Seq(
                      seal(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.seals mustBe "1/sid1."
        }
      }

      "when <=3 transport equipment and multiple seals" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipment]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    sequenceNumber = 1,
                    Seal = Seq(
                      seal(1),
                      seal(2),
                      seal(3)
                    )
                  ),
                  transportEquipment.copy(
                    sequenceNumber = 2,
                    Seal = Seq(
                      seal(4),
                      seal(5),
                      seal(6)
                    )
                  ),
                  transportEquipment.copy(
                    sequenceNumber = 3,
                    Seal = Seq(
                      seal(7),
                      seal(8),
                      seal(9)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.seals mustBe "1/sid1-sid3; 2/sid4-sid6; 3/sid7-sid9."
        }
      }

      "when more than 3 transport equipments and multiple seals" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipment]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    sequenceNumber = 1,
                    Seal = Seq(
                      seal(1),
                      seal(2),
                      seal(3),
                      seal(4)
                    )
                  ),
                  transportEquipment.copy(
                    sequenceNumber = 2,
                    Seal = Seq(
                      seal(1),
                      seal(2),
                      seal(3),
                      seal(4)
                    )
                  ),
                  transportEquipment.copy(
                    sequenceNumber = 3,
                    Seal = Seq(
                      seal(1),
                      seal(2),
                      seal(3),
                      seal(4)
                    )
                  ),
                  transportEquipment.copy(
                    sequenceNumber = 4,
                    Seal = Seq(
                      seal(1),
                      seal(2),
                      seal(3),
                      seal(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.seals mustBe "1/sid1-sid4;...;4/sid1-sid4."
        }
      }
    }

    "goodsReference" - {
      def goodsReference(i: Int): GoodsReference =
        new GoodsReference(
          sequenceNumber = i,
          declarationGoodsItemNumber = i
        )

      "when 1 goods reference" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipment]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    GoodsReference = Seq(
                      goodsReference(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.goodsReferences mustBe "1,1"
        }
      }

      "when 3 goods reference" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipment]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    GoodsReference = Seq(
                      goodsReference(1),
                      goodsReference(2),
                      goodsReference(3)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.goodsReferences mustBe "1,1; 2,2; 3,3"
        }
      }

      "when 4 goods reference" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipment]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    GoodsReference = Seq(
                      goodsReference(1),
                      goodsReference(2),
                      goodsReference(3),
                      goodsReference(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.goodsReferences mustBe "1,1; 2,2; 3,3..."
        }
      }
    }

    "previousDocuments" - {
      def consignmentPreviousDocument(i: Int): ConsignmentPreviousDocument =
        new ConsignmentPreviousDocument(
          sequenceNumber = i,
          typeValue = s"ptv$i",
          referenceNumber = s"prn$i",
          complementOfInformation = Some(s"pcoi$i")
        )

      def houseConsignmentPreviousDocument(i: Int): HouseConsignmentPreviousDocument =
        new HouseConsignmentPreviousDocument(
          sequenceNumber = i,
          typeValue = s"ptv$i",
          referenceNumber = s"prn$i",
          complementOfInformation = Some(s"pcoi$i")
        )

      "when there are previous documents at consignment level" in {
        forAll(arbitrary[CC029CType]) {
          ie029 =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  consignmentPreviousDocument(1)
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1/ptv1/prn1/pcoi1." + lineWithSpaces
        }
      }

      "when there are previous documents for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1/ptv1/prn1/pcoi1." + lineWithSpaces
        }
      }

      "when there are previous documents at consignment and house consignment level" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  consignmentPreviousDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(2),
                      houseConsignmentPreviousDocument(3)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1/ptv1/prn1/pcoi1; 2/ptv2/prn2/pcoi2; 3/ptv3/prn3/pcoi3." + lineWithSpaces
        }
      }

      "when there are more than 3 previous documents in total" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  consignmentPreviousDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(2)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(3)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1/ptv1/prn1/pcoi1;...;4/ptv4/prn4/pcoi4." + lineWithSpaces
        }
      }

      "when previous documents goes over 2 wide lines" in {
        def consignmentPreviousDocument(i: Int): ConsignmentPreviousDocument =
          new ConsignmentPreviousDocument(
            sequenceNumber = i,
            typeValue = s"previous document $i type value",
            referenceNumber = s"previous document $i reference number",
            complementOfInformation = Some(s"previous document $i complement of information")
          )

        def houseConsignmentPreviousDocument(i: Int): HouseConsignmentPreviousDocument =
          new HouseConsignmentPreviousDocument(
            sequenceNumber = i,
            typeValue = s"previous document $i type value",
            referenceNumber = s"previous document $i reference number",
            complementOfInformation = Some(s"previous document $i complement of information")
          )

        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  consignmentPreviousDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(2)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(3)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      houseConsignmentPreviousDocument(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1/previous document 1 type value/previous document 1 reference number/previous document 1 complement of information;...;4/previous document 4 type value/previous document 4 refe..."
        }
      }
    }

    "transportDocuments" - {
      def transportDocument(i: Int): TransportDocument =
        new TransportDocument(
          sequenceNumber = i,
          typeValue = s"ttv$i",
          referenceNumber = s"trn$i"
        )

      "when there are transport documents at consignment level" in {
        forAll(arbitrary[CC029CType]) {
          ie029 =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportDocument = Seq(
                  transportDocument(1)
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportDocuments mustBe "1/ttv1/trn1." + lineWithSpaces
        }
      }

      "when there are transport documents for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportDocuments mustBe "1/ttv1/trn1." + lineWithSpaces
        }
      }

      "when there are transport documents at consignment and house consignment level" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportDocument = Seq(
                  transportDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(2),
                      transportDocument(3)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportDocuments mustBe "1/ttv1/trn1; 2/ttv2/trn2; 3/ttv3/trn3." + lineWithSpaces
        }
      }

      "when there are more than 3 transport documents in total" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportDocument = Seq(
                  transportDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(2)
                    )
                  ),
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(3)
                    )
                  ),
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportDocuments mustBe "1/ttv1/trn1;...;4/ttv4/trn4." + lineWithSpaces
        }
      }

      "when transport documents goes over 2 wide lines" in {
        def transportDocument(i: Int): TransportDocument =
          new TransportDocument(
            sequenceNumber = i,
            typeValue = s"transport document $i type value",
            referenceNumber = s"transport document $i reference number"
          )

        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportDocument = Seq(
                  transportDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(2)
                    )
                  ),
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(3)
                    )
                  ),
                  houseConsignment.copy(
                    TransportDocument = Seq(
                      transportDocument(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportDocuments mustBe "1/transport document 1 type value/transport document 1 reference number;...;4/transport document 4 type value/transport document 4 reference number."
        }
      }
    }

    "supportingDocuments" - {
      def supportingDocument(i: Int): SupportingDocument =
        new SupportingDocument(
          sequenceNumber = i,
          typeValue = s"stv$i",
          referenceNumber = s"srn$i",
          documentLineItemNumber = Some(i),
          complementOfInformation = Some(s"scoi$i")
        )

      "when there are supporting documents at consignment level" in {
        forAll(arbitrary[CC029CType]) {
          ie029 =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                SupportingDocument = Seq(
                  supportingDocument(1)
                )
              )
            )
            val result = Table2ViewModel(data)
            result.supportingDocuments mustBe "1/stv1/srn1/1/scoi1." + lineWithSpaces
        }
      }

      "when there are supporting documents for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.supportingDocuments mustBe "1/stv1/srn1/1/scoi1." + lineWithSpaces
        }
      }

      "when there are supporting documents at consignment and house consignment level" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                SupportingDocument = Seq(
                  supportingDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(2),
                      supportingDocument(3)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.supportingDocuments mustBe "1/stv1/srn1/1/scoi1; 2/stv2/srn2/2/scoi2; 3/stv3/srn3/3/scoi3." + lineWithSpaces
        }
      }

      "when there are more than 3 supporting documents in total" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                SupportingDocument = Seq(
                  supportingDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(2)
                    )
                  ),
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(3)
                    )
                  ),
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.supportingDocuments mustBe "1/stv1/srn1/1/scoi1;...;4/stv4/srn4/4/scoi4." + lineWithSpaces
        }
      }

      "when supporting documents goes over 2 wide lines" in {
        def supportingDocument(i: Int): SupportingDocument =
          new SupportingDocument(
            sequenceNumber = i,
            typeValue = s"supporting document $i type value",
            referenceNumber = s"supporting document $i reference number",
            documentLineItemNumber = Some(i),
            complementOfInformation = Some(s"supporting document $i complement of information")
          )

        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                SupportingDocument = Seq(
                  supportingDocument(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(2)
                    )
                  ),
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(3)
                    )
                  ),
                  houseConsignment.copy(
                    SupportingDocument = Seq(
                      supportingDocument(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.supportingDocuments mustBe "1/supporting document 1 type value/supporting document 1 reference number/1/supporting document 1 complement of information;...;4/supporting document 4 type value/supporting doc..."
        }
      }
    }

    "additionalReferences" - {
      def additionalReference(i: Int): ConsignmentAdditionalReference =
        new ConsignmentAdditionalReference(
          sequenceNumber = i,
          typeValue = s"artv$i",
          referenceNumber = Some(s"arrn$i")
        )

      "when 1 additional reference" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalReference = Seq(
              additionalReference(1)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalReferences mustBe "1/artv1/arrn1."
      }

      "when 3 additional references" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalReference = Seq(
              additionalReference(1),
              additionalReference(2),
              additionalReference(3)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalReferences mustBe "1/artv1/arrn1; 2/artv2/arrn2; 3/artv3/arrn3."
      }

      "when 4 additional references" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalReference = Seq(
              additionalReference(1),
              additionalReference(2),
              additionalReference(3),
              additionalReference(4)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalReferences mustBe "1/artv1/arrn1;...;4/artv4/arrn4."
      }

      "when more than 90 characters" in {
        def additionalReference(i: Int): ConsignmentAdditionalReference =
          new ConsignmentAdditionalReference(
            sequenceNumber = i,
            typeValue = s"additional reference $i type value",
            referenceNumber = Some(s"additional reference $i reference number")
          )

        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalReference = Seq(
              additionalReference(1),
              additionalReference(2)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalReferences mustBe "1/additional reference 1 type value/additional reference 1 reference number; 2/addition..."
      }
    }

    "transportCharges" - {
      "when there are transport charges at consignment level" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportCharges = Some(
              new TransportCharges(
                methodOfPayment = "cmop"
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportCharges mustBe "cmop"
      }

      "when there are transport charges for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "hcmop"
                      )
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportCharges mustBe "hcmop"
        }
      }

      "when there are transport charges for three house consignments" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop1"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop2"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop3"
                      )
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportCharges mustBe "mop1; mop2; mop3"
        }
      }

      "when there are transport charges for four house consignments" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop1"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop2"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop3"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop4"
                      )
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportCharges mustBe "mop1; mop2; mop3..."
        }
      }

      "when there are transport charges at consignment level and one house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportCharges = Some(
                  new TransportCharges(
                    methodOfPayment = "cmop"
                  )
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "mop1"
                      )
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportCharges mustBe "cmop; mop1"
        }
      }

      "when more than 20 characters" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportCharges = Some(
                  new TransportCharges(
                    methodOfPayment = "cmop"
                  )
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "hcmop1"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "hcmop2"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      new TransportCharges(
                        methodOfPayment = "hcmop3"
                      )
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.transportCharges mustBe "cmop; hcmop1; hcm..."
        }
      }
    }

    "additionalInformation" - {
      def additionalInformation(i: Int): AdditionalInformation =
        new AdditionalInformation(
          sequenceNumber = i,
          code = s"aic$i",
          text = Some(s"ait$i")
        )

      "when 1 additional information" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalInformation = Seq(
              additionalInformation(1)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalInformation mustBe "1/aic1/ait1."
      }

      "when 3 additional information" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalInformation = Seq(
              additionalInformation(1),
              additionalInformation(2),
              additionalInformation(3)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalInformation mustBe "1/aic1/ait1; 2/aic2/ait2; 3/aic3/ait3."
      }

      "when 4 additional information" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalInformation = Seq(
              additionalInformation(1),
              additionalInformation(2),
              additionalInformation(3),
              additionalInformation(4)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalInformation mustBe "1/aic1/ait1;...;4/aic4/ait4."
      }

      "when more than 90 characters" in {
        def additionalInformation(i: Int): AdditionalInformation =
          new AdditionalInformation(
            sequenceNumber = i,
            code = s"additional information $i code",
            text = Some(s"additional information $i text")
          )
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            AdditionalInformation = Seq(
              additionalInformation(1),
              additionalInformation(2)
            )
          )
        )
        val result = Table2ViewModel(data)
        result.additionalInformation mustBe "1/additional information 1 code/additional information 1 text; 2/additional information..."
      }
    }

    "guarantees" - {
      def guaranteeReference(j: Int): Int => GuaranteeReference = i =>
        new GuaranteeReference(
          sequenceNumber = j,
          GRN = Some(s"${i}grn$j"),
          accessCode = Some(s"${i}ac$j"),
          amountToBeCovered = Some(BigDecimal(s"$i$j")),
          currency = Some(s"${i}c$j")
        )

      def guarantee(i: Int, guaranteeType: String, guaranteeReferences: Seq[Int => GuaranteeReference], otherGuaranteeRef: Option[String] = None): Guarantee =
        new Guarantee(
          sequenceNumber = i,
          guaranteeType = guaranteeType,
          otherGuaranteeReference = otherGuaranteeRef,
          GuaranteeReference = guaranteeReferences.map(_(i))
        )

      "Guarantee Type 0/1/2/4/9" - {
        "when 1 guarantee with 3 guarantee references" in {
          forAll(Gen.oneOf("0", "1", "2", "4", "9")) {
            guaranteeType =>
              val data = cc029c.copy(
                Guarantee = Seq(
                  guarantee(
                    1,
                    guaranteeType,
                    Seq(
                      guaranteeReference(1),
                      guaranteeReference(2),
                      guaranteeReference(3)
                    ),
                    None
                  )
                )
              )
              val result = Table2ViewModel(data)
              result.guarantees mustBe s"1/$guaranteeType/1grn1; 1grn2; 1grn3." + lineWithSpaces
          }
        }

        "when 1 guarantee with 4 guarantee references" in {
          forAll(Gen.oneOf("0", "1", "2", "4", "9")) {
            guaranteeType =>
              val data = cc029c.copy(
                Guarantee = Seq(
                  guarantee(
                    1,
                    guaranteeType,
                    Seq(
                      guaranteeReference(1),
                      guaranteeReference(2),
                      guaranteeReference(3),
                      guaranteeReference(4)
                    ),
                    None
                  )
                )
              )
              val result = Table2ViewModel(data)
              result.guarantees mustBe s"1/$guaranteeType/1grn1;...;1grn4." + lineWithSpaces
          }
        }
      }

      "Guarantee Type 3 or 8" in {
        forAll(Gen.oneOf("3", "8")) {
          guaranteeType =>
            val data = cc029c.copy(
              Guarantee = Seq(
                guarantee(
                  1,
                  guaranteeType,
                  Seq(),
                  otherGuaranteeRef = Some("other1")
                )
              )
            )
            val result = Table2ViewModel(data)
            result.guarantees mustBe s"1/$guaranteeType/other1." + lineWithSpaces
        }
      }

      "Guarantee Type 5 or A" in {
        forAll(Gen.oneOf("5", "A")) {
          guaranteeType =>
            val data = cc029c.copy(
              Guarantee = Seq(
                guarantee(
                  1,
                  guaranteeType,
                  Seq(),
                  None
                )
              )
            )
            val result = Table2ViewModel(data)
            result.guarantees mustBe s"1/$guaranteeType." + lineWithSpaces
        }
      }

      "Mixed Guarantee Types" - {
        "less than or equal to 3 types" - {
          "types 1/3/5 with less than or equal to 3 guarantee references" in {
            val data = cc029c.copy(
              Guarantee = Seq(
                guarantee(
                  1,
                  "1",
                  Seq(
                    guaranteeReference(1),
                    guaranteeReference(2),
                    guaranteeReference(3)
                  ),
                  None
                ),
                guarantee(
                  2,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other1")
                ),
                guarantee(
                  3,
                  "5",
                  Seq(),
                  None
                )
              )
            )
            val result = Table2ViewModel(data)
            result.guarantees mustBe "1/1/1grn1; 1grn2; 1grn3; 2/3/other1; 3/5." + lineWithSpaces
          }

          "types 1/3/5 with more than 3 guarantee references" in {
            val data = cc029c.copy(
              Guarantee = Seq(
                guarantee(
                  1,
                  "1",
                  Seq(
                    guaranteeReference(1),
                    guaranteeReference(2),
                    guaranteeReference(3),
                    guaranteeReference(4)
                  ),
                  None
                ),
                guarantee(
                  2,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other1")
                ),
                guarantee(
                  3,
                  "5",
                  Seq(),
                  None
                )
              )
            )
            val result = Table2ViewModel(data)
            result.guarantees mustBe "1/1/1grn1;...;1grn4; 2/3/other1; 3/5." + lineWithSpaces
          }
        }
        "more than 3 types" - {
          "types 0/1/3/5 with less than or equal to 3 guarantee references" in {
            val data = cc029c.copy(
              Guarantee = Seq(
                guarantee(
                  1,
                  "0",
                  Seq(
                    guaranteeReference(1),
                    guaranteeReference(2),
                    guaranteeReference(3)
                  ),
                  None
                ),
                guarantee(
                  2,
                  "1",
                  Seq(
                    guaranteeReference(1),
                    guaranteeReference(2),
                    guaranteeReference(3)
                  ),
                  None
                ),
                guarantee(
                  3,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other1")
                ),
                guarantee(
                  4,
                  "5",
                  Seq(),
                  None
                )
              )
            )
            val result = Table2ViewModel(data)
            result.guarantees mustBe "1/0/1grn1; 1grn2; 1grn3;...;4/5." + lineWithSpaces
          }

          "types 0/1/3/5 with more than 3 guarantee references" in {
            val data = cc029c.copy(
              Guarantee = Seq(
                guarantee(
                  1,
                  "0",
                  Seq(
                    guaranteeReference(1),
                    guaranteeReference(2),
                    guaranteeReference(3),
                    guaranteeReference(4)
                  )
                ),
                guarantee(
                  2,
                  "1",
                  Seq(
                    guaranteeReference(1),
                    guaranteeReference(2),
                    guaranteeReference(3),
                    guaranteeReference(4)
                  )
                ),
                guarantee(
                  3,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other1")
                ),
                guarantee(
                  3,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other2")
                ),
                guarantee(
                  3,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other3")
                ),
                guarantee(
                  3,
                  "3",
                  Seq(),
                  otherGuaranteeRef = Some("other4")
                ),
                guarantee(
                  4,
                  "5",
                  Seq()
                )
              )
            )
            val result = Table2ViewModel(data)
            result.guarantees mustBe "1/0/1grn1;...;1grn4;...;4/5." + lineWithSpaces
          }
        }
      }
    }

    "authorisations" - {
      def authorisation(i: Int): Authorisation =
        new Authorisation(
          sequenceNumber = i,
          typeValue = s"tv$i",
          referenceNumber = s"rn$i"
        )

      "when 1 authorisation" in {
        val data = cc029c.copy(
          Authorisation = Seq(
            authorisation(1)
          )
        )
        val result = Table2ViewModel(data)
        result.authorisations mustBe "1/tv1/rn1."
      }

      "when 3 authorisations" in {
        val data = cc029c.copy(
          Authorisation = Seq(
            authorisation(1),
            authorisation(2),
            authorisation(3)
          )
        )
        val result = Table2ViewModel(data)
        result.authorisations mustBe "1/tv1/rn1; 2/tv2/rn2; 3/tv3/rn3."
      }

      "when 4 authorisations" in {
        val data = cc029c.copy(
          Authorisation = Seq(
            authorisation(1),
            authorisation(2),
            authorisation(3),
            authorisation(4)
          )
        )
        val result = Table2ViewModel(data)
        result.authorisations mustBe "1/tv1/rn1;...;4/tv4/rn4."
      }
    }

    "containerIndicator" - {
      "when false" - {
        "must return 0" in {
          val data = cc029c.copy(
            Consignment = cc029c.Consignment.copy(
              containerIndicator = Number0
            )
          )
          val result = Table2ViewModel(data)
          result.containerIndicator mustBe ""
        }
      }

      "when true" - {
        "must return 1" in {
          val data = cc029c.copy(
            Consignment = cc029c.Consignment.copy(
              containerIndicator = Number1
            )
          )
          val result = Table2ViewModel(data)
          result.containerIndicator mustBe "X"
        }
      }
    }

    "reducedDatasetIndicator" - {
      "when false" - {
        "must return 0" in {
          val data = cc029c.copy(
            TransitOperation = cc029c.TransitOperation.copy(
              reducedDatasetIndicator = Number0
            )
          )
          val result = Table2ViewModel(data)
          result.reducedDatasetIndicator mustBe ""
        }
      }

      "when true" - {
        "must return 1" in {
          val data = cc029c.copy(
            TransitOperation = cc029c.TransitOperation.copy(
              reducedDatasetIndicator = Number1
            )
          )
          val result = Table2ViewModel(data)
          result.reducedDatasetIndicator mustBe "X"
        }
      }
    }
  }
}
