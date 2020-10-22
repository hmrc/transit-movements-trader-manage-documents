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

package viewmodels.tad
import models.DeclarationType
import models.reference.Country

//TODO: Do we need two document models?
//TODO: Spike having a single document model
case class TransitAccompanyingDocument(localReferenceNumber: String,
                                       declarationType: DeclarationType,
                                       singleCountryOfDispatch: Option[Country],
                                       singleCountryOfDestination: Option[Country])
