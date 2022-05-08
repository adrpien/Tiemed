package com.adrpien.tiemed.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.repositories.FirebaseRepository

class RepairListViewModel : ViewModel() {
    val repository = FirebaseRepository()
    val repairList= repository.getRepairList()
}