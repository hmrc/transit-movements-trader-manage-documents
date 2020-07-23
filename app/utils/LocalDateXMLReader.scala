package utils

import java.time.LocalDate

import com.lucidchart.open.xtract.XmlReader

class LocalDateXMLReader {
  implicit val xmlLocalDateReads: XmlReader[LocalDate] = ???
}
