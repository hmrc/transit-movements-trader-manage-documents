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

package views.components

import models.reference.ControlResultData
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import viewmodels.ControlResult
import views.xml.components.table_5.control_results

import java.time.LocalDate

class ControlResultViewSpec extends FreeSpec with MustMatchers {

  "control_result" - {
    "render the code if description is empty exists" in {
      val viewmodel: ControlResult = ControlResult(
        ControlResultData("1234", ""),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      )

      val view = control_results.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")

      block.size() mustBe 1
      block.get(0).text() mustBe "1234"
      val inlineBlock = block.get(0).getElementsByTag("fo:inline")
      inlineBlock.size() mustBe 1
      inlineBlock.get(0).attr("font-style") mustBe "italic"
      inlineBlock.get(0).text() mustBe "1234"
    }

    "render the code if description exists" in {
      val viewmodel: ControlResult = ControlResult(
        ControlResultData("1234", "Short"),
        models.ControlResult("1234", LocalDate.of(2020, 2, 2))
      )

      val view = control_results.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")

      block.size() mustBe 1
      block.get(0).text() mustBe "Short"
      val inlineBlock = block.get(0).getElementsByTag("fo:inline")
      inlineBlock.size() mustBe 0
    }

  }
}
