package com.adrpien.tiemed.datamodels.users

import com.adrpien.tiemed.datamodels.users.User
import com.adrpien.tiemed.datamodels.users.UserType

data class HospitalWorker(val userType: UserType = UserType.HospitalWorker): User() {
}