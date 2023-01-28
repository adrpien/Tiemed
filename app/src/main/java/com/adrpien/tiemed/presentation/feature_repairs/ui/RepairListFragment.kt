package com.adrpien.tiemed.presentation.feature_repairs.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
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
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.presentation.feature_users.RepairListAdapter
import com.adrpien.tiemed.presentation.feature_users.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RepairListFragment : BaseFragment() {

    // ViewBinding
    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    private val ACTION_BAR_TITLE: String = "Repair List"

    private lateinit var repairList: List<Repair>

    // Lazy creating ViewModelProvider
    val RepairListViewModel by viewModels<RepairViewModel>()

    // Options menu
    init{
        setHasOptionsMenu(true)
        activity?.setTitle("Repair List")
    }

    // Creating Fragment Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.repair_list_options_menu, menu)

        // Setting Action Bar Name according to open fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ACTION_BAR_TITLE

    }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.repairSortItem-> {
                // TODO Repairs sorting and update adapter
                Toast.makeText(context, getString(R.string.WAIT_FOR_IMPLEMENTATION), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.repairGroupItem-> {
                // TODO Repair grouping and update adapter
                Toast.makeText(context, getString(R.string.WAIT_FOR_IMPLEMENTATION), Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepairListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Setting RecyclerView
        binding.repairRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.repairRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))

        // Setting adapter and update adapter when data changed
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairListViewModel
                    .repairListStateFlow
                    .collect { result ->
                        val listener: onRepairItemClickListener = object: onRepairItemClickListener {
                            // onRepairItemClickListener interface implementation
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

                        when(result.resourceState) {
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    repairList = result.data
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(repairList, listener)
                                } else {
                                    // TODO show loading animation
                                }
                            }
                            ResourceState.SUCCESS -> {
                                if (result.data != null) {
                                    repairList = result.data
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(repairList, listener)
                                } else {
                                    repairList = emptyList()
                                    binding.repairRecyclerView.adapter =
                                        RepairListAdapter(repairList, listener)
                                }
                            }
                            ResourceState.ERROR -> {
                                // TODO some list loading error handling
                                Toast.makeText(context, "List loading Error", Toast.LENGTH_SHORT).show()
                                repairList = emptyList()
                                binding.repairRecyclerView.adapter =
                                    RepairListAdapter(repairList, listener)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
