package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.RepairStateEntity

data class RepairState (
    var repairStateId: String = "",
    var repairState: String = ""
        ) {

    fun toRepairStateEntity(): RepairStateEntity {
        return  RepairStateEntity(
            repairStateId = repairStateId,
            repairState = repairState
        )
    }

}