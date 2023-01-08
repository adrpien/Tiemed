package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.EstStateEntity
import com.adrpien.tiemed.data.local.entities.InspectionEntity

class EstStateWithInspections(
    @Embedded val EstState: EstStateEntity,
    @Relation(
        parentColumn = "estStateId",
        entityColumn = "inspectionId"
    ) val inspections: List<InspectionEntity>
) {
}