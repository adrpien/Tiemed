package com.adrpien.tiemed.datamodels

import org.w3c.dom.Comment
import java.util.*

data class Inspection(

    // General
    var id: String = "0",
    //  var inspectionState: InspectionState? = null,
    var inspectionState: String = "",

    // Device
    // var device: Device? = null,

    val deviceId: String = "",
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumber: String = "",
    var inventoryNumber: String = "",

    // Localization
    // var localization: Localization? = null,
    var hospital: String = "",
    var ward: String = "",

    // Inspection description
    //  var safetyTest: ESTState = "",
    var safetyTest: String = "",
    var comment: String = "",

    // Dates
    var inspectionDate: String = Calendar.getInstance().time.toString(),

    )
