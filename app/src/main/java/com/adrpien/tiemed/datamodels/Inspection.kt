package com.adrpien.tiemed.datamodels

import org.w3c.dom.Comment
import java.util.*

data class Inspection(

    // General
    var id: String = "0",
    var inspectionState: InspectionState? = null,

    // Device
    // var device: Device? = null,

    val deviceId: String? = null,
    var name: String? = null,
    var manufacturer: String? = null,
    var model: String? = null,
    var serialNumber: String? = null,
    var inventoryNumber: String? = null,

    // Localization
    var hospital: String? = null,
    var ward: String? = null,

    // Inspection description
    var safetyTest: ElectricalSafetyTest? = null,
    var comment: String? = null,

    // Dates
    var inspectionDate: String = Calendar.getInstance().time.toString(),

    )
