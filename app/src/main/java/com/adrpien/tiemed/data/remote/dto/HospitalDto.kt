package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.HospitalEntity

data class HospitalDto(
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