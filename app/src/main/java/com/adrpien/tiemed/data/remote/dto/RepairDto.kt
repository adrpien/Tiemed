package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.RepairEntity


data class RepairDto(
    val repairId: String,
    var repairStateId: String = "",
    val deviceId: String = "",
    var hospitalId: String = "",
    var ward: String = "",
    //var photoList: List<String> = emptyList<String>(),
    var defectDescription: String = "",
    var repairDescription: String = "",
    // var partList: List<String> = emptyList(),
    var partDescription: String = "",
    var comment: String = "",
    var estTestId: String = "",
    var closingDate: String = "",
    var openingDate: String = "",
    var repairingDate: String = "",
    var pickupTechnicianId: String = "",
    var repairTechnicianId: String = "",
    var returnTechnicianId: String = "",
    var rate: String = "",
    var recipient: String = "",
    var recipientSignatureId: String = "",
    ) {

    fun toRepairEntity(): RepairEntity {
    return RepairEntity(
        repairId = repairId,
        repairStateId = repairStateId,
        deviceId = deviceId,
        hospitalId = hospitalId,
        ward = ward,
        // photoList = photoList,
        defectDescription = defectDescription,
        repairDescription = repairDescription,
        // partList = partList,
        partDescription = partDescription,
        comment = comment,
        estTestId = estTestId,
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
            // photoList = photoList,
            defectDescription = defectDescription,
            repairDescription = repairDescription,
            // partList = partList,
            partDescription = partDescription,
            comment = comment,
            estTestId = estTestId,
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

