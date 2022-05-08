package com.adrpien.tiemed.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.adapters.RepairListAdapter
import com.adrpien.tiemed.adapters.onRepairItemClickListener
import com.adrpien.tiemed.viewmodels.RepairListViewModel
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.datamodels.Repair
import com.adrpien.tiemed.datamodels.State


class RepairListFragment : Fragment(), onRepairItemClickListener {

    // ViewBinding
    private var _binding: FragmentRepairListBinding? = null
    private val binding: FragmentRepairListBinding
        get() = _binding!!

    // Lazy creating ViewModelProvider
    val viewModelProvider by viewModels<RepairListViewModel>()

    // Creating Fragment Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.options_menu, menu)
        }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.sortItem-> {
                // TODO Set sorting and update adapter
                true
            }
            R.id.groupItem-> {
                // TODO Set sorting and update adapter
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

        // Update adapter when data changed
        viewModelProvider
            .repairList
            .observe(viewLifecycleOwner) { t ->
                if (t.isNotEmpty()) {
                    val adapter = RepairListAdapter(t, this)
                    binding.repairRecyclerView.adapter = adapter
                }
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun setOnRepairItemClick() {
        findNavController().navigate(
            RepairListFragmentDirections.actionRepairListFragmentToEditRepairFragment()
        )
    }
}
