package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.PartEntity
import com.adrpien.tiemed.data.local.entities.RepairEntity

class PartWithRepairs(
    @Embedded val part: PartEntity,
    @Relation(
        parentColumn = "partId",
        entityColumn = "repairId",
        associateBy = Junction(RepairPartCrossReference::class)
    ) val repairs: List<RepairEntity>
) {
}