package com.adrpien.tiemed.datamodels

import org.w3c.dom.Comment
import java.util.*

data class Inspection(

    // General
    var id: String = "0",
    var inspectionState: InspectionState? = null,

    // Device
    var device: Device? = null,

    // Inspection description
    var safetyTest: ElectricalSafetyTest? = null,
    var comment: String? = null,

    // Dates
    var inspectionDate: String = Calendar.getInstance().time.toString(),

)
