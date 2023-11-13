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
import refactor.viewmodels.p4.tad.Table4ViewModel._
import refactor.viewmodels.p5._

case class Table4ViewModel(
  guaranteeViewModels: Seq[GuaranteeViewModel],
  principal: PrincipalViewModel,
  departureOffice: String,
  acceptanceDate: String,
  transitOffices: Seq[TransitOfficeViewModel],
  destinationOffice: Option[TransitOfficeViewModel],
  authId: Option[String],
  isSimplifiedMovement: Boolean
)

object Table4ViewModel {

  case class GuaranteeViewModel(
    typeValue: String,
    referenceNumbers: Seq[String],
    otherReferenceNumber: String
  ) {

    val (ref, otherRef) = (typeValue, referenceNumbers) match {
      case ("4", (head :: _) :+ last) => (s"$head - ${last.takeRight(6)}", "")
      case (_, (head :: _) :+ _)      => (s"$head ***", s"$otherReferenceNumber ***")
      case (_, head :: _)             => (head, otherReferenceNumber)
      case _                          => ("", "")
    }
  }

  object GuaranteeViewModel {

    def apply(guarantee: GuaranteeType03): GuaranteeViewModel =
      new GuaranteeViewModel(
        typeValue = guarantee.guaranteeType,
        referenceNumbers = guarantee.GuaranteeReference.flatMap(_.GRN),
        otherReferenceNumber = guarantee.otherGuaranteeReference.orElseBlank
      )
  }

  case class PrincipalViewModel(
    name: String,
    streetAndNumber: String,
    postCode: String,
    city: String,
    country: String,
    eori: Option[String],
    tir: Option[String]
  ) {

    override def toString: String = (eori, tir) match {
      case (Some(eori), Some(tir)) => s"$eori / $tir"
      case (Some(eori), _)         => eori
      case _                       => ""
    }
  }

  object PrincipalViewModel {

    def apply(holderOfTheTransitProcedure: HolderOfTheTransitProcedureType05): PrincipalViewModel =
      new PrincipalViewModel(
        name = holderOfTheTransitProcedure.name.orElseBlank,
        streetAndNumber = holderOfTheTransitProcedure.Address.map(_.streetAndNumber).orElseBlank.takeN(32, "***"),
        postCode = holderOfTheTransitProcedure.Address.flatMap(_.postcode).orElseBlank,
        city = holderOfTheTransitProcedure.Address.map(_.city).orElseBlank,
        country = holderOfTheTransitProcedure.Address.map(_.country).orElseBlank,
        eori = holderOfTheTransitProcedure.identificationNumber,
        tir = holderOfTheTransitProcedure.TIRHolderIdentificationNumber
      )
  }

  case class TransitOfficeViewModel(
    reference: String,
    dateTime: Option[String]
  )

  object TransitOfficeViewModel {

    def apply(customsOffice: CustomsOfficeOfDestinationDeclaredType01): TransitOfficeViewModel =
      new TransitOfficeViewModel(
        reference = customsOffice.referenceNumber,
        dateTime = None
      )

    def apply(customsOffice: CustomsOfficeOfTransitDeclaredType04): TransitOfficeViewModel =
      new TransitOfficeViewModel(
        reference = customsOffice.referenceNumber,
        dateTime = customsOffice.arrivalDateAndTimeEstimated.map(_.dateAndTimeString)
      )
  }

  def apply(ie029: CC029CType): Table4ViewModel =
    new Table4ViewModel(
      guaranteeViewModels = ie029.Guarantee.map(GuaranteeViewModel(_)),
      principal = PrincipalViewModel(ie029.HolderOfTheTransitProcedure),
      departureOffice = ie029.CustomsOfficeOfDeparture.asString,
      acceptanceDate = ie029.TransitOperation.declarationAcceptanceDate.dateString,
      transitOffices = ie029.CustomsOfficeOfTransitDeclared.map(TransitOfficeViewModel(_)),
      destinationOffice = Some(TransitOfficeViewModel(ie029.CustomsOfficeOfDestinationDeclared)),
      authId = None,
      isSimplifiedMovement = ie029.Authorisation.exists(_.typeValue == "C521")
    )
}
