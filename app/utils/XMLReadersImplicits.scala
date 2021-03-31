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

package utils

import com.lucidchart.open.xtract.EmptyError
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.ParseSuccess
import com.lucidchart.open.xtract.XmlReader

object XMLReadersImplicits {

  implicit class OptionalXmlReaderOps[A](xmlReader: XmlReader[A]) {

    def validateOnlyIfPresent: XmlReader[Option[A]] = XmlReader {
      xml =>
        xmlReader
          .read(xml)
          .fold(
            onFailure = {
              case EmptyError(_) :: Nil => ParseSuccess(Option.empty[A])
              case errors               => ParseFailure(errors)
            }
          )(
            onSuccess = a => ParseSuccess(Some(a))
          )
    }
  }

}
