package com.adrpien.tiemed.presentation.feature_users

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.domain.model.Repair

class RepairListAdapter(var repairList: List<Repair>, val listener: onRepairItemClickListener)
    : RecyclerView.Adapter<RepairListAdapter.RepairViewHolder>() {

    // onCreateViewHolder implementation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.repair_list_row, parent, false)
        return RepairViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepairViewHolder, position: Int) {

        // View Button on Click listener
        holder.repairRowViewRepairButton.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView)
        }

        // Edit Button on Click listener
        holder.repairRowEditRepairButton.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView)
        }


        // Filling TextViews with values

        // Setting id
        holder.repairRowIdRepairTextView.setText(repairList[position].repairId)

        // Setting name
        // holder.repairRowNameRepairTextView.setText(repairList[position].name)

        // Setting manufacturer
        // holder.repairRowManufacturerTextView.setText(repairList[position].manufacturer)

        // Setting model
        // holder.repairRowModelTextView.setText(repairList[position].model)

        // Setting serial number
        // holder.repairRowSnNumberTextView.setText(repairList[position].serialNumber)

        // Setting inventory number
        // holder.repairInNumberTextView.setText(repairList[position].inventoryNumber)

        // Setting  hospital
        // holder.repairRowHospitalTextView.setText(repairList[position].hospitalString)

        // Setting ward
        // holder.repairRowWardTextView.setText(repairList[position].ward)

        // TODO State Marker implementation
        /*if(repairList[position].repairStateString == RepairState.FIXED.toString()){ holder.stateMarker.setBackgroundColor(Color.GREEN) }
        if(repairList[position].repairStateString == RepairState.SUBMITTED.toString()){ holder.stateMarker.setBackgroundColor(Color.RED) }
        if(repairList[position].repairStateString == RepairState.SENT_TO_SERVICE.toString()){ holder.stateMarker.setBackgroundColor(Color.BLUE) }
        */
    }

    // getItemCount implementation
    override fun getItemCount(): Int {
        return repairList.size
    }

    // ViewHolder class implementation
    inner class RepairViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // State marker
        val stateMarker = itemView.findViewById<View>(R.id.repairRowStateMarker)

        // Buttons
        val repairRowEditRepairButton = itemView.findViewById<ImageButton>(R.id.repairRowEditButton)
        val repairRowViewRepairButton = itemView.findViewById<ImageButton>(R.id.repairRowViewButton)

        // TextViews
        val repairRowIdRepairTextView = itemView.findViewById<TextView>(R.id.repairRowIdTextView)
        val repairRowNameRepairTextView = itemView.findViewById<TextView>(R.id.repairRowNameTextView)
        val repairRowManufacturerTextView = itemView.findViewById<TextView>(R.id.repairRowManufacturerTextView)
        val repairRowModelTextView = itemView.findViewById<TextView>(R.id.repairRowModelTextView)
        val repairRowSnNumberTextView = itemView.findViewById<TextView>(R.id.repairRowSNTextView)
        val repairInNumberTextView = itemView.findViewById<TextView>(R.id.repairRowINTextView)
        val repairRowWardTextView = itemView.findViewById<TextView>(R.id.repairRowWardTextView)
        val repairRowHospitalTextView = itemView.findViewById<TextView>(R.id.repairRowHospitalTextView)

    }
}

// Creating listener implemented by fragment
interface onRepairItemClickListener{
    fun setOnRepairItemClick(itemView: View)
    fun setOnEditRepairButtonClick(itemView: View)
    fun setOnViewRepairButtonClick(itemView: View)
}

// Returns date in format YYYY/MM/DD String representation using date in millis
fun getDateString(date: String): String {

    var text: String = ""

    // Parsing millis in String to Calendar Object
    val millis: Long = java.lang.Long.parseLong(date).toLong()
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