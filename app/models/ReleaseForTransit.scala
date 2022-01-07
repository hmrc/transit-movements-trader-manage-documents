/*
 * Copyright 2022 HM Revenue & Customs
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
import utils.NonEmptyListXMLReader.xmlNonEmptyListReads

import scala.xml.NodeSeq

final case class ReleaseForTransit(
  header: Header,
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
  goodsItems: NonEmptyList[GoodsItem],
  itineraries: Seq[Itinerary],
  safetyAndSecurityCarrier: Option[SafetyAndSecurityCarrier],
  safetyAndSecurityConsignor: Option[SecurityConsignor],
  safetyAndSecurityConsignee: Option[SecurityConsignee]
)

object ReleaseForTransit {

  implicit val xmlReader1: XmlReader[ReleaseForTransit] = (xml: NodeSeq) => {
    for {
      header                     <- (__ \ "HEAHEA").read[Header].read(xml)
      principal                  <- (__ \ "TRAPRIPC1").read[Principal].read(xml)
      consignor                  <- (__ \ "TRACONCO1").read[Consignor](Consignor.xmlReaderRootLevel).optional.read(xml)
      consignee                  <- (__ \ "TRACONCE1").read[Consignee](Consignee.xmlReaderRootLevel).optional.read(xml)
      customsOfficeOfTransit     <- (__ \ "CUSOFFTRARNS").read(strictReadSeq[CustomsOfficeOfTransit]).read(xml)
      guaranteeDetails           <- (__ \ "GUAGUA").read(xmlNonEmptyListReads[GuaranteeDetails]).read(xml)
      departureOffice            <- (__ \ "CUSOFFDEPEPT" \ "RefNumEPT1").read[String].read(xml)
      destinationOffice          <- (__ \ "CUSOFFDESEST" \ "RefNumEST1").read[String].read(xml)
      returnCopiesCustomsOffice  <- (__ \ "CUSOFFRETCOPOCP").read[ReturnCopiesCustomsOffice].optional.read(xml)
      controlResult              <- (__ \ "CONRESERS").read[ControlResult].optional.read(xml)
      seals                      <- (__ \ "SEAINFSLI" \ "SEAIDSID" \ "SeaIdeSID1").read(strictReadSeq[String]).read(xml)
      goodsItems                 <- (__ \ "GOOITEGDS").read(xmlNonEmptyListReads[GoodsItem]).read(xml)
      itineraries                <- (__ \ "ITI").read(strictReadSeq[Itinerary]).read(xml)
      carrier                    <- (__ \ "CARTRA100").read[SafetyAndSecurityCarrier].optional.read(xml)
      safetyAndSecurityConsignor <- (__ \ "TRACORSEC037").read[SecurityConsignor](SecurityConsignor.xmlReaderRootLevel).optional.read(xml)
      safetyAndSecurityConsignee <- (__ \ "TRACONSEC029").read[SecurityConsignee](SecurityConsignee.xmlReaderRootLevel).optional.read(xml)
    } yield ReleaseForTransit(
      header,
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
      goodsItems,
      itineraries,
      carrier,
      safetyAndSecurityConsignor,
      safetyAndSecurityConsignee
    )
  }
}
