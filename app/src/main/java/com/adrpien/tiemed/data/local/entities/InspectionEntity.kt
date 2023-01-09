package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.Inspection
import java.util.*

@Entity
data class InspectionEntity(
    @PrimaryKey(autoGenerate = false)
    var inspectionId: String,
    var deviceId: String = "",
    var hospitalId: String = "",
    var ward: String = "",
    var comment: String = "",
    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),
    var recipient: String = "",
    var recipientSignature: String = "",
    var technicianId: String = "",
    var inspectionStateId: String = "",
    var estStateId: String = ""
    ) {

    fun toInspection(): Inspection {
        return Inspection(
            inspectionId = inspectionId,
            deviceId = deviceId,
            hospitalId = hospitalId,
            ward = ward,
            comment = comment,
            inspectionDate = inspectionDate,
            technicianId = technicianId,
            recipient = recipient,
            recipientSignature = recipientSignature,
            inspectionStateId = inspectionStateId,
            estStateId = estStateId
        )
    }
}
