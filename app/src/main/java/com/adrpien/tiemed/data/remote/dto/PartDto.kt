package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.PartEntity

data class PartDto(
    val partId: Int,
    val name: String = "",
    var quantity: Int = 0
    ) {

    fun toPartEntity(): PartEntity {
        return PartEntity(
            partId = partId,
            name = name,
            quantity = quantity
        )
    }
}
