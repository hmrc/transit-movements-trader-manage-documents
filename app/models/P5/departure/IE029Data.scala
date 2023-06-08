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

package models.P5.departure

import play.api.libs.json.Json
import play.api.libs.json.OWrites
import play.api.libs.json.Reads
import play.api.libs.json.__

case class IE029Data(data: DepartureMessageData) {

  // TODO move this to viewmodel

  val lrn: String = data.TransitOperation.LRN

  val mrn: String = data.TransitOperation.MRN

  val tir: String = data.TransitOperation.TIRCarnetNumber.getOrElse("")

  val ucr: String = data.Consignment.referenceNumberUCR.getOrElse("")

  val declarationType: String = data.TransitOperation.declarationType

  val additionalDeclarationType: String = data.TransitOperation.additionalDeclarationType

  val consignee: String = data.Consignment.Consignee match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignee"
  }

  val consignor: String = data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val holderOfTransitProcedure: String = data.HolderOfTheTransitProcedure.toString

  val representative: String = data.Representative.toString

  val carrier: String = data.Consignment.Carrier.map(_.identificationNumber).getOrElse("")

  val additionalSupplyChainActor: String = data.Consignment.additionalSupplyChainActors.getOrElse("")

  val departureTransportMeans: String = data.Consignment.departureTransportMeans.getOrElse("")

  val activeBorderTransportMeans: String = data.Consignment.activeBorderTransportMeans.getOrElse("")

  val placeOfLoading: String = data.Consignment.PlaceOfLoading.toString

  val placeOfUnloading: String = data.Consignment.PlaceOfUnloading.map(_.toString).getOrElse("")
}

object IE029Data {
  implicit val reads: Reads[IE029Data]    = (__ \ "body" \ "n1:CC029C").read[DepartureMessageData].map(IE029Data.apply)
  implicit val writes: OWrites[IE029Data] = Json.writes[IE029Data]
}
