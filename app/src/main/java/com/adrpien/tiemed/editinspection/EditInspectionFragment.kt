package com.adrpien.tiemed.editinspection

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentEditInspectionBinding
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.databinding.FragmentInspectionListBinding
import com.adrpien.tiemed.databinding.FragmentRepairListBinding
import com.adrpien.tiemed.fragments.BaseFragment

class EditInspectionFragment : BaseFragment() {

    // ViewBinding
    private var _binding: FragmentEditInspectionBinding? = null
    private val binding: FragmentEditInspectionBinding
        get() = _binding!!

    init{
        // Options menu
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // ViewBinding
        _binding = FragmentEditInspectionBinding.inflate(layoutInflater)
        return binding.root
    }

    // Creating Fragment Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.edit_inspection_options_menu, menu)

        setActionBarTitle("Edit Inspection")
    }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.saveInspectionItem -> {
                // TODO Update/Add inspection record button reaction
                Toast.makeText(context,"Saved!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.addRepairRecordItem -> {
                // TODO addRepairRecordItem click reaction
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}