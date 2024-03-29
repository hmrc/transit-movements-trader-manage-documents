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

package viewmodels

import cats.data.NonEmptyList
import generators.ViewModelGenerators
import models.reference.Country
import models.SensitiveGoodsInformation
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PermissionToStartUnloadingSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ViewModelGenerators with OptionValues {

  private val genPermissionWithoutListOfItems = for {
    permission                <- arbitrary[PermissionToStartUnloading]
    containers                <- listWithMaxSize(stringWithMaxLength(17), 1)
    onePackage                <- arbitrary[Package]
    mentions                  <- listWithMaxSize(arbitrary[SpecialMention], 4)
    documents                 <- listWithMaxSize(arbitrary[ProducedDocument], 4)
    sensitiveGoodsInformation <- listWithMaxSize(arbitrary[SensitiveGoodsInformation], 1)
  } yield (permission, containers, onePackage, mentions, documents, sensitiveGoodsInformation)

  "must have a consignor" - {

    "when all goods items have the same consignor" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[Consignor]) {
        (permission, consignor) =>
          val goodsItemsWithConsignor = permission.goodsItems.map(_.copy(consignor = Some(consignor)))

          val permissionWithConsignors = permission.copy(goodsItems = goodsItemsWithConsignor)

          permissionWithConsignors.consignorOne.value mustEqual consignor
      }
    }

    "when no goods items have a consignor but header has a consignor" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[Consignor]) {
        (permission, consignor) =>
          val goodsItemsNoConsignor = permission.goodsItems.map(_.copy(consignor = None))

          val permissionWithConsignors = permission.copy(
            consignor = Some(consignor),
            goodsItems = goodsItemsNoConsignor
          )

          permissionWithConsignors.consignorOne.value mustEqual consignor
      }
    }
  }

  "must not have a consignor" - {

    "when no goods items have consignors and header has no consignor" in {

      forAll(arbitrary[PermissionToStartUnloading]) {
        permission =>
          val goodsItemsWithNoConsignors = permission.goodsItems.map(_.copy(consignor = None))

          val permissionWithNoConsignors = permission.copy(consignor = None, goodsItems = goodsItemsWithNoConsignors)

          permissionWithNoConsignors.consignorOne mustBe empty
      }
    }

    "when some goods items have different consignors" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Consignor], arbitrary[Consignor]) {
        (permission, goodsItem, consignor1, consignor2) =>
          whenever(consignor1 != consignor2) {

            val goodsItemWithConsignor1 = goodsItem.copy(consignor = Some(consignor1))
            val goodsItemWithConsignor2 = goodsItem.copy(consignor = Some(consignor2))

            val updatedPermission = permission.copy(consignor = None, goodsItems = NonEmptyList(goodsItemWithConsignor1, List(goodsItemWithConsignor2)))

            updatedPermission.consignorOne mustBe empty
          }
      }
    }

    "when some goods items have no consignor and the rest all have the same consignor" in {

      val gen = for {
        permission <- arbitrary[PermissionToStartUnloading]
        items1     <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 5)
        items2     <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 5)
        consignor  <- arbitrary[Consignor]
      } yield (permission, items1, items2, consignor)

      forAll(gen) {
        case (permission, items1, items2, consignor) =>
          val itemsWithNoConsignor = items1.map(_.copy(consignor = None))
          val itemsWithConsignor   = items2.map(_.copy(consignor = Some(consignor)))

          val updatedPermission = permission.copy(consignor = None, goodsItems = itemsWithNoConsignor.concatNel(itemsWithConsignor))

          updatedPermission.consignorOne mustBe empty
      }
    }
  }

  "must have a consignee" - {

    "when all goods items have the same consignee" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[Consignee]) {
        (permission, consignee) =>
          val goodsItemsWithConsignee = permission.goodsItems.map(_.copy(consignee = Some(consignee)))

          val permissionWithConsignees = permission.copy(goodsItems = goodsItemsWithConsignee)

          permissionWithConsignees.consigneeOne.value mustEqual consignee
      }
    }

    "when no goods items have a consignee but header has a consignee" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[Consignee]) {
        (permission, consignee) =>
          val goodsItemsWithNoConsignee = permission.goodsItems.map(_.copy(consignee = None))

          val permissionWithConsignees = permission.copy(consignee = Some(consignee), goodsItems = goodsItemsWithNoConsignee)

          permissionWithConsignees.consigneeOne.value mustEqual consignee
      }
    }
  }

  "must not have a consignee" - {

    "when no goods or header has consignees" in {

      forAll(arbitrary[PermissionToStartUnloading]) {
        permission =>
          val goodsItemsWithNoConsignees = permission.goodsItems.map(_.copy(consignee = None))

          val permissionWithNoConsignees = permission.copy(consignee = None, goodsItems = goodsItemsWithNoConsignees)

          permissionWithNoConsignees.consigneeOne mustBe empty
      }
    }

    "when some goods items have different consignees" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Consignee], arbitrary[Consignee]) {
        (permission, goodsItem, consignee1, consignee2) =>
          whenever(consignee1 != consignee2) {

            val goodsItemWithConsignee1 = goodsItem.copy(consignee = Some(consignee1))
            val goodsItemWithConsignee2 = goodsItem.copy(consignee = Some(consignee2))

            val updatedPermission = permission.copy(consignee = None, goodsItems = NonEmptyList(goodsItemWithConsignee1, List(goodsItemWithConsignee2)))

            updatedPermission.consigneeOne mustBe empty
          }
      }
    }

    "when some goods items have no consignee and the rest all have the same consignee" in {

      val gen = for {
        permission <- arbitrary[PermissionToStartUnloading]
        items1     <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 5)
        items2     <- nonEmptyListWithMaxSize(arbitrary[GoodsItem], 5)
        consignee  <- arbitrary[Consignee]
      } yield (permission, items1, items2, consignee)

      forAll(gen) {
        case (permission, items1, items2, consignee) =>
          val itemsWithNoConsignee = items1.map(_.copy(consignee = None))
          val itemsWithConsignee   = items2.map(_.copy(consignee = Some(consignee)))

          val updatedPermission = permission.copy(consignee = None, goodsItems = itemsWithNoConsignee.concatNel(itemsWithConsignee))

          updatedPermission.consigneeOne mustBe empty
      }
    }
  }

  "must have a country of dispatch" - {

    "when all goods items have the same country of dispatch" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[Country]) {
        (permission, countryOfDispatch) =>
          val goodsItemsWithCountryOfDispatch = permission.goodsItems.map(_.copy(countryOfDispatch = Some(countryOfDispatch)))

          val permissionWithCountriesOfDispatch = permission.copy(goodsItems = goodsItemsWithCountryOfDispatch)

          permissionWithCountriesOfDispatch.countryOfDispatch.value mustEqual countryOfDispatch
      }
    }
  }

  "must not have a country of dispatch" - {

    "when some goods items have different countries of dispatch" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Country], arbitrary[Country]) {
        (permission, goodsItem, countryOfDispatch1, countryOfDispatch2) =>
          whenever(countryOfDispatch1 != countryOfDispatch2) {

            val goodsItemWithCountryOfDispatch1 = goodsItem.copy(countryOfDispatch = Some(countryOfDispatch1))
            val goodsItemWithCountryOfDispatch2 = goodsItem.copy(countryOfDispatch = Some(countryOfDispatch2))

            val updatedPermission = permission.copy(goodsItems = NonEmptyList(goodsItemWithCountryOfDispatch1, List(goodsItemWithCountryOfDispatch2)))

            updatedPermission.countryOfDispatch mustBe empty
          }
      }
    }
  }

  "must have a country of destination" - {

    "when all goods items have the same country of destination" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[Country]) {
        (permission, countryOfDestination) =>
          val goodsItemsWithCountryOfDestination = permission.goodsItems.map(_.copy(countryOfDestination = Some(countryOfDestination)))

          val permissionWithCountriesOfDestination = permission.copy(goodsItems = goodsItemsWithCountryOfDestination)

          permissionWithCountriesOfDestination.countryOfDestination.value mustEqual countryOfDestination
      }
    }
  }

  "must not have a country of destination" - {

    "when some goods items have different countries of destination" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Country], arbitrary[Country]) {
        (permission, goodsItem, countryOfDestination1, countryOfDestination2) =>
          whenever(countryOfDestination1 != countryOfDestination2) {

            val goodsItemWithCountryOfDestination1 = goodsItem.copy(countryOfDestination = Some(countryOfDestination1))
            val goodsItemWithCountryOfDestination2 = goodsItem.copy(countryOfDestination = Some(countryOfDestination2))

            val updatedPermission = permission.copy(goodsItems = NonEmptyList(goodsItemWithCountryOfDestination1, List(goodsItemWithCountryOfDestination2)))

            updatedPermission.countryOfDestination mustBe empty
          }
      }
    }
  }

  "must indicate that a list of items should be printed" - {

    "when there are more than 1 goods items" in {

      forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem]) {
        (permission, extraGoodsItem) =>
          val updatedPermission = permission.copy(goodsItems = permission.goodsItems :+ extraGoodsItem)

          updatedPermission.printListOfItems mustEqual true
      }
    }

    "when there is a single goods item" - {

      "with more than 1 container" in {

        forAll(arbitrary[PermissionToStartUnloading], stringWithMaxLength(17), stringWithMaxLength(17)) {
          (permission, container1, container2) =>
            val updatedGoodsItem = permission.goodsItems.head.copy(containers = Seq(container1, container2))

            val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

            updatedPermission.printListOfItems mustEqual true
        }
      }

      "with more than one package" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[Package], arbitrary[Package]) {
          (permission, package1, package2) =>
            val updatedGoodsItem = permission.goodsItems.head.copy(packages = NonEmptyList(package1, List(package2)))

            val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

            updatedPermission.printListOfItems mustEqual true
        }
      }

      "with more than 4 special mentions" in {

        val gen = for {
          permission       <- arbitrary[PermissionToStartUnloading]
          numberOfMentions <- Gen.choose(5, 99)
          mentions         <- Gen.listOfN(numberOfMentions, arbitrary[SpecialMention])
        } yield (permission, mentions)

        forAll(gen) {
          case (permission, mentions) =>
            val updatedGoodsItem = permission.goodsItems.head.copy(specialMentions = mentions)

            val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

            updatedPermission.printListOfItems mustEqual true
        }
      }

      "with more than 4 produced documents" in {

        val gen = for {
          permission        <- arbitrary[PermissionToStartUnloading]
          numberOfDocuments <- Gen.choose(5, 99)
          documents         <- Gen.listOfN(numberOfDocuments, arbitrary[ProducedDocument])
        } yield (permission, documents)

        forAll(gen) {
          case (permission, documents) =>
            val updatedGoodsItem = permission.goodsItems.head.copy(producedDocuments = documents)

            val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

            updatedPermission.printListOfItems mustEqual true
        }
      }

      "with more than 1 sensitive goods item" in {

        forAll(arbitrary[PermissionToStartUnloading], stringWithMaxLength(2), Gen.choose(1, 99999)) {
          (permission, code, quantity) =>
            val updatedGoodsItem = permission.goodsItems.head
              .copy(sensitiveGoodsInformation = Seq(SensitiveGoodsInformation(Some(code), quantity), SensitiveGoodsInformation(Some(code), quantity)))

            val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

            updatedPermission.printListOfItems mustEqual true
        }
      }

      "must indicate that a list of items should not be printed" - {

        "when there is one goods item with at most one container, one package, at most four special mentions and at most four produced documents" in {

          forAll(genPermissionWithoutListOfItems) {
            case (permission, containers, onePackage, mentions, documents, sensitiveGoodsInformation) =>
              val updatedGoodsItem = permission.goodsItems.head.copy(
                containers = containers,
                packages = NonEmptyList.one(onePackage),
                specialMentions = mentions,
                producedDocuments = documents,
                sensitiveGoodsInformation = sensitiveGoodsInformation
              )

              val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

              updatedPermission.printListOfItems mustEqual false
          }
        }
      }
    }
  }

  "must indicate that various consignors be printed" - {

    "when a list of items should be printed" - {

      "and there are goods items with different consignors" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Consignor], arbitrary[Consignor]) {
          (permission, goodsItem, consignor1, consignor2) =>
            whenever(consignor1 != consignor2) {

              val goodsItemsWithConsignor = permission.goodsItems.map(_.copy(consignor = Some(consignor1)))

              val updatedGoodsItem = goodsItem.copy(consignor = Some(consignor2))

              val allGoodsItems = goodsItemsWithConsignor :+ updatedGoodsItem

              val updatedPermission = permission.copy(goodsItems = allGoodsItems)

              updatedPermission.printVariousConsignors mustEqual true
            }
        }
      }
    }
  }

  "must indicate that various consignors should not be printed" - {

    "when a list of items should not be printed" in {

      forAll(genPermissionWithoutListOfItems) {
        case (permission, containers, onePackage, mentions, documents, sensitiveGoodsInformation) =>
          val updatedGoodsItem = permission.goodsItems.head.copy(
            containers = containers,
            packages = NonEmptyList.one(onePackage),
            specialMentions = mentions,
            producedDocuments = documents,
            sensitiveGoodsInformation = sensitiveGoodsInformation
          )

          val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

          updatedPermission.printVariousConsignors mustEqual false
      }
    }

    "when a list of items should be printed" - {

      "and no goods items have consignors" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem]) {
          (permission, goodsItems) =>
            val allGoodsItems = permission.goodsItems :+ goodsItems

            val updatedGoodsItems = allGoodsItems.map(_.copy(consignor = None))

            val updatedPermission = permission.copy(goodsItems = updatedGoodsItems)

            updatedPermission.printVariousConsignors mustEqual false
        }
      }

      "and there is a single consignor" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Consignor]) {
          (permission, goodsItems, consignor) =>
            val allGoodsItems = permission.goodsItems :+ goodsItems

            val updatedGoodsItems = allGoodsItems.map(_.copy(consignor = Some(consignor)))

            val updatedPermission = permission.copy(goodsItems = updatedGoodsItems)

            updatedPermission.printVariousConsignors mustEqual false
        }
      }
    }
  }

  "must indicate that various consignees be printed" - {

    "when a list of items should be printed" - {

      "and there are goods items with different consignees" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Consignee], arbitrary[Consignee]) {
          (permission, goodsItem, consignee1, consignee2) =>
            whenever(consignee1 != consignee2) {

              val goodsItemsWithConsignee = permission.goodsItems.map(_.copy(consignee = Some(consignee1)))

              val updatedGoodsItem = goodsItem.copy(consignee = Some(consignee2))

              val allGoodsItems = goodsItemsWithConsignee :+ updatedGoodsItem

              val updatedPermission = permission.copy(goodsItems = allGoodsItems)

              updatedPermission.printVariousConsignees mustEqual true
            }
        }
      }
    }
  }

  "must indicate that various consignees should not be printed" - {

    "when a list of items should not be printed" in {

      forAll(genPermissionWithoutListOfItems) {
        case (permission, containers, onePackage, mentions, documents, sensitiveGoodsInformation) =>
          val updatedGoodsItem = permission.goodsItems.head.copy(
            containers = containers,
            packages = NonEmptyList.one(onePackage),
            specialMentions = mentions,
            producedDocuments = documents,
            sensitiveGoodsInformation = sensitiveGoodsInformation
          )

          val updatedPermission = permission.copy(goodsItems = NonEmptyList.one(updatedGoodsItem))

          updatedPermission.printVariousConsignees mustEqual false
      }
    }

    "when a list of items should be printed" - {

      "and no goods items have consignees" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem]) {
          (permission, goodsItems) =>
            val allGoodsItems = permission.goodsItems :+ goodsItems

            val updatedGoodsItems = allGoodsItems.map(_.copy(consignee = None))

            val updatedPermission = permission.copy(goodsItems = updatedGoodsItems)

            updatedPermission.printVariousConsignees mustEqual false
        }
      }

      "and there is a single consignee" in {

        forAll(arbitrary[PermissionToStartUnloading], arbitrary[GoodsItem], arbitrary[Consignee]) {
          (permission, goodsItems, consignee) =>
            val allGoodsItems = permission.goodsItems :+ goodsItems

            val updatedGoodsItems = allGoodsItems.map(_.copy(consignee = Some(consignee)))

            val updatedPermission = permission.copy(goodsItems = updatedGoodsItems)

            updatedPermission.printVariousConsignees mustEqual false
        }
      }
    }
  }

  "must print 'total number of packages' value" - {

    "must print the value as blank when 'total number of packages' data is missing" in {
      val permissionToStartUnloading = arbitrary[PermissionToStartUnloading].sample.value
      permissionToStartUnloading.copy(numberOfPackages = None).totalNumberOfPackages mustEqual ""
    }

    "must print the value when 'total number of packages' data is available" in {
      val numberOfPackage            = 10
      val permissionToStartUnloading = arbitrary[PermissionToStartUnloading].sample.value
      permissionToStartUnloading.copy(numberOfPackages = Some(numberOfPackage)).totalNumberOfPackages mustEqual numberOfPackage.toString
    }

  }
}
