/*
 * Copyright 2025 HM Revenue & Customs
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

import base.SpecBase
import models.IE015.*

import java.time.LocalDate

class IE015Spec extends SpecBase {

  "GoodsMeasure" - {
    "must deserialise" - {
      "when supplementaryUnits defined" in {
        val xml = <GoodsMeasure>
          <grossMass>1000</grossMass>
          <supplementaryUnits>11</supplementaryUnits>
        </GoodsMeasure>

        val result = GoodsMeasure.reads(xml)

        result mustEqual GoodsMeasure(Some(11))
      }

      "when supplementaryUnits undefined" in {
        val xml = <GoodsMeasure>
          <grossMass>1000</grossMass>
        </GoodsMeasure>

        val result = GoodsMeasure.reads(xml)

        result mustEqual GoodsMeasure(None)
      }
    }
  }

  "Commodity" - {
    "must deserialise" - {
      "when GoodsMeasure defined" in {
        val xml = <Commodity>
          <descriptionOfGoods>Toys</descriptionOfGoods>
          <GoodsMeasure>
            <grossMass>1000</grossMass>
            <supplementaryUnits>11</supplementaryUnits>
          </GoodsMeasure>
        </Commodity>

        val result = Commodity.reads(xml)

        result mustEqual Commodity(Some(GoodsMeasure(Some(11))))
      }

      "when GoodsMeasure undefined" in {
        val xml = <Commodity>
          <descriptionOfGoods>Toys</descriptionOfGoods>
        </Commodity>

        val result = Commodity.reads(xml)

        result mustEqual Commodity(None)
      }
    }
  }

  "ConsignmentItem" - {
    "must deserialise" in {
      val xml = <ConsignmentItem>
        <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
        <Commodity>
          <descriptionOfGoods>Toys</descriptionOfGoods>
          <GoodsMeasure>
            <grossMass>1000</grossMass>
            <supplementaryUnits>11</supplementaryUnits>
          </GoodsMeasure>
        </Commodity>
      </ConsignmentItem>

      val result = ConsignmentItem.reads(xml)

      result mustEqual ConsignmentItem(1, Commodity(Some(GoodsMeasure(Some(11)))))
    }
  }

  "HouseConsignment" - {
    "must deserialise" - {
      "when one consignment item" in {
        val xml = <HouseConsignment>
          <ConsignmentItem>
            <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
            <Commodity>
              <descriptionOfGoods>Toys</descriptionOfGoods>
              <GoodsMeasure>
                <grossMass>1000</grossMass>
                <supplementaryUnits>11</supplementaryUnits>
              </GoodsMeasure>
            </Commodity>
          </ConsignmentItem>
        </HouseConsignment>

        val result = HouseConsignment.reads(xml)

        result mustEqual HouseConsignment(
          Seq(
            ConsignmentItem(1, Commodity(Some(GoodsMeasure(Some(11)))))
          )
        )
      }

      "when multiple consignment items" in {
        val xml = <HouseConsignment>
          <ConsignmentItem>
            <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
            <Commodity>
              <descriptionOfGoods>Toys</descriptionOfGoods>
              <GoodsMeasure>
                <grossMass>1000</grossMass>
                <supplementaryUnits>11</supplementaryUnits>
              </GoodsMeasure>
            </Commodity>
          </ConsignmentItem>
          <ConsignmentItem>
            <declarationGoodsItemNumber>2</declarationGoodsItemNumber>
            <Commodity>
              <descriptionOfGoods>More toys</descriptionOfGoods>
              <GoodsMeasure>
                <grossMass>2000</grossMass>
                <supplementaryUnits>22</supplementaryUnits>
              </GoodsMeasure>
            </Commodity>
          </ConsignmentItem>
        </HouseConsignment>

        val result = HouseConsignment.reads(xml)

        result mustEqual HouseConsignment(
          Seq(
            ConsignmentItem(1, Commodity(Some(GoodsMeasure(Some(11))))),
            ConsignmentItem(2, Commodity(Some(GoodsMeasure(Some(22)))))
          )
        )
      }
    }
  }

  "Consignment" - {
    "must deserialise" - {
      "when one house consignment" in {
        val xml = <Consignment>
          <HouseConsignment>
            <ConsignmentItem>
              <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
              <Commodity>
                <descriptionOfGoods>Toys</descriptionOfGoods>
                <GoodsMeasure>
                  <grossMass>1000</grossMass>
                  <supplementaryUnits>11</supplementaryUnits>
                </GoodsMeasure>
              </Commodity>
            </ConsignmentItem>
          </HouseConsignment>
        </Consignment>

        val result = Consignment.reads(xml)

        result mustEqual Consignment(
          Seq(
            HouseConsignment(
              Seq(
                ConsignmentItem(1, Commodity(Some(GoodsMeasure(Some(11)))))
              )
            )
          )
        )
      }

      "when multiple house consignments" in {
        val xml = <Consignment>
          <HouseConsignment>
            <ConsignmentItem>
              <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
              <Commodity>
                <descriptionOfGoods>Toys</descriptionOfGoods>
                <GoodsMeasure>
                  <grossMass>1000</grossMass>
                  <supplementaryUnits>11</supplementaryUnits>
                </GoodsMeasure>
              </Commodity>
            </ConsignmentItem>
          </HouseConsignment>
          <HouseConsignment>
            <ConsignmentItem>
              <declarationGoodsItemNumber>2</declarationGoodsItemNumber>
              <Commodity>
                <descriptionOfGoods>More toys</descriptionOfGoods>
                <GoodsMeasure>
                  <grossMass>2000</grossMass>
                  <supplementaryUnits>22</supplementaryUnits>
                </GoodsMeasure>
              </Commodity>
            </ConsignmentItem>
          </HouseConsignment>
        </Consignment>

        val result = Consignment.reads(xml)

        result mustEqual Consignment(
          Seq(
            HouseConsignment(
              Seq(
                ConsignmentItem(1, Commodity(Some(GoodsMeasure(Some(11)))))
              )
            ),
            HouseConsignment(
              Seq(
                ConsignmentItem(2, Commodity(Some(GoodsMeasure(Some(22)))))
              )
            )
          )
        )
      }
    }
  }

  "TransitOperation" - {
    "must deserialise" - {
      "when limitDate defined" in {
        val xml = <TransitOperation>
          <lrn>LRN123</lrn>
          <limitDate>2022-02-03</limitDate>
        </TransitOperation>

        val result = TransitOperation.reads(xml)

        result mustEqual TransitOperation(Some(LocalDate.of(2022, 2, 3)))
      }

      "when limitDate undefined" in {
        val xml = <TransitOperation>
          <lrn>LRN123</lrn>
        </TransitOperation>

        val result = TransitOperation.reads(xml)

        result mustEqual TransitOperation(None)
      }
    }
  }

  "IE015" - {
    "must deserialise" in {
      val xml = <ncts:CC015C>
        <TransitOperation>
          <lrn>LRN123</lrn>
          <limitDate>2022-02-03</limitDate>
        </TransitOperation>
        <Consignment>
          <HouseConsignment>
            <ConsignmentItem>
              <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
              <Commodity>
                <descriptionOfGoods>Toys</descriptionOfGoods>
                <GoodsMeasure>
                  <grossMass>1000</grossMass>
                  <supplementaryUnits>11</supplementaryUnits>
                </GoodsMeasure>
              </Commodity>
            </ConsignmentItem>
          </HouseConsignment>
        </Consignment>
      </ncts:CC015C>

      val result = IE015.reads(xml)

      result mustEqual IE015(
        TransitOperation(Some(LocalDate.of(2022, 2, 3))),
        Consignment(
          Seq(
            HouseConsignment(
              Seq(
                ConsignmentItem(1, Commodity(Some(GoodsMeasure(Some(11)))))
              )
            )
          )
        )
      )
    }
  }
}
