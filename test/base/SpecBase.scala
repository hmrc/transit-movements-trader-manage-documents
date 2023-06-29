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

import models.P5.unloading._
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

  val seals: List[Seal] = List(Seal("1", "seal1"), Seal("2", "seal2"))

  val goodsReference: List[GoodsReference] = List(GoodsReference("seq1", 5))

  val transportEquipment: List[TransportEquipment] =
    List(TransportEquipment(Some("cin-2"), 35, Some(seals), Some(goodsReference)))

  val departureTransportMeans: List[DepartureTransportMeans] =
    List(DepartureTransportMeans("type1", "id1", "NG"))

  val previousDocument: List[PreviousDocument] = List(PreviousDocument("768", "ref1", None, Some("55")))

  val supportingDocument: List[SupportingDocument] = List(SupportingDocument("764", "ref2", Some("45")))

  val transportDocument: List[TransportDocument] = List(TransportDocument("767", "ref3"))

  val additionalReference: List[AdditionalReference] = List(AdditionalReference("4", Some("ref4")))

  val additionalInformation: List[AdditionalInformation] = List(AdditionalInformation("32", Some("additional ref text")))

  val goodsMeasure: GoodsMeasure = GoodsMeasure(10.5, None)
  val commodity: Commodity       = Commodity("commodity desc", None, None, None, goodsMeasure)

  val houseConsignment: Seq[HouseConsignment] = Seq(
    HouseConsignment(Seq(ConsignmentItem("1", 1, None, None, None, commodity, Seq(Packaging("package type", Some(3), None)), None, None, None, None, None)))
  )

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
