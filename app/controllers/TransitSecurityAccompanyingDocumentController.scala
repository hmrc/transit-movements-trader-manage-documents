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

package controllers

import play.api.mvc.Action
import play.api.mvc.ControllerComponents
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.inject.Inject
import scala.xml.NodeSeq

class TransitSecurityAccompanyingDocumentController @Inject()(
  cc: ControllerComponents
) extends BackendController(cc) {

  def get(): Action[NodeSeq] = Action(parse.xml) {
    implicit request =>
      lazy val path: Path            = Paths.get(getClass.getResource("/files/EmptyTAD.pdf").toURI)
      lazy val blankPdf: Array[Byte] = Files.readAllBytes(path)

      Ok(blankPdf)
  }
}
