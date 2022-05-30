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
import com.adrpien.tiemed.datepicker.InspectionDatePickerDialog
import com.adrpien.tiemed.datepicker.RepairDatePickerDialog
import java.lang.Long.parseLong

class InspectionListAdapter(val inspectionList: List<Inspection>, val listener: OnInspectionClickListener): RecyclerView.Adapter<InspectionListAdapter.InspectionViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.inspection_list_cell, parent, false)
        return InspectionViewHolder(view)

    }

    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {

        // Technician Button onClickListener
        holder.inspectionRowStateButton.setOnClickListener { view ->
            listener.setOnStateButtonClickListener(view)
        }

        // State Button onClickListener
        holder.inspectionRowDateButton.setOnClickListener { view ->
            listener.setOnDateButtonClickListener(view)
        }

        // Inspection row onClickListener
        holder.itemView.setOnClickListener { view ->
            listener.setOnInspectionItemClickListener(view)
        }

        // Filling TextViews with values
        holder.inspectionRowDateTextView.setText(getDateString(inspectionList[position].inspectionDate))
        holder.inspectionRowNameTextView.setText(inspectionList[position].name)
        val manufacturer: String = inspectionList[position].manufacturer + " " + inspectionList[position].model
        holder.inspectionRowManufacturerTextView.setText(manufacturer)




    }


    override fun getItemCount(): Int {
    return inspectionList.size
    }

    inner class InspectionViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {



        // State marker
        val inspectionRowStateMarker = itemview.findViewById<View>(R.id.inspectionRowStateMarker)

        // Buttons
        val inspectionRowStateButton = itemview.findViewById<ImageButton>(R.id.inspectionRowStateButton)
        val inspectionRowDateButton = itemview.findViewById<ImageButton>(R.id.inspectionRowDateButton)

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

interface OnInspectionClickListener {
    fun setOnInspectionItemClickListener(itemview: View)
    fun setOnDateButtonClickListener(itemview: View)
    fun setOnStateButtonClickListener(itemview: View)
}

// Returns date in format YYYY/MM/DD using millis in String representation
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