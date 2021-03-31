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

import models.reference.AdditionalInformation
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import viewmodels.SpecialMention
import views.xml.components.table_2.item.special_mentions

import scala.collection.JavaConverters.asScalaIterator

class SpecialMentionsViewSpec extends FreeSpec with MustMatchers {

  "special_mentions" - {
    "render only the description" in {
      val viewmodel: SpecialMention = SpecialMention(
        AdditionalInformation("1234", "Short"),
        models.SpecialMention(Some("AddInf"), Some("1234"), None, None)
      )
      val view = special_mentions.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")
      asScalaIterator(block.iterator()).toList must have length 1
      asScalaIterator(block.iterator()).toList.head.text() mustBe "Short"
    }
    "render not Only the description" in {
      val viewmodel: SpecialMention = SpecialMention(
        AdditionalInformation("2341", "Longer String"),
        models.SpecialMention(Some("AddInf"), Some("2341"), Some(true), None)
      )

      val view = special_mentions.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")
      asScalaIterator(block.iterator()).toList must have length 1
      asScalaIterator(block.iterator()).toList.head.text() mustBe "Export EU Longer String - AddInf"
    }

    "render not Only the description stripping export in the description" in {
      val viewmodel: SpecialMention = SpecialMention(
        AdditionalInformation("2341", "Export Longer String"),
        models.SpecialMention(Some("AddInf"), Some("2341"), Some(true), None)
      )
      val view = special_mentions.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")
      asScalaIterator(block.iterator()).toList must have length 1
      asScalaIterator(block.iterator()).toList.head.text() mustBe "Export EU Longer String - AddInf"
    }

    "render not Only the description with the country code in the description" in {
      val viewmodel: SpecialMention = SpecialMention(
        AdditionalInformation("2341", "Export Longer String"),
        models.SpecialMention(Some("AddInf"), Some("2341"), Some(true), Some("GB"))
      )
      val view = special_mentions.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")
      asScalaIterator(block.iterator()).toList must have length 1
      asScalaIterator(block.iterator()).toList.head.text() mustBe "Export EU GB Longer String - AddInf"
    }

    "render not Only the description with the country code without the EU text in the description" in {
      val viewmodel: SpecialMention = SpecialMention(
        AdditionalInformation("2341", "Export Longer String"),
        models.SpecialMention(Some("AddInfTwenty"), Some("2341"), None, Some("GB"))
      )
      val view = special_mentions.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")
      asScalaIterator(block.iterator()).toList must have length 1
      asScalaIterator(block.iterator()).toList.head.text() mustBe "Export GB Longer String - AddInfTwenty"
    }

    "render code if description is a blank string" in {
      val viewmodel: SpecialMention = SpecialMention(
        AdditionalInformation("2341", ""),
        models.SpecialMention(Some("AddInfTwenty"), Some("2341"), None, Some("GB"))
      )
      val view = special_mentions.render(viewmodel)
      val doc  = Jsoup.parse(view.toString(), "", Parser.xmlParser())

      val block = doc.getElementsByTag("fo:block")
      asScalaIterator(block.iterator()).toList must have length 1
      asScalaIterator(block.iterator()).toList.head.text() mustBe "2341 GB - AddInfTwenty"
    }
  }
}
