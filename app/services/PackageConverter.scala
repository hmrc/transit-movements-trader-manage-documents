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

package services

import models.reference.KindOfPackage

object PackageConverter extends Converter {

  def toViewModel(pkg: models.Package, path: String, kindsOfPackage: Seq[KindOfPackage]): ValidationResult[viewmodels.Package] =
    pkg match {
      case p: models.BulkPackage     => bulkPackageToViewModel(p, path, kindsOfPackage)
      case p: models.UnpackedPackage => unpackedPackageToViewModel(p, path, kindsOfPackage)
      case p: models.RegularPackage  => regularPackageToViewModel(p, path, kindsOfPackage)
    }

  private def bulkPackageToViewModel(pkg: models.BulkPackage, path: String, kindsOfPackage: Seq[KindOfPackage]): ValidationResult[viewmodels.BulkPackage] =
    findReferenceData(pkg.kindOfPackage, kindsOfPackage, s"$path.kindOfPackage")
      .map(viewmodels.BulkPackage(_, pkg.marksAndNumbers))

  private def unpackedPackageToViewModel(
    pkg: models.UnpackedPackage,
    path: String,
    kindsOfPackage: Seq[KindOfPackage]
  ): ValidationResult[viewmodels.UnpackedPackage] =
    findReferenceData(pkg.kindOfPackage, kindsOfPackage, s"$path.kindOfPackage")
      .map(viewmodels.UnpackedPackage(_, pkg.numberOfPieces, pkg.marksAndNumbers))

  private def regularPackageToViewModel(
    pkg: models.RegularPackage,
    path: String,
    kindsOfPackage: Seq[KindOfPackage]
  ): ValidationResult[viewmodels.RegularPackage] =
    findReferenceData(pkg.kindOfPackage, kindsOfPackage, s"$path.kindOfPackage")
      .map(viewmodels.RegularPackage(_, pkg.numberOfPackages, pkg.marksAndNumbers))
}
