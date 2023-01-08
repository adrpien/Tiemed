package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.DeviceEntity
import com.adrpien.tiemed.data.local.entities.HospitalEntity
import com.adrpien.tiemed.data.local.entities.RepairEntity

class HospitalWithRepairs (
    @Embedded val hospital: HospitalEntity,
    @Relation(
        parentColumn = "hospitalId",
        entityColumn = "repairId"
    ) val repairs: List<RepairEntity>
) {
}