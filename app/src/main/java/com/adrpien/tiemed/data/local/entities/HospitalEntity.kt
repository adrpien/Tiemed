package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.Hospital

@Entity
data class HospitalEntity(
    @PrimaryKey(autoGenerate = false)
    val hospitalId: String,
    val name: String = ""
){
    fun toHospital(): Hospital {
        return Hospital(
            hospitalId = hospitalId,
            hospitalName = name
        )
    }
}