package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.DeviceEntity
import com.adrpien.tiemed.data.local.entities.RepairEntity

class DeviceWithRepairs (
    @Embedded() val device: DeviceEntity,
    @Relation(
        parentColumn = "deviceId",
        entityColumn = "repairId"
    ) val repairs: List<RepairEntity>
) {
}