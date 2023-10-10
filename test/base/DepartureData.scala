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
import models.P5.departure._
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate
import java.time.LocalDateTime

trait DepartureData {

  val messageId                    = "6445005176e4e834"
  val departureId                  = "ID1"
  val mrn: MovementReferenceNumber = MovementReferenceNumber("MRN")
  implicit val hc: HeaderCarrier   = HeaderCarrier()

  val ie029TransitOperation: IE029TransitOperation =
    IE029TransitOperation(
      "LRN",
      "MRN",
      T1,
      "T2F",
      Some("TIR"),
      LocalDate.of(2020, 1, 1),
      LocalDate.of(2020, 1, 1),
      "security",
      "false",
      Some("SCI"),
      Some("english"),
      "1"
    )

  val ie015TransitOperation: IE015TransitOperation =
    IE015TransitOperation(
      Some("2010-11-15")
    )

  val address: Address             = Address("Address Line 1", Some("Address Line 2"), "Address Line 3", "GB")
  val contactPerson: ContactPerson = ContactPerson("name", "123456", Some("a@a.com"))

  val holderOfTheTransitProcedure: HolderOfTransitProcedure =
    HolderOfTransitProcedure(Some("id1"), Some("TIRID1"), Some("Bob"), Some(address), Some(contactPerson))

  val representative: Representative = Representative("ID1", "Status-1", Some(contactPerson))

  val controlResult = ControlResult("code", LocalDate.of(2020, 1, 1), "controlled", Some("text"))

  val consignor: Consignor = Consignor(Some("idnum1"), Some("Consignor Name"), Some(address), Some(contactPerson))
  val consignee: Consignee = Consignee(Some("idnum1"), Some("Consignee Name"), Some(address))
  val carrier: Carrier     = Carrier("idnum1", Some(contactPerson))

  val authorisation: Authorisation                                                 = Authorisation("SEQNum-1", "Auth-Type", "Reference-Numb-1")
  val customsOfficeOfTransitDeclared: CustomsOfficeOfTransitDeclared               = CustomsOfficeOfTransitDeclared("seq001", "AD000002", Some(LocalDateTime.MIN))
  val customsOfficeOfExitForTransitDeclared: CustomsOfficeOfExitForTransitDeclared = CustomsOfficeOfExitForTransitDeclared("seq001", "AD000002")
  val customsOfficeOfDeparture: CustomsOfficeOfDeparture                           = CustomsOfficeOfDeparture("AD000002")
  val customsOfficeOfDestinationDeclared: CustomsOfficeOfDestinationDeclared       = CustomsOfficeOfDestinationDeclared("AT240000")
  val guaranteeReference: GuaranteeReference                                       = GuaranteeReference("SEQNum-1", Some("GRN-1"), Some("Access-code-1"), 123456.1212, "GBP")
  val guarantee: Guarantee                                                         = Guarantee("SEQNum-1", "G", Some("otherGuaranteeReference"), Some(List(guaranteeReference)))
  val postcodeAddress: PostcodeAddress                                             = PostcodeAddress(Some("house1"), "BR", "UK")
  val economicOperator: EconomicOperator                                           = EconomicOperator("EconomicOperator-1")
  val customsOffice: CustomsOffice                                                 = CustomsOffice("Reference1")
  val gnss: GNSS                                                                   = GNSS("1232", "1234")
  val seal: Seal                                                                   = Seal("1232", "ID10012")
  val seals: List[Seal]                                                            = List(Seal("1232", "ID10012"), Seal("456", "ID10045"))
  val goodsReference: GoodsReference                                               = GoodsReference("1232", 3)
  val goodsReferences: List[GoodsReference]                                        = List(GoodsReference("1234", 1234), GoodsReference("4567", 4567))

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
  val placeOfLoading: PlaceOfLoading     = PlaceOfLoading(Some("LoCoCode-1"), Some("GB"), Some("L1"))
  val placeOfUnLoading: PlaceOfUnloading = PlaceOfUnloading(Some("UnLoCoCode-1"), Some("GB"), Some("L1"))

  val previousDocumentConsignment: PreviousDocument =
    PreviousDocument("Document-1", "Type-1", "Reference-1", Some("C1"), None, None, None, None, None)
  val transportDocumentConsignment: TransportDocument         = TransportDocument("Document-1", "Type-1", "Reference-1")
  val supportingDocumentConsignment: SupportingDocument       = SupportingDocument("Document-1", "Type-1", "Reference-1", Some(5), Some("C1"))
  val additionalReferenceConsignment: AdditionalReference     = AdditionalReference("Document-1", "Type-1", Some("Reference-1"))
  val additionalInformationConsignment: AdditionalInformation = AdditionalInformation("Document-1", "Type-1", Some("Reference-1"))

  val previousDocumentItem: PreviousDocument =
    PreviousDocument("Document-2", "Type-2", "Reference-2", Some("C1"), Some(3), Some("type"), Some(5), Some("measurement"), Some(5.0))
  val transportDocumentItem: TransportDocument                     = TransportDocument("Document-2", "Type-2", "Reference-2")
  val supportingDocumentItem: SupportingDocument                   = SupportingDocument("Document-2", "Type-2", "Reference-2", Some(5), Some("C1"))
  val additionalReferenceItem: AdditionalReference                 = AdditionalReference("Document-2", "Type-2", Some("Reference-2"))
  val additionalInformationItem: AdditionalInformation             = AdditionalInformation("Document-2", "Type-2", Some("Reference-2"))
  val countryOfRoutingOfConsignment: CountryOfRoutingOfConsignment = CountryOfRoutingOfConsignment("Seqnum12243", "GB")
  val packaging: Packaging                                         = Packaging("1", Some(3), "Rubber", Some("RubberMark"))
  val commodityCode: CommodityCode                                 = CommodityCode("SHC1", Some("NOMC1"))
  val goodsMeasure: GoodsMeasure                                   = GoodsMeasure(1.2, Some(1.4))
  val transportCharges: TransportCharges                           = TransportCharges("payPal")
  val dangerousGoods: DangerousGoods                               = DangerousGoods("seq1", "UNNumber1")
  val commodity: Commodity                                         = Commodity("Tiles", Some("CUSCode"), Some(commodityCode), Some(List(dangerousGoods)), Some(goodsMeasure))

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
    Some(List(previousDocumentItem)),
    Some(List(supportingDocumentItem)),
    Some(List(transportDocumentItem)),
    Some(List(additionalReferenceItem)),
    Some(List(additionalInformationItem)),
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
    Some(List(previousDocumentItem)),
    Some(List(supportingDocumentItem)),
    Some(List(transportDocumentItem)),
    Some(List(additionalReferenceItem)),
    Some(List(additionalInformationItem)),
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
    Some(List(previousDocumentItem)),
    Some(List(transportDocumentItem)),
    Some(List(supportingDocumentItem)),
    Some(List(additionalReferenceItem)),
    Some(List(additionalInformationItem)),
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
    Some(List(previousDocumentConsignment)),
    Some(List(transportDocumentConsignment)),
    Some(List(supportingDocumentConsignment)),
    Some(List(additionalReferenceConsignment)),
    Some(List(additionalInformationConsignment)),
    Some(transportCharges),
    Seq(houseConsignment)
  )

  val consignmentWithMultipleSeals = consigmment.copy(TransportEquipment = Some(List(transportEquipments)))

  val ie029MessageData: IE029MessageData =
    IE029MessageData(
      ie029TransitOperation,
      Some(List(authorisation)),
      customsOfficeOfDeparture,
      customsOfficeOfDestinationDeclared,
      Some(List(customsOfficeOfTransitDeclared)),
      Some(List(customsOfficeOfExitForTransitDeclared)),
      holderOfTheTransitProcedure,
      Some(representative),
      Some(controlResult),
      List(guarantee),
      consigmment
    )

  val ie015MessageData: IE015MessageData =
    IE015MessageData(
      ie015TransitOperation
    )
}
