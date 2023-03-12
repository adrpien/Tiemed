package com.adrpien.tiemed.presentation.feature_inspections

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.core.util.Helper
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
        holder.itemView.setOnClickListener { view ->
            listener.setOnInspectionItemClick(holder.itemView, position)
        }

/* *********************** Filling TextViews with values ************************************ */
        val deviceId = inspectionList[position].deviceId
        val device = deviceList.find { device: Device ->
            device.deviceId == deviceId
        }
        holder.inspectionRowIdTextView.setText(inspectionList[position].inspectionId)
        if (inspectionList[position].inspectionDate != "") {
            holder.inspectionRowDateTextView.setText("Inspection date: " + Helper.getDateString(inspectionList[position].inspectionDate.toLong()))
        }
        holder.inspectionRowNameTextView.setText(device?.name)
        holder.inspectionRowManufacturerTextView.setText(device?.manufacturer)
        holder.inspectionRowModelTextView.setText(device?.model)
        holder.inspectionRowSNTextView.setText("SN: " + device?.serialNumber)
        holder.inspectionRowINTextView.setText("IN: " + device?.inventoryNumber)
        holder.inspectionRowWardTextView.setText(inspectionList[position].ward)
    }

    override fun getItemCount(): Int {
    return inspectionList.size
    }

    inner class InspectionViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {

        val inspectionRowStateMarker = itemview.findViewById<View>(R.id.inspectionRowStateMarker)
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
