package com.adrpien.tiemed.core.base_fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.reflect.full.memberProperties

@AndroidEntryPoint
abstract class BaseFragment: Fragment(){

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

    // Creating map of inspection fields with their values
    fun createInspectionMap(inspection: Inspection): Map<String, String> {
        var map: MutableMap<String, String> = mutableMapOf()
        for (component in Inspection::class.memberProperties){
            map.put(component.name, component.get(inspection).toString())
        }
        return map
    }
}