package com.adrpien.tiemed.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrpien.tiemed.data.local.entities.DeviceEntity
import com.adrpien.tiemed.data.local.entities.HospitalEntity
import com.adrpien.tiemed.data.local.entities.InspectionEntity

class HospitalWithInspections (
    @Embedded val hospital: HospitalEntity,
    @Relation(
        parentColumn = "hospitalId",
        entityColumn = "inspectionId"
    ) val inspections: List<InspectionEntity>
) {
}