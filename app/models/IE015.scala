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

import generated.CC015CType

import javax.xml.datatype.XMLGregorianCalendar

case class IE015(
  limitDate: Option[XMLGregorianCalendar],
  supplementaryUnits: Map[BigInt, Option[BigDecimal]]
)

object IE015 {

  def apply(ie015: CC015CType): IE015 =
    new IE015(
      limitDate = ie015.TransitOperation.limitDate,
      supplementaryUnits = ie015.Consignment.HouseConsignment
        .flatMap(_.ConsignmentItem)
        .foldLeft(Map[BigInt, Option[BigDecimal]]()) {
          case (acc, consignmentItem) =>
            acc + (consignmentItem.declarationGoodsItemNumber -> consignmentItem.Commodity.GoodsMeasure.flatMap(_.supplementaryUnits))
        }
    )
}
