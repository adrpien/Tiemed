package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.InspectionEntity
import java.util.*

data class InspectionDto(
    val inspectionId: String,
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
) {

    fun toInspectionEntity(): InspectionEntity {
        return InspectionEntity(
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

    fun toInspectionDto(): InspectionDto {
        return InspectionDto(
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
