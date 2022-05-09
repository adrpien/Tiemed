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
import com.adrpien.tiemed.datamodels.State

class RepairListAdapter(var repairList: List<Repair>, val listener: onRepairItemClickListener)
    : RecyclerView.Adapter<RepairListAdapter.RepairViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.repair_list_cell, parent, false)
        return RepairViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepairViewHolder, position: Int) {
        holder.idTextView.text = repairList[position].id.toString()
        holder.stateTextView.text = repairList[position].state.toString()

        holder.viewRepairButton.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView)
        }

        holder.editRepairButton.setOnClickListener {
            listener.setOnRepairItemClick(holder.itemView)
        }

        if(repairList[position].state == State.Naprawiony){ holder.stateMarker.setBackgroundColor(Color.GREEN) }
        if(repairList[position].state == State.Zgloszony){ holder.stateMarker.setBackgroundColor(Color.RED) }
        if(repairList[position].state == State.Wyslany_do_serwisu){ holder.stateMarker.setBackgroundColor(Color.BLUE) }

    }

    override fun getItemCount(): Int {
        return repairList.size
    }

    inner class RepairViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val idTextView = itemView.findViewById<TextView>(R.id.idTextView)
        val stateTextView = itemView.findViewById<TextView>(R.id.stateTextView)
        val stateMarker = itemView.findViewById<View>(R.id.stateMarker)
        val editRepairButton = itemView.findViewById<ImageButton>(R.id.editRepairButton)
        val viewRepairButton = itemView.findViewById<ImageButton>(R.id.viewRepairButton)

       /*
       init {
            itemView.setOnClickListener {
                listener.setOnRepairItemClick(itemView)
            }
        }
        */

    }
}

interface onRepairItemClickListener{
    fun setOnRepairItemClick(itemView: View)
}