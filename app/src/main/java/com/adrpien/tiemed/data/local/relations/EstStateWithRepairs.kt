package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.EstStateEntity
import com.adrpien.tiemed.data.local.entities.InspectionEntity
import com.adrpien.tiemed.data.local.entities.RepairEntity

class EstStateWithRepairs(
    @Embedded val EstState: EstStateEntity,
    @Relation(
        parentColumn = "estStateId",
        entityColumn = "repairId"
    ) val repairs: List<RepairEntity>
) {
}