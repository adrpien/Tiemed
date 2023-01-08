package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HospitalEntity(

    @PrimaryKey(autoGenerate = true)
    val hospitalId: String,

    val name: String = ""
){

}