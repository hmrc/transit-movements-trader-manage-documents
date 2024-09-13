/*
 * Copyright 2024 HM Revenue & Customs
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

package services.pdf

import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.fop.apps.FopFactory
import org.apache.xmlgraphics.util.MimeConstants
import play.twirl.api.XmlFormat

import java.io.StringReader
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

@Singleton
class FopService @Inject() (
  fopFactory: FopFactory
) {

  def processTwirlXml(document: XmlFormat.Appendable): Array[Byte] = {
    val out       = new ByteArrayOutputStream()
    val userAgent = fopFactory.newFOUserAgent()
    val fop       = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, out)

    val transformerFactory = TransformerFactory.newInstance()
    val transformer        = transformerFactory.newTransformer()

    val source = new StreamSource(new StringReader(document.body))

    val result = new SAXResult(fop.getDefaultHandler)

    transformer.transform(source, result)
    out.toByteArray
  }
}
