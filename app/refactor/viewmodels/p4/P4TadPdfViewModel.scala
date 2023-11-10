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

package refactor.viewmodels.p4

import generated.p4._
import generated.p5._
import models.reference.Country

case class P4TadPdfViewModel()

object P4TadPdfViewModel {

  // P4
  def apply(ie029: CC029BType): P4TadPdfViewModel =
    new P4TadPdfViewModel()

  // P5 transition
  def apply(ie015: CC015CType, ie029: CC029CType, countries: Seq[Country]): P4TadPdfViewModel =
    new P4TadPdfViewModel()
}
