package com.adrpien.tiemed.domain.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.data.local.entities.InspectionEntity
import com.adrpien.tiemed.data.local.entities.PartEntity
import com.adrpien.tiemed.data.remote.dto.PartDto

data class Part(
    var partId: String = "",
    var name: String = "",
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
