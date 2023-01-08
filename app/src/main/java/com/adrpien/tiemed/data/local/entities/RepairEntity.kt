package com.adrpien.tiemed.data.local.entities

import com.adrpien.tiemed.datamodels.Part
import kotlin.collections.ArrayList


data class Repair(

    // General
    // var repairId: String = "",
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
    var photosList: ArrayList<Photo> = arrayListOf(),
    var defectDescription: String = "",
    var repairDescription: String = "",

    // Used Parts
    var partList: ArrayList<Part> = arrayListOf(),
    var partDescription: String = "",

    // Comment
    var comment: String = "",

    // Safety Test
    // var electricalSafetyTest: ElectricalSafetyTest? = null,
    var electricalSafetyTestString: String = "",

    // Dates
    var closingDate: String = "",
    var openingDate: String = "",
    var repairingDate: String = "",

    // Technician
    //var pickupTechnician: User? = null,
    //var repairTechnician: User? = null,
    //var returnTechnician: User? = null,

    // Rate
    var rate: String = "",

    // Recipient
    var recipient: String = "",
    var recipientSignature: String = "",

    // Related inspections
    //var relatedInspections: ArrayList<Inspection>? = null,


    )

