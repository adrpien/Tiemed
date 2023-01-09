package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.Device

@Entity
data class DeviceEntity(
    @PrimaryKey(autoGenerate = false)
    val deviceId: String,
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumber: String = "",
    var inventoryNumber: String = "",
    // var inspections: List<String> = emptyList(),
    // var repairs: List<String>? = emptyList()
) {
    fun toDevice(): Device {
        return Device(
            deviceId = deviceId,
            name = name,
            manufacturer = manufacturer,
            model = model,
            serialNumber = serialNumber,
            inventoryNumber = inventoryNumber,
            // inspections = inspections,
            // repairs = repairs
        )
    }
}
