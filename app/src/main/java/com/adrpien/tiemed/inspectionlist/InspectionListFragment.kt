package com.adrpien.tiemed.inspectionlist

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.adapters.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.InspectionState
import com.adrpien.tiemed.datepicker.InspectionDatePickerDialog
import com.adrpien.tiemed.datepicker.RepairDatePickerDialog

// ViewBinding
private var _binding: FragmentInspectionListBinding? = null
private val binding
    get() = _binding!!

class InspectionListFragment : Fragment(), OnInspectionClickListener, DatePickerDialog.OnDateSetListener {

    val viewModelProvider by viewModels<InspectionListViewModel>()

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater: MenuInflater = inflater
        menuInflater.inflate(R.menu.inspection_list_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.inspectionSortItem -> {
                // TODO Inspection list sorting
                true
            }
            R.id.inspectionFilterItem -> {
                // TODO Inspection list filtering
                true
            }
            R.id.inspectionGroupItem -> {
                // TODO Inspection list filetring
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModelProvider.inspectionList.observe(viewLifecycleOwner) { t ->
            if(t.isNotEmpty())
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(t, this)
        }


    }

    override fun setOnInspectionItemClickListener(itemView: View) {

        val id = itemView.findViewById<TextView>(R.id.inspectionRowIdTextView).text.toString()
        findNavController().navigate(
            InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
            bundleOf("id" to id )
        )

    }

    override fun setOnDateButtonClickListener(itemview: View) {
        // Create TimePicker
        val dialog = InspectionDatePickerDialog()
        // show MyTimePicker
        dialog.show(childFragmentManager, "inspection_time_picker")
    }

    override fun setOnStateButtonClickListener(itemview: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Device State")
        val checkedItem = 1
        val statesArray = arrayListOf<String>()
        val testStates = arrayOf("Passed", "Failed", "Conditionally Passed", "Finished", "Awaiting")
        val states = InspectionState.values()
         for(state in states){
             statesArray.add(state.name)
         }
        builder.setSingleChoiceItems(testStates, checkedItem) { dialog, which ->
            // user checked an item
        }

        // add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
        // user clicked OK
        }
        builder.setNegativeButton("Cancel", null)
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // TODO("Not yet implemented")
    }

}