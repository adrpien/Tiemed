package com.adrpien.tiemed.data.remote.dto

import com.adrpien.tiemed.data.local.entities.InspectionStateEntity

data class  InspectionStateDto (
    val inspectionStateId: String,
    val state: String =  ""
){

    fun toInspectionStateEntity(): InspectionStateEntity{
        return InspectionStateEntity(
            inspectionStateId = inspectionStateId,
            state = state
        )
    }
}
