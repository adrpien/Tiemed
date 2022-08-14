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
import com.adrpien.tiemed.fragments.BaseFragment
import java.lang.Long.parseLong

class InspectionListAdapter(val inspectionList: List<Inspection>, val listener: OnInspectionClickListener): RecyclerView.Adapter<InspectionListAdapter.InspectionViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.inspection_list_row, parent, false)
        return InspectionViewHolder(view)

    }

    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {

        // Technician Button onClickListener
        holder.inspectionRowStateButton.setOnClickListener { view ->
            listener.setOnStateButtonClickListener(view, holder.adapterPosition)
        }

        // State Button onClickListener
        holder.inspectionRowDateButton.setOnClickListener { view ->
            listener.setOnDateButtonClickListener(view, holder.adapterPosition)
        }

        // Inspection row onClickListener
        holder.itemView.setOnClickListener { view ->
            listener.setOnInspectionItemClickListener(view, holder.adapterPosition)
        }

        // Filling TextViews with values

        // Set inspectionRowIdTextView
        holder.inspectionRowIdTextView.setText(inspectionList[position].inspectionUid)

        // Setting inspectionRowDateTextView
        holder.inspectionRowDateTextView.append(getDateString(inspectionList[position].inspectionDate))

        // Setting inspectionRowNameTextView
        holder.inspectionRowNameTextView.setText(inspectionList[position].name)

        // Setting inspectionRowManufacturerTextView
        holder.inspectionRowManufacturerTextView.setText(inspectionList[position].manufacturer)

        // Setting inspectionRowModelTextView
        holder.inspectionRowModelTextView.setText(inspectionList[position].model)

        // Setting inspectionRowSNTextView
        holder.inspectionRowSNTextView.append(inspectionList[position].serialNumber)

        // Setting inspectionRowINTextView
        holder.inspectionRowINTextView.append(inspectionList[position].inventoryNumber)

        // Setting inspectionRowHospitalTextView
        holder.inspectionRowHospitalTextView.setText(inspectionList[position].hospitalString)

        // Setting inspectionRowWardTextView
        holder.inspectionRowWardTextView.setText(inspectionList[position].ward)




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
        val inspectionRowModelTextView = itemView.findViewById<TextView>(R.id.inspectionRowModelTextView)
        val inspectionRowSNTextView = itemView.findViewById<TextView>(R.id.inspectionRowINTextView)
        val inspectionRowINTextView = itemView.findViewById<TextView>(R.id.inspectionRowSNTextView)
        val inspectionRowHospitalTextView = itemView.findViewById<TextView>(R.id.inspectionRowHospitalTextView)
        val inspectionRowWardTextView = itemView.findViewById<TextView>(R.id.inspectionRowWardTextView)

    }
}

interface OnInspectionClickListener {
    fun setOnInspectionItemClickListener(itemview: View, position: Int)
    fun setOnDateButtonClickListener(itemview: View, position: Int)
    fun setOnStateButtonClickListener(itemview: View, position: Int)
}

// Returns date in format YYYY/MM/DD String representation using date in millis
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