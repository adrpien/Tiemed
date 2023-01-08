package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Hospital(
    val hospitalId: String,
    val name: String = ""
){

}