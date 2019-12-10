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

package services

import cats.implicits._
import models.reference.Country

object TraderConverter extends Converter {

  private def traderWithEoriToViewModel(trader: models.TraderAtDestinationWithEori,
                                        path: String,
                                        countries: Seq[Country]): ValidationResult[viewmodels.TraderAtDestinationWithEori] =
    trader.countryCode match {
      case Some(countryCode) =>
        findReferenceData(countryCode, countries, s"$path.countryCode")
          .map(country => viewmodels.TraderAtDestinationWithEori(trader.eori, trader.name, trader.streetAndNumber, trader.postCode, trader.city, Some(country)))
      case None =>
        viewmodels.TraderAtDestinationWithEori(trader.eori, trader.name, trader.streetAndNumber, trader.postCode, trader.city, None).validNec
    }

  private def traderWithoutEoriToViewModel(trader: models.TraderAtDestinationWithoutEori,
                                           path: String,
                                           countries: Seq[Country]): ValidationResult[viewmodels.TraderAtDestinationWithoutEori] =
    findReferenceData(trader.countryCode, countries, s"$path.countryCode")
      .map(viewmodels.TraderAtDestinationWithoutEori(trader.name, trader.streetAndNumber, trader.postCode, trader.city, _))

  def toViewModel(trader: models.TraderAtDestination, path: String, countries: Seq[Country]): ValidationResult[viewmodels.TraderAtDestination] =
    trader match {
      case t: models.TraderAtDestinationWithEori    => traderWithEoriToViewModel(t, path, countries)
      case t: models.TraderAtDestinationWithoutEori => traderWithoutEoriToViewModel(t, path, countries)
    }
}
