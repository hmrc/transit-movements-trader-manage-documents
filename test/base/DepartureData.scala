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

import models.DeclarationType.T1
import models.P5.departure.TransportEquipment
import models.P5.departure._
import uk.gov.hmrc.http.HeaderCarrier
import utils.FormattedDate

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

trait DepartureData {

  val messageId                    = "6445005176e4e834"
  val departureId                  = "ID1"
  val mrn: MovementReferenceNumber = MovementReferenceNumber("MRN")
  implicit val hc: HeaderCarrier   = HeaderCarrier()

  val transitOperation: TransitOperation =
    TransitOperation(
      "LRN",
      "MRN",
      T1,
      "T2F",
      Some("TIR"),
      Some(LocalDate.of(2020, 1, 1)),
      LocalDate.of(2020, 1, 1),
      "security",
      "false",
      Some("SCI"),
      Some("english"),
      "1"
    )

  val address: Address             = Address("Address Line 1", Some("Address Line 2"), "Address Line 3", "GB")
  val contactPerson: ContactPerson = ContactPerson("name", "123456", Some("a@a.com"))

  val holderOfTheTransitProcedure: HolderOfTransitProcedure =
    HolderOfTransitProcedure(Some("id1"), Some("TIRID1"), Some("Bob"), Some(address), Some(contactPerson))

  val representative: Representative = Representative(Some("ID1"), Some("Status-1"), Some(contactPerson))

  val controlResult = ControlResult("code", LocalDate.of(2020, 1, 1), Some("controlled"), Some("text"))

  val consignor: Consignor = Consignor(Some("idnum1"), Some("Consignor Name"), Some(address), Some(contactPerson))
  val consignee: Consignee = Consignee(Some("idnum1"), Some("Consignee Name"), Some(address))
  val carrier: Carrier     = Carrier("idnum1", Some(contactPerson))

  val authorisation: Authorisation                                                 = Authorisation(Some("SEQNum-1"), Some("Auth-Type"), Some("Reference-Numb-1"))
  val customsOfficeOfTransitDeclared: CustomsOfficeOfTransitDeclared               = CustomsOfficeOfTransitDeclared(Some("seq001"), Some("AD000002"), Some(LocalDateTime.MIN))
  val customsOfficeOfExitForTransitDeclared: CustomsOfficeOfExitForTransitDeclared = CustomsOfficeOfExitForTransitDeclared(Some("seq001"), Some("AD000002"))
  val customsOfficeOfDeparture: CustomsOfficeOfDeparture                           = CustomsOfficeOfDeparture("AD000002")
  val customsOfficeOfDestinationDeclared: CustomsOfficeOfDestinationDeclared       = CustomsOfficeOfDestinationDeclared("AT240000")
  val guaranteeReference: GuaranteeReference                                       = GuaranteeReference(Some("SEQNum-1"), Some("GRN-1"), Some("Access-code-1"), Some(123456.1212), Some("GBP"))
  val guarantee: Guarantee                                                         = Guarantee("SEQNum-1", Some("SomeGuaranteeType"), Some("otherGuaranteeReference"), Some(List(guaranteeReference)))
  val postcodeAddress: PostcodeAddress                                             = PostcodeAddress(Some("house1"), "BR", "UK")
  val economicOperator: EconomicOperator                                           = EconomicOperator("EconomicOperator-1")
  val customsOffice: CustomsOffice                                                 = CustomsOffice("Reference1")
  val gnss: GNSS                                                                   = GNSS("1232", "1234")
  val seal: Seal                                                                   = Seal("1232", "ID10012")
  val seals: List[Seal]                                                            = List(Seal("1232", "ID10012"), Seal("456", "ID10045"))
  val goodsReference: GoodsReference                                               = GoodsReference(Some("1232"), Some(1234))
  val goodsReferences: List[GoodsReference]                                        = List(GoodsReference(Some("1234"), Some(1234)), GoodsReference(Some("4567"), Some(4567)))

  val locationOfGoods: LocationOfGoods = LocationOfGoods(
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

  val additionalSupplyChainActor: AdditionalSupplyChainActor = AdditionalSupplyChainActor("Actor-Role", "ID001", "ID33")
  val departureTransportMeans                                = DepartureTransportMeans("Actor-Role", "ID001", "TYPE01", "GB")
  val transportEquipment: TransportEquipment                 = TransportEquipment("12123", Some("Container-ID-1"), 8, Some(List(seal)), Some(List(goodsReference)))
  val transportEquipments                                    = TransportEquipment("12123", Some("Container-ID-1"), 8, Some(seals), Some(goodsReferences))

  val activeBorderTransportMeans: ActiveBorderTransportMeans =
    ActiveBorderTransportMeans("GB0001", "T1", "ID001", "GB", "nationality", Some("conveyReferenceNumber-1"))
  val placeOfLoading: PlaceOfLoading                               = PlaceOfLoading(Some("LoCoCode-1"), Some("GB"), Some("L1"))
  val placeOfUnLoading: PlaceOfUnloading                           = PlaceOfUnloading(Some("UnLoCoCode-1"), Some("GB"), Some("L1"))
  val previousDocument: PreviousDocument                           = PreviousDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"), Some("C1"))
  val transportDocument: TransportDocument                         = TransportDocument(Some("Document-2"), Some("Type-2"), Some("Reference-2"))
  val supportingDocument: SupportingDocument                       = SupportingDocument(Some("Document-1"), Some("Type-1"), Some("Reference-1"), Some(5), Some("C1"))
  val additionalReference: AdditionalReference                     = AdditionalReference(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val additionalInformation: AdditionalInformation                 = AdditionalInformation(Some("Document-1"), Some("Type-1"), Some("Reference-1"))
  val countryOfRoutingOfConsignment: CountryOfRoutingOfConsignment = CountryOfRoutingOfConsignment(Some("Seqnum12243"), Some("GB"))
  val packaging: Packaging                                         = Packaging("1", Some(3), "Rubber", Some("RubberMark"))
  val commodityCode: CommodityCode                                 = CommodityCode("SHC1", Some("NOMC1"))
  val goodsMeasure: GoodsMeasure                                   = GoodsMeasure(1.2, Some(1.4))
  val transportCharges: TransportCharges                           = TransportCharges(Some("payPal"))
  val dangerousGoods: DangerousGoods                               = DangerousGoods("seq1", Some("UNNumber1"))
  val commodity: Commodity                                         = Commodity("Tiles", Some("CUSCode"), Some(commodityCode), Some(List(dangerousGoods)), goodsMeasure)

  val consignmentItem1: ConsignmentItem = ConsignmentItem(
    "1T1",
    1,
    Some("T1"),
    Some("GB"),
    Some("GE"),
    Some("refUCR"),
    Some(consignee),
    Some(List(additionalSupplyChainActor)),
    commodity,
    Seq(packaging),
    Some(List(previousDocument)),
    Some(List(supportingDocument)),
    Some(List(transportDocument)),
    Some(List(additionalReference)),
    Some(List(additionalInformation)),
    Some(transportCharges)
  )

  val consignmentItem2: ConsignmentItem = ConsignmentItem(
    "1T2",
    2,
    Some("T1"),
    Some("GB2"),
    Some("GE2"),
    Some("refUCR2"),
    Some(consignee),
    Some(List(additionalSupplyChainActor)),
    commodity,
    Seq(packaging),
    Some(List(previousDocument)),
    Some(List(supportingDocument)),
    Some(List(transportDocument)),
    Some(List(additionalReference)),
    Some(List(additionalInformation)),
    Some(transportCharges)
  )

  val houseConsignment: HouseConsignment = HouseConsignment(
    "1",
    Some("GB"),
    20.0,
    Some("UCRRef"),
    Some("SecurityIndicator"),
    Some(consignor),
    Some(consignee),
    Some(List(additionalSupplyChainActor)),
    Some(List(departureTransportMeans)),
    Some(List(previousDocument)),
    Some(List(transportDocument)),
    Some(List(supportingDocument)),
    Some(List(additionalReference)),
    Some(List(additionalInformation)),
    Some(transportCharges),
    Seq(consignmentItem1)
  )

  val consigmment: Consignment = Consignment(
    Some("GB"),
    Some("GE"),
    "ContainerIndicator",
    Some("Road"),
    Some("Boat"),
    52.02,
    Some("UCRRefNumber"),
    Some(carrier),
    Some(consignor),
    Some(consignee),
    Some(List(additionalSupplyChainActor)),
    Some(List(transportEquipment)),
    Some(locationOfGoods),
    Some(List(departureTransportMeans)),
    Some(List(countryOfRoutingOfConsignment)),
    Some(List(activeBorderTransportMeans)),
    Some(placeOfLoading),
    Some(placeOfUnLoading),
    Some(List(previousDocument)),
    Some(List(transportDocument)),
    Some(List(supportingDocument)),
    Some(List(additionalReference)),
    Some(List(additionalInformation)),
    Some(transportCharges),
    Seq(houseConsignment)
  )

  val consignmentWithMultipleSeals = consigmment.copy(TransportEquipment = Some(List(transportEquipments)))

  val departureMessageData: DepartureMessageData =
    DepartureMessageData(
      transitOperation,
      Some(List(authorisation)),
      customsOfficeOfDeparture,
      customsOfficeOfDestinationDeclared,
      Some(List(customsOfficeOfTransitDeclared)),
      Some(List(customsOfficeOfExitForTransitDeclared)),
      holderOfTheTransitProcedure,
      representative,
      controlResult,
      Some(List(guarantee)),
      consigmment
    )
}
