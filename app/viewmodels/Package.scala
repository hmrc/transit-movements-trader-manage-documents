/*
 * Copyright 2020 HM Revenue & Customs
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

  val marksAndNumbersValue: Option[String] = this match {
    case packageType: BulkPackage     => packageType.marksAndNumbers.filter(x => x.trim.nonEmpty)
    case packageType: UnpackedPackage => packageType.marksAndNumbers.filter(x => x.trim.nonEmpty)
    case packageType: RegularPackage  => if (packageType.marksAndNumbers.nonEmpty) Some(packageType.marksAndNumbers) else None
  }

  val packageAndNumber: Option[String] = this match {
    case bulkPackage: BulkPackage => if (bulkPackage.kindOfPackage.description.nonEmpty) Some(s"1 - ${bulkPackage.kindOfPackage.description}") else None
    case packageType: UnpackedPackage => {

      if (packageType.numberOfPieces > 0 && packageType.kindOfPackage.description.nonEmpty) {
        Some(s"${packageType.numberOfPieces} - ${packageType.kindOfPackage.description}")
      } else None

    }
    case packageType: RegularPackage => {
      if (packageType.numberOfPackages > 0 && packageType.kindOfPackage.description.nonEmpty) {
        Some(s"${packageType.numberOfPackages} - ${packageType.kindOfPackage.description}")
      } else None
    }
  }

  val numberOfPiecesValue: Int = this match {
    case packageType: UnpackedPackage => packageType.numberOfPieces
    case _                            => 0
  }
}

final case class BulkPackage(
  kindOfPackage: KindOfPackage,
  marksAndNumbers: Option[String]
) extends Package

final case class UnpackedPackage(
  kindOfPackage: KindOfPackage,
  numberOfPieces: Int,
  marksAndNumbers: Option[String]
) extends Package

final case class RegularPackage(
  kindOfPackage: KindOfPackage,
  numberOfPackages: Int,
  marksAndNumbers: String
) extends Package
