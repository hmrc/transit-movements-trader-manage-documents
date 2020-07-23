package models

import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

import scala.xml.NodeSeq

class PrincipalSpec
  extends FreeSpec
    with MustMatchers
    with ScalaCheckPropertyChecks
    with ModelGenerators
    with OptionValues {

  "Principal" - {
    "XML" - {

      "must deserialise" in {
        forAll(arbitrary[Principal]) {
          principal =>
            val xml =
              <TRAPRIPC1>
                <NamPC17>{principal.name}</NamPC17>
                <StrAndNumPC122>{principal.streetAndNumber}</StrAndNumPC122>
                <PosCodPC123>{principal.postCode}</PosCodPC123>
                <CitPC124>{principal.city}</CitPC124>
                <CouPC125>{principal.countryCode}</CouPC125>
                {principal.eori.fold(NodeSeq.Empty)(eori => <TINPC159>{eori}</TINPC159>)}
              </TRAPRIPC1>

            val result = XmlReader.of[Principal].read(xml).toOption.value

            result mustBe principal
        }
      }

      "must fail to deserialise when a mandatory field is missing" in {
        val xml =
          <TRAPRIPC1>
            <NamPC17></NamPC17>
          </TRAPRIPC1>

        val result = XmlReader.of[Principal].read(xml).toOption

        result mustBe None
      }
    }
  }

}