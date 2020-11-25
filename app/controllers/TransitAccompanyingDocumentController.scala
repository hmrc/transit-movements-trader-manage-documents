/*
 * Copyright 2020 HM Revenue & Customs
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

package controllers

import java.time.LocalDate

import cats.data.NonEmptyList
import cats.data.Validated
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.ParseSuccess
import javax.inject.Inject
import models.DeclarationType
import models.SensitiveGoodsInformation
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import services._
import services.conversion.TransitAccompanyingDocumentConversionService
import services.pdf.UnloadingPermissionPdfGenerator
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.NodeSeq

class TransitAccompanyingDocumentController @Inject()(
  conversionService: TransitAccompanyingDocumentConversionService,
  pdf: UnloadingPermissionPdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  def get(): Action[NodeSeq] = Action.async(parse.xml) {
    implicit request =>
      XMLToTransitAccompanyingDocument.convert(request.body) match {
        case ParseSuccess(transitAccompanyingDocument) =>
          conversionService.toViewModel(transitAccompanyingDocument, "mrn").map {
            case Validated.Valid(viewModel) => Ok(pdf.generate(viewModel))
            case Validated.Invalid(errors)  => InternalServerError(s"Failed to convert to TransitAccompanyingDocumentViewModel with following errors: $errors")
          }
        case ParseFailure(errors) =>
          Future.successful(BadRequest(s"Failed to parse xml to TransitAccompanyingDocument with the following errors: $errors"))
      }
  }

//  def get(): Action[AnyContent] = Action {
//    implicit request =>
//      val permissionMultiple = viewmodels.TransitAccompanyingDocument(
//        movementReferenceNumber = "19GB9876AB88901209",
//        declarationType = DeclarationType.T1,
//        singleCountryOfDispatch = None,
//        singleCountryOfDestination = None,
//        transportIdentity = Some("identity"),
//        transportCountry = Some(Country("valid", "AA", "Country A")),
//        numberOfItems = 1,
//        numberOfPackages = 3,
//        grossMass = 1.0,
//        principal = viewmodels.Principal(
//          "Principal name",
//          "Principal street",
//          "Principal street",
//          "Principal postCode",
//          "Principal city",
//          Country("valid", "AA", "Country A"),
//          Some("Principal EORI"),
//          None
//        ),
//        consignor = Some(
//          viewmodels.Consignor("consignor name",
//                               "consignor street",
//                               "consignor street",
//                               "consignor postCode",
//                               "consignor city",
//                               Country("valid", "AA", "Country A"),
//                               Some("IT444100201000"))),
//        consignee = Some(
//          viewmodels.Consignee("consignee name",
//                               "consignee street",
//                               "consignee street",
//                               "consignee postCode",
//                               "consignee city",
//                               Country("valid", "AA", "Country A"),
//                               None)),
//        //traderAtDestination = Some(viewmodels.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None)),
//        departureOffice = "IT021300",
//        departureOfficeTrimmed = "IT021300",
//        // presentationOffice = Some("Presentation office"),
//        seals = Seq("seal 1"),
//        goodsItems = NonEmptyList.one(
//          viewmodels.GoodsItem(
//            itemNumber = 1,
//            commodityCode = None,
//            declarationType = None,
//            description = "Flowers",
//            grossMass = Some(1.0),
//            netMass = Some(0.9),
//            countryOfDispatch = Some(Country("valid", "AA", "Country A")),
//            countryOfDestination = Some(Country("valid", "AA", "Country A")),
//            producedDocuments = Seq(viewmodels.ProducedDocument(DocumentType("T1", "Document 1", transportDocument = true), None, None)),
//            specialMentions = Seq(
//              viewmodels.SpecialMentionEc(AdditionalInformation("I1", "Info 1")),
//              viewmodels.SpecialMentionNonEc(AdditionalInformation("I122222", "Info 1"), Country("valid", "AA", "Country A")),
//              viewmodels.SpecialMentionNoCountry(AdditionalInformation("I1", "Info 1"))
//            ),
//            consignor = Some(
//              viewmodels.Consignor("consignor name",
//                                   "consignor street",
//                                   "consignor street",
//                                   "consignor postCode",
//                                   "consignor city",
//                                   Country("valid", "AA", "Country A"),
//                                   Some("IT444100201000"))),
//            consignee = Some(
//              viewmodels.Consignee("consignee name",
//                                   "consignee street",
//                                   "consignee street",
//                                   "consignee postCode",
//                                   "consignee city",
//                                   Country("valid", "AA", "Country A"),
//                                   None)),
//            containers = Seq("container 1"),
//            packages = NonEmptyList(
//              viewmodels.BulkPackage(KindOfPackage("P1", "Package 1"), Some("numbers")),
//              List(
//                viewmodels.UnpackedPackage(KindOfPackage("P1", "Package 1"), 1, Some("marks")),
//                viewmodels.RegularPackage(KindOfPackage("P1", "Package 1"), 1, "marks and numbers")
//              )
//            ),
//            sensitiveGoodsInformation = Seq(SensitiveGoodsInformation(Some("010210"), 2), SensitiveGoodsInformation(None, 3))
//          )
//        )
//      )
//
//      permissionMultiple.goodsItems.head.containers.map(
//        x => x
//      )
//
//      Ok(pdf.generate(permissionMultiple))
//  }

}
