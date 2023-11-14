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

package models

import generated.p5._
import scalaxb.ElemName
import scalaxb.XMLFormat

case class NamespaceBinding[T](namespace: String, format: XMLFormat[T]) {

  def stack: List[ElemName] = List(ElemName(Some(namespace), ""))
}

object NamespaceBinding {

  private def bind[T](message: String)(implicit format: XMLFormat[T]): NamespaceBinding[T] =
    new NamespaceBinding[T](s"ncts:$message", implicitly)

  implicit val ie015NameSpaceBinding: NamespaceBinding[CC015CType] = bind("CC015C")

  implicit val ie029NameSpaceBinding: NamespaceBinding[CC029CType] = bind("CC029C")

  implicit val ie043NameSpaceBinding: NamespaceBinding[CC043CType] = bind("CC043C")
}
