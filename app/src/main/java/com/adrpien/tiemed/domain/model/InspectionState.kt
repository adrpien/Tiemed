package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class  InspectionState (

    val inspectionStateId: String,

    val state: String =  ""
        ){

}
