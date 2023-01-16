package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.PartEntity

data class PartDto(
    val partId: String,
    val name: String = "",
    var quantity: String = "0"
    ) {

    fun toPartEntity(): PartEntity {
        return PartEntity(
            partId = partId,
            name = name,
            quantity = quantity
        )
    }

    fun toPartDto(): PartDto {
        return PartDto(
            partId = partId,
            name = name,
            quantity = quantity
        )
    }
}
