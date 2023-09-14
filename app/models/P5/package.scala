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

package object P5 {

  implicit class RichOptionSeqT[T](ts: Option[Seq[T]]) {

    def showAll: String = ts.getOrElse(Nil).showAll

    def showFirstAndLast: String = {
      val (headOption, lastOption) = ts match {
        case Some(head :: tail) => (Some(head), tail.lastOption)
        case _                  => (None, None)
      }
      Seq(headOption, lastOption).flatten.map(_.toString).mkString("...")
    }
  }

  implicit class RichSeqT[T](ts: Seq[T]) {

    def showAll: String = ts.map(_.toString).mkString("; ")

    def showAllList: Seq[String] = ts.map(_.toString)

    def total(f: T => Int): Int = ts.map(f).sum
  }

}
