package com.adrpien.tiemed.datamodels

data class Localization(
    val hospital: Hospital = Hospital.DLUGA,
    val ward: String = "") {
}
