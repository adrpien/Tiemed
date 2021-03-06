package com.adrpien.tiemed.editinspection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.repositories.FirebaseRepository

class EditInspectionViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    /*
    *********************************
    INSPECTION
    *********************************
     */

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

    /*
    *********************************
    SIGNATURE
    *********************************
     */

    fun uploadSignature(signatureBytes: ByteArray, signatureId: String){
        firebaseRepository.uploadSignature(signatureBytes, signatureId)
    }


    fun getSignature(inspectionUid: String): MutableLiveData<ByteArray> {
        return firebaseRepository.getSignature(inspectionUid)
    }

    /*
    *********************************
    INSPECTION
    *********************************
     */

    fun createRepair(repair: Repair){
        firebaseRepository.createNewRepair(repair)
    }
}