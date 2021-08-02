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

package viewmodels

import models.reference.KindOfPackage

sealed trait Package {

  val kindOfPackage: KindOfPackage
  val marksAndNumbersValue: Option[String]
  val packageAndNumber: Option[String]
  val numberOfPiecesValue: Int
  val countPackageAndMarks: Option[String]

  val validateString: Option[String] => Option[String] = _.filter(
    x => x.trim.nonEmpty
  )

  val validateCountAndPackage: Int => String => Option[String] =
    count =>
      description =>
        if (count > 0 && description.nonEmpty) {
          Some(s"$count - $description")
        } else None

  val validatePackage: String => Option[String] =
    description =>
      if (description.nonEmpty) {
        Some(s"1 - $description")
      } else None

  val validateCountPackageAndMarks: Int => KindOfPackage => Option[String] =
    count =>
      kindOfPackage =>
        if (count > 0 && kindOfPackage.description.nonEmpty) {
          Some(s"$count - ${kindOfPackage.code} (${kindOfPackage.description})")
        } else None
}

final case class BulkPackage(
  kindOfPackage: KindOfPackage,
  marksAndNumbers: Option[String]
) extends Package {

  override val marksAndNumbersValue: Option[String] = validateString(marksAndNumbers)

  override val packageAndNumber: Option[String] = validatePackage(kindOfPackage.description)

  override val numberOfPiecesValue: Int = 0

  override val countPackageAndMarks: Option[String] = validateCountPackageAndMarks(0)(kindOfPackage)
}

final case class UnpackedPackage(
  kindOfPackage: KindOfPackage,
  numberOfPieces: Int,
  marksAndNumbers: Option[String]
) extends Package {

  override val marksAndNumbersValue: Option[String] = validateString(marksAndNumbers)

  override val packageAndNumber: Option[String] = validateCountAndPackage(numberOfPieces)(kindOfPackage.description)

  override val countPackageAndMarks: Option[String] = validateCountPackageAndMarks(0)(kindOfPackage)

  override val numberOfPiecesValue: Int = numberOfPieces
}

final case class RegularPackage(
  kindOfPackage: KindOfPackage,
  numberOfPackages: Int,
  marksAndNumbers: String
) extends Package {

  override val marksAndNumbersValue: Option[String] = validateString(Some(marksAndNumbers))

  override val packageAndNumber: Option[String] = validateCountAndPackage(numberOfPackages)(kindOfPackage.description)

  override val countPackageAndMarks: Option[String] = validateCountPackageAndMarks(numberOfPackages)(kindOfPackage)

  override val numberOfPiecesValue: Int = 0
}
