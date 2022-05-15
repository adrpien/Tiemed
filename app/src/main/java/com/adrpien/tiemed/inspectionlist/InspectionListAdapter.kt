package com.adrpien.tiemed.inspectionlist

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.datamodels.Inspection
import java.lang.reflect.Array.get
import java.util.*
import java.util.Calendar as Calendar1

class InspectionListAdapter(val inspectionList: List<Inspection>, listener: OnInspectionItemClickListener): RecyclerView.Adapter<InspectionListAdapter.InspectionViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.inspection_list_cell, parent, false)
        return InspectionViewHolder(view)

    }

    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {

        holder.inspectionIdTextView.setText(inspectionList[position].id)
        holder.inspectionDateTextView.setText(getDateString(inspectionList[position].inspectionDate))
        holder.inspectionStateTextView.setText(inspectionList[position].inspectionState)

    }


    override fun getItemCount(): Int {
    return inspectionList.size
    }

    inner class InspectionViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {

        val inspectionIdTextView = itemView.findViewById<TextView>(R.id.inspectionIdTextView)
        val inspectionStateTextView = itemView.findViewById<TextView>(R.id.inspectionStateTextView)
        val inspectionDateTextView = itemView.findViewById<TextView>(R.id.inspectionDateTextView)

    }
}

interface OnInspectionItemClickListener {
    fun setOnInspectionItemClickListener(itemview: View)
}


fun getDateString(date: String): String {

    var text: String = ""

    // Parsing millis in String to Calendar Object
    val millis: Long = Integer.parseInt(date).toLong()
    val date = Calendar.getInstance()
    date.timeInMillis = millis

    // Creating String with with date
    val yearString = if(date.get(Calendar.YEAR)<10){
        date.get(Calendar.YEAR). toString()
    }
    else {
        "0" + date.get(Calendar.YEAR).toString()
    }

    val month = date.get(Calendar.MONTH) + 1
    val monthString = if(date.get(Calendar.MONTH)<10){
        date.get(Calendar.MONTH). toString()
    }
    else {
        "0" + date.get(Calendar.MONTH).toString()
    }

    val day = date.get(Calendar.DAY_OF_MONTH)
    val dayString = if(date.get(Calendar.DAY_OF_MONTH)<10){
        date.get(Calendar.DAY_OF_MONTH). toString()
    }
    else {
        "0" + date.get(Calendar.DAY_OF_MONTH).toString()
    }

    text = "$yearString/$monthString/$dayString"

    return text
}