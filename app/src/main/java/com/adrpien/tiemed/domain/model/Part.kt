package com.adrpien.tiemed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Part(
    val partId: Int,
    val name: String = "",
    var quantity: Int = 0
    ) {
}
