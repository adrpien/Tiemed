package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.PartEntity
import com.adrpien.tiemed.data.local.entities.RepairEntity

class RepairWithParts (
    @Embedded val repair: RepairEntity,
    @Relation(
        parentColumn = "repairId",
        entityColumn = "partId",
        associateBy = Junction(RepairPartCrossReference::class)
    ) val parts: List<PartEntity>
) {
}