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

package views

import cats.data.NonEmptyList
import generators.ViewmodelGenerators
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.CustomsOffice
import models.reference.DocumentType
import models.reference.KindOfPackage
import models.reference.PreviousDocumentTypes
import models._
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import services.conversion.TransitAccompanyingDocumentConversionService
import uk.gov.hmrc.http.HeaderCarrier
import utils.FormattedDate
import viewmodels.CustomsOfficeWithOptionalDate
import viewmodels.PreviousDocumentType
import views.xml.unloading_permission.TransitAccompanyingDocument

import java.time.LocalDate
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global

class TransitAccompanyingDocumentViewSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with ViewmodelGenerators
    with OptionValues
    with ScalaFutures {

  val view: TransitAccompanyingDocument = app.injector.instanceOf[TransitAccompanyingDocument]
  implicit val hc: HeaderCarrier        = HeaderCarrier()
  val inject                            = app.injector.instanceOf[TransitAccompanyingDocumentConversionService]

  "transitAccompanyingDocument view" - {
    "render the correct xml" in {

      val resultView = view.apply(declarationXmlFull).toString()
      val doc        = Jsoup.parse(resultView, "", Parser.xmlParser())

      val pageOne: Elements = doc.getElementsByTag("fo:page-sequence").attr("initial-page-number", "1")
      println(pageOne.first().getElementById("table-1"))

      pageOne.first().getElementById("table_1_row_1_mrn").text mustBe declarationXmlFull.movementReferenceNumber

    }
  }

  private val countries                 = Seq(Country("valid", "AA", "Country A"), Country("valid", "BB", "Country B"))
  private val kindsOfPackage            = Seq(KindOfPackage("P1", "Package 1"), KindOfPackage("P2", "Package 2"))
  private val documentTypes             = Seq(DocumentType("T1", "Document 1", transportDocument = true), DocumentType("T2", "Document 2", transportDocument = false))
  private val additionalInfo            = Seq(AdditionalInformation("I1", "Info 1"), AdditionalInformation("I2", "info 2"))
  private val sensitiveGoodsInformation = Nil

  private def declarationXmlFull =
    viewmodels.TransitAccompanyingDocumentPDF(
      movementReferenceNumber = "123455432",
      declarationType = DeclarationType.T1,
      singleCountryOfDispatch = Some(countries.head),
      singleCountryOfDestination = Some(countries.head),
      transportIdentity = Some("identity"),
      transportCountry = Some(countries.head),
      acceptanceDate = Some(FormattedDate(LocalDate.of(2020, 1, 1))),
      numberOfItems = 1,
      numberOfPackages = Some(3),
      grossMass = 1.0,
      printBindingItinerary = true,
      authId = Some("AuthId"),
      copyType = false,
      principal = viewmodels.Principal("Principal name",
                                       "Principal street",
                                       "Principal street",
                                       "Principal postCode",
                                       "Principal city",
                                       countries.head,
                                       Some("Principal EORI"),
                                       Some("tir")),
      consignor =
        Some(viewmodels.Consignor("consignor name", "consignor street", "consignor street", "consignor postCode", "consignor city", countries.head, None)),
      consignee =
        Some(viewmodels.Consignee("consignee name", "consignee street", "consignee street", "consignee postCode", "consignee city", countries.head, None)),
      departureOffice = CustomsOfficeWithOptionalDate(CustomsOffice("AB124", Some("Departure Office")), None),
      destinationOffice = CustomsOfficeWithOptionalDate(CustomsOffice("AB125", Some("Destination Office")), None),
      customsOfficeOfTransit = Seq(CustomsOfficeWithOptionalDate(CustomsOffice("AB123", Some("Transit Office")), Some(LocalDateTime.of(2020, 1, 1, 0, 0)))),
      guaranteeDetails = Seq(
        GuaranteeDetails("A", Seq(GuaranteeReference(Some("RefNum"), None, None, Nil)))
      ),
      seals = Seq("seal 1"),
      controlResult = Some(ControlResult("code", LocalDate.of(1990, 2, 3))),
      goodsItems = NonEmptyList.one(
        viewmodels.GoodsItem(
          itemNumber = 1,
          commodityCode = None,
          declarationType = None,
          description = "Description",
          grossMass = Some(1.0),
          netMass = Some(0.9),
          countryOfDispatch = Some(countries.head),
          countryOfDestination = Some(countries.head),
          producedDocuments = Seq(viewmodels.ProducedDocument(documentTypes.head, None, None)),
          previousDocumentTypes = Seq(
            PreviousDocumentType(
              PreviousDocumentTypes("123", "Some Description"),
              PreviousAdministrativeReference("123", "ABABA", None)
            )
          ),
          specialMentions = Seq(
            viewmodels.SpecialMentionEc(additionalInfo.head),
            viewmodels.SpecialMentionNonEc(additionalInfo.head, countries.head),
            viewmodels.SpecialMentionNoCountry(additionalInfo.head)
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
      )
    )
}
