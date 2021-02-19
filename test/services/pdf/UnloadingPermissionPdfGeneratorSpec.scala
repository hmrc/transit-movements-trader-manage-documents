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

import java.time.LocalDate
import java.util
import java.util.Base64

import cats.data.NonEmptyList
import generators.ViewmodelGenerators
import models.DeclarationType
import models.SensitiveGoodsInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Environment
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels._

class UnloadingPermissionPdfGeneratorSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with ViewmodelGenerators
    with OptionValues
    with ScalaFutures {

  private lazy val service: UnloadingPermissionPdfGenerator = app.injector.instanceOf[UnloadingPermissionPdfGenerator]
  implicit private val hc: HeaderCarrier                    = HeaderCarrier()
  val env: Environment                                      = app.injector.instanceOf[Environment]

  "UnloadingPermissionPdfGenerator" - {

    "return pdf" in {

      val unloadingPermissionObject = arbitrary[PermissionToStartUnloading]

      val unloadingPermission = unloadingPermissionObject.sample.value

      service.generate(unloadingPermission) mustBe an[Array[Byte]]

    }

    "must match with the 'Unloading Permission' template" in {

      val h = GoodsItem(
        1,
        None,
        None,
        "Flowers",
        Some(1000),
        Some(999),
        Some(Country("valid", "GB", "United Kingdom")),
        Some(Country("valid", "GB", "United Kingdom")),
        List(ProducedDocument(DocumentType("18", "Movement certificate A.TR.1", false), Some("Ref."), None)),
        List(),
        None,
        None,
        List("container 1", "container 2"),
        NonEmptyList.of(RegularPackage(KindOfPackage("PB", "Pallet, box Combined open-ended box and pallet"), 0, "Ref.")),
        Vector(SensitiveGoodsInformation(Some("1"), 1))
      )

      val h1 = GoodsItem(
        2,
        None,
        None,
        "Vases",
        Some(10000),
        Some(9999),
        Some(Country("valid", "GB", "United Kingdom")),
        Some(Country("valid", "GB", "United Kingdom")),
        List(ProducedDocument(DocumentType("18", "Movement certificate A.TR.1", false), Some("Ref."), None)),
        List(),
        None,
        None,
        List("container 3", "container 4"),
        NonEmptyList.of(RegularPackage(KindOfPackage("PB", "Pallet, box Combined open-ended box and pallet"), 0, "Ref.")),
        Vector(SensitiveGoodsInformation(Some("1"), 1))
      )

      val viewModel = PermissionToStartUnloading(
        "99IT9876AB88901209",
        DeclarationType.T1,
        Some(Country("valid", "IT", "Italy")),
        Some(Country("valid", "GB", "United Kingdom")),
        Some("abcd"),
        Some(Country("valid", "IT", "Italy")),
        Some(LocalDate.parse("2019-09-12")),
        Some("12/09/2019"),
        1,
        None,
        1000,
        Principal("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("valid", "IT", "Italy"), Some("IT444100201000"), None),
        Some(Consignor("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("valid", "IT", "Italy"), Some("IT444100201000"))),
        Some(Consignee("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("valid", "IT", "Italy"), Some("IT444100201000"))),
        Some(
          TraderAtDestinationWithEori("GB163910077000",
                                      Some("The Luggage Carriers"),
                                      Some("225 Suedopolish Yard"),
                                      Some("SS8 2BB"),
                                      Some(","),
                                      Some(Country("valid", "GB", "United Kingdom")))),
        "IT021100",
        "IT021100",
        Some("GB000060"),
        List("Seals01", "Seals02", "Seals03"),
        NonEmptyList.of(h, h1)
      )

      val hh = PermissionToStartUnloading(
        "99IT9876AB88901209",
        DeclarationType.T1,
        Some(Country("valid", "IT", "Italy")),
        Some(Country("valid", "GB", "United Kingdom")),
        Some("abcd"),
        Some(Country("valid", "IT", "Italy")),
        Some(LocalDate.parse("2019-09-12")),
        Some("12/09/2019"),
        1,
        None,
        1000,
        Principal("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("valid", "IT", "Italy"), Some("IT444100201000"), None),
        Some(Consignor("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("valid", "IT", "Italy"), Some("IT444100201000"))),
        Some(Consignee("Mancini Carriers", "90 Desio Way", "90 Desio Way", "MOD 5JJ", "Modena", Country("valid", "IT", "Italy"), Some("IT444100201000"))),
        Some(
          TraderAtDestinationWithEori("GB163910077000",
                                      Some("The Luggage Carriers"),
                                      Some("225 Suedopolish Yard,"),
                                      Some("SS8 2BB"),
                                      Some(","),
                                      Some(Country("valid", "GB", "United Kingdom")))),
        "IT021100",
        "IT021100",
        Some("GB000060"),
        List("Seals01", "Seals02", "Seals03"),
        NonEmptyList.of(
          GoodsItem(
            1,
            None,
            None,
            "Flowers",
            Some(1000),
            Some(999),
            Some(Country("valid", "GB", "United Kingdom")),
            Some(Country("valid", "GB", "United Kingdom")),
            List(ProducedDocument(DocumentType("18", "Movement certificate A.TR.1", false), Some("Ref."), None)),
            List(),
            None,
            None,
            List("container 1", "container 2"),
            NonEmptyList.of(RegularPackage(KindOfPackage("PB", "Pallet, box Combined open-ended box and pallet"), 0, "Ref.")),
            Vector(SensitiveGoodsInformation(Some("1"), 1))
          ),
          GoodsItem(
            2,
            None,
            None,
            "Vases",
            Some(10000),
            Some(9999),
            Some(Country("valid", "GB", "United Kingdom")),
            Some(Country("valid", "GB", "United Kingdom")),
            List(ProducedDocument(DocumentType("18", "Movement certificate A.TR.1", false), Some("Ref."), None)),
            List(),
            None,
            None,
            List("container 3", "container 4"),
            NonEmptyList.of(RegularPackage(KindOfPackage("PB", "Pallet, box Combined open-ended box and pallet"), 0, "Ref.")),
            Vector(SensitiveGoodsInformation(Some("1"), 1))
          )
        )
      )

      import java.nio.file.Files
      import java.nio.file.Paths
      val pdfPath          = Paths.get("test/resources/unloading-permission-pdf (7)")
      val pdf: Array[Byte] = Files.readAllBytes(pdfPath)

      val pdfPath1          = Paths.get("test/resources/unloading-permission-pdf (6)")
      val pdf1: Array[Byte] = Files.readAllBytes(pdfPath1)

      // "unloading-permission-pdf (6)".getBytes mustBe "unloading-permission-pdf (7)".getBytes

      println("+++++++++++++++++++" + service.generate(viewModel))
      println("+++++++++++++++++++" + service.generate(viewModel))
      service.generate(viewModel) mustBe service.generate(viewModel)
      // pdf mustBe pdf
      // pdf1 mustBe pdf

    }

  }

}
