package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EstStateEntity (

    @PrimaryKey(autoGenerate = true)
    val EstStateId: String,

    val state: String = ""
){

}

