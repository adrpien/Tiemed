package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PartEntity(

    @PrimaryKey
    val partId: Int,

    val name: String = "",
    var quantity: Int = 0
    ) {
}
