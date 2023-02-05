package com.adrpien.tiemed.presentation.feature_repairs.ui

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.core.dialogs.filtering_dialog.FilteringDialog
import com.adrpien.tiemed.presentation.feature_users.RepairListAdapter
import com.adrpien.tiemed.presentation.feature_users.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.model.Hospital
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.model.RepairState
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepairListFragment : BaseFragment() {

    // ViewBinding
    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    private val ACTION_BAR_TITLE: String = "Repair List"
    private val FILTERING_REQUEST_KEY: String = "FILTERING_REQUEST_KEY"
    private val SORTING_REQUEST_KEY: String = "SORTING_REQUEST_KEY"

    private var deviceList: List<Device> = listOf()
    private var repairList: List<Repair> = listOf()
    private var hospitalList: List<Hospital> = listOf()
    private var preparedRepairList: List<Repair> = listOf()
    private var repairStateList: List<RepairState> = listOf()

    // Filtering, sorting and grouping
    private var filteringCondition: Bundle = bundleOf()
    private var sortingConditions: Bundle = bundleOf()
    private var groupingCondition: Bundle = bundleOf()

    // Lazy instance of ViewModelProvider
    val RepairListViewModel by viewModels<RepairViewModel>()

    // Options menu
    init{
        setHasOptionsMenu(true)
        // activity?.setTitle("Repair List")
    }

    val listener: onRepairItemClickListener = object: onRepairItemClickListener {
        // onRepairItemClickListener interface implementation
        // Fetching id of clicked record and startinng fragment
        override fun setOnRepairItemClick(itemView: View) {
            val fragment = EditRepairFragment()
            fragment.arguments = bundleOf("id" to itemView.findViewById<EditText>(R.id.editRepairIdInputEditText).text.toString())
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.detailsFragmentContainerView, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        override fun setOnRepairItemLongClick(itemView: View) {
            val fragment = EditRepairFragment()
            fragment.arguments = bundleOf("id" to itemView.findViewById<EditText>(R.id.editRepairIdInputEditText).text.toString())
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.pinnedFragmentContainerView, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    /* ***************************** MENU ******************************************************* */
    // Creating Fragment Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.repair_list_options_menu, menu)

        // Setting Action Bar Name according to open fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ACTION_BAR_TITLE

        // Hospital spinner in action bar implementation
        val spinnerItem = menu.findItem(R.id.repairListHospitalSpinner)
        val repairListHospitalSpinner = spinnerItem.actionView as AppCompatSpinner
        spinnerItem.setActionView(R.id.hospitalSpinnerActionBar)
        val hospitalSpinnerAdapter = activity?.baseContext?.let { it ->
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                hospitalList
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
    // HandLing options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handling repair menu item selection
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* *************************** LIFECYCLE FUNCTIONS ****************************************** */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepairListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setting RecyclerView
        binding.repairRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // List divider
        binding.repairRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))

        /* ********************** COLLECT STATE FLOW ******************************************** */
        // repairList
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel
                    .repairListStateFlow
                    .collect { result ->
                        when(result.resourceState) {
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    repairList = result.data
                                    preparedRepairList = repairList
                                    prepareRepairList()
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
                                } else {
                                    binding.repairListLoadingPanel.visibility = View.VISIBLE
                                }
                            }
                            ResourceState.SUCCESS -> {
                                binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                if (result.data != null) {
                                    repairList = result.data
                                    preparedRepairList = repairList
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
                                } else {
                                    binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                    preparedRepairList = repairList
                                    prepareRepairList()
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
                                }
                            }
                            ResourceState.ERROR -> {
                                binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                Toast.makeText(context, "List loading Error", Toast.LENGTH_SHORT).show()
                                preparedRepairList = repairList
                                binding.repairRecyclerView.adapter =
                                    RepairListAdapter(preparedRepairList, hospitalList, deviceList, repairStateList , listener)
                            }
                        }


                    }
            }
        }
        // hospitalList
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel.hospitalListStateFlow.collect { result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if(result.data != null) {
                                hospitalList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if(result.data != null) {
                                hospitalList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            // TODO Some hospital list loading error handling
                        }
                    }
                }
            }
        }

        // Adding new repair record
        binding.addRepairFABButton.setOnClickListener {
            findNavController().navigate(RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment())
        }
    }
    override fun onResume() {
        super.onResume()
        // activity?.invalidateOptionsMenu()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /* ***************************** FUNCTIONS ************************************************** */
    private fun selectHospital(position: Int) {
        val selection = hospitalList[position].name
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
        val bundleType = bundle.get("bundle_type") as String
        val switch = bundle.get("switch") as Boolean
        val value = bundle.get("switch") as Long
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
        val bundleType = bundle.get("bundle_type") as String
        val switch = bundle.get("switch") as Boolean
        val value = bundle.get("value") as Long
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
        filterRepairList(filteringCondition)
        sortRepairList(sortingConditions)
        groupRepairList(groupingCondition)
    }
}
