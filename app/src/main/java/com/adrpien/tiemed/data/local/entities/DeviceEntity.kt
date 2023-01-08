package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeviceEntity(

    @PrimaryKey(autoGenerate = true)
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
