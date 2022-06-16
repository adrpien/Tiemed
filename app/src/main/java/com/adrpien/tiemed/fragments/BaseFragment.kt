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

    fun getDateString(millis: Long): String{
        var date: Calendar = Calendar.getInstance()
        date.timeInMillis = millis


        var day = date.get(Calendar.DAY_OF_MONTH)
        var month = date.get(Calendar.MONTH) + 1
        var year = date.get(Calendar.YEAR)

        var dayString = if( day < 10) {
            "0" + day.toString()
        } else {
            day.toString()
        }

        var monthString = if(month < 10){
            "0" + month.toString()
        } else {
            month.toString()
        }

        var yearString = year.toString()

        var string = "$dayString/$monthString/$year"

        return string
    }

    fun getDateString(): String{
        var date: Calendar = Calendar.getInstance()
        var string: String = date.getTime().toString()
        return string
    }
}