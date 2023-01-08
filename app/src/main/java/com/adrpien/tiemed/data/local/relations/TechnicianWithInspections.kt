package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.InspectionEntity
import com.adrpien.tiemed.data.local.entities.TechnicianEntity

class TechnicianWithInspections(
    @Embedded val technician: TechnicianEntity,
    @Relation(
        parentColumn = "technicianId",
        entityColumn = "inspectionId"
    ) val inspections: List<InspectionEntity>
) {
}