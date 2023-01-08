package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Device(

    val deviceId: String,

    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumer: String = "",
    var inventoryNumber: String = "",

    var localizationId : String = "",

    var inspections: List<String> = emptyList(),

    var repairs: List<String>? = emptyList()
)
