package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.DeviceEntity

data class DeviceDto(
    val deviceId: String,
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumber: String = "",
    var inventoryNumber: String = "",
    // var inspections: List<String> = emptyList(),
    // var repairs: List<String>? = emptyList()
){

    fun toDeviceEntity(): DeviceEntity {
        return DeviceEntity(
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

    fun toDeviceDto(): DeviceDto {
        return DeviceDto(
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