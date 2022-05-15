package com.adrpien.tiemed.datamodels

import java.util.*

data class Inspection(
    var id: String = "0",
    var inspectionDate: String = Calendar.getInstance().time.toString(),
    var inspectionState: String = InspectionState.Do_zrealizowania.toString(),
    // var photos: List<Photo> = listOf<Photo>()
)
