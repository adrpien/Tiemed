package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.Technician

@Entity
data class TechnicianEntity(
    @PrimaryKey(autoGenerate = false)
    val technicianId: String,
    val technicianName: String = ""
) {

    fun toTechnician(): Technician {
        return Technician(
            technicianId = technicianId,
            technicianName = technicianName
        )
    }
}
