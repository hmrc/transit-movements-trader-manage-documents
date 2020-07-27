package models

import generators.ModelGenerators
import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Arbitrary.arbitrary

import scala.xml.NodeSeq

class GoodsItemSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "GoodsItem" - {

    "XML" - {

      "must deserialise" in {

        forAll(arbitrary[GoodsItem]) {
          goodsItem =>

          val xml = {
            <GOOITEGDS>
              <IteNumGDS7>{goodsItem.itemNumber}</IteNumGDS7>
              {
                goodsItem.commodityCode.fold(NodeSeq.Empty) { commodityCode =>
                  <ComCodTarCodGDS10>{commodityCode}</ComCodTarCodGDS10>
                } ++
                goodsItem.declarationType.fold(NodeSeq.Empty) { declarationType =>
                  <TypOfDecHEA24>{declarationType.toString}</TypOfDecHEA24>
                }
              }
              <GooDesGDS23>{goodsItem.description}</GooDesGDS23>
              {
                goodsItem.grossMass.fold(NodeSeq.Empty) { grossMass =>
                  <GroMasGDS46>{grossMass}</GroMasGDS46>
                } ++
                goodsItem.netMass.fold(NodeSeq.Empty) { netMass =>
                  <NetMasGDS48>{netMass}</NetMasGDS48>
                }
              }
              <CouOfDisGDS58>{goodsItem.countryOfDispatch}</CouOfDisGDS58>
              <CouOfDesGDS59>{goodsItem.countryOfDestination}</CouOfDesGDS59>
              {
                goodsItem.producedDocuments.map(producedDocumentXML) ++
                goodsItem.specialMentions.map(specialMentionXML) ++
                goodsItem.consignor.map(consignorXML) ++
                goodsItem.consignee.map(consigneeXML) ++
                goodsItem.containers.map {
                  value =>
                    <CONNR2>{value}</CONNR2>
                } ++
                goodsItem.packages.map()
              }
            </GOOITEGDS>
          }
        }

//        (
//          (__ \ "IteNumGDS7").read[Int],
//          (__ \ "ComCodTarCodGDS10").read[String].optional,
//          (__ \ "DecTypGDS15").read[DeclarationType].optional,
//          (__ \ "GooDesGDS23").read[String],
//          (__ \ "GroMasGDS46").read[BigDecimal].optional,
//          (__ \ "NetMasGDS48").read[BigDecimal].optional,
//          (__ \ "CouOfDisGDS58").read[String],
//          (__ \ "CouOfDesGDS59").read[String],
//          (__ \ "PRODOCDC2").read(seq[ProducedDocument]),
//          (__ \ "SPEMENMT2").read(seq[SpecialMention]),
//          (__ \ "TRACONCO2").read[Consignor].optional,
//          (__ \ "TRACONCE2").read[Consignee].optional,
//          (__ \ "CONNR2").read(seq[String]),
//          (__ \ "PACGS2").read(xmlNonEmptyListReads[Package]),
//          (__ \ "SGICODSD2").read(seq[SensitiveGoodsInformation])
//          ).mapN(apply)

      }
    }
  }

  private def producedDocumentXML(producedDocument: ProducedDocument): NodeSeq = {
    <PRODOCDC2>
      <DocTypDC21>{producedDocument.documentType}</DocTypDC21>
      {
      producedDocument.reference.fold(NodeSeq.Empty) { reference =>
        <DocRefDC23>{reference}</DocRefDC23>
      } ++
        producedDocument.complementOfInformation.fold(NodeSeq.Empty) { information =>
          <ComOfInfDC25>{information}</ComOfInfDC25>
        }
      }
    </PRODOCDC2>
  }

  private def specialMentionXML(specialMention: SpecialMention): NodeSeq = {
    <SPEMENMT2>
    {
      specialMention match {
        case value: SpecialMentionEc =>
          <ExpFroECMT24>1</ExpFroECMT24>
          <AddInfCodMT23>{value.additionalInformationCoded}</AddInfCodMT23>
        case value: SpecialMentionNonEc =>
          <ExpFroECMT24>0</ExpFroECMT24>
          <AddInfCodMT23>{value.additionalInformationCoded}</AddInfCodMT23>
          <ExpFroCouMT25>{value.exportFromCountry}</ExpFroCouMT25>
        case value: SpecialMentionNoCountry =>
          <AddInfCodMT23>{value.additionalInformationCoded}</AddInfCodMT23>
      }
    }
    </SPEMENMT2>
  }

  private def consignorXML(consignor: Consignor): NodeSeq = {
    <TRACONCO1>
      <NamCO17>{consignor.name}</NamCO17>
      <StrAndNumCO122>{consignor.streetAndNumber}</StrAndNumCO122>
      <PosCodCO123>{consignor.postCode}</PosCodCO123>
      <CitCO124>{consignor.city}</CitCO124>
      <CouCO125>{consignor.countryCode}</CouCO125>
      {
      consignor.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
        <NADLNGCO>{nadLangCode}</NADLNGCO>
      } ++
        consignor.eori.fold(NodeSeq.Empty) { eori =>
          <TINCO159>{eori}</TINCO159>
        }
      }
    </TRACONCO1>
  }

  private def consigneeXML(consignee: Consignee): NodeSeq = {
    <TRACONCE1>
      <NamCE17>{consignee.name}</NamCE17>
      <StrAndNumCE122>{consignee.streetAndNumber}</StrAndNumCE122>
      <PosCodCE123>{consignee.postCode}</PosCodCE123>
      <CitCE124>{consignee.city}</CitCE124>
      <CouCE125>{consignee.countryCode}</CouCE125>
      {
      consignee.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
        <NADLNGCE>{nadLangCode}</NADLNGCE>
      } ++
        consignee.eori.fold(NodeSeq.Empty) { eori =>
          <TINCE159>{eori}</TINCE159>
        }
      }
    </TRACONCE1>
  }

  private def packageToXML(packageModel: Package): NodeSeq = {
    packageModel match {
      case value: UnpackedPackage => ???
      case value: RegularPackage  => ???
      case value: BulkPackage     => ???
    }
  }

}
