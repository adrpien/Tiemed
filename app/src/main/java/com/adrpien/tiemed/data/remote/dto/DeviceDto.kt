package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.DeviceEntity
import com.adrpien.tiemed.domain.model.Device

data class DeviceDto(
    val deviceId: String,
    var name: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var serialNumer: String = "",
    var inventoryNumber: String = "",
    var localizationId : String = "",
    var inspections: List<String> = emptyList(),
    var repairs: List<String>? = emptyList()
){

    fun toDeviceEntity(): DeviceEntity {
        return DeviceEntity(
            deviceId = deviceId,
            name = name,
            manufacturer = manufacturer,
            model = model,
            serialNumer = serialNumer,
            inventoryNumber = inventoryNumber,
            localizationId = localizationId,
            inspections = inspections,
            repairs = repairs
        )
    }
}