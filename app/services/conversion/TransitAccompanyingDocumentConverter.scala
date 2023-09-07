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

package services.conversion

import cats.data.NonEmptyList
import cats.implicits._
import models.P5.departure.DepartureMessageData
import models.P5.departure.IE029Data
import models._
import models.reference._
import services._
import utils.FormattedDate
import viewmodels.CustomsOfficeWithOptionalDate

import java.time.LocalDate

object TransitAccompanyingDocumentConverter extends Converter with ConversionHelpers {

  def toViewModel(
    transitAccompanyingDocument: models.ReleaseForTransit,
    countries: Seq[Country],
    additionalInfo: Seq[reference.AdditionalInformation],
    kindsOfPackage: Seq[KindOfPackage],
    documentTypes: Seq[DocumentType],
    departureOffice: CustomsOfficeWithOptionalDate,
    destinationOffice: CustomsOfficeWithOptionalDate,
    transitOffices: Seq[CustomsOfficeWithOptionalDate],
    previousDocumentTypes: Seq[PreviousDocumentTypes],
    controlResult: Option[viewmodels.ControlResult]
  ): ValidationResult[viewmodels.TransitAccompanyingDocumentPDF] =
    (
      convertCountryOfDispatch(transitAccompanyingDocument.header.countryOfDispatch, countries),
      convertCountryOfDestination(transitAccompanyingDocument.header.countryOfDestination, countries),
      PrincipalConverter.toViewModel(transitAccompanyingDocument.principal, "principal", countries),
      convertTransportCountry(transitAccompanyingDocument.header.transportCountry, countries),
      convertGoodsItems(transitAccompanyingDocument.goodsItems, countries, additionalInfo, kindsOfPackage, documentTypes, previousDocumentTypes),
      convertConsignor(transitAccompanyingDocument.consignor, countries),
      convertConsignee(transitAccompanyingDocument.consignee, countries),
      convertReturnCopiesCustomsOffice(transitAccompanyingDocument.returnCopiesCustomsOffice, countries)
    ).mapN(
      (dispatch, destination, principal, transportCountry, goodsItems, consignor, consignee, returnCopiesCustomsOffice) =>
        viewmodels.TransitAccompanyingDocumentPDF(
          movementReferenceNumber = transitAccompanyingDocument.header.movementReferenceNumber,
          declarationType = transitAccompanyingDocument.header.declarationType,
          singleCountryOfDispatch = dispatch,
          singleCountryOfDestination = destination,
          transportIdentity = transitAccompanyingDocument.header.transportIdentity,
          transportCountry = transportCountry,
          acceptanceDate = Some(FormattedDate(transitAccompanyingDocument.header.acceptanceDate)),
          numberOfItems = transitAccompanyingDocument.header.numberOfItems,
          numberOfPackages = transitAccompanyingDocument.header.numberOfPackages,
          grossMass = transitAccompanyingDocument.header.grossMass,
          printBindingItinerary = transitAccompanyingDocument.header.printBindingItinerary,
          authId = transitAccompanyingDocument.header.authId,
          copyType = transitAccompanyingDocument.header.returnCopy,
          principal = principal,
          consignor = consignor,
          consignee = consignee,
          departureOffice = departureOffice,
          destinationOffice = destinationOffice,
          customsOfficeOfTransit = transitOffices,
          guaranteeDetails = transitAccompanyingDocument.guaranteeDetails.toList,
          seals = transitAccompanyingDocument.seals,
          returnCopiesCustomsOffice = returnCopiesCustomsOffice,
          controlResult = controlResult,
          goodsItems = goodsItems
        )
    )

  // TODO pass ref data values here along with main model (IE029Data)

  def fromP5ToViewModel(
    ie029: IE029Data,
    countries: Seq[Country], //TODO: Will this fetch all countries?
    additionalInfo: Seq[AdditionalInformation],
    kindsOfPackages: Seq[KindOfPackage],
    documentType: Seq[DocumentType],
    previousDocuments: Seq[PreviousDocumentTypes],
    circumstanceIndicators: Seq[CircumstanceIndicator],
    customsOffices: Seq[CustomsOffice],
    controlResults: Seq[ControlResult]
  ): ValidationResult[viewmodels.TransitAccompanyingDocumentPDF] = {

    val kindsOfPackage            = kindsOfPackages // P5
    val documentTypes             = documentType    // P5
    val sensitiveGoodsInformation = Nil

    val controlResult = viewmodels.ControlResult(ControlResultData("code", "description a2"), ControlResult("code", LocalDate.of(1990, 2, 3)))

    val additionalInfo = Seq(reference.AdditionalInformation("I1", "Info 1"), reference.AdditionalInformation("I2", "info 2"))

    val specialMentionEc                 = models.SpecialMention(None, additionalInfo.head.code, Some(true), None)
    val specialMentionEcViewModel        = viewmodels.SpecialMention(additionalInfo.head, specialMentionEc)
    val specialMentionNonEc              = models.SpecialMention(None, additionalInfo.head.code, None, countries.headOption.map(_.code))
    val specialMentionNonEcViewModel     = viewmodels.SpecialMention(additionalInfo.head, specialMentionNonEc)
    val specialMentionNoCountry          = models.SpecialMention(Some("Description"), additionalInfo.head.code, None, None)
    val specialMentionNoCountryViewModel = viewmodels.SpecialMention(additionalInfo.head, specialMentionNoCountry)

    ie029.data match {
      case DepartureMessageData(
            transitOperation,
            authorisation,
            officeOfDeparture,
            officeOfDestination,
            officesOfTransit,
            officeOfExit,
            holderOfTransit,
            representative,
            controlResult,
            guarantee,
            consignment
          ) =>
        (
          convertConsignor(consignment.consignor.map(_.toP4), countries), // TODO unsure if we need this, it doesnt get printed in full anyways???
          convertConsignee(consignment.consignee.map(_.toP4), countries)
        ).mapN {
          (consignor, consignee) =>
            // TODO maybe make an implicit class / viewModel

            def intStringToBool(intString: String) = intString match {
              case "1" => true
              case _   => false
            }

            viewmodels.TransitAccompanyingDocumentPDF(
              movementReferenceNumber = transitOperation.MRN,                                    // P5
              declarationType = transitOperation.declarationType,                                // P5
              singleCountryOfDispatch = consignment.countryOfDispatch.map(Country(_, "")),       // P5
              singleCountryOfDestination = consignment.countryOfDestination.map(Country(_, "")), // P5
              transportIdentity = consignment.departureTransportMeansIdentity,                   // P5
              transportCountry = Some(countries.head),
              acceptanceDate = transitOperation.declarationAcceptanceDate.map(FormattedDate(_)), // P5
              numberOfItems = consignment.totalItems,
              numberOfPackages = Some(consignment.totalPackages),
              grossMass = consignment.grossMass,                                          // P5
              printBindingItinerary = intStringToBool(transitOperation.bindingItinerary), // P5
              authId = ie029.data.authorisation,                                          // P5  //TODO check again
              copyType = false,                                                           //      TODO check again
              principal = viewmodels.Principal(
                "Principal name",
                "Principal street",
                "Principal street",
                "Principal postCode",
                "Principal city",
                countries.head,
                Some("Principal EORI"),
                Some("tir")
              ),                     //TODO
              consignor = consignor, // P5
              consignee = consignee, // P5
              departureOffice = CustomsOfficeWithOptionalDate(
                customsOffices
                  .find(
                    office => office.id == officeOfDeparture.referenceNumber
                  )
                  .get,
                None //TODO: Date?
              ),
              destinationOffice = CustomsOfficeWithOptionalDate(
                customsOffices
                  .find(
                    office => office.id == officeOfDestination.referenceNumber
                  )
                  .get,
                None //TODO: Date?
              ),
              customsOfficeOfTransit = officesOfTransit
                .map(
                  officeOfTransitList =>
                    officeOfTransitList.flatMap(
                      officeOfTransit =>
                        customsOffices
                          .find(
                            office => office.id == officeOfTransit.referenceNumber.get
                          )
                          .map(
                            foundOffice => CustomsOfficeWithOptionalDate(foundOffice, None)
                          )
                    )
                )
                .getOrElse(Nil),
              guaranteeDetails = guarantee match {
                case Some(guaranteeList) =>
                  guaranteeList.map(
                    guarantee =>
                      GuaranteeDetails(
                        guarantee.guaranteeType.getOrElse(""),
                        Seq(GuaranteeReference(None, None, None, Seq("")))
                      ) //TODO: P4 & P5 GuaranteeReference are completely different
                  )
                case _ => Nil
              },
              seals = consignment.seals, // P5
              Some(viewmodels.ReturnCopiesCustomsOffice("office", "street", "postcode", "city", countries.head)),
              controlResult = Some(controlResult),
//              goodsItems = consignment.houseConsignments.map((houseConsignment) => houseConsignment.consignmentItems.map(consignmentItem =>  {
//                viewmodels.GoodsItem(
//                  itemNumber = consignmentItem.goodsItemNumber.toInt,
//                  commodityCode = Some(consignmentItem.commodityCode),
//                  declarationType = DeclarationType.values.find((declarationType) => consignmentItem.declarationType == declarationType.toString)
//
//                )
//              }))
              goodsItems = NonEmptyList.one(
                viewmodels.GoodsItem(
                  itemNumber = consignment.houseConsignments.map(houseConsignment => houseConsignment.consignmentItems.headOption.map(item => item.goodsItemNumber.toInt)),         //TODO: Clarify with Sayak on how items is displayed in the doc,
                  commodityCode = consignment.houseConsignments.map(houseConsignment => houseConsignment.consignmentItems.map(item => item.commodityCode)), //P5
                  declarationType = consignment.houseConsignments.map(houseConsignment => houseConsignment.consignmentItems.map(item => DeclarationType.values.find(_.toString == item.declarationType)))

                    .headOption.flatMap {
                    x => DeclarationType.values.find(_.toString == x.declarationType)
                  },                                                                                  //P5
                  description = consignment.items.headOption.map(_.descriptionOfGoods).getOrElse(""), //P5
                  grossMass = consignment.items.headOption.map(
                    x => BigDecimal(x.grossMass)
                  ), //P5
                  netMass = consignment.items.headOption.map(
                    x => BigDecimal(x.netMass)
                  ), //P5
                  countryOfDispatch = consignment.items.headOption.flatMap {
                    x =>
                      countries
                        .find(
                          country => country.code === x.countryOfDispatch
                        )
                  }, //P5
                  countryOfDestination = consignment.items.headOption.flatMap {
                    x =>
                      countries
                        .find(
                          country => country.code === x.countryOfDestination
                        )
                  },                                                                      //P5
                  methodOfPayment = consignment.items.headOption.map(_.transportCharges), //P5
                  commercialReferenceNumber = None,                                       //TODO: More clarification on this
                  unDangerGoodsCode = consignment.items.headOption.map(_.dangerousGoods), //P5
                  producedDocuments = Nil,                                                //TODO: More clarification on this
                  previousDocumentTypes = Nil,                                            //TODO: More clarification on this
                  specialMentions = Nil,                                                  //TODO More clarification on this
                  consignor = consignor,                                                  //P5
                  consignee = consignee,                                                  //P5
                  containers = Nil,                                                       //TODO More clarification on this
                  packages = NonEmptyList(
                    viewmodels.BulkPackage(kindsOfPackage.head, Some("numbers")),
                    List(
                      viewmodels.UnpackedPackage(kindsOfPackage.head, 1, Some("marks")),
                      viewmodels.RegularPackage(kindsOfPackage.head, 1, "marks and numbers")
                    )
                  ),
                  sensitiveGoodsInformation = Nil, //TODO More clarification on this
                  securityConsignor = None,        //TODO More clarification on this
                  securityConsignee = None         //TODO More clarification on this
                )
              )
            )
        }

    }

  }
}
