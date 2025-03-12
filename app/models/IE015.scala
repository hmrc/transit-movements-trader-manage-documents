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

import models.IE015.*

import java.time.LocalDate
import scala.util.Try
import scala.xml.Node

// Since we only need a couple of fields from the IE015,
// we have created a custom case class to only retrieve the fields we need
// rather than using scalaxb and storing the entire message in memory

case class IE015(
  TransitOperation: TransitOperation,
  Consignment: Consignment
)

object IE015 {

  def reads(node: Node): IE015 = {
    val transitOperation = TransitOperation.reads((node \ "TransitOperation").head)
    val consignment      = Consignment.reads((node \ "Consignment").head)
    new IE015(transitOperation, consignment)
  }

  case class TransitOperation(limitDate: Option[LocalDate])

  object TransitOperation {

    def reads(node: Node): TransitOperation = {
      val limitDate = Try(LocalDate.parse((node \ "limitDate").text)).toOption
      new TransitOperation(limitDate)
    }
  }

  case class Consignment(HouseConsignment: Seq[HouseConsignment])

  object Consignment {

    def reads(node: Node): Consignment = {
      val houseConsignments = (node \ "HouseConsignment").map(HouseConsignment.reads)
      new Consignment(houseConsignments)
    }
  }

  case class HouseConsignment(ConsignmentItem: Seq[ConsignmentItem])

  object HouseConsignment {

    def reads(node: Node): HouseConsignment = {
      val consignmentItems = (node \ "ConsignmentItem").map(ConsignmentItem.reads)
      new HouseConsignment(consignmentItems)
    }
  }

  case class ConsignmentItem(
    declarationGoodsItemNumber: BigInt,
    Commodity: Commodity
  )

  object ConsignmentItem {

    def reads(node: Node): ConsignmentItem = {
      val declarationGoodsItemNumber = BigInt((node \ "declarationGoodsItemNumber").text)
      val commodity                  = Commodity.reads((node \ "Commodity").head)
      new ConsignmentItem(declarationGoodsItemNumber, commodity)
    }
  }

  case class Commodity(GoodsMeasure: Option[GoodsMeasure])

  object Commodity {

    def reads(node: Node): Commodity = {
      val goodsMeasure = Try(GoodsMeasure.reads((node \ "GoodsMeasure").head)).toOption
      new Commodity(goodsMeasure)
    }
  }

  case class GoodsMeasure(supplementaryUnits: Option[BigDecimal])

  object GoodsMeasure {

    def reads(node: Node): GoodsMeasure = {
      val supplementaryUnits = Try(BigDecimal((node \ "supplementaryUnits").text)).toOption
      new GoodsMeasure(supplementaryUnits)
    }
  }
}
