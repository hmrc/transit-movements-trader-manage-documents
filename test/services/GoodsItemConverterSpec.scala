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
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class GoodsItemConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val countries                 = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))
  private val kindsOfPackage            = Seq(KindOfPackage("P1", "Package 1"), KindOfPackage("P2", "Package 2"))
  private val documentTypes             = Seq(DocumentType("T1", "Document 1", transportDocument = true), DocumentType("T2", "Document 2", transportDocument = false))
  private val additionalInfo            = Seq(AdditionalInformation("I1", "Info 1"), AdditionalInformation("I2", "info 2"))
  private val sensitiveGoodsInformation = Nil

  private val invalidCode = "non-existent code"

  "toViewModel" - {

    val specialMentionEc                 = models.SpecialMention(None, additionalInfo.head.code, Some(true), None)
    val specialMentionEcViewModel        = viewmodels.SpecialMention(additionalInfo.head, specialMentionEc)
    val specialMentionNonEc              = models.SpecialMention(None, additionalInfo.head.code, None, countries.headOption.map(_.code))
    val specialMentionNonEcViewModel     = viewmodels.SpecialMention(additionalInfo.head, specialMentionNonEc)
    val specialMentionNoCountry          = models.SpecialMention(Some("Description"), additionalInfo.head.code, None, None)
    val specialMentionNoCountryViewModel = viewmodels.SpecialMention(additionalInfo.head, specialMentionNoCountry)

    "must return a view model when all of the necessary reference data can be found" in {

      val goodsItem = models.GoodsItem(
        itemNumber = 1,
        commodityCode = None,
        declarationType = None,
        description = "Description",
        grossMass = Some(1.0),
        netMass = Some(0.9),
        countryOfDispatch = Some(countries.head.code),
        countryOfDestination = Some(countries.head.code),
        producedDocuments = Seq(models.ProducedDocument(documentTypes.head.code, None, None)),
        previousAdminRef = Nil,
        specialMentions = Seq(
          specialMentionEc,
          specialMentionNonEc,
          specialMentionNoCountry
        ),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", countries.head.code, None, None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", countries.head.code, None, None)),
        containers = Seq("container 1"),
        packages = NonEmptyList(
          models.BulkPackage(kindsOfPackage.head.code, Some("numbers")),
          List(
            models.UnpackedPackage(kindsOfPackage.head.code, 1, Some("marks")),
            models.RegularPackage(kindsOfPackage.head.code, 1, "marks and numbers")
          )
        ),
        sensitiveGoodsInformation = sensitiveGoodsInformation
      )

      val expectedViewModel = viewmodels.GoodsItem(
        itemNumber = 1,
        commodityCode = None,
        declarationType = None,
        description = "Description",
        grossMass = Some(1.0),
        netMass = Some(0.9),
        countryOfDispatch = Some(countries.head),
        countryOfDestination = Some(countries.head),
        producedDocuments = Seq(viewmodels.ProducedDocument(documentTypes.head, None, None)),
        previousDocumentTypes = Nil,
        specialMentions = Seq(
          specialMentionEcViewModel,
          specialMentionNonEcViewModel,
          specialMentionNoCountryViewModel
        ),
        consignor =
          Some(viewmodels.Consignor("consignor name", "consignor street", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
        consignee =
          Some(viewmodels.Consignee("consignee name", "consignee street", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
        containers = Seq("container 1"),
        packages = NonEmptyList(
          viewmodels.BulkPackage(kindsOfPackage.head, Some("numbers")),
          List(
            viewmodels.UnpackedPackage(kindsOfPackage.head, 1, Some("marks")),
            viewmodels.RegularPackage(kindsOfPackage.head, 1, "marks and numbers")
          )
        ),
        sensitiveGoodsInformation = sensitiveGoodsInformation
      )

      val result = GoodsItemConverter.toViewModel(goodsItem, "path", countries, additionalInfo, kindsOfPackage, documentTypes)

      result.valid.value mustEqual expectedViewModel
    }

    "must return errors when codes cannot be found in the reference data" in {

      val goodsItem = models.GoodsItem(
        itemNumber = 1,
        commodityCode = None,
        declarationType = None,
        description = "Description",
        grossMass = Some(1.0),
        netMass = Some(0.9),
        countryOfDispatch = Some(invalidCode),
        countryOfDestination = Some(invalidCode),
        producedDocuments = Seq(models.ProducedDocument(invalidCode, None, None)),
        previousAdminRef = Nil,
        specialMentions = Seq(
          specialMentionEc.copy(additionalInformationCoded = invalidCode),
          specialMentionNonEc.copy(additionalInformationCoded = invalidCode),
          specialMentionNoCountry.copy(additionalInformationCoded = invalidCode)
        ),
        consignor = Some(models.Consignor("consignor name", "consignor street", "consignor postCode", "consignor city", invalidCode, None, None)),
        consignee = Some(models.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", invalidCode, None, None)),
        containers = Seq("container 1"),
        packages = NonEmptyList(
          models.BulkPackage(invalidCode, Some("numbers")),
          List(
            models.UnpackedPackage(invalidCode, 1, Some("marks")),
            models.RegularPackage(invalidCode, 1, "marks and numbers")
          )
        ),
        sensitiveGoodsInformation = sensitiveGoodsInformation
      )

      val result = GoodsItemConverter.toViewModel(goodsItem, "path", countries, additionalInfo, kindsOfPackage, documentTypes)

      val expectedErrors = Seq(
        ReferenceDataNotFound("path.countryOfDispatch", invalidCode),
        ReferenceDataNotFound("path.countryOfDestination", invalidCode),
        ReferenceDataNotFound("path.producedDocuments[0].documentType", invalidCode),
        ReferenceDataNotFound("path.specialMentions[0].additionalInformationCoded", invalidCode),
        ReferenceDataNotFound("path.specialMentions[1].additionalInformationCoded", invalidCode),
        ReferenceDataNotFound("path.specialMentions[2].additionalInformationCoded", invalidCode),
        ReferenceDataNotFound("path.consignor.countryCode", invalidCode),
        ReferenceDataNotFound("path.consignee.countryCode", invalidCode),
        ReferenceDataNotFound("path.packages[0].kindOfPackage", invalidCode),
        ReferenceDataNotFound("path.packages[1].kindOfPackage", invalidCode),
        ReferenceDataNotFound("path.packages[2].kindOfPackage", invalidCode)
      )

      result.invalidValue.toChain.toList must contain theSameElementsAs expectedErrors
    }
  }
}
