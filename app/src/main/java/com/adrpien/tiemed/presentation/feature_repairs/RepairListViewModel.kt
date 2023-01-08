package com.adrpien.tiemed.presentation.feature_repairs

import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.data.FirebaseRepository

class RepairListViewModel : ViewModel() {

    val firebaseRepository = FirebaseRepository()

    val repairList = firebaseRepository.getRepairList()
}