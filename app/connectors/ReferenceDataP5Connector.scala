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

package connectors

import cats.implicits._
import config.ReferenceDataP5Config
import play.api.Logging
import play.api.http.Status
import play.api.libs.json.Reads
import services.JsonError
import services.ReferenceDataRetrievalError
import services.ValidationResult
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.http.HttpReads
import uk.gov.hmrc.http.HttpResponse
import sttp.model.HeaderNames

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class ReferenceDataP5Connector @Inject() (
  config: ReferenceDataP5Config,
  httpClient: HttpClient
) extends Logging {

  private def referenceDataReads[A](typeOfData: String)(implicit ev: Reads[A]): HttpReads[ValidationResult[A]] =
    (_, _, response: HttpResponse) =>
      response.status match {
        case Status.OK =>
          (response.json \ "data")
            .validate[A]
            .fold(
              errors => JsonError(typeOfData, errors).invalidNec,
              result => result.validNec
            )
        case _ => ReferenceDataRetrievalError(typeOfData, response.status, response.body).invalidNec
      }

  def getList[A](listName: String)(implicit ec: ExecutionContext, hc: HeaderCarrier, reads: Reads[A]): Future[ValidationResult[A]] = {

    val customReads: HttpReads[ValidationResult[A]] = referenceDataReads[A](listName)

    val serviceUrl = s"${config.referenceDataURL}/lists/$listName"

    httpClient.GET[ValidationResult[A]](serviceUrl, headers = version2Header)(customReads, implicitly, implicitly)
  }

  private def version2Header: Seq[(String, String)] = Seq(
    HeaderNames.Accept -> "application/vnd.hmrc.2.0+json"
  )
}
