package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.RepairEntity
import com.adrpien.tiemed.data.remote.dto.RepairDto


data class Repair(
    var repairId: String = "",
    var repairStateId: String = "",
    var deviceId: String = "",
    var hospitalId: String = "",
    var ward: String = "",
    var defectDescription: String = "",
    var repairDescription: String = "",
    var partDescription: String = "",
    var comment: String = "",
    var estStateId: String = "",
    var closingDate: String = "",
    var openingDate: String = "",
    var repairingDate: String = "",
    var pickupTechnicianId: String = "",
    var repairTechnicianId: String = "",
    var returnTechnicianId: String = "",
    var rate: String = "",
    var recipient: String = "",
    var recipientSignatureId: String = "",

    var device: Device = Device()
    ) {
    fun toRepairEntity(): RepairEntity {
        return RepairEntity(
            repairId = repairId,
            repairStateId = repairStateId,
            deviceId = deviceId,
            hospitalId = hospitalId,
            ward = ward,
            //photoList = photoList,
            defectDescription = defectDescription,
            repairDescription = repairDescription,
            // partList = partList,
            partDescription = partDescription,
            comment = comment,
            estTestId = estStateId,
            closingDate = closingDate,
            openingDate = openingDate,
            repairingDate = repairingDate,
            pickupTechnicianId = pickupTechnicianId,
            repairTechnicianId = repairTechnicianId,
            returnTechnicianId = returnTechnicianId,
            rate = rate,
            recipient = recipient,
            recipientSignatureId = recipientSignatureId
        )
    }

    fun toRepairDto(): RepairDto {
        return RepairDto(
            repairId = repairId,
            repairStateId = repairStateId,
            deviceId = deviceId,
            hospitalId = hospitalId,
            ward = ward,
            //photoList = photoList,
            defectDescription = defectDescription,
            repairDescription = repairDescription,
            // partList = partList,
            partDescription = partDescription,
            comment = comment,
            estTestId = estStateId,
            closingDate = closingDate,
            openingDate = openingDate,
            repairingDate = repairingDate,
            pickupTechnicianId = pickupTechnicianId,
            repairTechnicianId = repairTechnicianId,
            returnTechnicianId = returnTechnicianId,
            rate = rate,
            recipient = recipient,
            recipientSignatureId = recipientSignatureId
        )
    }
}

