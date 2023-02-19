package com.adrpien.tiemed.presentation.feature_inspections.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
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
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.core.dialogs.filtering_dialog.FilteringDialog
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_inspections.InspectionListAdapter
import com.adrpien.tiemed.presentation.feature_inspections.OnInspectionItemClickListener
import com.adrpien.tiemed.presentation.feature_inspections.view_model.InspectionViewModel
import com.adrpien.tiemed.presentation.feature_repairs.ui.EditRepairFragment
import com.adrpien.tiemed.presentation.feature_users.OnRepairItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InspectionListFragment() : BaseFragment(){

    // ViewBinding
    private var _binding: FragmentInspectionListBinding? = null
    private val binding
        get() = _binding!!

    private val ACTION_BAR_TITLE: String = "Inspection List"
    private val SORTING_REQUEST_KEY: String = "SORTING_REQUEST_KEY"
    private val FILTERING_REQUEST_KEY: String = "FILTERING_REQUEST_KEY"

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

    val listener: OnInspectionItemClickListener = object : OnInspectionItemClickListener {
        // onRepairItemClickListener interface implementation
        // Fetching id of clicked record and startinng fragment
        override fun setOnInspectionItemClick(itemView: View, position: Int) {
            val fragment = EditRepairFragment()
            fragment.arguments = bundleOf("id" to inspectionList[position].inspectionId)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.detailsFragmentContainerView, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        override fun setOnInspectionItemLongClick(itemView: View, position: Int) {
            val fragment = EditRepairFragment()
            fragment.arguments = bundleOf("id" to inspectionList[position].inspectionId)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.pinnedFragmentContainerView, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
    /* ***************************** MENU ******************************************************* */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater: MenuInflater = inflater
        menuInflater.inflate(R.menu.inspection_list_options, menu)

        // Setting Action Bar Name according to open fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ACTION_BAR_TITLE

        // Hospital spinner in action bar implementation
        val spinnerItem = menu.findItem(R.id.inspectionListHospitalSpinner)
        val inspectionListHospitalSpinner = spinnerItem.actionView as AppCompatSpinner
        val hospitalSpinnerAdapter = activity?.baseContext?.let { it ->
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                hospitalList
            )
        }
        inspectionListHospitalSpinner.adapter = hospitalSpinnerAdapter
        inspectionListHospitalSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectHospital(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.inspectionSortItemClick -> {
                sortInspectionListItemClick()
                true
            }
            R.id.inspectionFilterItemClick -> {
                filterInspectionListItemClick()
                true
            }
            R.id.inspectionGroupItemClick -> {
                groupInspectionListItemClick()
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



    /* ***************************** FUNCTIONS ************************************************** */

    private fun selectHospital(position: Int) {
        val selection = hospitalList[position].name
        preparedInspectionList = inspectionList.filter { it.hospitalId == selection }
        binding.inspectionRecyclerView.adapter =
            InspectionListAdapter(preparedInspectionList, hospitalList, deviceList, inspectionStateList , listener)
    }
    private fun groupInspectionListItemClick() {
        groupingCondition = getGroupingConditions()
        groupInspectionList(groupingCondition)
    }
    private fun filterInspectionListItemClick() {
        filteringCondition = getFilteringConditions()
        filterInspectionList(filteringCondition)
    }
    private fun sortInspectionListItemClick() {
        sortingConditions = getSortingConditions()
        sortInspectionList(sortingConditions)
    }
    private fun groupInspectionList(bundle: Bundle) {
        val condition = bundle.get("")
        val condition2 = bundle.get("")
        // TODO Inspections grouping and update adapter
        Toast.makeText(context, getString(R.string.WAIT_FOR_IMPLEMENTATION), Toast.LENGTH_SHORT).show()
    }
    private fun getSortingConditions(): Bundle {
        val dialog = FilteringDialog()
        dialog.show(parentFragmentManager, "SORTING ALERT DIALOG")
        childFragmentManager.setFragmentResultListener(SORTING_REQUEST_KEY, viewLifecycleOwner) { string, bundle ->
            filteringCondition = bundle
        }
        return  filteringCondition
    }
    private fun sortInspectionList(bundle: Bundle) {
        val bundleType = bundle.get("bundle_type") as String
        val switch = bundle.get("switch") as Boolean
        val value = bundle.get("switch") as Long
        preparedInspectionList = inspectionList.sortedBy { inspection ->
            if (switch) {
                inspection.inspectionDate.toLong() > value
            } else {
                inspection.inspectionDate.toLong() < value
            }
        }
        binding.inspectionRecyclerView.adapter = InspectionListAdapter(preparedInspectionList, hospitalList, deviceList, inspectionStateList , listener)

    }
    private fun getGroupingConditions(): Bundle {
        // TODO Open AlertDialog and return bundle with conditions
        val condition = bundleOf()
        return  condition
    }
    private fun filterInspectionList(bundle: Bundle) {
        val bundleType = bundle.get("bundle_type") as String
        val switch = bundle.get("switch") as Boolean
        val value = bundle.get("value") as Long
        preparedInspectionList = inspectionList.filter { inspection ->
            if (switch) {
                inspection.inspectionDate.toLong() > value
            } else {
                inspection.inspectionDate.toLong() < value
            }
        }
        binding.inspectionRecyclerView.adapter = InspectionListAdapter(preparedInspectionList, hospitalList, deviceList, inspectionStateList , listener)
    }
    private fun getFilteringConditions(): Bundle {
        val dialog = FilteringDialog()
        dialog.show(parentFragmentManager, "FILTERING ALERT DIALOG")
        childFragmentManager.setFragmentResultListener(FILTERING_REQUEST_KEY, viewLifecycleOwner) { string, bundle ->
            filteringCondition = bundle
        }
        return  filteringCondition
    }
    private fun prepareInspectionList() {
        filterInspectionList(filteringCondition)
        sortInspectionList(sortingConditions)
        groupInspectionList(groupingCondition)
    }
}

