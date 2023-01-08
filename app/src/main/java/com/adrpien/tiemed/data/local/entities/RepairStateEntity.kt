package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepairStateEntity (

    @PrimaryKey(autoGenerate = true)
    val repairStateId: String,

    val state: String = ""
        ) {

}