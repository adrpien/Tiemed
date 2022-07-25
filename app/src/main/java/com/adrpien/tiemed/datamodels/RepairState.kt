package com.adrpien.tiemed.datamodels

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