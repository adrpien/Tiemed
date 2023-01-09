package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.Part
import com.adrpien.tiemed.domain.model.Repair

@Entity
data class PartEntity(
    @PrimaryKey(autoGenerate = false)
    val partId: Int,
    val name: String = "",
    var quantity: Int = 0
) {
    fun toPart(): Part {
        return Part(
            partId = partId,
            name = name,
            quantity = quantity
        )
    }
}
