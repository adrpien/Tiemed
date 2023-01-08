package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.EstStateEntity

data class EstStateDto (
    val estStateId: String,
    val state: String = ""
){
        fun toEstStateEntity(): EstStateEntity {
            return EstStateEntity(
                estStateId = estStateId,
                state = state
            )
        }
}

