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

package base

import models.P5.unloading.AdditionalInformation
import models.P5.unloading.AdditionalReference
import models.P5.unloading.Address
import models.P5.unloading.Consignee
import models.P5.unloading.Consignment
import models.P5.unloading.ConsignmentItem
import models.P5.unloading.Consignor
import models.P5.unloading.CustomsOfficeOfDestinationActual
import models.P5.unloading.DepartureTransportMeans
import models.P5.unloading.GoodsReference
import models.P5.unloading.HolderOfTransitProcedure
import models.P5.unloading.HouseConsignment
import models.P5.unloading.Packaging
import models.P5.unloading.PreviousDocument
import models.P5.unloading.Seal
import models.P5.unloading.SupportingDocument
import models.P5.unloading.TransitOperation
import models.P5.unloading.TransportDocument
import models.P5.unloading.TransportEquipment
import models.P5.unloading.UnloadingMessageData
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier

trait SpecBase extends AnyFreeSpec with Matchers with OptionValues with MockitoSugar {
  val arrivalId                  = "arrId"
  implicit val hc: HeaderCarrier = HeaderCarrier()
  val messageId                  = "7443005176c4e834"

  val transitOperation: TransitOperation =
    TransitOperation("38VYQTYFU3T0KUTUM3", Some("T1"), Some("2014-06-09+01:00"), "4", "1")

  val customsOfficeOfDestinationActual: CustomsOfficeOfDestinationActual =
    CustomsOfficeOfDestinationActual("GB000068")

  val address: Address = Address("Address Line 1", Some("Address Line 2"), "Address Line 3", "Address Line 4")

  val holderOfTheTransitProcedure: HolderOfTransitProcedure =
    HolderOfTransitProcedure(Some("id"), Some("tirId"), "Travis", Some(address))

  val consignor: Consignor = Consignor(Some("idnum1"), Some("Consignor Name"), Some(address))
  val consignee: Consignee = Consignee(Some("idnum2"), Some("Consignee Name"), Some(address))

  val seals: List[Seal] = List(Seal("seq1", "sealId"))

  val goodsReference: List[GoodsReference] = List(GoodsReference("seq1", 5))

  val transportEquipment: List[TransportEquipment] =
    List(TransportEquipment("te2", Some("cin-2"), 35, Some(seals), Some(goodsReference)))

  val departureTransportMeans: List[DepartureTransportMeans] =
    List(DepartureTransportMeans("seq1", "type1", "id1", "NG"))

  val previousDocument: List[PreviousDocument] = List(PreviousDocument("pr1", "768", "ref1", Some("55")))

  val supportingDocument: List[SupportingDocument] = List(SupportingDocument("sp1", "764", "ref2", Some("45")))

  val transportDocument: List[TransportDocument] = List(TransportDocument("tp1", "767", "ref3"))

  val additionalReference: List[AdditionalReference] = List(AdditionalReference("adRef1", "4", Some("ref4")))

  val additionalInformation: List[AdditionalInformation] = List(AdditionalInformation("adInf1", "32", Some("additional ref text")))

  val houseConsignment: Seq[HouseConsignment] = Seq(HouseConsignment(Seq(ConsignmentItem(Seq(Packaging(Some(3)))))))

  val consignment: Consignment = Consignment(
    Some("FR"),
    "1",
    Some("2"),
    Some(1000.99),
    Some(consignor),
    Some(consignee),
    Some(transportEquipment),
    Some(departureTransportMeans),
    Some(previousDocument),
    Some(supportingDocument),
    Some(transportDocument),
    Some(additionalReference),
    Some(additionalInformation),
    houseConsignment
  )

  val unloadingPermissionMessageData: UnloadingMessageData =
    UnloadingMessageData(transitOperation, customsOfficeOfDestinationActual, holderOfTheTransitProcedure, Some(consignment))
}
