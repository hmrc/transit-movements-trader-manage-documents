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

import models.P5.departure.TransportEquipment
import models.P5.departure._
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDateTime

trait DepartureData {

  val messageId                    = "6445005176e4e834"
  val departureId                  = "ID1"
  val mrn: MovementReferenceNumber = MovementReferenceNumber("MRN")
  implicit val hc: HeaderCarrier   = HeaderCarrier()

  val transitOperation: TransitOperation =
    TransitOperation("MRN", "LRN", "T1", "T2F", "sec", Some("TIR"), Some("SCI"))

  val address: Address             = Address("Address Line 1", Some("Address Line 2"), "Address Line 3", "Address Line 4")
  val contactPerson: ContactPerson = ContactPerson("Contact Person Name", "123456", Some("a@a.com"))

  val holderOfTheTransitProcedure: HolderOfTransitProcedure =
    HolderOfTransitProcedure(Some("id1"), Some("TIRID1"), Some("Bob"), Some(address), Some(contactPerson))

  val representative: Representative = Representative(Some("ID1"), Some("Status-1"), Some(contactPerson))

  val consignor = Consignor(Some("idnum1"), Some("Consignor Name"), Some(address), Some(contactPerson))
  val consignee = Consignee(Some("idnum1"), Some("Consignee Name"), Some(address), Some(contactPerson))
  val carrier   = Carrier("idnum1", Some(contactPerson))

  val authorisation: Authorisation                                                 = Authorisation(Some("SEQNum-1"), Some("Auth-Type"), Some("Reference-Numb-1"))
  val customsOfficeOfTransitDeclared                                               = CustomsOfficeOfTransitDeclared(Some("seq001"), Some("ref001"), Some(LocalDateTime.MIN))
  val customsOfficeOfExitForTransitDeclared: CustomsOfficeOfExitForTransitDeclared = CustomsOfficeOfExitForTransitDeclared(Some("seq001"), Some("ref001"))
  val customsOfficeOfDeparture                                                     = CustomsOfficeOfDeparture(Some("Ref001"))
  val customsOfficeOfDestinationDeclared                                           = CustomsOfficeOfDestinationDeclared(Some("Ref001"))
  val guaranteeReference: GuaranteeReference                                       = GuaranteeReference(Some("SEQNum-1"), Some("GRN-1"), Some("Access-code-1"), Some(123456.1212), Some("GBP"))
  val guarantee: Guarantee                                                         = Guarantee(Some("SEQNum-1"), Some("SomeGuaranteeType"), Some("otherGuaranteeReference"), Some(List(guaranteeReference)))
  val postcodeAddress                                                              = PostcodeAddress(Some("house1"), "BR", "UK")
  val economicOperator                                                             = EconomicOperator("EconomicOperator-1")
  val customsOffice                                                                = CustomsOffice("Reference1")
  val gnss                                                                         = GNSS("1232", "1234")
  val seal                                                                         = Seal(Some("1232"), Some("ID10012"))
  val seals                                                                        = List(Seal(Some("1232"), Some("ID10012")), Seal(Some("456"), Some("ID10045")))
  val goodsReference                                                               = GoodsReference(Some("1232"), Some(1234))
  val goodsReferences                                                              = List(GoodsReference(Some("1234"), Some(1234)), GoodsReference(Some("4567"), Some(4567)))

  val locationOfGoods = LocationOfGoods(
    "Warehouse",
    "qualifierIdentifier-num-1",
    Some("1212"),
    Some("ID0001"),
    Some("LNCODE1"),
    Some(customsOffice),
    Some(gnss),
    Some(economicOperator),
    Some(address),
    Some(postcodeAddress),
    Some(contactPerson)
  )

  val additionalSupplyChainActor    = AdditionalSupplyChainActor("Actor-Role", "ID001")
  val departureTransportMeans       = DepartureTransportMeans("Actor-Role", "ID001", Some("Nationality"))
  val transportEquipment            = TransportEquipment("12123", Some("Container-ID-1"), 8, Some(List(seal)), Some(List(goodsReference)))
  val transportEquipments           = TransportEquipment("12123", Some("Container-ID-1"), 8, Some(seals), Some(goodsReferences))
  val activeBorderTransportMeans    = ActiveBorderTransportMeans("GB0001", "T1", "ID001", "GB", Some("conveyReferenceNumber-1"))
  val placeOfLoading                = PlaceOfLoading(Some("LoCoCode-1"), Some("GB"), Some("L1"))
  val placeOfUnLoading              = PlaceOfUnloading(Some("UnLoCoCode-1"), Some("GB"), Some("L1"))
  val previousDocument              = PreviousDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"), Some("C1"), None, None, None, None, None)
  val transportDocument             = TransportDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val supportingDocument            = SupportingDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"), Some(5), Some("C1"))
  val additionalReference           = AdditionalReference(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val additionalInformation         = AdditionalInformation(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val countryOfRoutingOfConsignment = CountryOfRoutingOfConsignment(Some("Seqnum12243"), Some("GB"))
  val packaging                     = Packaging(Some(5), "Plastic", Some("rubberStamp"))
  val commodityCode                 = CommodityCode("SHC1", Some("NOMC1"))
  val goodsMeasure                  = GoodsMeasure(1.2, Some(1.4))
  val transportCharges              = TransportCharges(Some("payPal"))
  val dangerousGoods                = DangerousGoods("seq1", Some("UNNumber1"))
  val commodity                     = Commodity("Tiles", Some(commodityCode), goodsMeasure, Some("shippingMark1"), Some("CUSTCODE1"), List(dangerousGoods))

  val consignmentItem1 = ConsignmentItem(
    Some("1T1"),
    Some("GER1"),
    Some("GB1"),
    Some(consignor),
    Some(consignee),
    "123545",
    99991,
    Seq(packaging),
    commodity,
    Some("ref1"),
    Some(transportCharges),
    Some(List(previousDocument)),
    Some(List(supportingDocument)),
    Some(List(transportDocument)),
    Some(List(additionalReference)),
    Some(List(additionalInformation)),
    Some(List(additionalSupplyChainActor)),
    Some(List(departureTransportMeans))
  )

  val consignmentItem2 = ConsignmentItem(
    Some("2T1"),
    Some("GER2"),
    Some("GB2"),
    Some(consignor),
    Some(consignee),
    "23545",
    99992,
    Seq(packaging),
    commodity,
    Some("ref2"),
    Some(transportCharges),
    Some(List(previousDocument)),
    Some(List(supportingDocument)),
    Some(List(transportDocument)),
    Some(List(additionalReference)),
    Some(List(additionalInformation)),
    Some(List(additionalSupplyChainActor)),
    Some(List(departureTransportMeans))
  )

  val houseConsignment = HouseConsignment(
    Seq(consignmentItem1, consignmentItem2),
    Some(consignor),
    Some(consignee),
    Some(List(previousDocument)),
    Some(List(transportDocument)),
    Some(List(supportingDocument)),
    Some(List(additionalInformation)),
    Some(List(additionalReference)),
    Some(transportCharges),
    1.453,
    Some("GER"),
    Some("UCR")
  )

  val consigmment: Consignment = Consignment(
    1.0,
    Some("T1"),
    Some("Road"),
    Some("GER"),
    Some("GB"),
    Some("UCR001"),
    Some(consignor),
    Some(consignee),
    Some(carrier),
    Document(Some(List(previousDocument)), Some(List(transportDocument)), Some(List(supportingDocument))),
    Some(locationOfGoods),
    Some(List(additionalSupplyChainActor)),
    Some(List(departureTransportMeans)),
    Some(List(transportEquipment)),
    Some(List(activeBorderTransportMeans)),
    Some(placeOfLoading),
    Some(placeOfUnLoading),
    Seq(houseConsignment),
    Some(List(additionalInformation)),
    Some(List(additionalReference)),
    Some(TransportCharges(Some("payPal"))),
    Some(List(countryOfRoutingOfConsignment))
  )

  val consignmentWithMultipleSeals = consigmment.copy(TransportEquipment = Some(List(transportEquipments)))

  val departureMessageData: DepartureMessageData =
    DepartureMessageData(
      transitOperation,
      holderOfTheTransitProcedure,
      representative,
      consigmment,
      Some(List(guarantee)),
      Some(List(authorisation)),
      Some(List(customsOfficeOfTransitDeclared)),
      Some(List(customsOfficeOfExitForTransitDeclared)),
      customsOfficeOfDeparture,
      customsOfficeOfDestinationDeclared
    )

}
