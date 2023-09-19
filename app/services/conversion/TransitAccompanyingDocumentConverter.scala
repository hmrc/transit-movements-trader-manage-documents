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
import cats.syntax.list._
import cats.implicits._
import models.P5.departure.DepartureMessageData
import models.P5.departure.IE029Data
import models._
import models.reference._
import services._
import utils.FormattedDate
import viewmodels.CustomsOfficeWithOptionalDate
import viewmodels.PreviousDocumentType

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
    additionalInformation: Seq[AdditionalInformation],
    kindsOfPackages: Seq[KindOfPackage],
    previousDocuments: Seq[PreviousDocumentTypes],
    supportDocuments: Seq[SupportingDocumentTypes],
    transportDocuments: Seq[TransportDocumentTypes],
    circumstanceIndicators: Seq[CircumstanceIndicator],
    customsOffices: Seq[CustomsOffice],
    controlResults: Seq[ControlResult]
  ): ValidationResult[viewmodels.TransitAccompanyingDocumentPDF] = {

    val kindsOfPackage = kindsOfPackages // P5
//    val documentTypes             = documentType    // P5
    val sensitiveGoodsInformation = Nil

    val controlResult = ControlResult("A", LocalDate.now())

//    val additionalInfo = Seq(reference.AdditionalInformation("I1", "Info 1"), reference.AdditionalInformation("I2", "info 2"))
    val additionalInfo = additionalInformation

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
          convertConsignor(consignment.Consignor.map(_.toP4), countries), // TODO unsure if we need this, it doesn't get printed in full anyways???
          convertConsignee(consignment.Consignee.map(_.toP4), countries)
        ).mapN {
          (consignor, consignee) =>
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
              authId = ie029.data.authorisationDisplay,                                   // P5  //TODO check again
              copyType = false,                                                           //      TODO check again
              principal = viewmodels.Principal(
                holderOfTransit.ContactPerson
                  .map(
                    contactPerson => contactPerson.name
                  )
                  .getOrElse(""),
                holderOfTransit.Address
                  .map(
                    address => address.streetAndNumber
                  )
                  .getOrElse(""),
                holderOfTransit.Address
                  .map(
                    address => address.streetAndNumber.trim()
                  )
                  .getOrElse(""),
                holderOfTransit.Address
                  .map(
                    address => address.postcode.getOrElse("")
                  )
                  .getOrElse(""),
                holderOfTransit.Address
                  .map(
                    address => address.city
                  )
                  .getOrElse(""),
                countries
                  .find(
                    country =>
                      country.code == holderOfTransit.Address
                        .map(
                          address => address.country
                        )
                        .getOrElse("")
                  )
                  .get,
                holderOfTransit.identificationNumber,
                holderOfTransit.TIRHolderIdentificationNumber
              ),
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
              seals = consignment.TransportEquipment match {
                case Some(transportEquipmentList) =>
                  transportEquipmentList.flatMap(
                    transportEquipment => transportEquipment.sealsList
                  )
                case _ => Nil
              }, // P5
              None,
              controlResult = Some(controlResult.toP4),
              goodsItems = {
                val goodsItemList = consignment.consignmentItems.map {
                  consignmentItem =>
                    viewmodels.GoodsItem(
                      itemNumber = s"${consignmentItem.goodsItemNumber}/${consignmentItem.declarationGoodsItemNumber}",
                      commodityCode = Some(consignmentItem.commodityCode),
                      declarationType = DeclarationType.values.find(
                        declarationType => consignmentItem.declarationType.getOrElse("") == declarationType.toString
                      ),
                      description = consignmentItem.descriptionOfGoods,
                      grossMass = Some(BigDecimal(consignmentItem.grossMass)),
                      netMass = Some(BigDecimal(consignmentItem.netMass)),
                      countryOfDispatch = countries.find(
                        country => country.code == consignmentItem.countryOfDispatch.getOrElse("")
                      ),
                      countryOfDestination = countries.find(
                        country => country.code == consignmentItem.countryOfDestination.getOrElse("")
                      ),
                      methodOfPayment = consignmentItem.TransportCharges.map(
                        transportCharge => transportCharge.toString
                      ),
                      commercialReferenceNumber = None,
                      unDangerGoodsCode = Some(consignmentItem.dangerousGoods),
                      producedDocuments = consignmentItem.allProducedDocuments,
                      previousDocumentTypes = consignmentItem.PreviousDocument
                        .map {
                          previousDocuments =>
                            previousDocuments.flatMap {
                              prevDocument =>
                                val previousType = PreviousDocumentTypes("token", None)
                                val adminReference = PreviousAdministrativeReference(
                                  prevDocument.`type`.getOrElse(""),
                                  prevDocument.referenceNumber.getOrElse(""),
                                  prevDocument.complementOfInformation
                                )
                                Some(PreviousDocumentType(previousType, adminReference))
                            }
                        }
                        .getOrElse(Seq.empty),
                      specialMentions = consignmentItem.AdditionalInformation
                        .map(
                          additionalInformationList =>
                            additionalInformationList.map {
                              additionalInformation =>
                                viewmodels.SpecialMention(
                                  AdditionalInformation(additionalInformation.code.getOrElse(""), additionalInformation.text.getOrElse("")),
                                  models.SpecialMention(additionalInformation.text, additionalInformation.code.getOrElse(""), None, None)
                                )
                            }
                        )
                        .getOrElse(Seq.empty),
                      consignor = consignor,
                      consignee = consignee,
                      containers = consignment.TransportEquipment
                        .map(
                          transportEquipments =>
                            transportEquipments.map(
                              transportEquipment => transportEquipment.containerIdentificationNumber.getOrElse("")
                            )
                        )
                        .getOrElse(Seq.empty),
                      packages = consignmentItem.packagingFormat,
                      sensitiveGoodsInformation = Seq(SensitiveGoodsInformation(consignmentItem.Commodity.cusCode, 3)),
                      securityConsignor = None,
                      securityConsignee = None
                    )
                }.toList
                NonEmptyList.fromList(goodsItemList).get
              }
            )
        }

    }

  }
}
