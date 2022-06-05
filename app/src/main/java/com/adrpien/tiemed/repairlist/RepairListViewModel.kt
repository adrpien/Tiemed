package com.adrpien.tiemed.repairlist

import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.repositories.FirebaseRepository

class RepairListViewModel : ViewModel() {

    val firebaseRepository = FirebaseRepository()

    val repairList = firebaseRepository.getRepairList()
}