package com.adrpien.tiemed.inspectionlist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.InspectionState
import com.adrpien.tiemed.datepickers.InspectionDatePickerDialog
import com.adrpien.tiemed.fragments.BaseFragment

class InspectionListFragment : BaseFragment(), OnInspectionClickListener, DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

    private var sortSelection: Int = 0
    private var sortSelectionString: String = ""
    private var stateSelection: Int = 0

    // ViewBinding
    private var _binding: FragmentInspectionListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var inspectionUid: String
    private lateinit var inspectionList: MutableList<Inspection>

    private val ACTION_BAR_TITLE: String = "Inspection List"

    val viewModelProvider by viewModels<InspectionListViewModel>()

    private var tempInspection = Inspection()

    private lateinit var inspectionStates: MutableList<String>

    init {

        // Options Menu configuration
        setHasOptionsMenu(true)
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
                sortInspections()
                true
            }
            R.id.inspectionFilterItem -> {
                // TODO Inspection list filtering
                true
            }
            R.id.inspectionGroupItem -> {
                // TODO Inspection list filtering
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sortInspections() {

        createSortAlertDialog(sortSelection)

    }

    private fun createSortAlertDialog(position: Int) {


        // Alternative version of creating sortSelectionList

        /*
        val sortSelectionList = mutableListOf<String>()
        for(selection in Inspection::class.memberProperties){
            sortSelectionList.add(selection.name)
        }

        */

        val sortSelectionList = mutableListOf<String>("Date", "State",  )
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Sort by")
        builder.setSingleChoiceItems(sortSelectionList.toTypedArray(), sortSelection, null)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            inspectionList.sortWith(compareBy { it ->
                //
                val listView = (dialog as AlertDialog).listView
                sortSelectionString = listView.adapter.getItem(listView.checkedItemPosition).toString()
                sortSelection = listView.checkedItemPosition

                if(sortSelectionString == "Date"){
                    it.inspectionDate
                } else if(sortSelectionString == "State"){
                   it.inspectionStateString
                }
                else {
                    it.inspectionUid
                }
            })
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(inspectionList, this)
        })
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inspectionStates
        inspectionStates = mutableListOf<String>()
        for(item in InspectionState.values()){
            inspectionStates.add(item.name)
        }

        // implementing inspection list Recycler View
        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModelProvider.inspectionList.observe(viewLifecycleOwner) { t ->
            if(t.isNotEmpty())
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(t, this)
            inspectionList = t
        }

        // FAB Button implementation
        binding.addInspectionFABButton.setOnClickListener {  view ->
            findNavController().navigate(InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment())
        }

    }

    override fun setOnInspectionItemClickListener(itemView: View, position: Int) {

        // updateInspectionUid
        updateInspectionUid(position)

        if(inspectionUid != null) {
            findNavController().navigate(
                InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
                bundleOf("uid" to inspectionUid))
        } else {
            findNavController().navigate(
                InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
                bundleOf("uid" to null))
        }

    }

    // Implementing  inspectionRowDateButton click reaction
    override fun setOnDateButtonClickListener(itemview: View, position: Int) {

        // update InspectionUid
        updateInspectionUid(position)

        // getSelectedInspection
        updateTempInspection(position)

        // Create TimePicker
        val dialog = InspectionDatePickerDialog()

        // show MyTimePicker
        dialog.show(childFragmentManager, "inspection_time_picker")
    }

    // TODO HOW TO MAKE WAIT THIS FUNCTION TILL TEMPINSPECTION UPDATED
    // Implementing inspectionRowStateButton click reaction
    override fun setOnStateButtonClickListener(itemview: View, position: Int) {

        // UpdateInspectionUid
        updateInspectionUid(position)

        // getSelectedInspection
        updateTempInspection(position)

        stateSelection = 0
        var selection = 0
        for (item in InspectionState.values()) {
            if (tempInspection.inspectionStateString == item.toString()) {
                stateSelection = selection
            }
            selection += 1
        }

        // Creating inspection state selection dialog
        createInspectionSelectionDialog(stateSelection)

    }

    private fun updateTempInspection(position: Int) {

        viewModelProvider.getInspection(inspectionUid).observe(viewLifecycleOwner) { inspection ->
            tempInspection = inspection

        }
    }

    private fun updateInspectionUid(position: Int) {
        inspectionUid = inspectionList[position].inspectionUid

    }

    private fun createInspectionSelectionDialog(checkedItem: Int) {



        val builder = AlertDialog.Builder(context)
        builder.setTitle("Device State")
        builder.setSingleChoiceItems(inspectionStates.toTypedArray(), checkedItem, null)
        builder.setPositiveButton("OK", this)
        builder.setNegativeButton("Cancel", null)
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

    override fun onClick(dialog: DialogInterface?, which: Int) {

        // Getting single choice AlertDialog selected position
        val listView: ListView = (dialog as AlertDialog).listView

        val map: Map<String, String> = createInspectionMap(tempInspection)
        viewModelProvider.updateInspection(map, inspectionUid)

    }
}