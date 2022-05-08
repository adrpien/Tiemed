package com.adrpien.tiemed.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        // TODO
    }

    // Return list of repairss
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
}