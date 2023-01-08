package com.adrpien.tiemed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrpien.tiemed.domain.model.EstState

@Entity
data class EstStateEntity (
    @PrimaryKey(autoGenerate = false)
    val estStateId: String,
    val state: String = ""
){

    fun toEstState(): EstState {
        return EstState(
            estStateId = estStateId,
            state = state
        )
    }
}

