package com.adrpien.tiemed.data.local.entities

// RepairState is enum class which stores possible states of repair
enum class RepairState {
    SUBMITTED,
    PENDING,
    FIXED,
    COMPLETED,
    INVOICED,
    AWAITING,
    SENT_TO_SERVICE,
    CANCELED
}