package com.adrpien.tiemed.inspectionlist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.datamodels.InspectionState
import com.adrpien.tiemed.datepickers.InspectionDatePickerDialog

class InspectionListFragment : Fragment(), OnInspectionClickListener, DatePickerDialog.OnDateSetListener {

    // ViewBinding
    private var _binding: FragmentInspectionListBinding? = null
    private val binding
        get() = _binding!!


    private val ACTION_BAR_TITLE: String = "Inspection List"

    val viewModelProvider by viewModels<InspectionListViewModel>()

    init {

        // Options Menu configuration
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // ViewBinding
        _binding = FragmentInspectionListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater: MenuInflater = inflater
        menuInflater.inflate(R.menu.inspection_list_options, menu)

        // Setting Action Bar Name according to open fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ACTION_BAR_TITLE

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

        // implementing inspection list Recycler View
        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModelProvider.inspectionList.observe(viewLifecycleOwner) { t ->
            if(t.isNotEmpty())
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(t, this)
        }


    }

    override fun setOnInspectionItemClickListener(itemView: View) {

        // implementing inspection list Recycler View item click
        // Move to EditInspectionFragment when inspection list Recycler View item clicked
        // Add bundle with record id
        val id = itemView.findViewById<TextView>(R.id.inspectionRowIdTextView).text.toString()
        findNavController().navigate(
            InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
            bundleOf("id" to id )
        )

    }

    // Implementing  inspectionRowDateButton click reaction
    override fun setOnDateButtonClickListener(itemview: View) {
        // TODO inspectionRowDateButton click reaction
        // Create TimePicker
        val dialog = InspectionDatePickerDialog()
        // show MyTimePicker
        dialog.show(childFragmentManager, "inspection_time_picker")
    }

    // Implementing inspectionRowStateButton click reaction
    override fun setOnStateButtonClickListener(itemview: View) {
        // TODO inspectionRowStateButton click Reaction
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
        // TODO Implement InspectionDateDialogPicker date set reaction
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}