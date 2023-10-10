package models

import play.api.libs.json.{Format, Json}

import scala.xml.NodeSeq

case class Message(
  body: NodeSeq
)

object Message {
  implicit val format: Format[Message] = Json.format[Message]
}
