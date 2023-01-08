package com.adrpien.tiemed.presentation.feature_inspections

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.data.FirebaseRepository

class InspectionListViewModel: ViewModel() {

    val firebaseRepository = FirebaseRepository()

    val inspectionList = firebaseRepository.getInspectionList()


    fun getInspection(id: String): MutableLiveData<Inspection>{
        return firebaseRepository.getInspection(id)
    }

    fun updateInspection(map: Map<String, String>, id: String){
        firebaseRepository.updateInspection(map, id)
    }

}