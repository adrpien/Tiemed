package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.RepairState

@Entity
data class RepairStateEntity (
    @PrimaryKey(autoGenerate = false)
    val repairStateId: String,
    val repairState: String = ""
) {

    fun toRepairState(): RepairState {
        return RepairState(
            repairStateId = repairStateId,
            repairState = repairState
        )
    }
}