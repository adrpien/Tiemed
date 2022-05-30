package com.adrpien.tiemed.editrepair

import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.repositories.FirebaseRepository

class EditRepairViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    fun createRepair(repair: Repair){
        repository.createNewRepair(repair)
    }

    fun updateRepair(map: Map<String, String>, uid: String){
        repository.updateRepair(map, uid)
    }
}
