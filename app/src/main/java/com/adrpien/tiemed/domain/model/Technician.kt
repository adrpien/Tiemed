package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.TechnicianEntity

data class Technician(
    var technicianId: String = "",
    var technicianName: String = ""
) {
    fun toTechncianEntity(): TechnicianEntity {
        return TechnicianEntity(
            technicianId = technicianId,
            technicianName = technicianName
        )
    }
}
