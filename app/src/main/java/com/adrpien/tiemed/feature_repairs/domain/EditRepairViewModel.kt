package com.adrpien.tiemed.feature_repairs.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.data.local.entities.Repair
import com.adrpien.tiemed.data.FirebaseRepository

class EditRepairViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    fun createRepair(repair: Repair){
        firebaseRepository.createNewRepair(repair)
    }

    fun updateRepair(map: Map<String, String>, uid: String){
        firebaseRepository.updateRepair(map, uid)
    }

    fun getRepair(id: String): MutableLiveData<Repair> {
        return firebaseRepository.getRepair(id)
    }

    fun getHospitalList(): MutableLiveData<MutableList<Hospital>>{
        return firebaseRepository.getHospitalList()
    }

    fun getSignature(uid: String): MutableLiveData<ByteArray>{
        return firebaseRepository.getSignature(uid)
    }
}
