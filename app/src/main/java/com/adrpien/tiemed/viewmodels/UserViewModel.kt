package com.adrpien.tiemed.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adrpien.tiemed.data.User
import com.adrpien.tiemed.repositories.UserRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

// User ViewModel class
class UserViewModel: ViewModel() {

    private lateinit var user: MutableLiveData<User>

    val userRepository = UserRepository()

    fun getUser(): MutableLiveData<User> {
        return userRepository.getUserData()
    }

    fun setUser(user: User){
        userRepository.setUserData(user)
    }

}