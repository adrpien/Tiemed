package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class InspectionEntity(

    @PrimaryKey(autoGenerate = true)
    var inspectionId: String,

    var deviceId: String = "",

    var hospitalId: String = "",
    var ward: String = "",

    var comment: String = "",

    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),

    var recipient: String = "",
    var recipientSignature: String = "",

    var techniciadId: String = "",

    var inspectionStateId: String = "",

    var EstStateId: String = ""
    )
