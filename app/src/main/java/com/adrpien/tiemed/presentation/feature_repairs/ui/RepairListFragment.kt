package com.adrpien.tiemed.presentation.feature_repairs.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
import com.adrpien.tiemed.core.dialogs.filtering_dialog.FilteringDialog
import com.adrpien.tiemed.presentation.feature_users.RepairListAdapter
import com.adrpien.tiemed.presentation.feature_users.OnRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepairListFragment: Fragment() {

    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    private val ACTION_BAR_TITLE: String = "Repair List"
    private val FILTERING_REQUEST_KEY: String = "FILTERING_REQUEST_KEY"
    private val SORTING_REQUEST_KEY: String = "SORTING_REQUEST_KEY"

    private val REPAIR_LIST_FRAGMENT: String = "REPAIR_LIST_FRAGMENT"

    private var repairList: List<Repair> = listOf()
        set(value) {
            RepairListViewModel.updateRoomRepairListFlow(value)
            field = value
            preparedRepairList = value
            prepareRepairList()
        }
    private var deviceList: List<Device> = listOf()
        set(value) {
            field = value
            RepairListViewModel.updateRoomDeviceListFlow(value)
            updateRecyclerViewAdapter()
        }
    private var hospitalList: List<Hospital> = listOf()
        set(value) {
            field =value
            RepairListViewModel.updateRoomHospitalListFlow(value)
            updateRecyclerViewAdapter()
            activity?.invalidateOptionsMenu()
        }
    private var repairStateList: List<RepairState> = listOf()
        set(value) {
            field =value
            RepairListViewModel.updateRoomRepairStateListFlow(value)
            updateRecyclerViewAdapter()

        }
    private var technicianList: List<Technician> = listOf()
        set(value) {
            field =value
            RepairListViewModel.updateRoomTechnicianListFlow(value)
            updateRecyclerViewAdapter()
        }

    private var preparedRepairList: List<Repair> = listOf()
        set(value) {
            field = value
            updateRecyclerViewAdapter()
        }

    lateinit var hospitalSpinnerItem: MenuItem
    lateinit var repairListHospitalSpinner: Spinner
    private val spinnerHospitalList = mutableListOf<String>()


    private var filteringCondition: Bundle = bundleOf()
    private var sortingConditions: Bundle = bundleOf()
    private var groupingCondition: Bundle = bundleOf()

    val RepairListViewModel by viewModels<RepairViewModel>()

    val recyclerViewListener: OnRepairItemClickListener = object: OnRepairItemClickListener {
        override fun setOnRepairItemClick(itemView: View, position: Int) {
            if (requireActivity().resources.configuration.screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                val fragment = EditRepairFragment()
                fragment.arguments = bundleOf(
                    "repairId" to repairList[position].repairId,
                    "deviceId" to repairList[position].deviceId
                    )
                childFragmentManager.beginTransaction()
                    .add(R.id.detailsFragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                findNavController().navigate(RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment())
            }
        }
        override fun setOnRepairItemLongClick(itemView: View, position: Int) {
            // TODO I don't know, i will use this one day
        }
    }


    /* *************************** LIFECYCLE FUNCTIONS ****************************************** */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onPause() {
        super.onPause()
        updateRecyclerViewAdapter()
    }
    override fun onStart() {
        super.onStart()
        updateRecyclerViewAdapter()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepairListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /* ********************** MENU ******************************************** */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.repair_list_options_menu, menu)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        ACTION_BAR_TITLE

                    // Hospital spinner in action bar implementation
                    hospitalList.forEach {
                        spinnerHospitalList.add(it.hospitalName)
                    }
                    hospitalSpinnerItem = menu.findItem(R.id.repairListHospitalSpinner)
                    repairListHospitalSpinner = hospitalSpinnerItem.actionView as Spinner
                    val hospitalSpinnerAdapter = activity?.baseContext?.let { it ->
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            spinnerHospitalList
                        )
                    }
                    repairListHospitalSpinner.adapter = hospitalSpinnerAdapter
                    repairListHospitalSpinner.onItemSelectedListener =
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
                        R.id.repairSortItem -> {
                            sortRepairListItemClick()
                            true
                        }
                        R.id.repairFilterItem -> {
                            filterRepairListItemClick()
                            true
                        }
                        R.id.repairGroupItem -> {
                            groupRepairListItemClick()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        /* ********************** RECYCLER VIEW ******************************************** */
        // Setting RecyclerView
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.repairRecyclerView.layoutManager = LinearLayoutManager(activity)
            // List divider
            binding.repairRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        } else {
            binding.repairRecyclerView.layoutManager = GridLayoutManager(activity, 2)
            // List divider
            binding.repairRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        }

        /* ********************** COLLECT STATE FLOW ******************************************** */
        // repairList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel
                    .repairListStateFlow
                    .collect { result ->
                        when (result.resourceState) {
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    repairList = result.data
                                    binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                } else {
                                    binding.repairListLoadingPanel.visibility = View.VISIBLE
                                }
                            }
                            ResourceState.SUCCESS -> {
                                binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                if (result.data != null) {
                                    repairList = result.data
                                }
                            }
                            ResourceState.ERROR -> {
                                binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                Log.d(REPAIR_LIST_FRAGMENT, "Repair list collecting error")
                            }
                        }


                    }
            }
        }
        // hospitalList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.hospitalListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                hospitalList = result.data
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
                RepairListViewModel.deviceListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                deviceList = result.data
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
        // repairStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.repairStateListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(REPAIR_LIST_FRAGMENT, "Repair state list collecting error")

                        }
                    }
                }
            }
        }
        // technician list
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.technicianListStateFlow.collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                technicianList = result.data
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
        /* ********************** FAB BUTTON ******************************************** */
        // Adding new repair record
        binding.addRepairFABButton.setOnClickListener {
            if (requireActivity().resources.configuration.screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                val fragment = EditRepairFragment()
                fragment.arguments = bundleOf("id" to "")
                childFragmentManager.beginTransaction()
                    ?.add(R.id.detailsFragmentContainerView, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            } else {
                findNavController().navigate(RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment())
            }
        }
    }
    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
        updateRecyclerViewAdapter()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /* ***************************** FUNCTIONS ************************************************** */
    private fun selectHospital(position: Int) {
        val selection = hospitalList[position].hospitalId
        preparedRepairList = repairList.filter { it.hospitalId == selection }
        binding.repairRecyclerView.adapter =
            RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , recyclerViewListener)
    }
    private fun groupRepairListItemClick() {
        groupingCondition = getGroupingConditions()
        groupRepairList(groupingCondition)
    }
    private fun filterRepairListItemClick() {
        filteringCondition = getFilteringConditions()
        filterRepairList(filteringCondition)
    }
    private fun sortRepairListItemClick() {
        sortingConditions = getSortingConditions()
        sortRepairList(sortingConditions)
    }
    private fun groupRepairList(bundle: Bundle) {
        val condition = bundle.get("")
        val condition2 = bundle.get("")
        // TODO Repairs grouping and update adapter
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
    private fun sortRepairList(bundle: Bundle) {
        val bundleType = bundle.getString("bundle_type")
        val switch = bundle.getBoolean("switch")
        val value = bundle.getLong("switch")
        preparedRepairList = repairList.sortedBy { repair ->
            if (switch) {
                repair.openingDate.toLong() > value
            } else {
                repair.openingDate.toLong() < value
            }
        }
        binding.repairRecyclerView.adapter = RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , recyclerViewListener)

    }
    private fun getGroupingConditions(): Bundle {
        // TODO Open AlertDialog and return bundle with conditions
        val condition = bundleOf()
        return  condition
    }
    private fun filterRepairList(bundle: Bundle) {
        val bundleType = bundle.getString("bundle_type")
        val switch = bundle.getBoolean("switch")
        val value = bundle.getLong("value")
        preparedRepairList = repairList.filter { repair ->
                if (switch) {
                    repair.openingDate.toLong() > value
                } else {
                    repair.openingDate.toLong() < value
                }
        }
        binding.repairRecyclerView.adapter = RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , recyclerViewListener)
    }
    private fun getFilteringConditions(): Bundle {
        val dialog = FilteringDialog()
        dialog.show(parentFragmentManager, "FILTERING ALERT DIALOG")
        childFragmentManager.setFragmentResultListener(FILTERING_REQUEST_KEY, viewLifecycleOwner) { string, bundle ->
            filteringCondition = bundle
        }
        return  filteringCondition
    }
    private fun prepareRepairList() {
        //filterRepairList(filteringCondition)
        //sortRepairList(sortingConditions)
        //groupRepairList(groupingCondition)
    }
    private fun updateRecyclerViewAdapter(){
        binding.repairRecyclerView.adapter =
            RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , recyclerViewListener)
    }
}