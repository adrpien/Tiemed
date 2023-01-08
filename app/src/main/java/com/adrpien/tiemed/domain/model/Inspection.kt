package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class Inspection(

    var inspectionId: String,

    var deviceId: String = "",

    var hospitalId: String = "",

    var ward: String = "",

    var comment: String = "",

    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),

    var recipient: String = "",
    var recipientSignature: String = "",

    var inspectionStateId: String = "",

    var EstStateId: String = ""
    )
