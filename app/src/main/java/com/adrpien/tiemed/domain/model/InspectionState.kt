package com.adrpien.tiemed.domain.model

import com.adrpien.tiemed.data.local.entities.InspectionStateEntity

data class  InspectionState (
    val inspectionStateId: String = "",
    val inspectionState: String =  ""
        ){
    fun toInspectionStateEntity(): InspectionStateEntity {
        return InspectionStateEntity(
            inspectionStateId = inspectionStateId,
            inspectionState = inspectionState
        )
    }

}
