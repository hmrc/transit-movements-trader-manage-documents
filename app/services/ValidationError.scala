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

package services

import play.api.libs.json.JsPath
import play.api.libs.json.JsonValidationError

trait ValidationError {

  def errorMessage: String
}

case class JsonError(typeOfData: String, errors: Seq[(JsPath, Seq[JsonValidationError])]) extends ValidationError {

  private val errorsToPrint = errors.map {
    error =>
      s"Error(s) at path `${error._1}`: " + error._2.map(_.message).mkString(", ")
  }

  override def errorMessage: String = s"There was an error reading the JSON when trying to retrieve $typeOfData data\n" + errorsToPrint.mkString("\n")
}

case class ReferenceDataRetrievalError(typeOfData: String, errorCode: Int, errorBody: String) extends ValidationError {

  override def errorMessage: String = s"Call to retrieve `$typeOfData` data failed with error code $errorCode.\n$errorBody"
}

case class ReferenceDataNotFound(path: String, value: String) extends ValidationError {

  def errorMessage: String = s"Error at path `$path`: value `$value` was not found in reference data"
}
