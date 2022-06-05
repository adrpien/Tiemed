package com.adrpien.tiemed.datamodels

import com.adrpien.tiemed.datamodels.users.User
import java.util.*
import kotlin.collections.ArrayList

data class Repair(

    // General
    val id: String? = null,
    var repairState: RepairState? = null,

    // Related device
    val device: Device? = null,

    // Repair description
    var photosList: ArrayList<Photo>? = null,
    var defectDescription: String? = null,
    var repairDescription: String? = null,
    var partList: ArrayList<Part>? = null,
    var partDescription: String? = null,
    var comment: String? = null,
    var electricalSafetyTest: ElectricalSafetyTest? = null,

    // Dates
    var closingDate: Calendar? = null,
    var openingDate: Calendar? = null,
    var repairingDate: Calendar? = null,

    // Technician
    var pickupTechnician: User? = null,
    var repairTechnician: User? = null,
    var returnTechnician: User? = null,

    // Rate
    var rate: Int? = null,

    // Recipient
    var recipient: String? = null,
    var recipientSignature: Photo? = null

    )

