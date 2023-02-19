package com.adrpien.tiemed.presentation.feature_inspections

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.model.Hospital
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.model.InspectionState

class InspectionListAdapter(val inspectionList: List<Inspection>, val hospitalList: List<Hospital>, val deviceList: List<Device>,val  inspectionStateList: List<InspectionState>, val listener: OnInspectionItemClickListener): RecyclerView.Adapter<InspectionListAdapter.InspectionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_row_inspection, parent, false)
        return InspectionViewHolder(view)

    }

    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {

/* ******************** Click listeners ************************************************* */
        holder.itemView.setOnClickListener { view ->
            listener.setOnInspectionItemLongClick(holder.itemView, position)
        }

        // Inspection row onClickListener
        holder.itemView.setOnClickListener { view ->
            listener.setOnInspectionItemClick(view, position)
        }

        // Filling TextViews with values
        holder.inspectionRowIdTextView.setText(inspectionList[position].inspectionId)
        holder.inspectionRowDateTextView.append(getDateString(inspectionList[position].inspectionDate.toLong()))
        holder.inspectionRowNameTextView.setText(deviceList[deviceList.indexOf(inspectionList[position].device)].name)
        holder.inspectionRowManufacturerTextView.setText(deviceList[deviceList.indexOf(inspectionList[position].device)].manufacturer)
        holder.inspectionRowModelTextView.setText(deviceList[deviceList.indexOf(inspectionList[position].device)].model)
        holder.inspectionRowSNTextView.append(deviceList[deviceList.indexOf(inspectionList[position].device)].serialNumber)
        holder.inspectionRowINTextView.append(deviceList[deviceList.indexOf(inspectionList[position].device)].inventoryNumber)
        holder.inspectionRowWardTextView.setText(inspectionList[position].ward)
    }

    override fun getItemCount(): Int {
    return inspectionList.size
    }

    inner class InspectionViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {

        // State marker
        val inspectionRowStateMarker = itemview.findViewById<View>(R.id.inspectionRowStateMarker)

        // TextViews
        val inspectionRowIdTextView = itemView.findViewById<TextView>(R.id.inspectionRowIdTextView)
        val inspectionRowDateTextView = itemView.findViewById<TextView>(R.id.inspectionRowDateTextView)
        val inspectionRowNameTextView = itemView.findViewById<TextView>(R.id.inspectionRowNameTextView)
        val inspectionRowManufacturerTextView = itemView.findViewById<TextView>(R.id.inspectionRowManufacturerTextView)
        val inspectionRowModelTextView = itemView.findViewById<TextView>(R.id.inspectionRowModelTextView)
        val inspectionRowSNTextView = itemView.findViewById<TextView>(R.id.inspectionRowINTextView)
        val inspectionRowINTextView = itemView.findViewById<TextView>(R.id.inspectionRowSNTextView)
        val inspectionRowWardTextView = itemView.findViewById<TextView>(R.id.inspectionRowWardTextView)
    }
}

interface OnInspectionItemClickListener {
    fun setOnInspectionItemClick(itemview: View, position: Int)
    fun setOnInspectionItemLongClick(itemview: View, position: Int)

}

// Returns date in format YYYY/MM/DD String representation using date in millis
fun getDateString(millis: Long): String {

    var text: String = ""

    val date = Calendar.getInstance()
    date.timeInMillis = millis

    // Creating String with with date
    val yearString = date.get(Calendar.YEAR).toString()

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