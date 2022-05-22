package com.adrpien.tiemed.datamodels

import java.util.*

data class Inspection(

    // General
    var id: String = "0",
    var inspectionDate: String = Calendar.getInstance().time.toString(),
    var inspectionState: String = InspectionState.AWAITING.toString(),

    // Device
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumer: String = "",
    var inventoryNumber: String = "",

    // Localization
    var hospital: String = "",
    var ward: String = ""
)
