package com.adrpien.tiemed.editinspection

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentEditInspectionBinding
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datepickers.InspectionDatePickerDialog
import com.adrpien.tiemed.fragments.BaseFragment
import java.lang.NullPointerException

class EditInspectionFragment : BaseFragment(), DatePickerDialog.OnDateSetListener,  AdapterView.OnItemSelectedListener{

    // ViewModel
    val viewModelProvider by viewModels<EditInspectionViewModel>()

    private var hospitalList: MutableList<String> = mutableListOf()
    private lateinit var inspection: MutableLiveData<Inspection>

    // ViewBinding
    private var _binding: FragmentEditInspectionBinding? = null
    private val binding: FragmentEditInspectionBinding
        get() = _binding!!

    init{
        // Setting options menu
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id")

        // TODO W IF DALES TRY-CATCH DEBILU XDDDDDD
        if(id != null){
            try {
                inspection = viewModelProvider.getInspection(id)
            }
            catch (e: NullPointerException) {
                // TODO getInspection NullPointerException catch implementation
            }

        // Hospital spinner implementation
        viewModelProvider.getHospitalList().observe(viewLifecycleOwner) {
            for(item in it){
                item.name.let {
                    hospitalList.add(it)
                }
            }
            val hospitalListArrayAdapter = activity?.baseContext?.let { it ->
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    hospitalList
                )
            }
            binding.inspectionHospitalSpinner.adapter = hospitalListArrayAdapter

        }
        binding.inspectionHospitalSpinner.onItemSelectedListener = this

        // Date button implementation
        binding.inspectionDateButton.setOnClickListener {
            // Create TimePicker
            val dialog = InspectionDatePickerDialog()
            // show MyTimePicker
            dialog.show(childFragmentManager, "inspection_time_picker")
        }
            viewModelProvider.getInspection(id).observe(viewLifecycleOwner) { inspection ->
                binding.inspectionDateButton.setText(getDataString(inspection.inspectionDate.toLong()))
                bindInspectionData(inspection)
            }
        }

    }

    private fun bindInspectionData(inspection: Inspection) {
        binding.inspectionIDTextInputEditText.setText(inspection.id)
        binding.inspectionNameTextInputEditText.setText(inspection.name)
        binding.inspectionManufacturerTextInputEditText.setText(inspection.manufacturer)
        binding.inspectionModelTextInputEditText.setText(inspection.model)
        binding.inspectionINTextInputEditText.setText(inspection.inventoryNumber)
        binding.inspectionSNTextInputEditText.setText(inspection.serialNumber)
        binding.inspectionWardTextInputEditText.setText(inspection.ward)
        // TODO Hospital spinner bind data to implement
        val selection = 1
        //inspection.hospital?.uppercase()
        binding.inspectionHospitalSpinner.setSelection(selection)

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

        // Setting action bar title
        setActionBarTitle("Edit Inspection")
    }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.saveInspectionItem -> {
                // TODO Update/Add inspection record button reaction
                val map: Map<String, String> = mapOf(
                    // "id" to binding.InspectionIDTextInputEditText.text.toString(),
                    // "name" to binding.na
                )
                //val id =
                //viewModelProvider.updateInspection(map, id)
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // TODO inspectionDateButton onInspectionDateSet implementation
        Toast.makeText(context, "Inspection date changed", Toast.LENGTH_SHORT).show()
    }

    // Spinner override

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // TODO hospitalListSpinner onItemSelected implementation
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO hospitalListSpinner onNothingSelected implemention
    }


}