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

package viewmodels.P5

import models.P5.departure.Commodity
import models.P5.departure.CommodityCode
import models.P5.departure.ConsignmentItem
import models.P5.departure.IE029Data
import models.P5.departure.Packaging

case class ConsignmentItemViewModel(implicit ie029Data: IE029Data) {

  val consignmentItem: Seq[ConsignmentItem] = ie029Data.data.Consignment.consignmentItems

}