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

package viewmodels.tad.posttransition

import base.SpecBase
import generated.p5._
import generators.ScalaxbModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import viewmodels.DummyData

class Table1ViewModelSpec extends SpecBase with DummyData with ScalaCheckPropertyChecks with ScalaxbModelGenerators {

  private val lineWithSpaces = " " * 80

  "must map data to view model" - {

    val result = Table1ViewModel(cc029c)

    "additionalDeclarationType" in {
      result.additionalDeclarationType mustBe "adt"
    }

    "consignees" - {
      def consignee(i: Int): ConsigneeType04 =
        ConsigneeType04(
          identificationNumber = Some(s"in$i"),
          name = Some(s"name$i"),
          Address = Some(
            AddressType07(
              streetAndNumber = s"san$i",
              postcode = Some(s"pc$i"),
              city = s"city$i",
              country = s"country$i"
            )
          )
        )

      "when 1 consignee (spanning 1 narrow line out of available 2)" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            Consignee = Some(
              consignee(1)
            )
          )
        )
        val result = Table1ViewModel(data)
        result.consignees mustBe "name1, san1, pc1, city1, country1" + lineWithSpaces
      }

      "when 2 consignees (spanning 2 narrow lines out of available 2)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignee = Some(
                      consignee(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignee = Some(
                      consignee(2)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignees mustBe "name1, san1, pc1, city1, country1; name2, san2, pc2, city2, country2"
        }
      }

      "when 3 consignees (spanning more than the available 2 narrow lines)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignee = Some(
                      consignee(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignee = Some(
                      consignee(2)
                    )
                  ),
                  houseConsignment.copy(
                    Consignee = Some(
                      consignee(3)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignees mustBe "name1, san1, pc1, city1, country1; name2, san2, pc2, city2, country2; name3, san3, pc3,..."
        }
      }
    }

    "consigneeIdentificationNumbers" in {
      result.consigneeIdentificationNumbers mustBe "in"
    }

    "consignors" - {
      def consignorType03(i: Int): ConsignorType03 =
        ConsignorType03(
          identificationNumber = Some(s"in$i"),
          name = Some(s"name$i"),
          Address = Some(
            AddressType07(
              streetAndNumber = s"san$i",
              postcode = Some(s"pc$i"),
              city = s"city$i",
              country = s"country$i"
            )
          ),
          ContactPerson = Some(
            ContactPersonType01(
              name = s"ccp$i",
              phoneNumber = s"ccptel$i",
              eMailAddress = Some(s"ccpemail$i")
            )
          )
        )

      def consignorType04(i: Int): ConsignorType04 =
        ConsignorType04(
          identificationNumber = Some(s"in$i"),
          name = Some(s"name$i"),
          Address = Some(
            AddressType07(
              streetAndNumber = s"san$i",
              postcode = Some(s"pc$i"),
              city = s"city$i",
              country = s"country$i"
            )
          ),
          ContactPerson = Some(
            ContactPersonType01(
              name = s"ccp$i",
              phoneNumber = s"ccptel$i",
              eMailAddress = Some(s"ccpemail$i")
            )
          )
        )

      "when 1 consignor (spanning 1 narrow line out of available 3)" in {
        val data = cc029c.copy(
          Consignment = cc029c.Consignment.copy(
            Consignor = Some(
              consignorType03(1)
            )
          )
        )
        val result = Table1ViewModel(data)
        result.consignors mustBe "name1, san1, pc1, city1, country1" + lineWithSpaces * 2
      }

      "when 2 consignors (spanning 2 narrow lines out of available 3)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(2)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignors mustBe "name1, san1, pc1, city1, country1; name2, san2, pc2, city2, country2" + lineWithSpaces
        }
      }

      "when 3 consignors (spanning 3 narrow lines out of available 3)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(2)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(3)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignors mustBe "name1, san1, pc1, city1, country1; name2, san2, pc2, city2, country2; name3, san3, pc3, city3, country3"
        }
      }

      "when 4 consignors (spanning more than the available 3 narrow lines)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignmentType03]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(2)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(3)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      consignorType04(4)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignors mustBe "name1, san1, pc1, city1, country1; name2, san2, pc2, city2, country2; name3, san3, pc3, city3, country3; name4, san4, pc4, city4, co..."
        }
      }
    }

    "consignorIdentificationNumbers" in {
      result.consignorIdentificationNumbers mustBe "in"
    }

    "consignorContactPersons" in {
      result.consignorContactPersons mustBe "ccp, ccptel" + lineWithSpaces
    }

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "holderOfTransitProcedure" in {
      result.holderOfTransitProcedure mustBe "thin, hn, san, pc, city, country" + lineWithSpaces * 2
    }

    "holderOfTransitProcedureIdentificationNumber" in {
      result.holderOfTransitProcedureIdentificationNumber mustBe "hin"
    }

    "hotPContactPerson" in {
      result.hotPContactPerson mustBe "cp, cptel" + lineWithSpaces
    }

    "representative" in {
      result.representative mustBe "rstatus"
    }

    "representativeContactPerson" in {
      result.representativeContactPerson mustBe "cp, cptel"
    }

    "lrn" in {
      result.lrn mustBe "lrn"
    }

    "carrierIdentificationNumber" in {
      result.carrierIdentificationNumber mustBe "cin"
    }

    "carrierContactPerson" in {
      result.carrierContactPerson mustBe "ccp, ccptel"
    }

    "additionalSupplyChainActorRoles" in {
      result.additionalSupplyChainActorRoles mustBe "role1; role2"
    }

    "additionalSupplyChainActorIdentificationNumbers" in {
      result.additionalSupplyChainActorIdentificationNumbers mustBe "id1; id2" + lineWithSpaces
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustBe "toi1, in1, nat1; toi2, in2, nat2; toi3, in3, nat3..." + lineWithSpaces * 2
    }

    "activeBorderTransportMeans" in {
      result.activeBorderTransportMeans mustBe "coabrn1, toi1, in1, nat1; coabrn2, toi2, in2, nat2; coabrn3, toi3, in3, nat3..."
    }

    "activeBorderTransportMeansConveyanceNumbers" in {
      result.activeBorderTransportMeansConveyanceNumbers mustBe "crn1; crn2; crn3; crn4" + lineWithSpaces
    }

    "placeOfLoading" in {
      result.placeOfLoading mustBe "polunl, polc, poll" + lineWithSpaces
    }

    "placeOfUnloading" in {
      result.placeOfUnloading mustBe "pouunl, pouc, poul" + lineWithSpaces
    }

    "modeOfTransportAtBorder" in {
      result.modeOfTransportAtBorder mustBe "motatb"
    }

    "inlandModeOfTransport" in {
      result.inlandModeOfTransport mustBe "imot"
    }

    "locationOfGoods" in {
      result.locationOfGoods mustBe "tol, qoi, an, unl, corn, lat, lon, eoin, san, pc, city, country, hn, pc, country"
    }

    "locationOfGoodsContactPerson" in {
      result.locationOfGoodsContactPerson mustBe "logcp, logcptel"
    }

    "security" in {
      result.security mustBe "security"
    }

    "tir" in {
      result.tir mustBe "tir"
    }

    "totalGrossMass" in {
      result.totalGrossMass mustBe "200.0"
    }

    "specificCircumstanceIndicator" in {
      result.specificCircumstanceIndicator mustBe "sci"
    }

    "totalItems" in {
      result.totalItems mustBe "2"
    }

    "totalPackages" in {
      result.totalPackages mustBe "66"
    }

    "ucr" in {
      result.ucr mustBe "ucr"
    }
  }
}
