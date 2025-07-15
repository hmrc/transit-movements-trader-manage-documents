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
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import viewmodels.DummyData

class Table1ViewModelSpec extends SpecBase with DummyData with ScalaCheckPropertyChecks with IE029ScalaxbModelGenerators {

  private val lineWithSpaces = " " * 80

  "must map data to view model" - {

    val result = Table1ViewModel(cc029c)

    "additionalDeclarationType" in {
      result.additionalDeclarationType mustEqual "adt"
    }

    "consignees" - {
      def consignee(i: Int): Consignee =
        new Consignee(
          identificationNumber = Some(s"in$i"),
          name = Some(s"name$i"),
          Address = Some(
            new ConsigneeAddress(
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
        result.consignees mustEqual "name1/san1, pc1, city1, country1." + lineWithSpaces
      }

      "when 2 consignees (spanning 2 narrow lines out of available 2)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
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
            result.consignees mustEqual "name1/san1, pc1, city1, country1; name2/san2, pc2, city2, country2."
        }
      }

      "when 3 consignees (spanning more than the available 2 narrow lines)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
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
            result.consignees mustEqual "name1/san1, pc1, city1, country1; name2/san2, pc2, city2, country2; name3/san3, pc3, ci..."
        }
      }
    }

    "consigneeIdentificationNumbers" in {
      result.consigneeIdentificationNumbers mustEqual "in."
    }

    "consignors" - {
      def consignmentConsignor(i: Int): ConsignmentConsignor =
        new ConsignmentConsignor(
          identificationNumber = Some(s"in$i"),
          name = Some(s"name$i"),
          Address = Some(
            new ConsignmentConsignorAddress(
              streetAndNumber = s"san$i",
              postcode = Some(s"pc$i"),
              city = s"city$i",
              country = s"country$i"
            )
          ),
          ContactPerson = Some(
            new ConsignmentConsignorContactPerson(
              name = s"ccp$i",
              phoneNumber = s"ccptel$i",
              eMailAddress = Some(s"ccpemail$i")
            )
          )
        )

      def houseConsignmentConsignor(i: Int): HouseConsignmentConsignor =
        new HouseConsignmentConsignor(
          identificationNumber = Some(s"in$i"),
          name = Some(s"name$i"),
          Address = Some(
            new HouseConsignmentConsignorAddress(
              streetAndNumber = s"san$i",
              postcode = Some(s"pc$i"),
              city = s"city$i",
              country = s"country$i"
            )
          ),
          ContactPerson = Some(
            new HouseConsignmentConsignorContactPerson(
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
              consignmentConsignor(1)
            )
          )
        )
        val result = Table1ViewModel(data)
        result.consignors mustEqual "name1/san1, pc1, city1, country1." + lineWithSpaces * 2
      }

      "when 2 consignors (spanning 2 narrow lines out of available 3)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(2)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignors mustEqual "name1/san1, pc1, city1, country1; name2/san2, pc2, city2, country2." + lineWithSpaces
        }
      }

      "when 3 consignors (spanning 3 narrow lines out of available 3)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(2)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(3)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignors mustEqual "name1/san1, pc1, city1, country1; name2/san2, pc2, city2, country2; name3/san3, pc3, city3, country3."
        }
      }

      "when 4 consignors (spanning more than the available 3 narrow lines)" in {
        forAll(arbitrary[CC029CType], arbitrary[HouseConsignment]) {
          (ie029, houseConsignment) =>
            val data = ie029.copy(
              Consignment = ie029.Consignment.copy(
                Consignor = None,
                HouseConsignment = Seq(
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(1)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(2)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(3)
                    )
                  ),
                  houseConsignment.copy(
                    Consignor = Some(
                      houseConsignmentConsignor(4)
                    )
                  )
                )
              )
            )
            val result = Table1ViewModel(data)
            result.consignors mustEqual "name1/san1, pc1, city1, country1; name2/san2, pc2, city2, country2; name3/san3, pc3, city3, country3; name4/san4, pc4, city4, countr..."
        }
      }
    }

    "consignorIdentificationNumbers" in {
      result.consignorIdentificationNumbers mustEqual "in."
    }

    "consignorContactPersons" in {
      result.consignorContactPersons mustEqual "ccp/ccptel." + lineWithSpaces
    }

    "declarationType" in {
      result.declarationType mustEqual "T"
    }

    "holderOfTransitProcedure" in {
      result.holderOfTransitProcedure mustEqual "thin/hn/san, pc, city, country." + lineWithSpaces * 2
    }

    "holderOfTransitProcedureIdentificationNumber" in {
      result.holderOfTransitProcedureIdentificationNumber mustEqual "hin."
    }

    "hotPContactPerson" in {
      result.hotPContactPerson mustEqual "cp/cptel." + lineWithSpaces
    }

    "representative" in {
      result.representative mustEqual "rstatus."
    }

    "representativeContactPerson" in {
      result.representativeContactPerson mustEqual "cp/cptel."
    }

    "lrn" in {
      result.lrn mustEqual "lrn"
    }

    "carrierIdentificationNumber" in {
      result.carrierIdentificationNumber mustEqual "cin."
    }

    "carrierContactPerson" in {
      result.carrierContactPerson mustEqual "ccp/ccptel."
    }

    "additionalSupplyChainActorRoles" in {
      result.additionalSupplyChainActorRoles mustEqual "1/role1; 2/role2."
    }

    "additionalSupplyChainActorIdentificationNumbers" in {
      result.additionalSupplyChainActorIdentificationNumbers mustEqual "id1; id2." + lineWithSpaces
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustEqual "toi1/in1/nat1;...;toi4/in4/nat4." + lineWithSpaces * 2
    }

    "activeBorderTransportMeans" in {
      result.activeBorderTransportMeans mustEqual "coabrn1/toi1/in1/nat1;...;coabrn4/toi4/in4/nat4."
    }

    "activeBorderTransportMeansConveyanceNumbers" in {
      result.activeBorderTransportMeansConveyanceNumbers mustEqual "crn1;...;crn4." + lineWithSpaces
    }

    "placeOfLoading" in {
      result.placeOfLoading mustEqual "polunl, polc, poll" + lineWithSpaces
    }

    "placeOfUnloading" in {
      result.placeOfUnloading mustEqual "pouunl, pouc, poul" + lineWithSpaces
    }

    "modeOfTransportAtBorder" in {
      result.modeOfTransportAtBorder mustEqual "motatb"
    }

    "inlandModeOfTransport" in {
      result.inlandModeOfTransport mustEqual "imot"
    }

    "locationOfGoods" in {
      result.locationOfGoods mustEqual "tol, qoi, an, unl, corn, lat, lon, eoin, san, pc, city, country, hn, pc, country"
    }

    "locationOfGoodsContactPerson" in {
      result.locationOfGoodsContactPerson mustEqual "logcp, logcptel"
    }

    "security" in {
      result.security mustEqual "security"
    }

    "tir" in {
      result.tir mustEqual "tir"
    }

    "totalGrossMass" in {
      result.totalGrossMass mustEqual "200.0"
    }

    "specificCircumstanceIndicator" in {
      result.specificCircumstanceIndicator mustEqual "sci"
    }

    "totalItems" in {
      result.totalItems mustEqual "2"
    }

    "totalPackages" in {
      result.totalPackages mustEqual "66"
    }

    "ucr" in {
      result.ucr mustEqual "ucr"
    }
  }
}
