package com.adrpien.tiemed.presentation.feature_inspections.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.core.dialogs.filtering_dialog.FilteringDialog
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_inspections.InspectionListAdapter
import com.adrpien.tiemed.presentation.feature_inspections.OnInspectionItemClickListener
import com.adrpien.tiemed.presentation.feature_inspections.view_model.InspectionViewModel
import com.adrpien.tiemed.presentation.feature_users.RepairListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.pow

@AndroidEntryPoint
class InspectionListFragment() : Fragment(){

    // ViewBinding
    private var _binding: FragmentInspectionListBinding? = null
    private val binding
        get() = _binding!!

    val InspectionViewModel by viewModels<InspectionViewModel>()

    private val ACTION_BAR_TITLE: String = "Inspection List"
    private val SORTING_REQUEST_KEY: String = "SORTING_REQUEST_KEY"
    private val FILTERING_REQUEST_KEY: String = "FILTERING_REQUEST_KEY"

    private val REPAIR_LIST_FRAGMENT: String = "REPAIR_LIST_FRAGMENT"

    private var inspectionList: List<Inspection> = listOf()
        set(value) {
            if(value.isNotEmpty()){
                // InspectionListViewModel.updateRoomInspectionListFlow(value)
                field = value
                preparedInspectionList = value
                prepareInspectionList()
            }
        }
    private var deviceList: List<Device> = listOf()
        set(value) {
            if(value.isNotEmpty()){
                field = value
                // InspectionListViewModel.updateRoomDeviceListFlow(value)
                updateRecyclerViewAdapter()
            }
        }
    private var hospitalList: List<Hospital> = listOf()
        set(value) {
            if(value.isNotEmpty()){
                field =value
                // InspectionListViewModel.updateRoomHospitalListFlow(value)
                updateRecyclerViewAdapter()
                activity?.invalidateOptionsMenu()
            }

        }
    private var inspectionStateList: List<InspectionState> = listOf()
        set(value) {
            if(value.isNotEmpty()){
                field =value
                // InspectionListViewModel.updateRoomInspectionStateListFlow(value)
                updateRecyclerViewAdapter()
            }
        }
    private var technicianList: List<Technician> = listOf()
        set(value) {
            if(value.isNotEmpty()){
                field =value
                // InspectionListViewModel.updateRoomTechnicianListFlow(value)
                updateRecyclerViewAdapter()
            }
        }

    private var preparedInspectionList: List<Inspection> = listOf()
        set(value) {
            field = value
            updateRecyclerViewAdapter()
        }

    lateinit var hospitalSpinnerItem: MenuItem
    lateinit var inspectionListHospitalSpinner: Spinner
    private val spinnerHospitalList = mutableListOf<String>()

    private var filteringCondition: Bundle = bundleOf()
    private var sortingConditions: Bundle = bundleOf()
    private var groupingCondition: Bundle = bundleOf()

    val recyclerViewListener: OnInspectionItemClickListener = object: OnInspectionItemClickListener {
        override fun setOnInspectionItemClick(itemView: View, position: Int) {
            val bundle = bundleOf(
                "inspectionId" to inspectionList[position].inspectionId,
                "deviceId" to inspectionList[position].deviceId,
                "editFlag" to false
            )

            val displayHeightInInches = (Resources.getSystem().displayMetrics.heightPixels) / (Resources.getSystem().displayMetrics.ydpi)
            val displayWidthInInches = (Resources.getSystem().displayMetrics.widthPixels) / (Resources.getSystem().displayMetrics.xdpi)
            val x = Math.pow(displayWidthInInches.toDouble(), 2.0)
            val y = Math.pow(displayHeightInInches.toDouble(), 2.0)
            val screenSizeInInches = Math.sqrt(x + y)

            if (screenSizeInInches > 7 ) {
                val fragment = EditInspectionFragment()
                fragment.arguments = bundle
                childFragmentManager.beginTransaction()
                    .replace(R.id.inspectionDetailsFragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                findNavController().navigate(InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId, bundle)
            }
        }
        override fun setOnInspectionItemLongClick(itemView: View, position: Int) {
        }
    }


    init {
        // Options Menu configuration
        setHasOptionsMenu(true)

    }

    /* *************************** LIFECYCLE FUNCTIONS ****************************************** */
    override fun onPause() {
        super.onPause()
        updateRecyclerViewAdapter()
    }
    override fun onStart() {
        super.onStart()
        updateRecyclerViewAdapter()
    }
    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
        updateRecyclerViewAdapter()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionListBinding.inflate(layoutInflater)
        _binding = FragmentInspectionListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ********************** RECYCLER VIEW ******************************************** */
        // Setting RecyclerView
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(activity)
            // List divider
            binding.inspectionRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        } else {
            binding.inspectionRecyclerView.layoutManager = GridLayoutManager(activity, 2)
            // List divider
            binding.inspectionRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        }

        /* ********************** COLLECT STATE FLOW ******************************************** */
        // inspectionList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel
                    .inspectionListStateFlow
                    .collect { result ->
                        when (result.resourceState) {
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    inspectionList = result.data
                                    binding.inspectionListLoadingPanel.visibility = View.INVISIBLE
                                } else {
                                    binding.inspectionListLoadingPanel.visibility = View.VISIBLE
                                }
                            }
                            ResourceState.SUCCESS -> {
                                binding.inspectionListLoadingPanel.visibility = View.INVISIBLE
                                if (result.data != null) {
                                    inspectionList = result.data
                                    InspectionViewModel.updateRoomInspectionListFlow(inspectionList).collect(){
                                    }

                                }
                            }
                            ResourceState.ERROR -> {
                                binding.inspectionListLoadingPanel.visibility = View.INVISIBLE
                                Log.d(REPAIR_LIST_FRAGMENT, "Inspection list collecting error")
                            }
                        }
                    }
            }
        }
        // hospitalList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.hospitalListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                hospitalList = result.data
                                InspectionViewModel.updateRoomHospitalListFlow(hospitalList).collect(){
                                }
                                initMenu()
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                hospitalList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(REPAIR_LIST_FRAGMENT, "Hospital list collecting error")
                        }
                    }
                }
            }
        }
        // deviceList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.deviceListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                deviceList = result.data
                                InspectionViewModel.updateRoomDeviceListFlow(deviceList).collect(){
                                }
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                deviceList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(REPAIR_LIST_FRAGMENT, "Device list collecting error")

                        }
                    }
                }
            }
        }
        // inspectionStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.inspectionStateListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                inspectionStateList = result.data
                                InspectionViewModel.updateRoomInspectionStateListFlow(inspectionStateList).collect(){
                                }
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                inspectionStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(REPAIR_LIST_FRAGMENT, "Inspection state list collecting error")

                        }
                    }
                }
            }
        }
        // technician list
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.technicianListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                technicianList = result.data
                                InspectionViewModel.updateRoomTechnicianListFlow(technicianList).collect(){
                                }
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                technicianList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(REPAIR_LIST_FRAGMENT, "Technician list collecting error")
                        }
                    }
                }
            }
        }


        // FAB Button implementation
        binding.addInspectionFABButton.setOnClickListener {
            if (requireActivity().resources.configuration.screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                val fragment = EditInspectionFragment()
                fragment.arguments = bundleOf("isEditable" to true)
                childFragmentManager.beginTransaction()
                    // .replace(R.id.inspectionDetailsFragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                findNavController().navigate(InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /* ***************************** FUNCTIONS ************************************************** */
    private fun selectHospital(position: Int) {
        if(position != 0) {
            val selection = hospitalList[position].hospitalId
            preparedInspectionList = inspectionList.filter { it.hospitalId == selection }
            binding.inspectionRecyclerView.adapter =
                InspectionListAdapter(
                    preparedInspectionList,
                    hospitalList,
                    deviceList,
                    inspectionStateList,
                    recyclerViewListener
                )
        } else {
            preparedInspectionList = inspectionList
            binding.inspectionRecyclerView.adapter =
                InspectionListAdapter(
                    preparedInspectionList,
                    hospitalList,
                    deviceList,
                    inspectionStateList,
                    recyclerViewListener
                )
        }
    }
    private fun updateRecyclerViewAdapter() {
        binding.inspectionRecyclerView.adapter =
            InspectionListAdapter(preparedInspectionList, hospitalList, deviceList, inspectionStateList , recyclerViewListener)    }
    private fun initMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider
            {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.inspection_list_options_menu, menu)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        ACTION_BAR_TITLE

                    // Hospital spinner in action bar implementation
                    spinnerHospitalList.clear()
                    spinnerHospitalList.add(0, "")
                    hospitalList.forEach {
                        spinnerHospitalList.add(it.hospitalName)
                    }
                    hospitalSpinnerItem = menu.findItem(R.id.repairListHospitalSpinner)
                    inspectionListHospitalSpinner = hospitalSpinnerItem.actionView as Spinner
                    val hospitalSpinnerAdapter = activity?.baseContext?.let { it ->
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            spinnerHospitalList
                        )
                    }
                    inspectionListHospitalSpinner.adapter = hospitalSpinnerAdapter
                    inspectionListHospitalSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
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
                override fun onMenuItemSelected(item: MenuItem): Boolean {
                    return when (item.itemId) {
                        R.id.inspectionSortItem -> {
                            sortInspectionListItemClick()
                            true
                        }
                        R.id.inspectionFilterItem -> {
                            filterInspectionListItemClick()
                            true
                        }
                        R.id.inspectionGroupItem -> {
                            groupInspectionListItemClick()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }
    // TODO prepareInspectionList() in InspectionListFragment
    private fun prepareInspectionList()  {
        // filterInspectionList(filteringCondition)
        // sortInspectionList(sortingConditions)
        // groupInspectionList(groupingCondition)
    }

    // TODO Sorting, filtering and grouping to implement in InspectionListFragment
    private fun groupInspectionListItemClick() {
        Toast.makeText(requireActivity(),"Waiting for implementation", Toast.LENGTH_SHORT).show()
        // groupingCondition = getGroupingConditions()
        // groupInspectionList(groupingCondition)
    }
    private fun filterInspectionListItemClick() {
        Toast.makeText(requireActivity(),"Waiting for implementation", Toast.LENGTH_SHORT).show()
        // filteringCondition = getFilteringConditions()
        // filterInspectionList(filteringCondition)
    }
    private fun sortInspectionListItemClick() {
        Toast.makeText(requireActivity(),"Waiting for implementation", Toast.LENGTH_SHORT).show()
        // sortingConditions = getSortingConditions()
        // sortInspectionList(sortingConditions)
    }

    private fun getSortingConditions(): Bundle {
        // val dialog = FilteringDialog()
        // dialog.show(parentFragmentManager, "SORTING ALERT DIALOG")
        // childFragmentManager.setFragmentResultListener(SORTING_REQUEST_KEY, viewLifecycleOwner) { string, bundle ->
        //     filteringCondition = bundle
        // }
        return  filteringCondition
    }
    private fun getGroupingConditions(): Bundle {
        //val condition = bundleOf()
        return  groupingCondition
    }
    private fun getFilteringConditions(): Bundle {
        // val dialog = FilteringDialog()
        // dialog.show(parentFragmentManager, "FILTERING ALERT DIALOG")
        // childFragmentManager.setFragmentResultListener(FILTERING_REQUEST_KEY, viewLifecycleOwner) { string, bundle ->
        //     filteringCondition = bundle
        // }
        return  filteringCondition
    }

    private fun groupInspectionList(bundle: Bundle) {
        // val condition = bundle.get("")
        // val condition2 = bundle.get("")
    }
    private fun sortInspectionList(bundle: Bundle) {
        // val bundleType = bundle.get("bundle_type") as String
        // val switch = bundle.get("switch") as Boolean
        // val value = bundle.get("switch") as Long
        // preparedInspectionList = inspectionList.sortedBy { inspection ->
        //     if (switch) {
        //         inspection.inspectionDate.toLong() > value
        //     } else {
        //         inspection.inspectionDate.toLong() < value
        //     }
        // }
        // binding.inspectionRecyclerView.adapter = InspectionListAdapter(preparedInspectionList, hospitalList, deviceList, inspectionStateList , listener)
    }
    private fun filterInspectionList(bundle: Bundle) {
        // val bundleType = bundle.get("bundle_type") as String
        // val switch = bundle.get("switch") as Boolean
        // val value = bundle.get("value") as Long
        // preparedInspectionList = inspectionList.filter { inspection ->
        //     if (switch) {
        //         inspection.inspectionDate.toLong() > value
        //     } else {
        //         inspection.inspectionDate.toLong() < value
        //     }
        // }
        // binding.inspectionRecyclerView.adapter = InspectionListAdapter(preparedInspectionList, hospitalList, deviceList, inspectionStateList , listener)
    }
}

