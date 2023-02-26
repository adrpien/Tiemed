package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.EstStateEntity
import com.adrpien.tiemed.data.local.entities.HospitalEntity

data class EstState (
    val estStateId: String = "",
    val estState: String = ""
){
    fun toEstStateEntity(): EstStateEntity {
        return EstStateEntity(
            estStateId = estStateId,
            state = estState
        )
    }

}

