package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TechnicianEntity(

    @PrimaryKey(autoGenerate = true)
    val technicianId: String,

    val name: String = ""
)
