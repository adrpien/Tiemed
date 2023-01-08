package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class InspectionEntity(

    // Primary key
    @PrimaryKey(autoGenerate = true)
    var inspectionId: String,

    // Device
    var deviceId: String = "",

    // Localization
    var hospitalId: String = "",
    var ward: String = "",

    // Comment
    var comment: String = "",

    // Inspection date
    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),

    // Recipient
    var recipient: String = "",
    var recipientSignature: String = "",

    // State
    var inspectionStateId: String = "",

    // Safety test
    var EstStateId: String = ""
    )
