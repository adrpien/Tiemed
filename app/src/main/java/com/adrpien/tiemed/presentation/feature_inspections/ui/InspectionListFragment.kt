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
import com.adrpien.tiemed.datamodels.InspectionState
import com.adrpien.tiemed.core.date_picker_dialog.InspectionDatePickerDialog
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.presentation.feature_inspections.InspectionListAdapter
import com.adrpien.tiemed.presentation.feature_inspections.OnInspectionClickListener
import com.adrpien.tiemed.presentation.feature_inspections.view_model.InspectionViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class InspectionListFragment : BaseFragment(), DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

    private lateinit var filteredInspectionList: List<Inspection>

    private var groupSelection: Int = 0
    private var groupSelectionString: String = ""
    private var sortSelection: Int = 0
    private var sortSelectionString: String = ""

    private var tempStateSelection: Int = 0

    // ViewBinding
    private var _binding: FragmentInspectionListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var inspectionId: String
    private lateinit var inspectionList: List<Inspection>

    private val ACTION_BAR_TITLE: String = "Inspection List"

    val InspectionViewModel by viewModels<InspectionViewModel>()

    private var tempInspection = Inspection()

    private lateinit var inspectionStates: MutableList<String>

    private lateinit var filterDate: Calendar

    private lateinit var filterAlertDialogView: View

    var adapter = InspectionListAdapter(listener = this)

    init {

        // Options Menu configuration
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this.viewLifecycleOwner
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
                sortInspectionList()
                true
            }
            R.id.inspectionFilterItem -> {
                filterInspectionList()
                true
            }
            R.id.inspectionGroupItem -> {
                groupInspectionList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler View
        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.inspectionRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))

        // View model state flows
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                InspectionViewModel.inspectionListStateFlow.collect { result ->
                    val listener = object: OnInspectionClickListener {
                        override fun setOnInspectionItemClickListener(itemView: View, position: Int) {

                            // updateInspectionUid
                            updateTempInspectionId(position)

                            if(inspectionId != null) {
                                findNavController().navigate(
                                    InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
                                    bundleOf("uid" to inspectionId))
                            } else {
                                findNavController().navigate(
                                    InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
                                    bundleOf("uid" to null))
                            }

                        }
                        override fun setOnDateButtonClickListener(itemview: View, position: Int) {

                            // update InspectionUid
                            updateTempInspectionId(position)

                            // getSelectedInspection
                            updateTempInspection(position)

                            val millis = inspectionList[position].inspectionDate.toLong()

                            // Create TimePicker
                            val dialog = InspectionDatePickerDialog(millis)

                            // show MyTimePicker
                            dialog.show(childFragmentManager, "inspection_time_picker")
                        }
                        override fun setOnStateButtonClickListener(itemview: View, position: Int) {

                            // UpdateTempInspectionUid
                            updateTempInspectionId(position)

                            // getTempSelectedInspection
                            updateTempInspection(position)

                            // updateTempStateSelection
                            updateTempInspectionStateSelection(position)

                            // Creating inspection state selection dialog
                            createInspectionStateSelectionDialog(tempStateSelection)

                        }

                    }
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                inspectionList = result.data
                            }
                            binding.inspectionRecyclerView.adapter = InspectionListAdapter(inspectionList, listener)
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                inspectionList = result.data
                            }
                            binding.inspectionRecyclerView.adapter = InspectionListAdapter(inspectionList, listener)
                        }
                        ResourceState.ERROR -> {
                            inspectionList = emptyList()
                            binding.inspectionRecyclerView.adapter = InspectionListAdapter(inspectionList, listener)
                        }
                    }



                }

            }
        }

        // FAB Button implementation
        binding.addInspectionFABButton.setOnClickListener {  view ->
            findNavController().navigate(com.adrpien.tiemed.inspectionlist.InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment())
        }

        // inspectionListFilterDateButton implementation
        setFilterDateButton()

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                val filterSwitch = filterAlertDialogView.findViewById<Switch>(R.id.inspectionListFilterSwitch)
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
        filterAlertDialogView = layoutInflater.inflate(R.layout.filter_view, null)
        val filterButton = filterAlertDialogView.findViewById<Button>(R.id.inspectionListFilterDateButton)
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
        viewModelProvider.updateInspection(map, inspectionId)
    }
}

