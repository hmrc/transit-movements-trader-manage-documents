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

import cats.data.Validated
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.ParseSuccess
import javax.inject.Inject
import logging.Logging
import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import services._
import services.conversion.UnloadingPermissionConversionService
import services.pdf.UnloadingPermissionPdfGenerator
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.NodeSeq

class UnloadingPermissionController @Inject()(
  conversionService: UnloadingPermissionConversionService,
  pdf: UnloadingPermissionPdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def get(): Action[NodeSeq] = Action.async(parse.xml) {
    implicit request =>
      XMLToPermissionToStartUnloading.convert(request.body) match {
        case ParseSuccess(unloadingPermission) =>
          conversionService.toViewModel(unloadingPermission).map {
            case Validated.Valid(viewModel) => Ok(pdf.generate(viewModel))
            case Validated.Invalid(errors) =>
              logger.error(s"Failed to convert to UnloadingPermissionViewModel with following errors: $errors")

              InternalServerError
          }
        case ParseFailure(errors) =>
          logger.error(s"Failed to parse xml to UnloadingPermission with the following errors: $errors")

          Future.successful(BadRequest)
      }
  }

  //TODO: This can be removed
//  def get(): Action[AnyContent] = Action {
//    implicit request =>
//      val permissionMultiple = viewmodels.PermissionToStartUnloading(
//        movementReferenceNumber = "19GB9876AB88901209",
//        declarationType = DeclarationType.T1,
//        transportIdentity = Some("identity"),
//        transportCountry = Some(Country("valid", "AA", "Country A")),
//        acceptanceDate = LocalDate.now(),
//        acceptanceDateFormatted = "23/07/2015",
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
//        consignee =
//          Some(viewmodels.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", Country("valid", "AA", "Country A"), None)),
//        traderAtDestination = viewmodels.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
//        departureOffice = "IT021300",
//        departureOfficeTrimmed = "IT021300",
//        presentationOffice = "Presentation office",
//        seals = Seq("seal 1"),
//        goodsItems = NonEmptyList.one(
//          viewmodels.GoodsItem(
//            itemNumber = 1,
//            commodityCode = None,
//            declarationType = None,
//            description = "Flowers",
//            grossMass = Some(1.0),
//            netMass = Some(0.9),
//            countryOfDispatch = Country("valid", "AA", "Country A"),
//            countryOfDestination = Country("valid", "AA", "Country A"),
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
//              viewmodels.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", Country("valid", "AA", "Country A"), None)),
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
//      val permission = viewmodels.PermissionToStartUnloading(
//        movementReferenceNumber = "19GB9876AB88901209",
//        declarationType = DeclarationType.T1,
//        transportIdentity = Some("identity"),
//        transportCountry = Some(Country("valid", "AA", "Country A")),
//        acceptanceDate = LocalDate.now(),
//        acceptanceDateFormatted = "23/07/2015",
//        numberOfItems = 1,
//        numberOfPackages = 3,
//        grossMass = 1.0,
//        principal = viewmodels.Principal(
//          "Mancini Carriers",
//          "street and number should be trimmed",
//          "street and number should be t***",
//          "MOD 5JJ",
//          "Modea",
//          Country("valid", "IT", "Italy"),
//          Some("IT444100201000"),
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
//        consignee =
//          Some(viewmodels.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", Country("valid", "AA", "Country A"), None)),
//        traderAtDestination = viewmodels.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
//        departureOffice = "IT021300",
//        departureOfficeTrimmed = "Trimmed departure office value here ***",
//        presentationOffice = "Presentation office",
//        seals = Seq("seal 1", "seal 2", "seal 3"),
//        goodsItems = NonEmptyList.one(
//          viewmodels.GoodsItem(
//            itemNumber = 1,
//            commodityCode = Some("CC"),
//            declarationType = None,
//            description = "Flowers",
//            grossMass = Some(1.0),
//            netMass = Some(0.9),
//            countryOfDispatch = Country("valid", "AA", "Country A"),
//            countryOfDestination = Country("valid", "AA", "Country A"),
//            producedDocuments = Seq(
//              viewmodels.ProducedDocument(DocumentType("T1", "Document 1", transportDocument = true), Some("ref"), Some("info")),
//              viewmodels.ProducedDocument(DocumentType("T2", "Document 2", transportDocument = false), None, Some("info here"))
//            ),
//            specialMentions = Seq(
//              viewmodels.SpecialMentionEc(AdditionalInformation("I1", "Info 1")),
//              viewmodels.SpecialMentionNonEc(AdditionalInformation("I122222", "Info 1"), Country("valid", "AA", "Country A")),
//              viewmodels.SpecialMentionNoCountry(AdditionalInformation("I1", "Info 1"))
//            ),
//            consignor = None,
//            consignee = None,
//            containers = Seq("INTERFLORA005"),
//            packages = NonEmptyList(
//              viewmodels.BulkPackage(KindOfPackage("P1", "Box"), Some("INTERFLORA05")),
//              Nil
//            ),
//            sensitiveGoodsInformation = Seq(SensitiveGoodsInformation(Some("010210"), 2))
//          )
//        )
//      )
//
//      permission.goodsItems.head.containers.map(
//        x => x
//      )
//
//      Ok(pdf.generateUnloadingPermission(permissionMultiple))
//  }

}
