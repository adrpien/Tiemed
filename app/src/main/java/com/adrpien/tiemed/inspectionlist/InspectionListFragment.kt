package com.adrpien.tiemed.inspectionlist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
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
    private var tempStateSelection: Int = 0

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
        // _binding.lifecycleOwner = this.viewLifecycleOwner
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

        val sortSelectionList = mutableListOf<String>("Date", "State")
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
        updateTempInspectionUid(position)

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
        updateTempInspectionUid(position)

        // getSelectedInspection
        updateTempInspection(position)

        val millis = inspectionList[position].inspectionDate.toLong()

        // Create TimePicker
        val dialog = InspectionDatePickerDialog(millis)

        // show MyTimePicker
        dialog.show(childFragmentManager, "inspection_time_picker")
    }

    // Implementing inspectionRowStateButton click reaction
    override fun setOnStateButtonClickListener(itemview: View, position: Int) {

        // UpdateTempInspectionUid
        updateTempInspectionUid(position)

        // getTempSelectedInspection
        updateTempInspection(position)

        // updateTempStateSelection
        updateTempInspectionStateSelection(position)

        // Creating inspection state selection dialog
        createInspectionStateSelectionDialog(tempStateSelection)

    }

    private fun updateTempInspectionStateSelection(position: Int) {
        tempStateSelection = 0
        var selection = 0
        for (item in InspectionState.values()) {
            if (inspectionList[position].inspectionStateString == item.toString()) {
                tempStateSelection = selection
            }
            selection += 1
        }
    }

    private fun updateTempInspectionStateSelection(state: String) {
        tempInspection.inspectionStateString = state
    }


    private fun updateTempInspection(position: Int) {
        tempInspection = inspectionList[position]
    }


    private fun updateTempInspectionUid(position: Int) {
        inspectionUid = inspectionList[position].inspectionUid
    }

    private fun createInspectionStateSelectionDialog(checkedItem: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Device State")
        builder.setSingleChoiceItems(inspectionStates.toTypedArray(), checkedItem, null)
        builder.setPositiveButton("OK", this)
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        val millis: Long = date.timeInMillis

        // update tempInspectionDate
        updateTempInspectionDate(millis.toString())

        // updateInspectionState
        updateInspectionDate()
    }

    private fun updateTempInspectionDate(date: String) {
        tempInspection.inspectionDate = date
    }

    private fun updateInspectionDate() {
        val map: Map<String, String> = createInspectionMap(tempInspection)
        viewModelProvider.updateInspection(map, inspectionUid)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Save inspection state
    override fun onClick(dialog: DialogInterface?, which: Int) {
        // Getting single choice AlertDialog selection
        val listView: ListView = (dialog as AlertDialog).listView
        val state= listView.getItemAtPosition(listView.checkedItemPosition).toString()

        //  updateTempStateSelection
        updateTempInspectionStateSelection(state)

        updateInspectionStateString()

    }

    private fun updateInspectionStateString() {
        val map: Map<String, String> = createInspectionMap(tempInspection)
        viewModelProvider.updateInspection(map, inspectionUid)
    }
}