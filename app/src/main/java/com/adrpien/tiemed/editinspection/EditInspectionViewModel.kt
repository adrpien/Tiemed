package com.adrpien.tiemed.editinspection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.repositories.FirebaseRepository

class EditInspectionViewModel: ViewModel() {

    val firebaseRepository = FirebaseRepository()

    fun updateInspection(map: Map<String, String>, id: String){
        firebaseRepository.updateInspection(map, id)
    }

    fun createInspection(inspection: Inspection){
        firebaseRepository.createNewInspection(inspection)
    }

    fun getInspection(id: String): MutableLiveData<Inspection>{
        return firebaseRepository.getInspection(id)
    }

    fun getHospitalList(): MutableLiveData<List<Hospital>>{
        return firebaseRepository.getHospitalList()
    }

}