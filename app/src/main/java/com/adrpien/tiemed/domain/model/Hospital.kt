package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.data.local.entities.HospitalEntity
import com.adrpien.tiemed.data.remote.dto.HospitalDto

data class Hospital(
    val hospitalId: String,
    val name: String = ""
){
    fun toHospitalEntity(): HospitalEntity {
        return HospitalEntity(
            hospitalId = hospitalId,
            name = name
        )
    }

    fun toHospitalDto(): HospitalDto {
        return HospitalDto(
            hospitalId = hospitalId,
            name = name
        )
    }
}