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

package models

import java.time.LocalDate

import cats.data.NonEmptyList
import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json._
import utils.BigDecimalXMLReader._
import utils.LocalDateXMLReader._
import utils.NonEmptyListXMLReader._
import json.NonEmptyListOps._

sealed trait UnloadingPermission

object UnloadingPermission {

  implicit lazy val reads: Reads[UnloadingPermission] = {

    implicit class ReadsWithContravariantOr[A](a: Reads[A]) {

      def or[B >: A](b: Reads[B]): Reads[B] =
        a.map[B](identity).orElse(b)
    }

    implicit def convertToSupertype[A, B >: A](a: Reads[A]): Reads[B] =
      a.map(identity)

    PermissionToStartUnloading.format or
      PermissionToContinueUnloading.format
  }

  implicit lazy val writes: OWrites[UnloadingPermission] = OWrites {
    case up: PermissionToContinueUnloading => Json.toJsObject(up)(PermissionToContinueUnloading.format)
    case up: PermissionToStartUnloading    => Json.toJsObject(up)(PermissionToStartUnloading.format)
  }
}

final case class PermissionToStartUnloading(
  movementReferenceNumber: String,
  declarationType: String,
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  transportIdentity: Option[String],
  transportCountry: Option[String],
  acceptanceDate: LocalDate,
  numberOfItems: Int,
  numberOfPackages: Option[Int],
  grossMass: BigDecimal,
  principal: Principal,
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  traderAtDestination: TraderAtDestination, //TODO: Is this used for pdf?
  departureOffice: String,
  presentationOffice: String,
  seals: Seq[String],
  goodsItems: NonEmptyList[GoodsItem]
) extends UnloadingPermission

object PermissionToStartUnloading {

  implicit lazy val format: OFormat[PermissionToStartUnloading] =
    Json.format[PermissionToStartUnloading]

  implicit val xmlReader: XmlReader[PermissionToStartUnloading] =
    (
      (__ \ "HEAHEA" \ "DocNumHEA5").read[String],
      (__ \ "HEAHEA" \ "TypOfDecHEA24").read[String],
      (__ \ "HEAHEA" \ "CouOfDisCodHEA55").read[String].optional,
      (__ \ "HEAHEA" \ "CouOfDesCodHEA30").read[String].optional,
      (__ \ "HEAHEA" \ "IdeOfMeaOfTraAtDHEA78").read[String].optional,
      (__ \ "HEAHEA" \ "NatOfMeaOfTraAtDHEA80").read[String].optional,
      (__ \ "HEAHEA" \ "AccDatHEA158").read[LocalDate],
      (__ \ "HEAHEA" \ "TotNumOfIteHEA305").read[Int],
      (__ \ "HEAHEA" \ "TotNumOfPacHEA306").read[Int].optional,
      (__ \ "HEAHEA" \ "TotGroMasHEA307").read[BigDecimal],
      (__ \ "TRAPRIPC1").read[Principal],
      (__ \ "TRACONCO1").read[Consignor](Consignor.xmlReaderRootLevel).optional,
      (__ \ "TRACONCE1").read[Consignee](Consignee.xmlReaderRootLevel).optional,
      (__ \ "TRADESTRD").read[TraderAtDestination],
      (__ \ "CUSOFFDEPEPT" \ "RefNumEPT1").read[String],
      (__ \ "CUSOFFPREOFFRES" \ "RefNumRES1").read[String],
      (__ \ "SEAINFSLI" \ "SEAIDSID" \ "SeaIdeSID1").read(strictReadSeq[String]),
      (__ \ "GOOITEGDS").read(xmlNonEmptyListReads[GoodsItem])
    ).mapN(apply)
}

final case class PermissionToContinueUnloading(
  movementReferenceNumber: String,
  continueUnloading: Int,
  presentationOffice: String,
  traderAtDestination: TraderAtDestination
) extends UnloadingPermission

object PermissionToContinueUnloading {

  implicit lazy val format: OFormat[PermissionToContinueUnloading] =
    Json.format[PermissionToContinueUnloading]
}
