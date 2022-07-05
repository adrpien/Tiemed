package com.adrpien.tiemed.repositories

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.main.MainActivity
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.datamodels.users.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


// Repository class
class FirebaseRepository {

    private val REPOSITORY_DEBUG = "REPOSITORY_DEBUG"

    // Signature
    private lateinit var signatureURL: Uri

    // User
    private lateinit var user: User

    // MutableLiveData with inspection
    var inspection: MutableLiveData<Inspection> = MutableLiveData<Inspection>()

    // Repair
    private lateinit var repair: Repair

    // MutableLiveData with list of inspections
    private lateinit var inspectionList: MutableLiveData<List<Inspection>>

    // MutableLiveData with list of repairs
    private lateinit var repairList: MutableLiveData<List<Repair>>

    // MutableLiveData with list of hospitals
    private lateinit var hospitalList: MutableLiveData<List<Hospital>>

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
    fun getUser(): User {

        // Get user UID from Firebase Authentication
        val uid = firebaseAuth.currentUser?.uid
        // Getting user from Firebase Firestore and converting it into User object
        firebaseFirestore.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                user = it.toObject(User::class.java)!!
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
        firebaseFirestore.collection("repairs")
            .document(repair.id!!)
            .set(repair)
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "Repair record created")
            }
    }

    // Return list of repairs
    fun getRepairList(): MutableLiveData<List<Repair>>{
        repairList = MutableLiveData<List<Repair>>()
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
    fun getRepair(id: String): Repair {
            firebaseFirestore.collection("repairs")
                .document(id)
                .get()
                .addOnSuccessListener {
                    repair = it.toObject(Repair::class.java)!!
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
            .get().addOnSuccessListener{
                val inspection = it.toObjects(Inspection::class.java)
                inspectionList.postValue(inspection)
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }

        return inspectionList
    }

    // Creates new inspection
    fun createNewInspection(inspection: Inspection){
        firebaseFirestore.collection("inspections")
            .document()
            .set(inspection)
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
            .addOnSuccessListener {
                Toast.makeText(MainActivity(),"Inspection record created",Toast.LENGTH_SHORT ).show()
            }
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
    fun getInspection(id: String): MutableLiveData<Inspection> {
        firebaseFirestore.collection("inspections")
            .document(id)
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
            }
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "Signature uploaded")
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, "it.message.toString()")
            }
    }

    // Get inspection signature Url
    fun getInspectionSignatureUrl(inspectionId: String): Uri {
        firebaseStorage.getReference("signatures")
            .child("${inspectionId}.jpg")
            .downloadUrl
                        .addOnSuccessListener {
                            signatureURL = it
                        }
                        .addOnFailureListener{
                            Log.d(REPOSITORY_DEBUG, it.message.toString())
                        }
        return signatureURL
    }

    /*
    *********************************
    HOSPITALS
    *********************************
     */

    // Return list of hospitals
    fun getHospitalList(): MutableLiveData<List<Hospital>>{
        hospitalList = MutableLiveData<List<Hospital>>()
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