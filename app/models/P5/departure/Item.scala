package models.P5.departure
case class Item(declarationGoodsItemNumber: String, goodsItemNumber: String, packaging: String, consignor: String,
                consignee: String, referenceNumberUcr: String, transportCharges: String, countryOfDispatch: String,
                countryOfDestination: String, declarationType: String, additionalSupplyChainActor: String,
                commodityCode: String, departureTransportMeans: String, dangerousGoods: String,  cusCode: String,
                descriptionOfGoods: String, previousDocument: String, supportingDocument: String, transportDocument: String,
                additionalReference: String, additionalInformation: String, grossMass: String, netMass: String)



