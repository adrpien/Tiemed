package com.adrpien.tiemed.inspectionlist

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.datamodels.Inspection
import java.lang.Long.parseLong

class InspectionListAdapter(val inspectionList: List<Inspection>, listener: OnInspectionItemClickListener): RecyclerView.Adapter<InspectionListAdapter.InspectionViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.inspection_list_cell, parent, false)
        return InspectionViewHolder(view)

    }

    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {

        holder.inspectionRowIdTextView.setText(inspectionList[position].id)
        holder.inspectionRowDateTextView.setText(getDateString(inspectionList[position].inspectionDate))

    }


    override fun getItemCount(): Int {
    return inspectionList.size
    }

    inner class InspectionViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {

        // State marker
        val inspectionRowStateMarker = itemview.findViewById<View>(R.id.inspectionRowStateMarker)

        // Buttons
        val inspectionRowTechnicianButton = itemview.findViewById<ImageButton>(R.id.inspectionRowTechnicianButton)
        val inspectionRowStateButton = itemview.findViewById<ImageButton>(R.id.inspectionRowStateButton)

        // TextViews
        val inspectionRowIdTextView = itemView.findViewById<TextView>(R.id.inspectionRowIdTextView)
        val inspectionRowDateTextView = itemView.findViewById<TextView>(R.id.inspectionRowDateTextView)
        val inspectionRowNameTextView = itemView.findViewById<TextView>(R.id.inspectionRowNameTextView)
        val inspectionRowManufacturerTextView = itemView.findViewById<TextView>(R.id.inspectionRowManufacturerTextView)
        val inspectionRowSNTextView = itemView.findViewById<TextView>(R.id.inspectionRowINTextView)
        val inspectionRowINTextView = itemView.findViewById<TextView>(R.id.inspectionRowINTextView)
        val inspectionRoweLocalizationTextView = itemView.findViewById<TextView>(R.id.inspectionRowLocalizationTextView)

    }
}

interface OnInspectionItemClickListener {
    fun setOnInspectionItemClickListener(itemview: View)
}

// Returns date in format YYYY/MM/DD
fun getDateString(date: String): String {

    var text: String = ""

    // Parsing millis in String to Calendar Object
    val millis: Long = parseLong(date).toLong()
    val date = Calendar.getInstance()
    date.timeInMillis = millis

    // Creating String with with date
    val yearString = date.get(Calendar.YEAR). toString()

    val month = date.get(Calendar.MONTH) + 1
    val monthString = if(month<10){
            "0" + month.toString()
        } else {
            month.toString()
        }

    val day = date.get(Calendar.DAY_OF_MONTH)
    val dayString = if(date.get(Calendar.DAY_OF_MONTH)<10){
        "0" + day.toString()
    }
    else {
        day.toString()
    }

    text = "$yearString/$monthString/$dayString"

    return text
}