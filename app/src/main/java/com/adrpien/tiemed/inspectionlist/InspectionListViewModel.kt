package com.adrpien.tiemed.inspectionlist

import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.repositories.FirebaseRepository

class InspectionListViewModel: ViewModel() {

    val firebaseRepository = FirebaseRepository()

    val inspectionList = firebaseRepository.getInspectionList()

}