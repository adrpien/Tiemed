package com.adrpien.tiemed.datamodels

data class Device(
    var type: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumer: String = "",
    var inventoryNumber: String = "",
    var localization : Localization = Localization()

)
