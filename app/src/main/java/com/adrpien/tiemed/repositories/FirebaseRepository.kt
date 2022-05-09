package com.adrpien.tiemed.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.activities.MainActivity
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.datamodels.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// Repository class
class FirebaseRepository {

    private val REPOSITORY_DEBUG = "REPOSITORY_DEBUG"

    // MutableLiveData with user
    private lateinit var userData: MutableLiveData<User>

    // Firebase initialization
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Returns user stored in LiveData
    fun getUserData(): MutableLiveData<User> {
        userData = MutableLiveData<User>()
        // Get user UID from Firebase Authentication
        val uid = firebaseAuth.currentUser?.uid
        // Getting user from Firebase Firestore and converting it into User object
        firebaseFirestore.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                userData.postValue(user!!)
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
        return userData
    }

    // Sets user data in Firestore
    fun setUserData(user: User){
        // TODO setUserData
    }

    // Creates new repair
    fun createNewRepair(repair: Repair){
        firebaseFirestore.collection("repairs")
            .document(repair.id!!)
            .set(repair)
            .addOnFailureListener {
                Log.d(REPOSITORY_DEBUG, it.message.toString())
            }
            .addOnSuccessListener {
                Toast.makeText(MainActivity(),"Dodano rekord",Toast.LENGTH_SHORT ).show()

            }
    }

    // Return list of repairs
    fun getRepairList(): MutableLiveData<List<Repair>>{
        val repairList = MutableLiveData<List<Repair>>()
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
    fun updateRepair(map: Map<String, String>){
        firebaseFirestore.collection("repairs")
            .document("1")
            .update(map)
            .addOnSuccessListener {
                Log.d(REPOSITORY_DEBUG, "Zaktualizowano dane!")
            }
            .addOnFailureListener{
                Log.d(REPOSITORY_DEBUG, it.message.toString())

            }
    }
}