package com.adrpien.tiemed.domain.model

import java.util.*

data class Inspection(
    var inspectionId: String,
    var deviceId: String = "",
    var hospitalId: String = "",
    var ward: String = "",
    var comment: String = "",
    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),
    var technicianId: String = "",
    var recipient: String = "",
    var recipientSignature: String = "",
    var inspectionStateId: String = "",
    var estStateId: String = ""
    )
