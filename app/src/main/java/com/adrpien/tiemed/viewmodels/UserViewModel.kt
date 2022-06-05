package com.adrpien.tiemed.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.datamodels.users.User
import com.adrpien.tiemed.repositories.FirebaseRepository

// User ViewModel class
class UserViewModel: ViewModel() {

    // User stored in MuatableLiveData
    private lateinit var user: MutableLiveData<User>

    // Repository initalization
    private val userRepository = FirebaseRepository()

    // Returns User
    fun getUser(): User {
        return userRepository.getUser()
    }

    // Sets User
    fun setUser(user: User){
        userRepository.setUserData(user)
    }

}