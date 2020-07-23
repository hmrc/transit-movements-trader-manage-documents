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

import cats.data.NonEmptyChain
import cats.data.NonEmptyList
import javax.inject.Inject
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import models.DeclarationType
import models.PermissionToStartUnloading
import play.api.Logger
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import services.ConversionService
import services.JsonError
import services.PdfGenerator
import services.ReferenceDataRetrievalError
import services.ValidationError
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import viewmodels.SensitiveGoodsInformation

import scala.concurrent.ExecutionContext

class UnloadingPermissionController @Inject()(
  conversionService: ConversionService,
  pdf: PdfGenerator,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  private val logger = Logger(getClass)

  private def logErrors(errors: NonEmptyChain[ValidationError]): Unit =
    logger.warn(errors.toChain.toList.mkString("\n"))

  def post(): Action[PermissionToStartUnloading] = Action.async(parse.json[PermissionToStartUnloading]) {
    implicit request =>
      conversionService.convertUnloadingPermission(request.body).map {
        _.fold(
          errors => {
            logErrors(errors)

            errors match {
              case x if x.exists(y => y.isInstanceOf[JsonError])                   => InternalServerError
              case x if x.exists(y => y.isInstanceOf[ReferenceDataRetrievalError]) => BadGateway
              case _                                                               => BadRequest
            }
          },
          _ => Ok
        )
      }
  }

  def get(): Action[AnyContent] = Action {
    implicit request =>
      val permission = viewmodels.PermissionToStartUnloading(
        movementReferenceNumber = "19GB9876AB88901209",
        declarationType = DeclarationType.T1,
        transportIdentity = Some("identity"),
        transportCountry = Some(Country("valid", "AA", "Country A")),
        acceptanceDate = LocalDate.now(),
        numberOfItems = 1,
        numberOfPackages = 3,
        grossMass = 1.0,
        principal = viewmodels.Principal("Principal name",
                                         "Principal street",
                                         "Principal postCode",
                                         "Principal city",
                                         Country("valid", "AA", "Country A"),
                                         Some("Principal EORI"),
                                         None),
        traderAtDestination = viewmodels.TraderAtDestinationWithEori("Trader EORI", None, None, None, None, None),
        presentationOffice = "Presentation office",
        seals = Seq("seal 1"),
        goodsItems = NonEmptyList.one(
          viewmodels.GoodsItem(
            itemNumber = 1,
            commodityCode = None,
            declarationType = None,
            description = "Description",
            grossMass = Some(1.0),
            netMass = Some(0.9),
            countryOfDispatch = Country("valid", "AA", "Country A"),
            countryOfDestination = Country("valid", "AA", "Country A"),
            producedDocuments = Seq(viewmodels.ProducedDocument(DocumentType("T1", "Document 1", transportDocument = true), None, None)),
            specialMentions = Seq(
              viewmodels.SpecialMentionEc(AdditionalInformation("I1", "Info 1")),
              viewmodels.SpecialMentionNonEc(AdditionalInformation("I1", "Info 1"), Country("valid", "AA", "Country A")),
              viewmodels.SpecialMentionNoCountry(AdditionalInformation("I1", "Info 1"))
            ),
            consignor = Some(
              viewmodels.Consignor("consignor name",
                                   "consignor street",
                                   "consignor postCode",
                                   "consignor city",
                                   Country("valid", "AA", "Country A"),
                                   Some("IT444100201000"))),
            consignee = Some(
              viewmodels.Consignee("consignee name", "consignee street", "consignee postCode", "consignee city", Country("valid", "AA", "Country A"), None)),
            containers = Seq("container 1"),
            packages = NonEmptyList(
              viewmodels.BulkPackage(KindOfPackage("P1", "Package 1"), Some("numbers")),
              List(
                viewmodels.UnpackedPackage(KindOfPackage("P1", "Package 1"), 1, Some("marks")),
                viewmodels.RegularPackage(KindOfPackage("P1", "Package 1"), 1, "marks and numbers")
              )
            ),
            sensitiveGoodsInformation = Seq(SensitiveGoodsInformation(Some("010210"), 2))
          )
        )
      )

      permission.goodsItems.head.containers.map(
        x => x
      )

      Ok(pdf.generateUnloadingPermission(permission))
  }
}
