package com.adrpien.tiemed.data.local.entities

data class DeviceEntity(

    // Basic information
    val deviceId: Int? = null,
    var name: String? = null,
    var manufacturer: String? = null,
    var model: String? = null,
    var serialNumer: String? = null,
    var inventoryNumber: String? = null,

    // Device localization
    var localization : LocalizationEntity? = null,

    // List of inspections
    var inspections: ArrayList<InspectionEntity>? = null,

    // List of repairs:
    var repairs: ArrayList<Repair>? = null
)
