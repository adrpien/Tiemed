package com.adrpien.tiemed.repairlist

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.adapters.RepairListAdapter
import com.adrpien.tiemed.adapters.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentRepairListBinding


class RepairListFragment : Fragment(), onRepairItemClickListener {

    // ViewBinding
    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    // Lazy creating ViewModelProvider
    val viewModelProvider by viewModels<RepairListViewModel>()

    // Options menu
    init{
        setHasOptionsMenu(true)

    }

    // Creating Fragment Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.options_menu, menu)
        }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.repairSortItem-> {
                // TODO Repairs sorting and update adapter
                true
            }
            R.id.repairGroupItem-> {
                // TODO Repair grouping and update adapter
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


        // Setting RecyclerView Adapter
        binding.repairRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.repairRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))

        // Update adapter when data changed
        viewModelProvider
            .repairList
            .observe(viewLifecycleOwner) { t ->
                if (t.isNotEmpty()) {
                    val adapter = RepairListAdapter(t, this)
                    binding.repairRecyclerView.adapter = adapter
                }
            }

        binding.repairsFAB.setOnClickListener { t ->

            // Add new repair record
            findNavController().navigate(
                com.adrpien.tiemed.repairlist.RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

   override fun setOnRepairItemClick(itemView: View) {

       val id = itemView.findViewById<TextView>(R.id.repairRowIdTextView).text.toString()
        findNavController().navigate(
            com.adrpien.tiemed.repairlist.RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment().actionId,
            bundleOf("id" to id )
        )
    }
}
