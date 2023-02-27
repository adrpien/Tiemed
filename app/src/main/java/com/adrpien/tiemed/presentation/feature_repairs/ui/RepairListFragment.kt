package com.adrpien.tiemed.presentation.feature_repairs.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.core.dialogs.filtering_dialog.FilteringDialog
import com.adrpien.tiemed.presentation.feature_users.RepairListAdapter
import com.adrpien.tiemed.presentation.feature_users.OnRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepairListFragment : BaseFragment() {

    // ViewBinding
    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    // Constans
    private val ACTION_BAR_TITLE: String = "Repair List"
    private val FILTERING_REQUEST_KEY: String = "FILTERING_REQUEST_KEY"
    private val SORTING_REQUEST_KEY: String = "SORTING_REQUEST_KEY"

    // Data
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
    // Hospital Spinner

    lateinit var hospitalSpinnerItem: MenuItem
    lateinit var repairListHospitalSpinner: Spinner

    // Filtering, sorting and grouping
    private var filteringCondition: Bundle = bundleOf()
    private var sortingConditions: Bundle = bundleOf()
    private var groupingCondition: Bundle = bundleOf()

    // Lazy instance of ViewModelProvider
    val RepairListViewModel by viewModels<RepairViewModel>()

    // OnRepairItemClickListener implementation
    val listener: OnRepairItemClickListener = object: OnRepairItemClickListener {
        override fun setOnRepairItemClick(itemView: View, position: Int) {
            val fragment = EditRepairFragment()
            fragment.arguments = bundleOf("id" to repairList[position].repairId)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.detailsFragmentContainerView, fragment)
                ?.addToBackStack(null)
                ?.commit()

        }
        override fun setOnRepairItemLongClick(itemView: View, position: Int) {
            if (requireActivity().resources.configuration.screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                val fragment = EditRepairFragment()
                fragment.arguments = bundleOf("id" to repairList[position].repairId)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.pinnedFragmentContainerView, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            } else { }
        }
    }



    /* *************************** LIFECYCLE FUNCTIONS ****************************************** */
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
                    (requireActivity() as AppCompatActivity).supportActionBar?.title = ACTION_BAR_TITLE

                    // Hospital spinner in action bar implementation
                    // TODO Hospital spinner in RepairListFragment to implement...
                    val hospitalNameList = mutableListOf<String>()
                    hospitalList.forEach {
                        hospitalNameList.add(it.hospitalName)
                    }
                    hospitalSpinnerItem = menu.findItem(R.id.repairListHospitalSpinner)
                    repairListHospitalSpinner = hospitalSpinnerItem.actionView as Spinner
                    //spinnerItem.setActionView(R.id.hospitalSpinnerActionBar)
                    val hospitalSpinnerAdapter = activity?.baseContext?.let { it ->
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            hospitalNameList
                        )
                    }
                    repairListHospitalSpinner.adapter = hospitalSpinnerAdapter
                    repairListHospitalSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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
                        R.id.repairSortItem-> {
                            sortRepairListItemClick()
                            true
                        }
                        R.id.repairFilterItem-> {
                            filterRepairListItemClick()
                            true
                        }
                        R.id.repairGroupItem-> {
                            groupRepairListItemClick()
                            true
                        }
                        else -> false
                    }
                }
                                  },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED)

        /* ********************** RECYCLER VIEW ******************************************** */
        // Setting RecyclerView
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.repairRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            // List divider
            binding.repairRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        } else {
            binding.repairRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
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
                        when(result.resourceState) {
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
                                Toast.makeText(context, "repairList loading Error", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }
            }
        }
        // hospitalList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.repairStateListStateFlow.collect { result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Toast.makeText(context, "deviceList loading Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        // deviceList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.deviceListStateFlow.collect { result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                deviceList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                deviceList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Toast.makeText(context, "deviceList loading Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        // repairStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.repairStateListStateFlow.collect { result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Toast.makeText(context, "repairStateList loading Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        // technician list
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.technicianListStateFlow.collect { result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                technicianList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                technicianList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Toast.makeText(context, "technicianList loading Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        /* ********************** FAB BUTTON ******************************************** */
        // Adding new repair record
        binding.addRepairFABButton.setOnClickListener {
            findNavController().navigate(RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment())
        }
    }
    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /* ***************************** FUNCTIONS ************************************************** */
    private fun selectHospital(position: Int) {
        val selection = hospitalList[position].hospitalName
        preparedRepairList = repairList.filter { it.hospitalId == selection }
        binding.repairRecyclerView.adapter =
            RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
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
        binding.repairRecyclerView.adapter = RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)

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
        binding.repairRecyclerView.adapter = RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
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
            RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
    }
}
