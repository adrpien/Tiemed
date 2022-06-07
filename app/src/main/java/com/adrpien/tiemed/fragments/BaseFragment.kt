package com.adrpien.tiemed.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adrpien.tiemed.main.MainActivity
import java.text.DateFormat
import java.util.*

abstract class BaseFragment: Fragment(){

    protected fun startApp(){
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    fun setActionBarTitle(title: String){
        // Setting Action Bar Name according to open fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    fun getDataString(millis: Long): String{
        var date: Calendar = Calendar.getInstance()
        date.timeInMillis = millis
        var string: String = date.getTime().toString()
        return string
    }

    fun getDataString(): String{
        var date: Calendar = Calendar.getInstance()
        var string: String = date.getTime().toString()
        return string
    }
}