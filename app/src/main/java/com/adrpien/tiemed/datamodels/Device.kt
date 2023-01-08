package com.adrpien.tiemed.datamodels

import com.adrpien.tiemed.data.local.entities.Repair

data class Device(

    // Basic information
    val deviceId: Int? = null,
    var name: String? = null,
    var manufacturer: String? = null,
    var model: String? = null,
    var serialNumer: String? = null,
    var inventoryNumber: String? = null,

    // Device localization
    var localization : Localization? = null,

    // List of inspections
    var inspections: ArrayList<Inspection>? = null,

    // List of repairs:
    var repairs: ArrayList<Repair>? = null
)
