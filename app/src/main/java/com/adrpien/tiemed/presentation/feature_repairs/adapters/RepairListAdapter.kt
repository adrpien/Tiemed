package com.adrpien.tiemed.presentation.feature_users

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.model.Hospital
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.model.RepairState

class RepairListAdapter(val repairList: List<Repair>, val hospitalList: List<Hospital>, val deviceList: List<Device>, val repairStateList: List<RepairState>, val listener: OnRepairItemClickListener)
    : RecyclerView.Adapter<RepairListAdapter.RepairViewHolder>() {

    // onCreateViewHolder implementation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_row_repair, parent, false)
        return RepairViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepairViewHolder, position: Int) {

        // Sets colour of StateMarker
        // TODO This implementation works terrible, because order of documents in Firestore Database changes when you add new document, but leave this as it is to focus on other problems to solve
        fun setMarkerColour(repair: Repair) {
            if(repairStateList.isNotEmpty()) {
                when (repair.repairStateId) {
                    repairStateList[0].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.repaired)
                    }
                    repairStateList[1].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.diagnosed)
                    }
                    repairStateList[2].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.cancelled)
                    }
                    repairStateList[3].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.to_diagnose)
                    }
                    repairStateList[4].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.waiting_for_parts)
                    }
                    repairStateList[5].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.waiting)
                    }
                    repairStateList[6].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.sent_to_external_service)
                    }
                    repairStateList[7].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.finished)
                    }
                    repairStateList[8].repairStateId -> {
                        holder.stateMarker.setBackgroundResource(R.color.reported)
                    }
                }
            }
        }

        /* ******************** Click listeners ************************************************* */
        holder.itemView.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView, position)
        }
        holder.itemView.setOnLongClickListener {
            listener.setOnRepairItemLongClick(holder.itemView, position)
            true
        }

        /* ********** Filling TextViews with values ********************************************* */

        val deviceId = repairList[position].deviceId
        val device = deviceList.find { device: Device ->
            device.deviceId == deviceId
        }
        holder.repairRowIdRepairTextView.setText(repairList[position].repairId)
        holder.repairRowNameRepairTextView.setText(device?.name)
        holder.repairRowManufacturerTextView.setText(device?.manufacturer)
        holder.repairRowModelTextView.setText(device?.model)
        holder.repairRowSnNumberTextView.append(device?.serialNumber.toString())
        holder.repairInNumberTextView.append(device?.inventoryNumber.toString())
        holder.repairRowWardTextView.setText(repairList[position].ward)
        setMarkerColour(repairList[position])
        // holder.repairRowOpeningDateTextView.append(
        //     com.adrpien.tiemed.presentation.feature_inspections.getDateString(
        //         repairList[position].openingDate.toLong()
        //     )
        // )


    }



    // getItemCount implementation
    override fun getItemCount(): Int {
        return repairList.size
    }

    // ViewHolder class implementation
    inner class RepairViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // State marker
        val stateMarker = itemView.findViewById<View>(R.id.repairRowStateMarker)

        // TextViews
        val repairRowOpeningDateTextView = itemView.findViewById<TextView>(R.id.repairRowOpeningDateTextView)
        val repairRowIdRepairTextView = itemView.findViewById<TextView>(R.id.repairRowIdTextView)
        val repairRowNameRepairTextView = itemView.findViewById<TextView>(R.id.repairRowNameTextView)
        val repairRowManufacturerTextView = itemView.findViewById<TextView>(R.id.repairRowManufacturerTextView)
        val repairRowModelTextView = itemView.findViewById<TextView>(R.id.repairRowModelTextView)
        val repairRowSnNumberTextView = itemView.findViewById<TextView>(R.id.repairRowSNTextView)
        val repairInNumberTextView = itemView.findViewById<TextView>(R.id.repairRowINTextView)
        val repairRowWardTextView = itemView.findViewById<TextView>(R.id.repairRowWardTextView)

    }
}

// Creating listener implemented by fragment
interface OnRepairItemClickListener{
    fun setOnRepairItemClick(itemView: View, position: Int)
    fun setOnRepairItemLongClick(itemView: View, position: Int)
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


