package com.adrpien.tiemed.editrepair

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adrpien.tiemed.R
import com.adrpien.tiemed.datepicker.RepairDatePickerDialog
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.datamodels.Repair


class EditRepairFragment : Fragment(), DatePickerDialog.OnDateSetListener {


    // ViewBinding
    private var _binding: FragmentEditRepairBinding? = null
    private val binding
        get() = _binding!!

    // ViewModel
    val viewModelProvider by viewModels<EditRepairViewModel>()

    // Init
    init{
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditRepairBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting TextViews with values
        binding.idEditText.setText(arguments?.getString("id"))

        // Setting openingButton listener
        binding.openingDateButton.setOnClickListener {
             // Create TimePicker
             val dialog = RepairDatePickerDialog()
             // show MyTimePicker
             dialog.show(childFragmentManager, "repair_time_picker")
         }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.edit_repair_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId){
        R.id.saveRepairItem -> {
            if (arguments?.getString("uid") != null){
                // viewModelProvider.updateRepair(mapOf("id" to binding.idEditText.text.toString()))
            }
            else {
                viewModelProvider.createRepair(Repair(id = binding.idEditText.text.toString()))
            }
            return true
        }
        else -> super.onOptionsItemSelected(item)
    }
    }


    // Implementing DatePickerDialog.OnDateSetListener in fragment to use ViewModel, which is not in adapter
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Toast.makeText(activity, "Date: $year/$month/$dayOfMonth", Toast.LENGTH_SHORT).show()
    }

}