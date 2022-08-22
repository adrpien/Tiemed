package com.adrpien.tiemed.inspectionlist

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.InspectionState
import com.adrpien.tiemed.datepickers.InspectionDatePickerDialog
import com.adrpien.tiemed.fragments.BaseFragment
import com.google.android.material.snackbar.Snackbar


class InspectionListFragment : BaseFragment(), OnInspectionClickListener, DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

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

    private lateinit var inspectionUid: String
    private lateinit var inspectionList: ArrayList<Inspection>

    private val ACTION_BAR_TITLE: String = "Inspection List"

    val viewModelProvider by viewModels<InspectionListViewModel>()

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

        // ViewBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inspectionStates
        inspectionStates = mutableListOf<String>()
        for(item in InspectionState.values()){
            inspectionStates.add(item.name)
        }

        // implementing inspection list Recycler View
        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.inspectionRecyclerView.adapter = adapter

        /*
        TODO MutableLiveData does not work properly
        MutableLiveData does not trigger observer when inspectionDateButton and inspectionStateButton clicked.
        Changes are saved in FirebaseStorage, but observer of the MutableLiveData is not triggered.
        */
        viewModelProvider.inspectionList.observe(viewLifecycleOwner) {
            it?.let {
                inspectionList = ArrayList(it)
                adapter.refreshInspectionList(it)
            }
            /*if(t.isNotEmpty())
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(t, this)
            inspectionList = t*/
        }

        // FAB Button implementation
        binding.addInspectionFABButton.setOnClickListener {  view ->
            findNavController().navigate(InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment())
        }

        // inspectionListFilterDateButton implementation
        setFilterDateButton()

    }

    // inspectionListFilterButton implementation
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

