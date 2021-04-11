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

import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import utils.BinaryToBooleanXMLReader.xmlBinaryToBooleanReads
import utils.BigDecimalXMLReader._
import utils.LocalDateXMLReader._

import java.time.LocalDate
import scala.xml.NodeSeq

final case class Header(
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
)

object Header {

  implicit val xmlReader1: XmlReader[Header] = (xml: NodeSeq) => {
    for {
      mrn                            <- (__ \ "DocNumHEA5").read[String].read(xml)
      decType                        <- (__ \ "TypOfDecHEA24").read[DeclarationType].read(xml)
      countryOfDispatch              <- (__ \ "CouOfDisCodHEA55").read[String].optional.read(xml)
      countryOfDestination           <- (__ \ "CouOfDesCodHEA30").read[String].optional.read(xml)
      transportIdentity              <- (__ \ "IdeOfMeaOfTraAtDHEA78").read[String].optional.read(xml)
      transportCountry               <- (__ \ "NatOfMeaOfTraAtDHEA80").read[String].optional.read(xml)
      acceptanceDate                 <- (__ \ "AccDatHEA158").read[LocalDate].read(xml)
      numberOfItems                  <- (__ \ "TotNumOfIteHEA305").read[Int].read(xml)
      numberOfPackages               <- (__ \ "TotNumOfPacHEA306").read[Int].optional.read(xml)
      grossMass                      <- (__ \ "TotGroMasHEA307").read[BigDecimal].read(xml)
      printBindingItinerary          <- (__ \ "BinItiHEA246").read[Boolean](xmlBinaryToBooleanReads).read(xml)
      authId                         <- (__ \ "AutIdHEA380").read[String].optional.read(xml)
      returnCopy                     <- (__ \ "NCTRetCopHEA104").read[Boolean](xmlBinaryToBooleanReads).read(xml)
      circumstanceIndicator          <- (__ \ "SpeCirIndHEA1").read[String].optional.read(xml)
      security                       <- (__ \ "SecHEA358").read[Int].optional.read(xml)
      commercialReferenceNumber      <- (__ \ "ComRefNumHEA").read[String].optional.read(xml)
      methodOfPayment                <- (__ \ "TraChaMetOfPayHEA1").read[String].optional.read(xml)
      identityOfTransportAtBorder    <- (__ \ "IdeOfMeaOfTraCroHEA85").read[String].optional.read(xml)
      nationalityOfTransportAtBorder <- (__ \ "NatOfMeaOfTraCroHEA87").read[String].optional.read(xml)
      transportModeAtBorder          <- (__ \ "TraModAtBorHEA76").read[String].optional.read(xml)
      agreedLocationOfGoodsCode      <- (__ \ "AgrLocOfGooCodHEA38").read[String].optional.read(xml)
    } yield
      Header(
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
        agreedLocationOfGoodsCode
      )
  }
}
