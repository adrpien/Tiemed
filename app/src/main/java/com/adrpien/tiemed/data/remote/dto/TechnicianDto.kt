package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.TechnicianEntity

data class TechnicianDto(
    val technicianId: String,
    val name: String = ""
) {

    fun toTechnicianEntity(): TechnicianEntity {
        return TechnicianEntity(
            technicianId = technicianId,
            technicianName = name
        )
    }

    fun toTechnicianDto(): TechnicianDto {
        return TechnicianDto(
            technicianId = technicianId,
            name = name
        )
    }
}
