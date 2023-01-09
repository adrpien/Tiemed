package com.adrpien.tiemed.domain.model

data class Device(
    val deviceId: String,
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumber: String = "",
    var inventoryNumber: String = "",
    // var inspections: List<String> = emptyList(),
    // var repairs: List<String>? = emptyList()
)
