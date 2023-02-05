package com.adrpien.tiemed.presentation.feature_inspections.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.ListView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.core.dialogs.date.InspectionDatePickerDialog
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_inspections.InspectionListAdapter
import com.adrpien.tiemed.presentation.feature_inspections.OnInspectionClickListener
import com.adrpien.tiemed.presentation.feature_inspections.view_model.InspectionViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class InspectionListFragment : BaseFragment(), DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

    // ViewBinding
    private var _binding: FragmentInspectionListBinding? = null
    private val binding
        get() = _binding!!

    private val ACTION_BAR_TITLE: String = "Inspection List"

    private var deviceList: List<Device> = listOf()
    private var inspectionList: List<Inspection> = listOf()
    private var hospitalList: List<Hospital> = listOf()
    private var preparedInspectionList: List<Inspection> = listOf()
    private var inspectionStateList: List<InspectionState> = listOf()

    // Filtering, sorting and grouping
    private var filteringCondition: Bundle = bundleOf()
    private var sortingConditions: Bundle = bundleOf()
    private var groupingCondition: Bundle = bundleOf()

    val InspectionViewModel by viewModels<InspectionViewModel>()

    init {
        // Options Menu configuration
        setHasOptionsMenu(true)
    }

    /* ***************************** MENU ******************************************************* */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater: MenuInflater = inflater
        menuInflater.inflate(R.menu.inspection_list_options, menu)

        // Setting Action Bar Name according to open fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ACTION_BAR_TITLE

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.inspectionSortItemClick -> {
                sortInspectionList()
                true
            }
            R.id.inspectionFilterItemClick -> {
                filterInspectionList()
                true
            }
            R.id.inspectionGroupItemClick -> {
                groupInspectionList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* *************************** LIFECYCLE FUNCTIONS ****************************************** */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler View
        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.inspectionRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))

        /* ********************** COLLECT STATE FLOW ******************************************** */
        // inspectionList
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.inspectionListStateFlow.collect() { result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                inspectionList = result.data
                                preparedInspectionList = inspectionList
                                // preparedInspectionList()
                                // TODO Correct when adapter will be ready
                                //binding.inspectionRecyclerView.adapter = InspectionListAdapter()
                            }
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                inspectionList = result.data
                                preparedInspectionList = inspectionList
                                // preparedInspectionList()
                                // TODO Correct when adapter will be ready

                                // binding.inspectionRecyclerView.adapter = InspectionListAdapter()
                            }
                        }
                        ResourceState.ERROR -> {
                            // TODO Some inspectionList loading error handlig
                        }
                    }

                }
            }
        }
        // hospitalList
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.hospitalListStateFlow.collect(){ result ->
                    when(result.resourceState) {
                        ResourceState.ERROR -> {
                            // TODO Some hospital list loading error handling
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                hospitalList = result.data
                            }
                        }
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                hospitalList = result.data
                            }
                        }
                    }

                }
            }
        }

        // FAB Button implementation
        binding.addInspectionFABButton.setOnClickListener {  view ->
            findNavController().navigate(InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment())
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /* *************************** INTERFACES *************************************************** */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        val millis: Long = date.timeInMillis

        // update tempInspectionDate
        updateTempInspectionDate(millis.toString())

        // updateInspectionState
        updateInspectionDate()
    }
    override fun onClick(dialog: DialogInterface?, which: Int) {
        // Getting single choice AlertDialog selection
        val listView: ListView = (dialog as AlertDialog).listView
        val state= listView.getItemAtPosition(listView.checkedItemPosition).toString()

        //  updateTempStateSelection
        updateTempInspectionStateSelection(state)

        updateInspectionStateString()

    }


    /* ***************************** FUNCTIONS ************************************************** */
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
    private fun updateTempInspectionId(position: Int) {
        inspectionId = inspectionList[position].inspectionUid
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
    private fun updateTempInspectionDate(date: String) {
        tempInspection.inspectionDate = date
    }
    private fun updateInspectionDate() {
        val map: Map<String, String> = createInspectionMap(tempInspection)
        viewModelProvider.updateInspection(map, inspectionId)
    }
    private fun groupInspectionList() {
        val groupSelectionList = mutableListOf<String>("Hospital", "State")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Group by")
        builder.setSingleChoiceItems(groupSelectionList.toTypedArray(), groupSelection, null)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            Snackbar.make(requireView(), "Wait for implementation", Snackbar.LENGTH_SHORT).show()
            // TODO groupInspectionList to implement
        })
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }
    private fun filterInspectionList() {

        // Remove view if that view have already parent
        if (filterAlertDialogView.parent != null) {
            (filterAlertDialogView.parent as ViewGroup).removeView(filterAlertDialogView)
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Filter")
        builder.setView(filterAlertDialogView)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            filteredInspectionList = inspectionList.filter {
                val filterSwitch = filterAlertDialogView.findViewById<Switch>(R.id.alertDialogSortingSwitch)
                if(filterSwitch.isChecked) {
                    it.inspectionDate.toLong() >= filterDate.timeInMillis
                } else {
                    it.inspectionDate.toLong() <= filterDate.timeInMillis
                }
            }
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(ArrayList(filteredInspectionList), this)
        })


        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }
    private fun setFilterDateButton() {
        filterDate = Calendar.getInstance()
        val filterDateYear = filterDate.get(Calendar.YEAR)
        val filterDateMonth = filterDate.get(Calendar.MONTH)
        val filterDateDay = filterDate.get(Calendar.DAY_OF_MONTH)
        filterAlertDialogView = layoutInflater.inflate(R.layout.view_filtering, null)
        val filterButton = filterAlertDialogView.findViewById<Button>(R.id.filteringAlertDialogDateButton)
        filterButton.setText(getDateString(filterDate.timeInMillis))
        filterButton.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                filterDate.set(year, month, dayOfMonth)
                filterButton.setText(getDateString(filterDate.timeInMillis))
            }, filterDateYear, filterDateMonth, filterDateDay)
            datePicker.show()
        }
    }
    private fun sortInspectionList() {
        val sortSelectionList = mutableListOf<String>("Date", "State")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Sort by")
        builder.setSingleChoiceItems(sortSelectionList.toTypedArray(), sortSelection, null)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            inspectionList.sortWith(compareBy { it ->
                //
                val sortListView = (dialog as AlertDialog).listView
                sortSelectionString = sortListView.adapter.getItem(sortListView.checkedItemPosition).toString()
                sortSelection = sortListView.checkedItemPosition

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
    private fun updateInspectionStateString() {
        val map: Map<String, String> = createInspectionMap(tempInspection)
        viewModelProvider.updateInspection(map, inspectionId)
    }

}

