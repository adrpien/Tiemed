package com.adrpien.tiemed.datamodels

import java.util.*

data class Repair(
    val id: String? = null,
    var openingDate: Date? = null,
    var closingDate: Date? = null,
    var state: State? = null,
    var hospital: Hospital? = null,
    var device: String? = null,
    var producer: String? = null,
    var model: String? = null,
    var serialNumber: String? = null,
    var inventoryNumber: String? = null,
    var photosList: List<Int>? = null, // List of photos URLs
    var defectDescription: String? = null,
    var repairDescription: String? = null,
    var partList: List<Part>? = null,






    )

