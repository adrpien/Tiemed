package com.adrpien.tiemed.inspectionlist

import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.repositories.FirebaseRepository

class InspectionListViewModel: ViewModel() {

    val repository = FirebaseRepository()

    val inspectionList = repository.getInspectionList()




}