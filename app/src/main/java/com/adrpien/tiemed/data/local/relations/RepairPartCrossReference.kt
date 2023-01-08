package com.adrpien.tiemed.data.local.relations

import androidx.room.Entity

@Entity(primaryKeys = [ "repairId", "partId"])
class RepairPartCrossReference(
    val repairId: String,
    val partId: String
) {
}