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

import generated._
import viewmodels._
import viewmodels.tad.transition.SecurityViewModel._

case class SecurityViewModel(
  security: String,
  modeOfTransportAtTheBorder: String,
  referenceNumberUcr: String,
  carrierIdentificationNumber: String,
  carrierContactPerson: ContactPersonViewModel,
  seals: SealsViewModel,
  locationOfGoods: LocationOfGoodsViewModel,
  countriesOfRoutingOfConsignment: CountriesOfRoutingOfConsignmentViewModel,
  activeBorderTransportMeans: ActiveBorderTransportMeansViewModel,
  conveyanceReferenceNumbers: String,
  placeOfLoading: PlaceOfLoadingViewModel,
  placeOfUnloading: PlaceOfLoadingViewModel,
  transportCharges: String,
  descriptionOfGoods: String,
  dangerousGoods: DangerousGoodsViewModel
)

object SecurityViewModel {

  case class ContactPersonViewModel(
    name: String,
    phoneNumber: String,
    emailAddress: String
  )

  object ContactPersonViewModel {

    def apply(): ContactPersonViewModel =
      new ContactPersonViewModel(
        name = "",
        phoneNumber = "",
        emailAddress = ""
      )

    def apply(contactPerson: ContactPersonType01): ContactPersonViewModel =
      new ContactPersonViewModel(
        name = contactPerson.name,
        phoneNumber = contactPerson.phoneNumber,
        emailAddress = contactPerson.eMailAddress.orElseBlank
      )

    def apply(contactPerson: ContactPersonType02): ContactPersonViewModel =
      new ContactPersonViewModel(
        name = contactPerson.name,
        phoneNumber = contactPerson.phoneNumber,
        emailAddress = contactPerson.eMailAddress.orElseBlank
      )
  }

  case class SealsViewModel(
    sequenceNumbers: String,
    numberOfSeals: String,
    identifiers: String
  )

  object SealsViewModel {

    def apply(seals: Seq[SealType04]): SealsViewModel =
      new SealsViewModel(
        sequenceNumbers = seals.map(_.sequenceNumber.toString).firstAndLast(", "),
        numberOfSeals = seals.length.toString,
        identifiers = seals
          .map(
            x => s"[${x.identifier}]"
          )
          .firstAndLast(", ")
      )
  }

  case class LocationOfGoodsViewModel(
    typeOfLocation: String,
    qualifierOfIdentification: String,
    authorisationNumber: String,
    additionalIdentifier: String,
    unLocode: String,
    customsOffice: String,
    gnssLatitude: String,
    gnssLongitude: String,
    economicOperator: String,
    addressStreetAndNumber: String,
    addressPostcode: String,
    addressCity: String,
    addressCountry: String,
    postcodeAddressHouseNumber: String,
    postcodeAddressPostcode: String,
    postcodeAddressCountry: String,
    contactPerson: ContactPersonViewModel
  )

  object LocationOfGoodsViewModel {

    def apply(log: Option[LocationOfGoodsType02]): LocationOfGoodsViewModel =
      new LocationOfGoodsViewModel(
        typeOfLocation = log.map(_.typeOfLocation).orElseBlank,
        qualifierOfIdentification = log.map(_.qualifierOfIdentification).orElseBlank,
        authorisationNumber = log.flatMap(_.authorisationNumber).orElseBlank,
        additionalIdentifier = log.flatMap(_.additionalIdentifier).orElseBlank,
        unLocode = log.flatMap(_.UNLocode).orElseBlank,
        customsOffice = log.flatMap(_.CustomsOffice).map(_.referenceNumber).orElseBlank,
        gnssLatitude = log.flatMap(_.GNSS.map(_.latitude)).orElseBlank,
        gnssLongitude = log.flatMap(_.GNSS.map(_.longitude)).orElseBlank,
        economicOperator = log.flatMap(_.EconomicOperator.map(_.identificationNumber)).orElseBlank,
        addressStreetAndNumber = log.flatMap(_.Address.map(_.streetAndNumber)).orElseBlank,
        addressPostcode = log.flatMap(_.Address.flatMap(_.postcode)).orElseBlank,
        addressCity = log.flatMap(_.Address.map(_.city)).orElseBlank,
        addressCountry = log.flatMap(_.Address.map(_.country)).orElseBlank,
        postcodeAddressHouseNumber = log.flatMap(_.PostcodeAddress.flatMap(_.houseNumber)).orElseBlank,
        postcodeAddressPostcode = log.flatMap(_.PostcodeAddress.map(_.postcode)).orElseBlank,
        postcodeAddressCountry = log.flatMap(_.PostcodeAddress.map(_.country)).orElseBlank,
        contactPerson = log.flatMap(_.ContactPerson).map(ContactPersonViewModel(_)).getOrElse(ContactPersonViewModel())
      )
  }

  case class CountriesOfRoutingOfConsignmentViewModel(
    sequenceNumbers: String,
    countries: String
  )

  object CountriesOfRoutingOfConsignmentViewModel {

    def apply(countriesOfRoutingOfConsignment: Seq[CountryOfRoutingOfConsignmentType01]): CountriesOfRoutingOfConsignmentViewModel =
      new CountriesOfRoutingOfConsignmentViewModel(
        sequenceNumbers = countriesOfRoutingOfConsignment.map(_.sequenceNumber.toString).toBeContinued(),
        countries = countriesOfRoutingOfConsignment.map(_.country).toBeContinued()
      )
  }

  case class ActiveBorderTransportMeansViewModel(
    sequenceNumbers: String,
    customsOfficeAtBorderReferenceNumbers: String,
    typesOfIdentification: String,
    identificationNumbers: String,
    nationalities: String
  )

  object ActiveBorderTransportMeansViewModel {

    def apply(activeBorderTransportMeans: Seq[CUSTOM_ActiveBorderTransportMeansType01]): ActiveBorderTransportMeansViewModel =
      new ActiveBorderTransportMeansViewModel(
        sequenceNumbers = activeBorderTransportMeans.map(_.sequenceNumber.toString).toBeContinued(),
        customsOfficeAtBorderReferenceNumbers = activeBorderTransportMeans.flatMap(_.customsOfficeAtBorderReferenceNumber).toBeContinued(),
        typesOfIdentification = activeBorderTransportMeans.flatMap(_.typeOfIdentification).toBeContinued(),
        identificationNumbers = activeBorderTransportMeans.flatMap(_.identificationNumber).toBeContinued(),
        nationalities = activeBorderTransportMeans.flatMap(_.nationality).toBeContinued()
      )
  }

  case class PlaceOfLoadingViewModel(
    unLocode: String,
    country: String,
    location: String
  )

  object PlaceOfLoadingViewModel {

    def apply(): PlaceOfLoadingViewModel =
      new PlaceOfLoadingViewModel(
        unLocode = "",
        country = "",
        location = ""
      )

    def apply(placeOfLoading: PlaceOfLoadingType02): PlaceOfLoadingViewModel =
      new PlaceOfLoadingViewModel(
        unLocode = placeOfLoading.UNLocode.orElseBlank,
        country = placeOfLoading.country.orElseBlank,
        location = placeOfLoading.location.orElseBlank
      )

    def apply(placeOfUnloading: PlaceOfUnloadingType02): PlaceOfLoadingViewModel =
      new PlaceOfLoadingViewModel(
        unLocode = placeOfUnloading.UNLocode.orElseBlank,
        country = placeOfUnloading.country.orElseBlank,
        location = placeOfUnloading.location.orElseBlank
      )
  }

  case class DangerousGoodsViewModel(
    sequenceNumbers: String,
    unNumbers: String
  )

  object DangerousGoodsViewModel {

    def apply(dangerousGoods: Seq[DangerousGoodsType01]): DangerousGoodsViewModel =
      new DangerousGoodsViewModel(
        sequenceNumbers = dangerousGoods.map(_.sequenceNumber.toString).toBeContinued(),
        unNumbers = dangerousGoods.map(_.UNNumber).toBeContinued()
      )
  }

  def apply(ie029: CC029CType): SecurityViewModel =
    new SecurityViewModel(
      security = ie029.TransitOperation.security,
      modeOfTransportAtTheBorder = ie029.Consignment.modeOfTransportAtTheBorder.orElseBlank,
      referenceNumberUcr = ie029.Consignment.referenceNumberUCR.orElseBlank,
      carrierIdentificationNumber = ie029.Consignment.Carrier.map(_.identificationNumber).orElseBlank,
      carrierContactPerson = ie029.Consignment.Carrier.flatMap(_.ContactPerson).map(ContactPersonViewModel(_)).getOrElse(ContactPersonViewModel()),
      seals = SealsViewModel(ie029.Consignment.TransportEquipment.flatMap(_.Seal)),
      locationOfGoods = LocationOfGoodsViewModel(ie029.Consignment.LocationOfGoods),
      countriesOfRoutingOfConsignment = CountriesOfRoutingOfConsignmentViewModel(ie029.Consignment.CountryOfRoutingOfConsignment),
      activeBorderTransportMeans = ActiveBorderTransportMeansViewModel(ie029.Consignment.ActiveBorderTransportMeans),
      conveyanceReferenceNumbers = ie029.Consignment.ActiveBorderTransportMeans.flatMap(_.conveyanceReferenceNumber).toBeContinued(),
      placeOfLoading = ie029.Consignment.PlaceOfLoading.map(PlaceOfLoadingViewModel(_)).getOrElse(PlaceOfLoadingViewModel()),
      placeOfUnloading = ie029.Consignment.PlaceOfUnloading.map(PlaceOfLoadingViewModel(_)).getOrElse(PlaceOfLoadingViewModel()),
      transportCharges = ie029.consignmentItems.flatMap(_.TransportCharges).map(_.methodOfPayment).toBeContinued(),
      descriptionOfGoods = ie029.consignmentItems.map(_.Commodity.descriptionOfGoods).toBeContinued(),
      dangerousGoods = DangerousGoodsViewModel(ie029.consignmentItems.flatMap(_.Commodity.DangerousGoods))
    )
}
