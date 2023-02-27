package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.HospitalEntity
import com.adrpien.tiemed.data.remote.dto.HospitalDto

data class Hospital(
    val hospitalId: String = "",
    val hospitalName: String = ""
){
    fun toHospitalEntity(): HospitalEntity {
        return HospitalEntity(
            hospitalId = hospitalId,
            name = hospitalName
        )
    }

    fun toHospitalDto(): HospitalDto {
        return HospitalDto(
            hospitalId = hospitalId,
            name = hospitalName
        )
    }
}