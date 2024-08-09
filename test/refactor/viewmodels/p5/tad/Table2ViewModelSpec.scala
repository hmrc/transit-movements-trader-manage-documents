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
import generators.ScalaxbModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import refactor.viewmodels.DummyData

class Table2ViewModelSpec extends SpecBase with DummyData with ScalaCheckPropertyChecks with ScalaxbModelGenerators {

  private val lineWithSpaces = " " * 160

  "must map data to view model" - {

    "transportEquipment" - {
      def seal(j: Int): Int => SealType04 = i =>
        SealType04(
          sequenceNumber = s"$i$j",
          identifier = s"${i}sid$j"
        )

      def goodsReference(j: Int): Int => GoodsReferenceType02 = i =>
        GoodsReferenceType02(
          sequenceNumber = s"$i$j",
          declarationGoodsItemNumber = BigInt(s"$i$j")
        )

      def transportEquipment(i: Int, seals: Seq[Int => SealType04], goodsReferences: Seq[Int => GoodsReferenceType02]): TransportEquipmentType05 =
        TransportEquipmentType05(
          sequenceNumber = i.toString,
          containerIdentificationNumber = Some(s"cin$i"),
          numberOfSeals = i,
          Seal = seals.map(_(i)),
          GoodsReference = goodsReferences.map(_(i))
        )

      "when 1 transport equipment" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
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
        result.transportEquipment mustBe "1, cin1, 1, 11:11" + lineWithSpaces
      }

      "when 3 transport equipment" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
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
        result.transportEquipment mustBe "1, cin1, 1, 11:11...13:13; 2, cin2, 2, 21:21...23:23; 3, cin3, 3, 31:31...33:33" + lineWithSpaces
      }

      "when more than 3 transport equipment" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              ),
              transportEquipment(
                2,
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              ),
              transportEquipment(
                3,
                Seq(
                  seal(1)
                ),
                Seq(
                  goodsReference(1)
                )
              ),
              transportEquipment(
                4,
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
        result.transportEquipment mustBe "1, cin1, 1, 11:11; 2, cin2, 2, 21:21; 3, cin3, 3, 31:31..." + lineWithSpaces
      }

      "when transport equipment goes over 2 wide lines" in {
        def transportEquipment(i: Int, seals: Seq[Int => SealType04], goodsReferences: Seq[Int => GoodsReferenceType02]): TransportEquipmentType05 =
          TransportEquipmentType05(
            sequenceNumber = i.toString,
            containerIdentificationNumber = Some(s"transport equipment $i container identification number"),
            numberOfSeals = i,
            Seal = seals.map(_(i)),
            GoodsReference = goodsReferences.map(_(i))
          )

        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportEquipment = Seq(
              transportEquipment(
                1,
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
        println(result.transportEquipment.length)
        result.transportEquipment mustBe "1, transport equipment 1 container identification number, 1, 11:11...12:12; 2, transport equipment 2 container identification number, 2, 21:21...22:22; 3, transport equipment 3 ..."
      }
    }

    "seals" - {
      def seal(i: Int): SealType04 =
        SealType04(
          sequenceNumber = i.toString,
          identifier = s"sid$i"
        )

      "when 1 seal" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipmentType05]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    Seal = Seq(
                      seal(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.seals mustBe "1/sid1"
        }
      }

      "when 3 seals" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipmentType05]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
                    Seal = Seq(
                      seal(1),
                      seal(2),
                      seal(3)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.seals mustBe "1/sid1;3/sid3"
        }
      }

      "when more than 3 seals" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipmentType05]) {
          (ie029, transportEquipment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportEquipment = Seq(
                  transportEquipment.copy(
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
            result.seals mustBe "1/sid1;4/sid4"
        }
      }
    }

    "goodsReference" - {
      def goodsReference(i: Int): GoodsReferenceType02 =
        GoodsReferenceType02(
          sequenceNumber = i.toString,
          declarationGoodsItemNumber = i
        )

      "when 1 goods reference" in {
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipmentType05]) {
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
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipmentType05]) {
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
        forAll(arbitrary[CC029CType], arbitrary[TransportEquipmentType05]) {
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
      def previousDocumentType06(i: Int): PreviousDocumentType06 =
        PreviousDocumentType06(
          sequenceNumber = i.toString,
          typeValue = s"ptv$i",
          referenceNumber = s"prn$i",
          complementOfInformation = Some(s"pcoi$i")
        )

      def previousDocumentType07(i: Int): PreviousDocumentType07 =
        PreviousDocumentType07(
          sequenceNumber = i.toString,
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
                  previousDocumentType06(1)
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1, ptv1, prn1, pcoi1" + lineWithSpaces
        }
      }

      "when there are previous documents for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(1)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1, ptv1, prn1, pcoi1" + lineWithSpaces
        }
      }

      "when there are previous documents at consignment and house consignment level" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  previousDocumentType06(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(2),
                      previousDocumentType07(3)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi2; 3, ptv3, prn3, pcoi3" + lineWithSpaces
        }
      }

      "when there are more than 3 previous documents in total" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  previousDocumentType06(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(2)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(3)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi2; 3, ptv3, prn3, pcoi3..." + lineWithSpaces
        }
      }

      "when previous documents goes over 2 wide lines" in {
        def previousDocumentType06(i: Int): PreviousDocumentType06 =
          PreviousDocumentType06(
            sequenceNumber = i.toString,
            typeValue = s"previous document $i type value",
            referenceNumber = s"previous document $i reference number",
            complementOfInformation = Some(s"previous document $i complement of information")
          )

        def previousDocumentType07(i: Int): PreviousDocumentType07 =
          PreviousDocumentType07(
            sequenceNumber = i.toString,
            typeValue = s"previous document $i type value",
            referenceNumber = s"previous document $i reference number",
            complementOfInformation = Some(s"previous document $i complement of information")
          )

        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                PreviousDocument = Seq(
                  previousDocumentType06(1)
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(2)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(3)
                    )
                  ),
                  houseConsignment.copy(
                    PreviousDocument = Seq(
                      previousDocumentType07(4)
                    )
                  )
                )
              )
            )
            val result = Table2ViewModel(data)
            result.previousDocuments mustBe "1, previous document 1 type value, previous document 1 reference number, previous document 1 complement of information; 2, previous document 2 type value, previous document 2 re..."
        }
      }
    }

    "transportDocuments" - {
      def transportDocument(i: Int): TransportDocumentType02 =
        TransportDocumentType02(
          sequenceNumber = i.toString,
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
            result.transportDocuments mustBe "1, ttv1, trn1" + lineWithSpaces
        }
      }

      "when there are transport documents for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.transportDocuments mustBe "1, ttv1, trn1" + lineWithSpaces
        }
      }

      "when there are transport documents at consignment and house consignment level" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.transportDocuments mustBe "1, ttv1, trn1; 2, ttv2, trn2; 3, ttv3, trn3" + lineWithSpaces
        }
      }

      "when there are more than 3 transport documents in total" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.transportDocuments mustBe "1, ttv1, trn1; 2, ttv2, trn2; 3, ttv3, trn3..." + lineWithSpaces
        }
      }

      "when transport documents goes over 2 wide lines" in {
        def transportDocument(i: Int): TransportDocumentType02 =
          TransportDocumentType02(
            sequenceNumber = i.toString,
            typeValue = s"transport document $i type value",
            referenceNumber = s"transport document $i reference number"
          )

        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.transportDocuments mustBe "1, transport document 1 type value, transport document 1 reference number; 2, transport document 2 type value, transport document 2 reference number; 3, transport document 3 typ..."
        }
      }
    }

    "supportingDocuments" - {
      def supportingDocument(i: Int): SupportingDocumentType06 =
        SupportingDocumentType06(
          sequenceNumber = i.toString,
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
            result.supportingDocuments mustBe "1, stv1, srn1, 1, scoi1" + lineWithSpaces
        }
      }

      "when there are supporting documents for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.supportingDocuments mustBe "1, stv1, srn1, 1, scoi1" + lineWithSpaces
        }
      }

      "when there are supporting documents at consignment and house consignment level" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.supportingDocuments mustBe "1, stv1, srn1, 1, scoi1; 2, stv2, srn2, 2, scoi2; 3, stv3, srn3, 3, scoi3" + lineWithSpaces
        }
      }

      "when there are more than 3 supporting documents in total" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.supportingDocuments mustBe "1, stv1, srn1, 1, scoi1; 2, stv2, srn2, 2, scoi2; 3, stv3, srn3, 3, scoi3..." + lineWithSpaces
        }
      }

      "when supporting documents goes over 2 wide lines" in {
        def supportingDocument(i: Int): SupportingDocumentType06 =
          SupportingDocumentType06(
            sequenceNumber = i.toString,
            typeValue = s"supporting document $i type value",
            referenceNumber = s"supporting document $i reference number",
            documentLineItemNumber = Some(i),
            complementOfInformation = Some(s"supporting document $i complement of information")
          )

        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
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
            result.supportingDocuments mustBe "1, supporting document 1 type value, supporting document 1 reference number, 1, supporting document 1 complement of information; 2, supporting document 2 type value, supporting ..."
        }
      }
    }

    "additionalReferences" - {
      def additionalReference(i: Int): AdditionalReferenceType03 =
        AdditionalReferenceType03(
          sequenceNumber = i.toString,
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
        result.additionalReferences mustBe "1, artv1, arrn1"
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
        result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2; 3, artv3, arrn3"
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
        result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2; 3, artv3, arrn3..."
      }

      "when more than 90 characters" in {
        def additionalReference(i: Int): AdditionalReferenceType03 =
          AdditionalReferenceType03(
            sequenceNumber = i.toString,
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
        result.additionalReferences mustBe "1, additional reference 1 type value, additional reference 1 reference number; 2, addit..."
      }
    }

    "transportCharges" - {
      "when there are transport charges at consignment level" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            TransportCharges = Some(
              TransportChargesType(
                methodOfPayment = "cmop"
              )
            )
          )
        )
        val result = Table2ViewModel(data)
        result.transportCharges mustBe "cmop"
      }

      "when there are transport charges for a single house consignment" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
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
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "mop1"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "mop2"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
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
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "mop1"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "mop2"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "mop3"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
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
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportCharges = Some(
                  TransportChargesType(
                    methodOfPayment = "cmop"
                  )
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
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
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                TransportCharges = Some(
                  TransportChargesType(
                    methodOfPayment = "cmop"
                  )
                ),
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "hcmop1"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
                        methodOfPayment = "hcmop2"
                      )
                    )
                  ),
                  houseConsignment.copy(
                    TransportCharges = Some(
                      TransportChargesType(
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
      def additionalInformation(i: Int): AdditionalInformationType02 =
        AdditionalInformationType02(
          sequenceNumber = i.toString,
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
        result.additionalInformation mustBe "1, aic1, ait1"
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
        result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2; 3, aic3, ait3"
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
        result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2; 3, aic3, ait3..."
      }

      "when more than 90 characters" in {
        def additionalInformation(i: Int): AdditionalInformationType02 =
          AdditionalInformationType02(
            sequenceNumber = i.toString,
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
        result.additionalInformation mustBe "1, additional information 1 code, additional information 1 text; 2, additional informat..."
      }
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
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1, ogr1" + lineWithSpaces
      }

      "when 1 guarantee with 2 guarantee references" in {
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
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1; 2, 1grn2, 12.0, 1c2, ogr1" + lineWithSpaces
      }

      "when 1 guarantee with 3 guarantee references" in {
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
        result.guarantees mustBe "1, g1, 1, 1grn1, 11.0, 1c1; 2, 1grn2, 12.0, 1c2..., ogr1" + lineWithSpaces
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

    "authorisations" - {
      def authorisation(i: Int): AuthorisationType02 =
        AuthorisationType02(
          sequenceNumber = i.toString,
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
        result.authorisations mustBe "1, tv1, rn1"
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
        result.authorisations mustBe "1, tv1, rn1; 2, tv2, rn2; 3, tv3, rn3"
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
        result.authorisations mustBe "1, tv1, rn1; 2, tv2, rn2; 3, tv3, rn3..."
      }
    }
  }
}
