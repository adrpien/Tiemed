package com.adrpien.tiemed.editinspection


import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentEditInspectionBinding
import com.adrpien.tiemed.datamodels.ESTState
import com.adrpien.tiemed.datamodels.Inspection
import com.adrpien.tiemed.datamodels.InspectionState
import com.adrpien.tiemed.datepickers.InspectionDatePickerDialog
import com.adrpien.tiemed.fragments.BaseFragment
import com.adrpien.tiemed.signature.SignatureDialog
import java.util.*
import kotlin.reflect.full.memberProperties


class EditInspectionFragment : BaseFragment(), DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    // ViewBinding
    private var _binding: FragmentEditInspectionBinding? = null
    private val binding: FragmentEditInspectionBinding
        get() = _binding!!

    // ViewModel
    val viewModelProvider by viewModels<EditInspectionViewModel>()

    private var hospitalList: MutableList<String> = mutableListOf()
    private lateinit var inspection: MutableLiveData<Inspection>

    private var tempInspection: Inspection = Inspection()
    private var tempSignatureByteArray = ByteArray(0)

    private lateinit var id: String

    val TAG = "EditInspectionFragment"

    init{
        // Setting options menu
        setHasOptionsMenu(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
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
                bindInspectionData(inspection)

                // Setting inspection data as tempInspection default value
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
            var hospitalListArrayAdapter = activity?.baseContext?.let { it ->
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

        // TODO signatureImageButton implementation
        // signatureImageButton implementation
        binding.signatureImageButton.setOnClickListener {

            // Open SignatureDialog when signatureImageButtonClicked
            val dialog = SignatureDialog()
            requireActivity().supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner,
                FragmentResultListener { requestKey, result ->
                    tempSignatureByteArray = result.getByteArray("signature")!!
                    val options = BitmapFactory.Options()
                    options.inMutable = true
                    val bmp = BitmapFactory.decodeByteArray(
                        tempSignatureByteArray,
                        0,
                        tempSignatureByteArray.size,
                        options
                    )

                    binding.signatureImageButton.setImageBitmap(bmp)
                })
            // show MyTimePicker
            dialog.show(childFragmentManager, "signature_dialog")
        }

        // EST spinner implementation
        binding.inspectionESTRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            tempInspection.safetyTest = group.findViewById<AppCompatRadioButton>(checkedId).text.toString()
                .uppercase()
                .replace(" ", "_")

        }

        // State spinner implementation
        binding.inspectionStateRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            tempInspection.inspectionState = group.findViewById<AppCompatRadioButton>(checkedId).text.toString()
                .uppercase().
                replace(" ", "_")
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
                // Update/Add inspection record button reaction
                viewModelProvider.uploadSignature(tempSignatureByteArray, tempInspection.id)
                updateTempInspection()
                val map = createMap(tempInspection)
                if(id != null) {
                    viewModelProvider.updateInspection(map, id)
                    Log.d(TAG, "Inspection updated")
                } else {
                    viewModelProvider.createInspection(tempInspection)
                }

                // Go to inspection list when record saved
                findNavController().navigate(EditInspectionFragmentDirections.actionEditInspectionFragmentToInspectionListFragment())
                true
            }
            R.id.addRepairRecordItem -> {
                // TODO addRepairRecordItem click reaction
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Handling date set reaction for date dialog
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        // Set values
        var date: Calendar = Calendar.getInstance()
        date.set(year, month, dayOfMonth)

        // Set button text
        tempInspection.inspectionDate = date.timeInMillis.toString()
        binding.inspectionDateButton.setText(getDateString(date.timeInMillis))
    }

    // Binding data of inspection in  appropriate components
    private fun bindInspectionData(inspection: Inspection) {
        binding.inspectionIDTextInputEditText.setText(inspection.id)
        binding.inspectionNameTextInputEditText.setText(inspection.name)
        binding.inspectionManufacturerTextInputEditText.setText(inspection.manufacturer)
        binding.inspectionModelTextInputEditText.setText(inspection.model)
        binding.inspectionINTextInputEditText.setText(inspection.inventoryNumber)
        binding.inspectionSNTextInputEditText.setText(inspection.serialNumber)
        binding.inspectionWardTextInputEditText.setText(inspection.ward)

        // Date button inspection data bind
        binding.inspectionDateButton.setText(getDateString(inspection.inspectionDate.toLong()))

        // Hospital spinner bind data to implement
        bindHospitalSpinner(inspection)

        // EST radio button bind
        bindESTRadioButton(inspection)

        // Inspection state radio button bind
        bindStateRadioButton(inspection)
    }

    // Binding data of inspection in state spinner
    private fun bindStateRadioButton(inspection: Inspection) {
        var position = 0
        var selection = 0
        for (item in InspectionState.values()) {
            if (inspection.inspectionState == item.toString()) {
                position = selection
            }
            selection += 1
        }
        binding.inspectionStateRadioGroup.check(binding.inspectionStateRadioGroup.getChildAt(position).id)
    }

    // Binding data of inspection in state spinner
    private fun bindESTRadioButton(inspection: Inspection) {
        var position = 0
        var selection = 0
        for (item in ESTState.values()) {
            if (inspection.safetyTest == item.toString()) {
                position = selection
            }
            selection += 1
        }
        binding.inspectionESTRadioGroup.check(binding.inspectionESTRadioGroup.getChildAt(position).id)
    }

    // Binding data of inspection in hospital spinner
    private fun bindHospitalSpinner(inspection: Inspection) {
        var position = 0
        var selection = 1
        for (item in hospitalList) {
            if (inspection.hospital == item.toString()) {
                position = selection
            }
            selection += 1
        }
        binding.inspectionHospitalSpinner.setSelection(position)
    }

    private fun bindSignatureImageButton(inspection: Inspection){
        // TODO Use Glide to bind signature photo with image button

    }

    // Creating map of inspection fields with their values
    private fun createMap(inspection: Inspection): Map<String, String> {
        var map: MutableMap<String, String> = mutableMapOf()
        for (component in Inspection::class.memberProperties){
            map.put(component.name, component.get(inspection).toString())
        }
        return map
    }

    // Updating tempInspection
    private fun updateTempInspection(){
        tempInspection.id = binding.inspectionIDTextInputEditText.text.toString()
        tempInspection.name = binding.inspectionNameTextInputEditText.text.toString()
        tempInspection.manufacturer = binding.inspectionManufacturerTextInputEditText.text.toString()
        tempInspection.model = binding.inspectionModelTextInputEditText.text.toString()
        tempInspection.inventoryNumber = binding.inspectionINTextInputEditText.text.toString()
        tempInspection.serialNumber = binding.inspectionSNTextInputEditText.text.toString()
        tempInspection.ward = binding.inspectionWardTextInputEditText.text.toString()
        tempInspection.hospital = binding.inspectionHospitalSpinner.selectedItem.toString()
        tempInspection.signature = viewModelProvider.getInspectionSignatureUrl(tempInspection.id).toString()
    }

    // Handling hospital spinner item selected reaction
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id == R.id.inspectionHospitalSpinner){
            tempInspection.hospital = parent.getItemAtPosition(position).toString()
        }
    }

    // Handling hostpial spinner no item selected reaction
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}