package com.adrpien.tiemed.presentation.feature_repairs.ui

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.adrpien.tiemed.presentation.feature_users.RepairListAdapter
import com.adrpien.tiemed.presentation.feature_users.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.domain.model.Hospital
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepairListFragment : BaseFragment() {

    // ViewBinding
    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    private val ACTION_BAR_TITLE: String = "Repair List"

    private var repairList: List<Repair> = listOf()
    private var hospitalList: List<Hospital> = listOf()
    private var filteredRepairList: List<Repair> = listOf()

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
            val id = itemView.findViewById<TextView>(R.id.repairRowIdTextView).text.toString()
            findNavController().navigate(
                RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment().actionId,
                bundleOf("id" to id)
            )
        }
        override fun setOnEditRepairButtonClick(itemView: View) {
            // TODO setOnEditRepairButtonClick fun to implement
        }
        override fun setOnViewRepairButtonClick(itemView: View) {
            // TODO setOnViewRepairButtonClick fun to implement
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
    // HanDLing options menu click events
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
        // Setting adapter and update adapter when data changed
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel
                    .repairListStateFlow
                    .collect { result ->
                        when(result.resourceState) {
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    repairList = result.data
                                    filteredRepairList = repairList
                                    applyFilteringSortingAndGrouping()
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(filteredRepairList, listener)
                                } else {
                                    binding.repairListLoadingPanel.visibility = View.VISIBLE
                                }
                            }
                            ResourceState.SUCCESS -> {
                                if (result.data != null) {
                                    repairList = result.data
                                    filteredRepairList = repairList
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(filteredRepairList, listener)
                                } else {
                                    binding.repairListLoadingPanel.visibility = View.VISIBLE
                                    repairList = emptyList()
                                    filteredRepairList = repairList
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(filteredRepairList, listener)
                                }
                            }
                            ResourceState.ERROR -> {
                                binding.repairListLoadingPanel.visibility = View.INVISIBLE
                                Toast.makeText(context, "List loading Error", Toast.LENGTH_SHORT).show()
                                repairList = emptyList()
                                filteredRepairList = repairList
                                binding.repairRecyclerView.adapter =
                                    RepairListAdapter(filteredRepairList, listener)
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
        filteredRepairList = repairList.filter { it.hospitalId == selection }
        binding.repairRecyclerView.adapter =
            RepairListAdapter(filteredRepairList, listener)
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
    private fun sortRepairList(bundle: Bundle) {
        val condition = bundle.get("")
        val condition2 = bundle.get("")
        // TODO Repairs sorting and update adapter
        Toast.makeText(context, getString(R.string.WAIT_FOR_IMPLEMENTATION), Toast.LENGTH_SHORT).show()
    }
    private fun getSortingConditions(): Bundle {
        // TODO Open AlertDialog and return bundle with conditions
        val conditions = bundleOf()
        return conditions
    }
    private fun groupRepairList(bundle: Bundle) {
        val condition = bundle.get("")
        val condition2 = bundle.get("")
        // TODO Repairs grouping and update adapter
        Toast.makeText(context, getString(R.string.WAIT_FOR_IMPLEMENTATION), Toast.LENGTH_SHORT).show()
    }
    private fun getGroupingConditions(): Bundle {
        // TODO Open AlertDialog and return bundle with conditions
        val condition = bundleOf()
        return  condition
    }
    private fun filterRepairList(bundle: Bundle) {
        val condition = bundle.get("")
        val condition2 = bundle.get("")
        // TODO Repairs sorting and update adapter
        Toast.makeText(context, getString(R.string.WAIT_FOR_IMPLEMENTATION), Toast.LENGTH_SHORT).show()
    }
    private fun getFilteringConditions(): Bundle {
        // TODO Open AlertDialog and return bundle with conditions
        val condition = bundleOf()
        return  condition
    }
    private fun applyFilteringSortingAndGrouping() {
        filterRepairList(filteringCondition)
        sortRepairList(sortingConditions)
        groupRepairList(groupingCondition)
    }
}
