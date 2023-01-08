package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class RepairState (

    val repairStateId: String,

    val state: String = ""
        ) {

}