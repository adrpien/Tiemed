package com.adrpien.tiemed.feature_inspections.domain.model

import java.util.*

data class Inspection(

    // General
    var inspectionUid: String = "",
    //var inspectionId: String = "",

    // State
    // Should be InspectionState?
    //var inspectionState: String = "",
    var inspectionStateString: String = "",

    // Device
    //var device: Device? = null,
    val deviceId: String = "",
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumber: String = "",
    var inventoryNumber: String = "",

    // Localization
    //var localization: Localization? = null,
    //var hospital: Hospital? = null,
    var hospitalString: String = "",
    var ward: String = "",


    // Safety test
    // Should be ESTState?
    // var safetyTest: ElectricalSafetyTest? = null,
    var electricalSafetyTestString: String = "",

    // Comment
    var comment: String = "",

    // Inspection date
    var inspectionDate: String = Calendar.getInstance().timeInMillis.toString(),

    // Recipient
    var recipient: String = "",
    var recipientSignature: String = "",

    // Related repairs
    //var relatedRepairs: ArrayList<Repair>? = null

    )
