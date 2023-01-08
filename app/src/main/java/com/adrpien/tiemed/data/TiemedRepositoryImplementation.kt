package com.adrpien.tiemed.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.data.local.entities.Repair
import com.adrpien.tiemed.datamodels.users.User
import com.adrpien.tiemed.domain.repository.TiemedRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


// Repository class
class  TiemedRepositoryImplementation: TiemedRepository {

    private val REPOSITORY_DEBUG = "REPOSITORY_DEBUG"

    // Signature
    private lateinit var signatureURL: MutableLiveData<String>

    // User
    private lateinit var user: MutableLiveData<User>

    private var signature: MutableLiveData<ByteArray> = MutableLiveData<ByteArray>()

    // MutableLiveData with inspection
    private var inspection: MutableLiveData<Inspection> = MutableLiveData<Inspection>()

    // Repair
    private lateinit var repair: MutableLiveData<Repair>

    // MutableLiveData with list of inspections
    private lateinit var inspectionList: MutableLiveData<List<Inspection>>

    // MutableLiveData with list of repairs
    private lateinit var repairList: MutableLiveData<MutableList<Repair>>

    // MutableLiveData with list of hospitals
    private lateinit var hospitalList: MutableLiveData<MutableList<Hospital>>

    // Firebase initialization
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private  val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()


    /*
    *********************************
    USERS
    *********************************
    */

    // Returns user stored in LiveData
    fun getUser(): MutableLiveData<User> {

        val user = MutableLiveData<User>()

        // Get user UID from Firebase Authentication
        val uid = firebaseAuth.currentUser?.uid
        // Getting user from Firebase Firestore and converting it into User object
        firebaseFirestore.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)!!
                this.user.postValue(user)
                Log.d(REPOSITORY_DEBUG, "User data delivered")
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
        return user
    }

    // Update user record
    fun updateUser(map: Map<String, String>, uid: String){
        firebaseFirestore.collection("users")
            .document(uid)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "User data updated")
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, it.message.toString())

            }
    }

    /*
    *********************************
    REPAIRS
    *********************************
     */

    // Creates new repair
    fun createNewRepair(repair: Repair){

        var documentReference = firebaseFirestore.collection("repairs")
            .document()
        var map = mapOf<String, String>(
            "repairUid" to documentReference.id,
            "repairState" to repair.repairStateString,
            "deviceId" to repair.deviceId,
            "name" to repair.name,
            "manufacturer" to repair.manufacturer,
            "model" to repair.model,
            "serialNumber" to repair.serialNumber,
            "inventoryNumber" to repair.inventoryNumber,
            "hospital" to repair.hospitalString,
            "ward" to repair.ward,
            "defectDescription" to repair.defectDescription,
            "repairDescription" to repair.repairDescription,
            "partDecription" to repair.partDescription,
            "comment" to repair.comment,
            "electricalSafetyTest" to repair.electricalSafetyTestString,
            "closingDate" to repair.closingDate,
            "openingDate" to repair.openingDate,
            "repairingDate" to repair.repairingDate,
            "rate" to repair.rate.toString(),
            "recipient" to repair.recipient,
            "recipientSignature" to repair.recipientSignature
        )
        documentReference.set(map)
    }

    // Return list of repairs
    fun getRepairList(): MutableLiveData<MutableList<Repair>>{
        repairList = MutableLiveData<MutableList<Repair>>()
        firebaseFirestore.collection("repairs")
            .get()
            .addOnSuccessListener {
                val repair = it.toObjects(Repair::class.java)
                repairList.postValue(repair)
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
        return repairList
    }

    // Update repair record
    fun updateRepair(map: Map<String, String>, id: String){
        firebaseFirestore.collection("repairs")
            .document(id)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "Repair record updated")
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, it.message.toString())

            }
    }

    // Returns repair according to delivered id
    fun getRepair(uid: String): MutableLiveData<Repair> {
            firebaseFirestore.collection("repairs")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    val repair = it.toObject(Repair::class.java)!!
                    this.repair.postValue(repair)
                    Log.d(REPOSITORY_DEBUG, "Repair record delivered")
                }
                .addOnFailureListener {
                    Log.d(REPOSITORY_DEBUG, it.message.toString())
                }
            return repair
    }

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