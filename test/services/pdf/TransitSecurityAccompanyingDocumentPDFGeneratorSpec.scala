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

package services.pdf

import generators.ViewmodelGenerators
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks.forAll
import play.api.Application
import play.api.Environment
import play.api.inject
import play.api.inject.guice.GuiceApplicationBuilder
import viewmodels.TransitSecurityAccompanyingDocumentPDF
import views.xml.securityComponents._

class TransitSecurityAccompanyingDocumentPDFGeneratorSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with ViewmodelGenerators
    with OptionValues
    with ScalaFutures {

  def arb[A: Arbitrary]: Gen[A] = Arbitrary.arbitrary[A]

  // Spying on the tables to ensure the right values are passed in
  lazy val spiedTable1: table_1.table    = Mockito.spy(new table_1.table())
  lazy val spiedTable2: table_2.table    = Mockito.spy(new table_2.table())
  lazy val spiedTable3: table_3.table    = Mockito.spy(new table_3.table())
  lazy val spiedTable4: table_4.table    = Mockito.spy(new table_4.table())
  lazy val spiedPage2: second_page.table = Mockito.spy(new second_page.table())

  implicit override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(
      inject.bind(classOf[table_1.table]).toInstance(spiedTable1),
      inject.bind(classOf[table_2.table]).toInstance(spiedTable2),
      inject.bind(classOf[table_3.table]).toInstance(spiedTable3),
      inject.bind(classOf[table_4.table]).toInstance(spiedTable4),
      inject.bind(classOf[second_page.table]).toInstance(spiedPage2)
    )
    .build()

  private lazy val service: TSADPdfGenerator = app.injector.instanceOf[TSADPdfGenerator]
  val env: Environment                       = app.injector.instanceOf[Environment]

  "TransitSecurityAccompanyingDocumentPDFGenerator" - {

    "return pdf" in {

      forAll(arb[TransitSecurityAccompanyingDocumentPDF]) {
        tad =>
          service.generate(tad) mustBe an[Array[Byte]]

          verify(spiedTable1, times(1))
            .apply(
              "TRANSIT / SECURITY – ACCOMPANYING DOCUMENT",
              false,
              tad
            )

          verify(spiedTable2, times(1))
            .apply(
              tad.printVariousSecurityConsignors,
              tad.securityConsigneeOne,
              tad.printVariousSecurityConsignors,
              tad.securityConsignorOne,
              tad.secCarrierFlg,
              tad.safetyAndSecurityCarrier
            )

          verify(spiedTable3, times(1))
            .apply(
              tad.principal,
              tad.departureOffice.referenceWithOfficeId,
              tad.acceptanceDate.map(_.formattedDate),
              tad.customsOfficeOfTransit,
              tad.guaranteeDetails,
              Some(tad.destinationOffice),
              tad.authId,
              tad.controlResult
            )

          verify(spiedTable4, times(1))
            .apply(
              tad.seals,
              tad.printBindingItinerary,
              tad.controlResult
            )

          verify(spiedPage2, times(1))
            .apply(
              tad.departureOffice.office.id,
              tad.movementReferenceNumber,
              tad.acceptanceDate.map(_.formattedDate),
              tad.goodsItems,
              tad.declarationType,
              tad.placeOfUnloadingCode
            )

          reset(spiedTable1, spiedTable2, spiedTable3, spiedTable4)
      }
    }
  }
}
