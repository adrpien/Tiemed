package com.adrpien.tiemed.domain.model


data class Repair(
    val repairId: String,
    var repairStateId: String = "",
    val deviceId: String = "",
    var hospitalId: String = "",
    var ward: String = "",
    // var photoList: List<String> = emptyList<String>(),
    var defectDescription: String = "",
    var repairDescription: String = "",
    //var partList: List<String> = emptyList(),
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
    )

