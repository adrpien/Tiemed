package com.adrpien.tiemed.inspectionlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.repositories.FirebaseRepository

class InspectionListViewModel: ViewModel() {

    val firebaseRepository = FirebaseRepository()

    val inspectionList = firebaseRepository.getInspectionList()

    fun getInspection(id: String): MutableLiveData<Inspection>{
        return firebaseRepository.getInspection(id)
    }

}