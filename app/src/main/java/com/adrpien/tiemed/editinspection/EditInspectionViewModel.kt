package com.adrpien.tiemed.editinspection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.repositories.FirebaseRepository

class EditInspectionViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    fun updateInspection(map: Map<String, String>, id: String){
        firebaseRepository.updateInspection(map, id)
    }

    fun createInspection(inspection: Inspection){
        firebaseRepository.createNewInspection(inspection)
    }

    fun getInspection(uid: String): MutableLiveData<Inspection>{
        return firebaseRepository.getInspection(uid)
    }

    fun getHospitalList(): MutableLiveData<List<Hospital>>{
        return firebaseRepository.getHospitalList()
    }

    fun uploadSignature(signatureBytes: ByteArray, signatureId: String){
        firebaseRepository.uploadSignature(signatureBytes, signatureId)
    }


    fun getSignature(inspectionUid: String): MutableLiveData<ByteArray> {
        return firebaseRepository.getSignature(inspectionUid)
    }

}