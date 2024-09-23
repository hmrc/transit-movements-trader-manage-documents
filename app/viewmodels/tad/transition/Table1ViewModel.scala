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

package viewmodels.tad.transition

import generated.rfc37._
import viewmodels._
import viewmodels.tad.transition.Table1ViewModel._

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
        eori = consignor.identificationNumber.orElse3Dashes,
        name = consignor.name.orElse3Dashes,
        streetAndNumber = consignor.Address.map(_.streetAndNumber).orElse3Dashes,
        postcode = consignor.Address.flatMap(_.postcode).orElse3Dashes,
        city = consignor.Address.map(_.city).orElse3Dashes,
        country = consignor.Address.map(_.country).orElse3Dashes,
        contactName = consignor.ContactPerson.map(_.name).orElse3Dashes,
        phoneNumber = consignor.ContactPerson.map(_.phoneNumber).orElse3Dashes,
        emailAddress = consignor.ContactPerson.map(_.eMailAddress.orElse3Dashes).orElse3Dashes
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
        eori = consignee.identificationNumber.orElse3Dashes,
        name = consignee.name.orElse3Dashes,
        streetAndNumber = consignee.Address.map(_.streetAndNumber).orElse3Dashes,
        postcode = consignee.Address.flatMap(_.postcode).orElse3Dashes,
        city = consignee.Address.map(_.city).orElse3Dashes,
        country = consignee.Address.map(_.country).orElse3Dashes
      )

    def apply(consignee: ConsigneeType04): ConsigneeViewModel =
      new ConsigneeViewModel(
        eori = consignee.identificationNumber.orElse3Dashes,
        name = consignee.name.orElse3Dashes,
        streetAndNumber = consignee.Address.map(_.streetAndNumber).orElse3Dashes,
        postcode = consignee.Address.flatMap(_.postcode).orElse3Dashes,
        city = consignee.Address.map(_.city).orElse3Dashes,
        country = consignee.Address.map(_.country).orElse3Dashes
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
      countryOfDispatch = ie029.Consignment.countryOfDispatch.orElse3Dashes, // In P4 we check this against reference data
      countryOfDestination = ie029.Consignment.countryOfDestination.orElse3Dashes, // In P4 we check this against reference data
      transportIdentity = ie029.Consignment.DepartureTransportMeans.map(_.asP4String).toBeContinued("---"),
      transportCountry = ie029.Consignment.DepartureTransportMeans.headOption.flatMap(_.nationality).getOrElse("---"),
      returnCopiesCustomsOffice = None
    )
}
