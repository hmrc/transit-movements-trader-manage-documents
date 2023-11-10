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

import generated.p5.CC029CType
import refactor.viewmodels._
import refactor.viewmodels.p5._

case class Table1ViewModel(
  additionalDeclarationType: String,
  consignee: String,
  consigneeIdentificationNumber: String,
  consignor: String,
  consignorIdentificationNumber: String,
  consignorContactPerson: String
)

object Table1ViewModel {

  def apply(ie029: CC029CType): Table1ViewModel =
    new Table1ViewModel(
      additionalDeclarationType = ie029.TransitOperation.additionalDeclarationType.take10,
      consignee = ie029.Consignment.Consignee match {
        case Some(value) => value.asString.take200
        case None        => ie029.Consignment.HouseConsignment.flatMap(_.Consignee.map(_.asString.take200)).seperateEntities
      },
      consigneeIdentificationNumber = ie029.Consignment.Consignee.flatMap(_.identificationNumber) match {
        case Some(value) => value.take20
        case None        => ie029.Consignment.HouseConsignment.flatMap(_.Consignee.flatMap(_.identificationNumber.map(_.take20))).seperateEntities
      },
      consignor = ie029.Consignment.Consignor match {
        case Some(value) => value.asString.take200
        case None        => ie029.Consignment.HouseConsignment.flatMap(_.Consignor.map(_.asString.take200)).seperateEntities
      },
      consignorIdentificationNumber = ie029.Consignment.Consignor.flatMap(_.identificationNumber) match {
        case Some(value) => value.take20
        case None        => ie029.Consignment.HouseConsignment.flatMap(_.Consignor.flatMap(_.identificationNumber.map(_.take20))).seperateEntities
      },
      consignorContactPerson = ie029.Consignment.Consignor.flatMap(_.ContactPerson) match {
        case Some(value) => value.asString.take100
        case None        => ie029.Consignment.HouseConsignment.flatMap(_.Consignor.flatMap(_.ContactPerson.map(_.asString.take100))).seperateEntities
      }
    )
}
