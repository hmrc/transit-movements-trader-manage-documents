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

package refactor.viewmodels.p4.tad

import generated.p5._
import refactor.viewmodels._
import refactor.viewmodels.p4.tad.Table1ViewModel._
import refactor.viewmodels.p5._

case class Table1ViewModel(
  declarationType: String,
  consignorViewModel: Option[ConsignorViewModel],
  numberOfItems: Int,
  totalNumberOfPackages: String,
  consigneeViewModel: Option[ConsigneeViewModel],
  countryOfDispatch: String,
  countryOfDestination: String,
  transportIdentity: String,
  transportCountry: String,
  returnCopiesCustomsOffice: Option[CustomsOfficeViewModel]
)

object Table1ViewModel {

  case class ConsignorViewModel(
    eori: String,
    name: String,
    streetAndNumber: String,
    postcode: String,
    city: String,
    country: String,
    contactName: String,
    phoneNumber: String,
    emailAddress: String
  )

  object ConsignorViewModel {

    def apply(ie029: CC029CType): Option[ConsignorViewModel] =
      ie029.Consignment.Consignor.map(ConsignorViewModel(_))

    def apply(consignor: ConsignorType03): ConsignorViewModel =
      new ConsignorViewModel(
        eori = consignor.identificationNumber.orElseBlank,
        name = consignor.name.orElseBlank,
        streetAndNumber = consignor.Address.map(_.streetAndNumber).orElseBlank,
        postcode = consignor.Address.flatMap(_.postcode).orElseBlank,
        city = consignor.Address.map(_.city).orElseBlank,
        country = consignor.Address.map(_.country).orElseBlank,
        contactName = consignor.ContactPerson.map(_.name).orElseBlank,
        phoneNumber = consignor.ContactPerson.map(_.phoneNumber).orElseBlank,
        emailAddress = consignor.ContactPerson.map(_.eMailAddress.orElseBlank).orElseBlank
      )
  }

  case class ConsigneeViewModel(
    eori: String,
    name: String,
    streetAndNumber: String,
    postcode: String,
    city: String,
    country: String
  )

  object ConsigneeViewModel {

    def apply(ie029: CC029CType): Option[ConsigneeViewModel] =
      ie029.Consignment.Consignee.map(ConsigneeViewModel(_)) orElse
        ie029.consignmentItems.map(_.Consignee).takeFirstIfAllTheSame.map(ConsigneeViewModel(_))

    def apply(consignee: ConsigneeType03): ConsigneeViewModel =
      new ConsigneeViewModel(
        eori = consignee.identificationNumber.orElseBlank,
        name = consignee.name.orElseBlank,
        streetAndNumber = consignee.Address.map(_.streetAndNumber).orElseBlank,
        postcode = consignee.Address.flatMap(_.postcode).orElseBlank,
        city = consignee.Address.map(_.city).orElseBlank,
        country = consignee.Address.map(_.country).orElseBlank
      )

    def apply(consignee: ConsigneeType04): ConsigneeViewModel =
      new ConsigneeViewModel(
        eori = consignee.identificationNumber.orElseBlank,
        name = consignee.name.orElseBlank,
        streetAndNumber = consignee.Address.map(_.streetAndNumber).orElseBlank,
        postcode = consignee.Address.flatMap(_.postcode).orElseBlank,
        city = consignee.Address.map(_.city).orElseBlank,
        country = consignee.Address.map(_.country).orElseBlank
      )
  }

  case class CustomsOfficeViewModel(
    name: String,
    streetAndNumber: String,
    postcode: String,
    city: String,
    country: String
  )

  def apply(ie029: CC029CType): Table1ViewModel =
    new Table1ViewModel(
      declarationType = ie029.TransitOperation.declarationType,
      consignorViewModel = ConsignorViewModel(ie029),
      numberOfItems = ie029.numberOfItems,
      totalNumberOfPackages = ie029.numberOfPackages.toString(),
      consigneeViewModel = ConsigneeViewModel(ie029),
      countryOfDispatch = ie029.Consignment.countryOfDispatch.orElse3Dashes,       // In P4 we check this against reference data
      countryOfDestination = ie029.Consignment.countryOfDestination.orElse3Dashes, // In P4 we check this against reference data
      transportIdentity = ie029.Consignment.DepartureTransportMeans.map(_.asP4String).toBeContinued("---"),
      transportCountry = ie029.Consignment.DepartureTransportMeans.headOption.map(_.nationality).getOrElse("---"),
      returnCopiesCustomsOffice = None
    )
}
