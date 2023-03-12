package com.adrpien.tiemed.presentation.feature_inspections.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.R
import com.adrpien.tiemed.core.dialogs.date.TiemedDatePickerDialog
import com.adrpien.tiemed.core.dialogs.signature_dialog.SignatureDialog
import com.adrpien.tiemed.core.util.Helper
import com.adrpien.tiemed.core.util.Helper.Companion.getDateString
import com.adrpien.tiemed.databinding.FragmentInspectionEditBinding
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_inspections.view_model.InspectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

@AndroidEntryPoint
class EditInspectionFragment() : Fragment() {

    private var _binding: FragmentInspectionEditBinding? = null
    private val binding
        get() = _binding!!

    val InspectionViewModel by viewModels<InspectionViewModel>()

    private val EDIT_REPAIR_FRAGMENT = "EDIT_REPAIR_FRAGMENT"
    private val REPAIR_UPDATE_TAG = "INSPECTION UPDATE TAG"
    private val SIGNATURE_DIALOG_TAG = "SIGNATURE DIALOG TAG"
    private var REQUEST_IMAGE_CAPTURE: Int = 0

    private var spinnerHospitalList = mutableListOf<String>()
    private var spinnerInspectionStateList = mutableListOf<String>()
    private var spinnerEstStateList = mutableListOf<String>()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var inspectionId: String = ""
    private var deviceId: String = ""

    private var tempInspection = Inspection()
    private var tempDevice = Device()
    private var tempSignatureByteArray = byteArrayOf()
    private var tempPhotoByteArray = byteArrayOf()

    private var isEditable = false

    private var hospitalList = listOf<Hospital>()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                // InspectionViewModel.updateRoomHospitalListFlow(value)
                initHospitalListSpinner()
                if (tempInspection.hospitalId != "") { bindHospitalListSpinner(tempInspection) }
            }
        }
    private var estStateList = listOf<EstState>()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                // InspectionViewModel.updateRoomEstStateListFlow(value)
                initEstStateSpinner()
                if(tempInspection.estStateId != ""){ bindInspectionStateSpinner(tempInspection)}
            }
        }
    private var inspectionStateList = listOf<InspectionState>()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                // InspectionViewModel.updateRoomInspectionStateListFlow(value)
                initInspectionStateSpinner()
                if(tempInspection.inspectionStateId != "") { bindInspectionStateSpinner(tempInspection) }
            }
        }
    private var device = Device()
        set(value) {
            if(value.deviceId != "") {
                field =value
                bindDeviceData(value)
                tempDevice = value
            }
        }
    private var inspection = Inspection()
        set(value) {
            if(value.inspectionId != "") {
                field =value
                tempInspection = value
                bindInspectionData(tempInspection)
                if(estStateList.isNotEmpty()){ bindEstStateSpinner(tempInspection)}
                if(hospitalList.isNotEmpty()) { bindHospitalListSpinner(tempInspection)}
                if(inspectionStateList.isNotEmpty()){ bindInspectionStateSpinner(tempInspection)}
            }
        }
    private var signatureByteArray = byteArrayOf()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                tempSignatureByteArray = value
                bindSignatureImageButton(value)
            }
        }

    val hospitalSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            p1: View?,
            position: Int,
            p3: Long
        ) {
            if (parent?.id == R.id.editInspectionHospitalSpinner) {
                val hospitalName = parent.getItemAtPosition(position).toString()
                val hospital = hospitalList.find { it.hospitalName == hospitalName }
                if (hospital != null) {
                    tempInspection.hospitalId = hospital.hospitalId
                }
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
            // lave empty here, i think
        }
    }
    val inspectionStateSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            p1: View?,
            position: Int,
            p3: Long
        ) {
            if (parent?.id == R.id.editInspectionInspectionStateSpinner) {
                val inspectionStateName = parent.getItemAtPosition(position).toString()
                val inspectionState = inspectionStateList.find { it.inspectionState == inspectionStateName }
                if(inspectionState != null){
                    tempInspection.inspectionStateId = inspectionState.inspectionStateId
                }
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
            // leave empty here, i think
        }
    }
    val inspectionEstStateSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            p1: View?,
            position: Int,
            p3: Long
        ) {
            if (parent?.id == R.id.editInspectionEstStateSpinner) {
                val estStateName = parent.getItemAtPosition(position).toString()
                val estState = estStateList.find { it.estState == estStateName }
                if(estState != null){
                    tempInspection.estStateId = estState.estStateId
                }
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
            // lave empty here, i think
        }
    }
    val openingDatePickerDialogListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            var date: Calendar = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            tempInspection.inspectionDate = date.timeInMillis.toString()
            binding.editInspectionInspectionDateButton.setText(getDateString(date.timeInMillis))
        }

    }


    init{
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /* *************************** LIFECYCLE FUNCTIONS ****************************************** */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // What id does??
        // photoResultLauncher()

        /* *************************** GET BUNDLE ****************************************** */
        inspectionId = arguments?.getString("inspectionId", "") ?: ""
        deviceId = arguments?.getString("deviceId", "") ?: ""
        isEditable = arguments?.getBoolean("isEditable", false) ?: false


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionEditBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* *************************** COLLECT STATE FLOW ****************************************** */
        if (inspectionId != "") {
            // inspection
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    InspectionViewModel.getInspectionFlow(inspectionId).collect { result ->
                        when (result.resourceState) {
                            ResourceState.ERROR -> {
                                Log.d(EDIT_REPAIR_FRAGMENT, "Inspection collecting error")
                            }
                            ResourceState.SUCCESS -> {
                                if (result.data != null) {
                                    inspection = result.data
                                }
                            }
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    inspection = result.data
                                }
                            }
                        }
                    }
                }
            }
            // device
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    InspectionViewModel.getDeviceFlow(deviceId).collect { result ->
                        when (result.resourceState) {
                            ResourceState.SUCCESS -> {
                                if (result.data != null) {
                                    device = result.data
                                }
                            }
                            ResourceState.LOADING -> {
                                if (result.data != null) {
                                    device = result.data
                                }
                            }
                            ResourceState.ERROR -> {
                                Log.d(EDIT_REPAIR_FRAGMENT, "Device collecting error")
                            }
                        }
                    }
                }
            }
            // signature
           viewLifecycleOwner.lifecycleScope.launch {
               viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                   InspectionViewModel.getSignatureFlow(inspectionId)
                       .collect { result ->
                           when (result.resourceState) {
                               ResourceState.SUCCESS -> {
                                   if (result.data != null) {
                                       signatureByteArray = result.data
                                       if (tempSignatureByteArray.isEmpty()) {
                                           tempSignatureByteArray = signatureByteArray
                                       }
                                   }
                               }
                               ResourceState.LOADING -> {
                                   if (result.data != null) {
                                       signatureByteArray = result.data
                                       if (tempSignatureByteArray.isEmpty()) {
                                           tempSignatureByteArray = signatureByteArray
                                       }
                                   }
                               }
                               ResourceState.ERROR -> {
                                   Log.d(EDIT_REPAIR_FRAGMENT, "Signature collecting error")
                               }
                           }
                       }
               }
           }
        }
        // hospitalList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.getHospitalListFlow().collect { result ->
                    when (result.resourceState) {
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Hospital list collecting error")
                        }
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                hospitalList = result.data
                                InspectionViewModel.updateRoomHospitalListFlow(hospitalList).collect(){

                                }
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                hospitalList = result.data
                            }
                        }
                    }
                }
            }
        }
        // inspectionStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.getInspectionStateListFlow().collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                inspectionStateList = result.data
                                InspectionViewModel.updateRoomInspectionStateListFlow(inspectionStateList).collect(){
                                }
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                inspectionStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Inspection state list collecting error")
                        }
                    }

                }
            }
        }
        // estStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.getEstStateListFlow().collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                estStateList = result.data
                                InspectionViewModel.updateRoomEstStateListFlow(estStateList).collect(){
                                }
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                estStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Est state list collecting error")
                        }
                    }
                }
            }
        }

        /* *************************** COMPONENTS INITIALIZATION ****************************************** */

        binding.editInspectionInspectionDateButton.setOnClickListener {
            val dialog = TiemedDatePickerDialog(openingDatePickerDialogListener)
            dialog.show(childFragmentManager, "inspection_time_picker")
        }
        binding.editInspectionHospitalSpinner.onItemSelectedListener = hospitalSpinnerListener
        binding.editInspectionSignatureImageButton.setOnClickListener {
            val dialog = SignatureDialog()
            // childFragmentManager ?
            requireActivity().supportFragmentManager.setFragmentResultListener(
                getString(R.string.signature_request_key),
                viewLifecycleOwner,
                FragmentResultListener { requestKey, result ->
                    signatureByteArray = result.getByteArray("signature")!!
                })
            dialog.show(childFragmentManager, SIGNATURE_DIALOG_TAG)
        }
        binding.editInspectionInspectionStateSpinner.onItemSelectedListener = inspectionStateSpinnerListener
        binding.editInspectionEstStateSpinner.onItemSelectedListener = inspectionEstStateSpinnerListener
        binding.editInspectionEditSaveButton.setOnClickListener {
            if (isEditable == true) {
                updateTempDevice()
                updateTempInspection()
                if (inspectionId != "") {
                    updateDevice()
                } else {
                    createDevice()
                }
                setComponentsToNotEditable()
                binding.editInspectionEditSaveButton.setImageResource(R.drawable.edit_icon)
                isEditable = false
            } else {
                setComponentsToEditable()
                binding.editInspectionEditSaveButton.setImageResource(R.drawable.accept_icon)
                isEditable = true
            }
        }
        binding.editInspectionCancelButton.setOnClickListener {
            if (isEditable == true) {
                setComponentsToNotEditable()
                isEditable = false
            } else {
                parentFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .addToBackStack(null)
                    .commit()
            }

        }

        if (isEditable) {
            setComponentsToEditable()
            binding.editInspectionEditSaveButton.setImageResource(R.drawable.accept_icon)
        } else {
            setComponentsToNotEditable()
            binding.editInspectionEditSaveButton.setImageResource(R.drawable.edit_icon)
        }
    }


    /* ***************************** FUNCTIONS ************************************************** */

    private fun createDevice() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.createDeviceFlow(tempDevice).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                tempInspection.deviceId = result.data
                                tempDevice.deviceId = result.data
                                // createSignature()
                                createInspection()
                            }
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create device success")

                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                tempInspection.deviceId = result.data
                                tempDevice.deviceId = result.data
                            }
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create device loading")

                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create inspection error")
                        }
                    }
                }

            }
        }
    }
    private fun createInspection() {
        viewLifecycleOwner.lifecycleScope.launch() {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.createInspectionFlow(tempInspection).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Toast.makeText(requireContext(), "Inspection created!", Toast.LENGTH_SHORT).show()
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create inspection success")
                            if(result.data != null) {
                                //tempInspection.recipientSignatureId = result.data
                                inspectionId = result.data
                                tempInspection.inspectionId = result.data
                                createSignature()
                            }
                        }
                        ResourceState.LOADING -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create inspection loading")
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create inspection error")
                            Toast.makeText(requireContext(), "Inspection NOT created!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private fun createSignature() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                InspectionViewModel.createSignatureFlow(tempInspection.inspectionId, tempSignatureByteArray).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create signature success")
                            if(result.data != null ) {
                                //tempInspection.recipientSignatureId = result.data
                                //createInspection()
                            }
                        }
                        ResourceState.LOADING -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create signature loading")
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create signature error")
                        }
                    }
                }
            }
        }
    }

    private fun updateDevice() {
        viewLifecycleOwner.lifecycleScope.launch(){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.updateDeviceFlow(tempDevice).collect() {result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update device success")
                            updateInspection()
                        }
                        ResourceState.LOADING -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update device loading")
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update device error")
                        }
                    }
                }
            }
        }
    }
    private fun updateInspection() {
        viewLifecycleOwner.lifecycleScope.launch(){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.updateInspectionFlow(tempInspection).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update inspection success")
                            updateSignature()
                        }
                        ResourceState.LOADING -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update inspection loading")
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update inspection error")
                        }
                    }
                }
            }
        }
    }
    private fun updateSignature() {
        viewLifecycleOwner.lifecycleScope.launch(){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                InspectionViewModel.updateSignatureFlow(tempInspection.inspectionId, tempSignatureByteArray).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update signature success")
                            if (result.data != null) {
                                //tempInspection.recipientSignatureId = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update signature loading")
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Update signature error")
                        }
                    }
                }
            }
        }    }

    private fun setComponentsToNotEditable() {
        binding.editInspectionNameEditText.isEnabled = false
        binding.editInspectionNameEditText.isClickable = false
        binding.editInspectionNameEditText.isFocusableInTouchMode = false

        binding.editInspectionManufacturerEditText.isEnabled = false
        binding.editInspectionManufacturerEditText.isClickable = false
        binding.editInspectionManufacturerEditText.isFocusableInTouchMode = false

        binding.editInspectionModelEditText.isEnabled = false
        binding.editInspectionModelEditText.isClickable = false
        binding.editInspectionModelEditText.isFocusableInTouchMode = false

        binding.editInspectionSerialNumberEditText.isEnabled = false
        binding.editInspectionSerialNumberEditText.isClickable = false
        binding.editInspectionSerialNumberEditText.isFocusableInTouchMode = false

        binding.editInspectionInventoryNumberEditText.isEnabled = false
        binding.editInspectionInventoryNumberEditText.isClickable = false
        binding.editInspectionInventoryNumberEditText.isFocusableInTouchMode = false

        binding.editInspectionWardEditText.isEnabled = false
        binding.editInspectionWardEditText.isClickable = false
        binding.editInspectionWardEditText.isFocusableInTouchMode = false

        binding.editInspectionCommentEditText.isEnabled = false
        binding.editInspectionCommentEditText.isClickable = false
        binding.editInspectionCommentEditText.isFocusableInTouchMode = false

        binding.editInspectionInspectionStateSpinner.isEnabled = false
        binding.editInspectionInspectionStateSpinner.isClickable = false
        binding.editInspectionInspectionStateSpinner.isFocusable = false


        binding.editInspectionHospitalSpinner.isEnabled = false
        binding.editInspectionHospitalSpinner.isClickable = false
        binding.editInspectionHospitalSpinner.isFocusable = false

        binding.editInspectionEstStateSpinner.isEnabled = false
        binding.editInspectionEstStateSpinner.isClickable = false
        binding.editInspectionEstStateSpinner.isFocusable = false

        binding.editInspectionSignatureImageButton.isEnabled = false
        binding.editInspectionSignatureImageButton.isClickable = false
        binding.editInspectionSignatureImageButton.isFocusable = false

        binding.editInspectionInspectionDateButton.isEnabled = false
        binding.editInspectionInspectionDateButton.isClickable = false
        binding.editInspectionInspectionDateButton.isFocusable = false
    }
    private fun setComponentsToEditable() {
        binding.editInspectionNameEditText.isEnabled = true
        binding.editInspectionNameEditText.isClickable = true
        binding.editInspectionNameEditText.isFocusableInTouchMode = true

        binding.editInspectionManufacturerEditText.isEnabled = true
        binding.editInspectionManufacturerEditText.isClickable = true
        binding.editInspectionManufacturerEditText.isFocusableInTouchMode = true

        binding.editInspectionModelEditText.isEnabled = true
        binding.editInspectionModelEditText.isClickable = true
        binding.editInspectionModelEditText.isFocusableInTouchMode = true

        binding.editInspectionSerialNumberEditText.isEnabled = true
        binding.editInspectionSerialNumberEditText.isClickable = true
        binding.editInspectionSerialNumberEditText.isFocusableInTouchMode = true

        binding.editInspectionInventoryNumberEditText.isEnabled = true
        binding.editInspectionInventoryNumberEditText.isClickable = true
        binding.editInspectionInventoryNumberEditText.isFocusableInTouchMode = true

        binding.editInspectionWardEditText.isEnabled = true
        binding.editInspectionWardEditText.isClickable = true
        binding.editInspectionWardEditText.isFocusableInTouchMode = true

        binding.editInspectionCommentEditText.isEnabled = true
        binding.editInspectionCommentEditText.isClickable = true
        binding.editInspectionCommentEditText.isFocusableInTouchMode = true

        binding.editInspectionInspectionStateSpinner.isEnabled = true
        binding.editInspectionInspectionStateSpinner.isClickable = true
        binding.editInspectionInspectionStateSpinner.isFocusable = true

        binding.editInspectionHospitalSpinner.isEnabled = true
        binding.editInspectionHospitalSpinner.isClickable = true
        binding.editInspectionHospitalSpinner.isFocusable = true

        binding.editInspectionEstStateSpinner.isEnabled = true
        binding.editInspectionEstStateSpinner.isClickable = true
        binding.editInspectionEstStateSpinner.isFocusable = true


        binding.editInspectionInspectionDateButton.isEnabled = true
        binding.editInspectionInspectionDateButton.isClickable = true
        binding.editInspectionInspectionDateButton.isFocusable = true

        binding.editInspectionSignatureImageButton.isEnabled = true
        binding.editInspectionSignatureImageButton.isClickable = true
        binding.editInspectionSignatureImageButton.isFocusable = true
    }

    private fun updateTempInspection(){
        tempInspection.ward = binding.editInspectionWardEditText.text.toString()
        tempInspection.comment = binding.editInspectionCommentEditText.text.toString()

    }
    private fun updateTempDevice(){
        tempDevice.name = binding.editInspectionNameEditText.text.toString()
        tempDevice.manufacturer = binding.editInspectionManufacturerEditText.text.toString()
        tempDevice.model = binding.editInspectionModelEditText.text.toString()
        tempDevice.inventoryNumber = binding.editInspectionInventoryNumberEditText.text.toString()
        tempDevice.serialNumber = binding.editInspectionSerialNumberEditText.text.toString()
    }

    // Binding data functions
    private fun bindInspectionData(inspection: Inspection) {
        binding.editInspectionIdTextView.setText("ID: "+ inspection.inspectionId)
        binding.editInspectionWardEditText.setText(inspection.ward)
        binding.editInspectionCommentEditText.setText(inspection.comment)
        if(inspection.inspectionDate != "") {
            binding.editInspectionInspectionDateButton.setText(Helper.getDateString(inspection.inspectionDate.toLong()))
        }
    }
    private fun bindDeviceData(device: Device) {
        binding.editInspectionManufacturerEditText.setText(device.manufacturer)
        binding.editInspectionModelEditText.setText(device.model)
        binding.editInspectionNameEditText.setText(device.name)
        binding.editInspectionSerialNumberEditText.setText(device.serialNumber)
        binding.editInspectionInventoryNumberEditText.setText(device.inventoryNumber)
    }
    private fun bindInspectionStateSpinner(inspection: Inspection) {
        var position = 0
        for (item in inspectionStateList) {
            if (inspection.inspectionStateId == item.inspectionStateId) {
                binding.editInspectionInspectionStateSpinner.setSelection(position)
            }
            position += 1

        }
    }
    private fun bindHospitalListSpinner(inspection: Inspection) {
        var position = 0
        for (item in hospitalList) {
            if (inspection.hospitalId == item.hospitalId) {
                binding.editInspectionHospitalSpinner.setSelection(position)
            }
            position += 1

        }
    }
    private fun bindEstStateSpinner(inspection: Inspection) {
        var position = 0
        for (item in estStateList) {
            if (inspection.estStateId == item.estStateId) {
                binding.editInspectionEstStateSpinner.setSelection(position)
            }
            position += 1
        }

    }
    private fun bindSignatureImageButton(bytes: ByteArray) {
        binding.editInspectionSignatureImageButton.setImageBitmap(convertByteArrayToBitmap(bytes))
    }

    // Components initialization
    private fun initEstStateSpinner() {
        spinnerEstStateList.clear()
        for (item in estStateList) {
            spinnerEstStateList.add(item.estState)
        }
        val estStateListArrayAdapter =
            activity?.baseContext?.let { it ->
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    spinnerEstStateList
                )
            }
        binding.editInspectionEstStateSpinner.adapter =
            estStateListArrayAdapter
    }
    private fun initInspectionStateSpinner() {
        spinnerInspectionStateList.clear()
        for (item in inspectionStateList) {
            spinnerInspectionStateList.add(item.inspectionState)
        }
        val inspectionStateListArrayAdapter = activity?.baseContext?.let { it ->
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                spinnerInspectionStateList
            )
        }
        binding.editInspectionInspectionStateSpinner.adapter = inspectionStateListArrayAdapter

    }
    private fun initHospitalListSpinner() {
        spinnerHospitalList.clear()
        for (item in hospitalList) {
            item.hospitalName.let {
                spinnerHospitalList.add(item.hospitalName)
            }
        }
        val hospitalListArrayAdapter = activity?.baseContext?.let { it ->
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                spinnerHospitalList
            )
        }
        binding.editInspectionHospitalSpinner.adapter = hospitalListArrayAdapter
    }

    // Other
    private fun convertByteArrayToBitmap(bytes: ByteArray): Bitmap {
        val options = BitmapFactory.Options()
        options.inMutable = true
        val bmp = BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size,
            options
        )
        return bmp
    }

    // Not in use now
    private fun bindData(){
        bindDeviceData(device)
        bindInspectionData(inspection)
        bindEstStateSpinner(inspection)
        bindHospitalListSpinner(inspection)
        bindSignatureImageButton(signatureByteArray)
        bindInspectionStateSpinner(inspection)
        //binding.editInspectionInspectioningDateButton.setText(getDateString(tempInspection.inspectioningDate.toLong()))
        //binding.editInspectionOpeningDateButton.setText(getDateString(tempInspection.openingDate.toLong()))
    }

}

