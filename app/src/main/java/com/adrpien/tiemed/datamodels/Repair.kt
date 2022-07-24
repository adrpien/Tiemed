package com.adrpien.tiemed.datamodels

import com.adrpien.tiemed.datamodels.users.User
import java.util.*
import kotlin.collections.ArrayList

data class Repair(

    // General
    var repairId: String = "",
    var repairUid: String = "",

    // State
    //var repairState: RepairState? = null,
    var repairStateString: String = "",

    // Related device
    //val device: Device? = null,
    var deviceId: String = "",
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumber: String = "",
    var inventoryNumber: String = "",

    // Localization
    //var localization: Localization? = null,
    //var hospital: String = "",
    var hospitalString: String = "",
    var ward: String = "",


    // Repair description
    var photosList: ArrayList<Photo>? = null,
    var defectDescription: String = "",
    var repairDescription: String = "",

    // Used Parts
    var partList: ArrayList<Part>? = null,
    var partDescription: String = "",

    // Comment
    var comment: String = "",

    // Safety Test
    // var electricalSafetyTest: ElectricalSafetyTest? = null,
    var electricalSafetyTest: String = "",

    // Dates
    var closingDate: String = "",
    var openingDate: String = "",
    var repairingDate: String = "",

    // Technician
    //var pickupTechnician: User? = null,
    //var repairTechnician: User? = null,
    //var returnTechnician: User? = null,

    // Rate
    var rate: Int? = null,

    // Recipient
    var recipient: String = "",
    var recipientSignature: String = "",

    // Related inspections
    //var relatedInspections: ArrayList<Inspection>? = null,


    )

