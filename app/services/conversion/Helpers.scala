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

package services.conversion

import cats.data.NonEmptyList
import cats.data.Validated.Valid
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import services.ConsigneeConverter
import services.ConsignorConverter
import services.GoodsItemConverter
import services.ValidationResult
import services.conversion.TransitAccompanyingDocumentConverter.findReferenceData
import cats.implicits._

trait Helpers {

  def convertTransportCountry(maybeCountry: Option[String], countries: Seq[Country]): ValidationResult[Option[Country]] =
    maybeCountry match {
      case Some(country) => findReferenceData[Country](country, countries, s"transportCountry").map(x => Some(x))
      case None          => Valid(None)
    }

  def convertConsignor(maybeConsignor: Option[models.Consignor], countries: Seq[Country]): ValidationResult[Option[viewmodels.Consignor]] =
    maybeConsignor match {
      case Some(consignor) => ConsignorConverter.toViewModel(consignor, s"consignor", countries).map(x => Some(x))
      case None            => Valid(None)
    }

  def convertConsignee(maybeConsignee: Option[models.Consignee], countries: Seq[Country]): ValidationResult[Option[viewmodels.Consignee]] =
    maybeConsignee match {
      case Some(consignee) => ConsigneeConverter.toViewModel(consignee, s"consignee", countries).map(x => Some(x))
      case None            => Valid(None)
    }

  def convertCountryOfDispatch(maybeCountryOfDispatch: Option[String], countries: Seq[Country]): ValidationResult[Option[Country]] =
    maybeCountryOfDispatch match {
      case Some(countryOfDispatch) => findReferenceData(countryOfDispatch, countries, s"countryOfDispatch").map(x => Some(x))
      case None                    => Valid(None)
    }

  def convertCountryOfDestination(maybeCountryOfDestination: Option[String], countries: Seq[Country]): ValidationResult[Option[Country]] =
    maybeCountryOfDestination match {
      case Some(countryOfDestination) => findReferenceData(countryOfDestination, countries, s"countryOfDestination").map(x => Some(x))
      case None                       => Valid(None)
    }

  def convertGoodsItems(items: NonEmptyList[models.GoodsItem],
                        countries: Seq[Country],
                        additionalInfo: Seq[AdditionalInformation],
                        kindsOfPackage: Seq[KindOfPackage],
                        documentTypes: Seq[DocumentType]): ValidationResult[NonEmptyList[viewmodels.GoodsItem]] = {

    val head = GoodsItemConverter.toViewModel(items.head, "goodsItems[0]", countries, additionalInfo, kindsOfPackage, documentTypes)

    val tail = items.tail.zipWithIndex.map {
      case (item, index) =>
        GoodsItemConverter.toViewModel(item, s"goodsItems[${index + 1}", countries, additionalInfo, kindsOfPackage, documentTypes)
    }.sequence

    (
      head,
      tail
    ).mapN(
      (head, tail) => NonEmptyList(head, tail)
    )
  }

}
