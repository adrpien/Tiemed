package com.adrpien.tiemed.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrpien.tiemed.R
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.datamodels.RepairState

class RepairListAdapter(var repairList: List<Repair>, val listener: onRepairItemClickListener)
    : RecyclerView.Adapter<RepairListAdapter.RepairViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.repair_list_cell, parent, false)
        return RepairViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepairViewHolder, position: Int) {
        holder.idRepairTextView.text = repairList[position].repairId.toString()
        holder.viewRepairButton.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView)
        }
        holder.editRepairButton.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView)
        }

        if(repairList[position].repairState == RepairState.FIXED){ holder.stateMarker.setBackgroundColor(Color.GREEN) }
        if(repairList[position].repairState == RepairState.SUBMITTED){ holder.stateMarker.setBackgroundColor(Color.RED) }
        if(repairList[position].repairState == RepairState.SENT_TO_SERVICE){ holder.stateMarker.setBackgroundColor(Color.BLUE) }

    }

    override fun getItemCount(): Int {
        return repairList.size
    }

    // ViewHolder class implementation
    inner class RepairViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val idRepairTextView = itemView.findViewById<TextView>(R.id.repairRowIdTextView)
        val stateMarker = itemView.findViewById<View>(R.id.repairRowStateMarker)
        val editRepairButton = itemView.findViewById<ImageButton>(R.id.repairRowEditButton)
        val viewRepairButton = itemView.findViewById<ImageButton>(R.id.repairRowViewButton)
    }
}

// Creating listener implemented by fragment
interface onRepairItemClickListener{
    fun setOnRepairItemClick(itemView: View)
}