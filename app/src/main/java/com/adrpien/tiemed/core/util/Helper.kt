package com.adrpien.tiemed.core.util

import androidx.appcompat.app.AppCompatActivity
import com.adrpien.tiemed.domain.model.Inspection
import java.util.*
import kotlin.reflect.full.memberProperties

class Helper {

    companion object {

        // Changing millis into String format
        fun getDateString(millis: Long): String{
            var dateString = ""
            if(millis != 0L) {
                val date: Calendar = Calendar.getInstance()
                date.timeInMillis = millis

                val day = date.get(Calendar.DAY_OF_MONTH)
                val month = date.get(Calendar.MONTH) + 1
                val year = date.get(Calendar.YEAR)

                val dayString = if( day < 10) { "0" + day.toString() }
                else { day.toString() }

                val monthString = if(month < 10)
                { "0" + month.toString() }
                else { month.toString() }

                val yearString = year.toString()
                dateString = "$dayString/$monthString/$yearString"
                return dateString
            }
            return dateString
        }

        // Creating map of inspection fields with their values
        fun createInspectionMap(inspection: Inspection): Map<String, String> {
            var map: MutableMap<String, String> = mutableMapOf()
            for (component in Inspection::class.memberProperties){
                map.put(component.name, component.get(inspection).toString())
            }
            return map
        }

        // Setting Action Bar Name according to open fragment
        fun setActionBarTitle(title: String){
            //(requireActivity() as AppCompatActivity).supportActionBar?.title = title
        }
    }

}