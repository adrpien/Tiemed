package com.adrpien.tiemed.inspectionlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrpien.tiemed.R
import com.adrpien.tiemed.adapters.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding

// ViewBinding
private var _binding: FragmentInspectionListBinding? = null
private val binding
    get() = _binding!!

class InspectionListFragment : Fragment(), OnInspectionItemClickListener {

    val viewModelProvider by viewModels<InspectionListViewModel>()

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater: MenuInflater = inflater
        menuInflater.inflate(R.menu.inspection_list_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.inspectionSortItem -> {
                // TODO Inspection list sorting
                true
            }
            R.id.inspectionFilterItem -> {
                // TODO Inspection list filtering
                true
            }
            R.id.inspectionGroupItem -> {
                // TODO Inspection list filetring
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inspectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModelProvider.inspectionList.observe(viewLifecycleOwner) { t ->

            if(t.isNotEmpty())
            binding.inspectionRecyclerView.adapter = InspectionListAdapter(t, this)
        }

    }

    override fun setOnInspectionItemClickListener(itemView: View) {

        val id = itemView.findViewById<TextView>(R.id.inspectionRowIdTextView).text.toString()
        findNavController().navigate(
            InspectionListFragmentDirections.actionInspectionListFragmentToEditInspectionFragment().actionId,
            bundleOf("id" to id )
        )

    }

}