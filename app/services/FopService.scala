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

package services

import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.fop.apps.FOUserAgent
import org.apache.fop.apps.FopFactory
import org.apache.xmlgraphics.util.MimeConstants
import play.twirl.api.XmlFormat

import java.io.StringReader
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
class FopService @Inject() (
  fopFactory: FopFactory
)(implicit ec: ExecutionContext) {
  def processTwirlXml(renderedDocument: XmlFormat.Appendable, MIME_PDF: String, autoDetectFontsForPDF: Boolean): Array[Byte] = ???

  private val userAgentBlock: FOUserAgent => Unit = {
    foUserAgent =>
      foUserAgent.setAccessibility(true)
      foUserAgent.setPdfUAEnabled(true)
      foUserAgent.setAuthor("HMRC forms service")
      foUserAgent.setProducer("HMRC forms services")
      foUserAgent.setCreator("HMRC forms services")
      foUserAgent.setSubject("Get your Transit Movements Trader Manage Documents by post form")
      foUserAgent.setTitle("Get your Transit Movements Trader Manage Documents by post form")
  }

  def render(input: String): Future[Array[Byte]] = Future {

    val out = new ByteArrayOutputStream()

    val userAgent = fopFactory.newFOUserAgent()
    userAgentBlock(userAgent)

    val fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, out)

    val transformerFactory = TransformerFactory.newInstance()
    val transformer        = transformerFactory.newTransformer()

    val source = new StreamSource(new StringReader(input))
    val result = new SAXResult(fop.getDefaultHandler)

    transformer.transform(source, result)

    out.toByteArray
  }
}
