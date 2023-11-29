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

package refactor.views.p4.tad.components.security.table_1

import base.SpecBase
import play.api.test.Helpers._
import play.twirl.api.XmlFormat
import refactor.viewmodels.DummyData
import refactor.views.xml.p4.tad.components.security.table_1

class FormsSpec extends SpecBase with DummyData {

  "security.table_1.forms" - {

    "render the template with page number using a ref-id from the consignment page " in {
      val html: XmlFormat.Appendable = table_1.forms()
      contentAsString(html) must include("""<fo:page-number-citation ref-id="A-last-page"/>""")

    }
  }

}
