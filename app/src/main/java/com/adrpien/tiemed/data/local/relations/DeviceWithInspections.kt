package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.DeviceEntity
import com.adrpien.tiemed.data.local.entities.InspectionEntity

data class DeviceWithInspections(
    @Embedded val device: DeviceEntity,
    @Relation(
        parentColumn = "deviceId",
        entityColumn = "inspectionId"
    )
    val inspections: List<InspectionEntity>
) {
}