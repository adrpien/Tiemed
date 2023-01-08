package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.InspectionEntity
import com.adrpien.tiemed.data.local.entities.InspectionStateEntity
import com.adrpien.tiemed.domain.model.Inspection

class InspectionStateWithInspections(
    @Embedded val inspectionState: InspectionStateEntity,
    @Relation(
        parentColumn = "inspectionStateId",
        entityColumn = "inspectionId"
    ) val inspections: List<InspectionEntity>
) {
}