package com.adrpien.tiemed.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*


// Repostiory class
class UserRepository() {

    private val REPOSITORY_DEBUG = "REPOSITORY_DEBUG"

    private lateinit var userData: MutableLiveData<User>


        private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Returns user stored in LiveData
    fun getUserData(): MutableLiveData<User> {


        userData = MutableLiveData<User>()


        // Get user UID from Firebase Authetication
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
}