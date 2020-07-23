package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateFormatter {
  val dateFormatter: DateTimeFormatter               = DateTimeFormatter.ofPattern("yyyyMMdd")
  def dateFormatted(date: LocalDate): String         = date.format(dateFormatter)
}
