/*
 * Copyright 2021 HM Revenue & Customs
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

package services

import cats.data.NonEmptyList
import cats.data.Validated.Valid
import cats.implicits._
import models.PreviousAdministrativeReference
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import models.reference.PreviousDocumentTypes

object GoodsItemConverter extends Converter {

  def toViewModel(goodsItem: models.GoodsItem,
                  path: String,
                  countries: Seq[Country],
                  additionalInfo: Seq[AdditionalInformation],
                  kindsOfPackage: Seq[KindOfPackage],
                  documentTypes: Seq[DocumentType],
                  previousDocumentTypes: Seq[PreviousDocumentTypes] = Nil,
                  previousAdministrativeReferences: Seq[PreviousAdministrativeReference] = Nil): ValidationResult[viewmodels.GoodsItem] = {

    def convertPreviousDocumentTypes(docs: Seq[models.PreviousAdministrativeReference]): ValidationResult[List[viewmodels.PreviousDocumentType]] =
      docs.zipWithIndex
        .map {
          case (doc, index) =>
            PreviousDocumentConverter.toViewModel(doc, s"$path.previousAdministrativeReference[$index]", previousDocumentTypes)
        }
        .toList
        .sequence

    def convertDocuments(docs: Seq[models.ProducedDocument]): ValidationResult[List[viewmodels.ProducedDocument]] =
      docs.zipWithIndex
        .map {
          case (doc, index) =>
            ProducedDocumentConverter.toViewModel(doc, s"$path.producedDocuments[$index]", documentTypes)
        }
        .toList
        .sequence

    def convertSpecialMentions(mentions: Seq[models.TADSpecialMention]): ValidationResult[List[viewmodels.TADSpecialMention]] =
      mentions
        .filter(!_.additionalInformationCoded.contains("CAL"))
        .zipWithIndex
        .map {
          case (sm, index) =>
            TADSpecialMentionConverter.toViewModel(sm, s"$path.specialMentions[$index]", additionalInfo)
        }
        .toList
        .sequence

    def convertPackages(packages: NonEmptyList[models.Package]): ValidationResult[NonEmptyList[viewmodels.Package]] = {
      val head = PackageConverter.toViewModel(packages.head, s"$path.packages[0]", kindsOfPackage)

      val tail = packages.tail.zipWithIndex.map {
        case (pkg, index) =>
          PackageConverter.toViewModel(pkg, s"$path.packages[${index + 1}]", kindsOfPackage)
      }.sequence

      (
        head,
        tail
      ).mapN(
        (head, tail) => NonEmptyList(head, tail)
      )
    }

    def convertConsignor(maybeConsignor: Option[models.Consignor]): ValidationResult[Option[viewmodels.Consignor]] =
      maybeConsignor match {
        case Some(consignor) => ConsignorConverter.toViewModel(consignor, s"$path.consignor", countries).map(x => Some(x))
        case None            => Valid(None)
      }

    def convertConsignee(maybeConsignee: Option[models.Consignee]): ValidationResult[Option[viewmodels.Consignee]] =
      maybeConsignee match {
        case Some(consignee) => ConsigneeConverter.toViewModel(consignee, s"$path.consignee", countries).map(x => Some(x))
        case None            => Valid(None)
      }

    def convertCountryOfDispatch(maybeCountryOfDispatch: Option[String]): ValidationResult[Option[Country]] =
      maybeCountryOfDispatch match {
        case Some(countryOfDispatch) => findReferenceData(countryOfDispatch, countries, s"$path.countryOfDispatch").map(x => Some(x))
        case None                    => Valid(None)
      }

    def convertCountryOfDestination(maybeCountryOfDestination: Option[String]): ValidationResult[Option[Country]] =
      maybeCountryOfDestination match {
        case Some(countryOfDestination) => findReferenceData(countryOfDestination, countries, s"$path.countryOfDestination").map(x => Some(x))
        case None                       => Valid(None)
      }

    (
      convertCountryOfDispatch(goodsItem.countryOfDispatch),
      convertCountryOfDestination(goodsItem.countryOfDestination),
      convertDocuments(goodsItem.producedDocuments),
      convertSpecialMentions(goodsItem.specialMentions),
      convertPackages(goodsItem.packages),
      convertConsignor(goodsItem.consignor),
      convertConsignee(goodsItem.consignee),
      convertPreviousDocumentTypes(previousAdministrativeReferences)
    ).mapN(
      (dispatch, destination, docs, mentions, packages, consignor, consignee, previousDocs) =>
        viewmodels.GoodsItem(
          goodsItem.itemNumber,
          goodsItem.commodityCode,
          goodsItem.declarationType,
          goodsItem.description,
          goodsItem.grossMass,
          goodsItem.netMass,
          dispatch,
          destination,
          docs,
          previousDocs,
          mentions,
          consignor,
          consignee,
          goodsItem.containers,
          packages,
          goodsItem.sensitiveGoodsInformation
      )
    )
  }
}
