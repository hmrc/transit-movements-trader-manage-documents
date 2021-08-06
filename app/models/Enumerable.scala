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

package models

import play.api.libs.json._
import play.api.mvc.PathBindable

trait Enumerable[A] {

  def withName(str: String): Option[A]
}

object Enumerable {

  def apply[A](entries: (String, A)*): Enumerable[A] =
    (str: String) => entries.toMap.get(str)

  trait Implicits {

    implicit def reads[A](implicit ev: Enumerable[A]): Reads[A] =
      Reads {
        case JsString(str) =>
          ev.withName(str)
            .map {
              s =>
                JsSuccess(s)
            }
            .getOrElse(JsError("error.invalid"))
        case _ =>
          JsError("error.invalid")
      }

    implicit def writes[A](implicit ev: Enumerable[A]): Writes[A] =
      Writes(
        value => JsString(value.toString)
      )

    implicit def pathBindable[A](implicit ev: Enumerable[A]): PathBindable[A] =
      new PathBindable[A] {

        override def bind(key: String, value: String): Either[String, A] =
          ev.withName(value).toRight("error.invalid")

        override def unbind(key: String, value: A): String =
          value.toString
      }
  }
}
