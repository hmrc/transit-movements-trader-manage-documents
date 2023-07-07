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

import models.P5.departure.ActiveBorderTransportMeans
import models.P5.departure.AdditionalInformation
import models.P5.departure.AdditionalReference
import models.P5.departure.AdditionalSupplyChainActor
import models.P5.departure.Address
import models.P5.departure.Authorisation
import models.P5.departure.Carrier
import models.P5.departure.Commodity
import models.P5.departure.CommodityCode
import models.P5.departure.Consignee
import models.P5.departure.Consignment
import models.P5.departure.ConsignmentItem
import models.P5.departure.Consignor
import models.P5.departure.ContactPerson
import models.P5.departure.CountryOfRoutingOfConsignment
import models.P5.departure.CustomsOffice
import models.P5.departure.CustomsOfficeOfDeparture
import models.P5.departure.CustomsOfficeOfDestinationDeclared
import models.P5.departure.CustomsOfficeOfExitForTransitDeclared
import models.P5.departure.CustomsOfficeOfTransitDeclared
import models.P5.departure.DangerousGoods
import models.P5.departure.DepartureMessageData
import models.P5.departure.DepartureTransportMeans
import models.P5.departure.Document
import models.P5.departure.EconomicOperator
import models.P5.departure.GNSS
import models.P5.departure.GoodsMeasure
import models.P5.departure.GoodsReference
import models.P5.departure.Guarantee
import models.P5.departure.GuaranteeReference
import models.P5.departure.HolderOfTransitProcedure
import models.P5.departure.HouseConsignment
import models.P5.departure.LocationOfGoods
import models.P5.departure.MovementReferenceNumber
import models.P5.departure.Packaging
import models.P5.departure.PlaceOfLoading
import models.P5.departure.PlaceOfUnloading
import models.P5.departure.PostcodeAddress
import models.P5.departure.PreviousDocument
import models.P5.departure.Representative
import models.P5.departure.Seal
import models.P5.departure.SupportingDocument
import models.P5.departure.TransitOperation
import models.P5.departure.TransportCharges
import models.P5.departure.TransportDocument
import models.P5.departure.TransportEquipment
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDateTime

trait SpecBase extends AnyFreeSpec with Matchers with OptionValues with MockitoSugar {}
