package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.RepairStateEntity

data class RepairStateDto (
    val repairStateId: String,
    val state: String = ""
        ) {

    fun toRepairStateEntity(): RepairStateEntity {
        return RepairStateEntity(
            repairStateId = repairStateId,
            state = state
        )
    }
}