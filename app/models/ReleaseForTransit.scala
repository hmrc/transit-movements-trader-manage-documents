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

package models

import cats.data.NonEmptyList
import com.lucidchart.open.xtract.XmlReader._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import utils.BigDecimalXMLReader._
import utils.LocalDateXMLReader._
import utils.NonEmptyListXMLReader.xmlNonEmptyListReads
import utils.BinaryToBooleanXMLReader._
import java.time.LocalDate
import scala.xml.NodeSeq

final case class ReleaseForTransit(
  movementReferenceNumber: String,
  declarationType: DeclarationType,
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  transportIdentity: Option[String],
  transportCountry: Option[String],
  acceptanceDate: LocalDate,
  numberOfItems: Int,
  numberOfPackages: Option[Int],
  grossMass: BigDecimal,
  printBindingItinerary: Boolean,
  authId: Option[String],
  returnCopy: Boolean,
  circumstanceIndicator: Option[String],
  security: Option[Int],
  commercialReferenceNumber: Option[String],
  methodOfPayment: Option[String],
  identityOfTransportAtBorder: Option[String],
  nationalityOfTransportAtBorder: Option[String],
  transportModeAtBorder: Option[String],
  agreedLocationOfGoodsCode: Option[String],
  principal: Principal,
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  customsOfficeOfTransit: Seq[CustomsOfficeOfTransit],
  guaranteeDetails: NonEmptyList[GuaranteeDetails],
  departureOffice: String,
  destinationOffice: String,
  returnCopiesCustomsOffice: Option[ReturnCopiesCustomsOffice],
  controlResult: Option[ControlResult],
  seals: Seq[String],
  goodsItems: NonEmptyList[GoodsItem]
)

object ReleaseForTransit {

  implicit val xmlReader1: XmlReader[ReleaseForTransit] = (xml: NodeSeq) => {
    for {
      mrn                            <- (__ \ "HEAHEA" \ "DocNumHEA5").read[String].read(xml)
      decType                        <- (__ \ "HEAHEA" \ "TypOfDecHEA24").read[DeclarationType].read(xml)
      countryOfDispatch              <- (__ \ "HEAHEA" \ "CouOfDisCodHEA55").read[String].optional.read(xml)
      countryOfDestination           <- (__ \ "HEAHEA" \ "CouOfDesCodHEA30").read[String].optional.read(xml)
      transportIdentity              <- (__ \ "HEAHEA" \ "IdeOfMeaOfTraAtDHEA78").read[String].optional.read(xml)
      transportCountry               <- (__ \ "HEAHEA" \ "NatOfMeaOfTraAtDHEA80").read[String].optional.read(xml)
      acceptanceDate                 <- (__ \ "HEAHEA" \ "AccDatHEA158").read[LocalDate].read(xml)
      numberOfItems                  <- (__ \ "HEAHEA" \ "TotNumOfIteHEA305").read[Int].read(xml)
      numberOfPackages               <- (__ \ "HEAHEA" \ "TotNumOfPacHEA306").read[Int].optional.read(xml)
      grossMass                      <- (__ \ "HEAHEA" \ "TotGroMasHEA307").read[BigDecimal].read(xml)
      printBindingItinerary          <- (__ \ "HEAHEA" \ "BinItiHEA246").read[Boolean](xmlBinaryToBooleanReads).read(xml)
      authId                         <- (__ \ "HEAHEA" \ "AutIdHEA380").read[String].optional.read(xml)
      returnCopy                     <- (__ \ "HEAHEA" \ "NCTRetCopHEA104").read[Boolean](xmlBinaryToBooleanReads).read(xml)
      circumstanceIndicator          <- (__ \ "HEAHEA" \ "SpeCirIndHEA1").read[String].optional.read(xml)
      security                       <- (__ \ "HEAHEA" \ "SecHEA358").read[Int].optional.read(xml)
      commercialReferenceNumber      <- (__ \ "HEAHEA" \ "ComRefNumHEA").read[String].optional.read(xml)
      methodOfPayment                <- (__ \ "HEAHEA" \ "TraChaMetOfPayHEA1").read[String].optional.read(xml)
      identityOfTransportAtBorder    <- (__ \ "HEAHEA" \ "IdeOfMeaOfTraCroHEA85").read[String].optional.read(xml)
      nationalityOfTransportAtBorder <- (__ \ "HEAHEA" \ "NatOfMeaOfTraCroHEA87").read[String].optional.read(xml)
      transportModeAtBorder          <- (__ \ "HEAHEA" \ "TraModAtBorHEA76").read[String].optional.read(xml)
      agreedLocationOfGoodsCode      <- (__ \ "HEAHEA" \ "AgrLocOfGooCodHEA38").read[String].optional.read(xml)
      principal                      <- (__ \ "TRAPRIPC1").read[Principal].read(xml)
      consignor                      <- (__ \ "TRACONCO1").read[Consignor](Consignor.xmlReaderRootLevel).optional.read(xml)
      consignee                      <- (__ \ "TRACONCE1").read[Consignee](Consignee.xmlReaderRootLevel).optional.read(xml)
      customsOfficeOfTransit         <- (__ \ "CUSOFFTRARNS").read(strictReadSeq[CustomsOfficeOfTransit]).read(xml)
      guaranteeDetails               <- (__ \ "GUAGUA").read(xmlNonEmptyListReads[GuaranteeDetails]).read(xml)
      departureOffice                <- (__ \ "CUSOFFDEPEPT" \ "RefNumEPT1").read[String].read(xml)
      destinationOffice              <- (__ \ "CUSOFFDESEST" \ "RefNumEST1").read[String].read(xml)
      returnCopiesCustomsOffice      <- (__ \ "CUSOFFRETCOPOCP").read[ReturnCopiesCustomsOffice].optional.read(xml)
      controlResult                  <- (__ \ "CONRESERS").read[ControlResult].optional.read(xml)
      seals                          <- (__ \ "SEAINFSLI" \ "SEAIDSID" \ "SeaIdeSID1").read(strictReadSeq[String]).read(xml)
      goodsItems                     <- (__ \ "GOOITEGDS").read(xmlNonEmptyListReads[GoodsItem]).read(xml)
    } yield
      ReleaseForTransit(
        mrn,
        decType,
        countryOfDispatch,
        countryOfDestination,
        transportIdentity,
        transportCountry,
        acceptanceDate,
        numberOfItems,
        numberOfPackages,
        grossMass,
        printBindingItinerary,
        authId,
        returnCopy,
        circumstanceIndicator,
        security,
        commercialReferenceNumber,
        methodOfPayment,
        identityOfTransportAtBorder,
        nationalityOfTransportAtBorder,
        transportModeAtBorder,
        agreedLocationOfGoodsCode,
        principal,
        consignor,
        consignee,
        customsOfficeOfTransit,
        guaranteeDetails,
        departureOffice,
        destinationOffice,
        returnCopiesCustomsOffice,
        controlResult,
        seals,
        goodsItems
      )
  }
}
