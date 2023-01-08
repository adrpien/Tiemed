package com.adrpien.tiemed.feature_repairs.domain

import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.data.FirebaseRepository

class RepairListViewModel : ViewModel() {

    val firebaseRepository = FirebaseRepository()

    val repairList = firebaseRepository.getRepairList()
}