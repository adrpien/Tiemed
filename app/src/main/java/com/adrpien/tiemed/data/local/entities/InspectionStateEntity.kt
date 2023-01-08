package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class  InspectionStateEntity (

    @PrimaryKey(autoGenerate = true)
    val inspectionStateId: String,

    val state: String =  ""
        ){

}
