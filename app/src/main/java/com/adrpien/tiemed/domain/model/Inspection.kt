package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.InspectionEntity
import com.adrpien.tiemed.data.remote.dto.InspectionDto
import java.util.*

data class Inspection(
    var inspectionId: String = "",
    var deviceId: String = "",
    var hospitalId: String = "",
    var ward: String = "",
    var comment: String = "",
    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),
    var technicianId: String = "",
    var recipient: String = "",
    var recipientSignature: String = "",
    var inspectionStateId: String = "",
    var estStateId: String = "",

    var device: Device = Device()
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