package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.InspectionEntity
import com.adrpien.tiemed.data.local.entities.RepairEntity
import com.adrpien.tiemed.data.local.entities.RepairStateEntity

class RepairStateWithRepairs(
    @Embedded val repairState: RepairStateEntity,
    @Relation(
        parentColumn = "repairStateId",
        entityColumn = "repairId"
    ) val inspections: List<RepairEntity>
) {
}