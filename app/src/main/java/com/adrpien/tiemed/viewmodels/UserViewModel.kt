package com.adrpien.tiemed.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.User
import com.adrpien.tiemed.repositories.FirebaseRepository

// User ViewModel class
class UserViewModel: ViewModel() {

    // User stored in MuatableLiveData
    private lateinit var user: MutableLiveData<User>

    // Repository initalization
    private val userRepository = FirebaseRepository()

    // Returns User
    fun getUser(): MutableLiveData<User> {
        return userRepository.getUserData()
    }

    // Sets User
    fun setUser(user: User){
        userRepository.setUserData(user)
    }

}