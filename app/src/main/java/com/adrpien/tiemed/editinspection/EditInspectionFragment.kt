package com.adrpien.tiemed.editinspection

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentEditInspectionBinding
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datepickers.InspectionDatePickerDialog
import com.adrpien.tiemed.fragments.BaseFragment
import java.util.*

import kotlin.reflect.full.memberProperties

class EditInspectionFragment : BaseFragment(), DatePickerDialog.OnDateSetListener,  AdapterView.OnItemSelectedListener{

    // ViewBinding
    private var _binding: FragmentEditInspectionBinding? = null
    private val binding: FragmentEditInspectionBinding
        get() = _binding!!

    // ViewModel
    val viewModelProvider by viewModels<EditInspectionViewModel>()

    private var hospitalList: MutableList<String> = mutableListOf()
    private lateinit var inspection: MutableLiveData<Inspection>

    private var tempInspection: Inspection = Inspection()

    private lateinit var id: String

    val TAG = "EditInspectionFragment"

    init{
        // Setting options menu
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Filling fields with data of opened record (in case of update of inspection, not creating)
        id = arguments?.getString("id").toString()
        if(id != null) {
            inspection = viewModelProvider.getInspection(id)

            viewModelProvider.getInspection(id).observe(viewLifecycleOwner) { inspection ->
                binding.inspectionDateButton.setText(getDateString(inspection.inspectionDate.toLong()))
                bindInspectionData(inspection)
                tempInspection = inspection
            }
        }

        // Hospital spinner implementation
        viewModelProvider.getHospitalList().observe(viewLifecycleOwner) {
            for (item in it) {
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

        // Hospital Spinner implementation
        binding.inspectionHospitalSpinner.onItemSelectedListener = this

        // Date button implementation
        binding.inspectionDateButton.setOnClickListener {
            // Create TimePicker
            val dialog = InspectionDatePickerDialog()
            // show MyTimePicker
            dialog.show(childFragmentManager, "inspection_time_picker")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                val map = createMap(tempInspection)
                if(id != null) {
                    viewModelProvider.updateInspection(map, id)
                    Log.d(TAG, "Inspection updated")
                } else {
                    viewModelProvider.createInspection(tempInspection)
                }
                true
            }
            R.id.addRepairRecordItem -> {
                // TODO addRepairRecordItem click reaction
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        // Set values
        var date: Calendar = Calendar.getInstance()
        date.set(year, month, dayOfMonth)

        // Set button text
        tempInspection.inspectionDate = date.timeInMillis.toString()
        binding.inspectionDateButton.setText(getDateString(date.timeInMillis))
    }

    // Hospital spinner override
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // TODO hospitalListSpinner onItemSelected implementation
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO hospitalListSpinner onNothingSelected implemention
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

    // Creating map of inspection fields with their values
    private fun createMap(inspection: Inspection): Map<String, String> {
        var map: MutableMap<String, String> = mutableMapOf()
        for (component in Inspection::class.memberProperties){
            map.put(component.name, component.get(inspection).toString())
        }
        return map
    }
    private fun updateTempInspection(){

    }

}