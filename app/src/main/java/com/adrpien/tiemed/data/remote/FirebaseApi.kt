package com.adrpien.tiemed.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.users.User


// Repository class
class  FirebaseApi {

    private val INSPECTION_REPOSITORY_DEBUG = "INSPECTION_REPOSITORY_DEBUG"

    // Signature
    private lateinit var signatureURL: MutableLiveData<String>

    // User
    private lateinit var user: MutableLiveData<User>

    private var signature: MutableLiveData<ByteArray> = MutableLiveData<ByteArray>()

    // MutableLiveData with inspection
    private var inspection: MutableLiveData<Inspection> = MutableLiveData<Inspection>()


    // MutableLiveData with list of inspections
    private lateinit var inspectionList: MutableLiveData<List<Inspection>>

    // MutableLiveData with list of hospitals
    private lateinit var hospitalList: MutableLiveData<MutableList<Hospital>>


    /*
    *********************************
    INSPECTIONS
    *********************************
     */

    // Returns list of inspections
    fun getInspectionList():MutableLiveData<List<Inspection>>{
        inspectionList = MutableLiveData<List<Inspection>>()
        firebaseFirestore.collection("inspections")
            .get()
            .addOnSuccessListener{
                val list = it.toObjects(Inspection::class.java)
                inspectionList.postValue(list)
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }

        return inspectionList
    }

    // Creates new inspection
    fun createNewInspection(inspection: Inspection){
        var documentReference = firebaseFirestore.collection("inspections")
            .document()
        var map = mapOf<String, String>(
            "inspectionUid" to documentReference.id,
            "inspectionStateString" to inspection.inspectionStateString,
            "deviceId" to inspection.deviceId,
            "name" to inspection.name,
            "manufacturer" to inspection.manufacturer,
            "model" to inspection.model,
            "serialNumber" to inspection.serialNumber,
            "inventoryNumber" to inspection.inventoryNumber,
            "hospital" to inspection.hospitalString,
            "ward" to inspection.ward,
            "electricalsafetyTestString" to inspection.electricalSafetyTestString,
            "comment" to inspection.comment,
            "inspectionDate" to inspection.inspectionDate,
            "recipient" to inspection.recipient,
            "signature" to inspection.recipientSignature
        )
            documentReference.set(map)


    }

    // Update inspection record
    fun updateInspection(map: Map<String, String>, id: String){
        firebaseFirestore.collection("inspections")
            .document(id)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "Inspection record updated")
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
    }

    //  Return inspection record according do delivered id
    fun getInspection(uid: String): MutableLiveData<Inspection> {
        firebaseFirestore.collection("inspections")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val inspection = it.toObject(Inspection::class.java)!!
                this.inspection.postValue(inspection)
                Log.d(REPOSITORY_DEBUG, "Inspection record delivered")
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
        return inspection
    }

    /*
    *********************************
    SIGNATURES
    *********************************
     */

    // Upload signature to finish
    fun uploadSignature(signatureBytes: ByteArray, signatureId: String){
        firebaseStorage.getReference("signatures")
            .child("${signatureId}.jpg")
            .putBytes(signatureBytes)
            .addOnCompleteListener{
                Log.d(REPOSITORY_DEBUG, "COMPLETE UPLOAD PHOTO")
            }
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "Signature uploaded")
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
    }

    // Get inspection signature
    fun getSignature(Uid: String): MutableLiveData<ByteArray> {
        firebaseStorage.getReference("signatures")
            .child("${Uid}.jpg")
            .getBytes(10000000) // 10MB
            .addOnSuccessListener {
                val signature = it
                this.signature.postValue(signature)
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
        return signature
    }


    /*
    *********************************
    HOSPITALS
    *********************************
     */

    // Return list of hospitals
    fun getHospitalList(): MutableLiveData<MutableList<Hospital>>{
        hospitalList = MutableLiveData<MutableList<Hospital>>()
        firebaseFirestore.collection("hospitals")
            .get()
            .addOnSuccessListener {
                val hospital = it.toObjects(Hospital::class.java)
                hospitalList.postValue(hospital)
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())

            }
        return hospitalList
    }
}