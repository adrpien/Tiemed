package com.adrpien.tiemed.presentation.feature_repairs.ui

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
import com.adrpien.tiemed.core.dialogs.date.RepairDatePickerDialog
import com.adrpien.tiemed.core.dialogs.signature_dialog.SignatureDialog
import com.adrpien.tiemed.core.util.Helper.Companion.getDateString
import com.adrpien.tiemed.databinding.FragmentRepairEditBinding
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

@AndroidEntryPoint
class EditRepairFragment() : Fragment() {

    // ViewBinding
    private var _binding: FragmentRepairEditBinding? = null
    private val binding
        get() = _binding!!

    // ViewModel
    val RepairViewModel by viewModels<RepairViewModel>()

    private val EDIT_REPAIR_FRAGMENT = "EDIT_REPAIR_FRAGMENT"
    private val REPAIR_UPDATE_TAG = "INSPECTION UPDATE TAG"
    private val SIGNATURE_DIALOG_TAG = "SIGNATURE DIALOG TAG"
    private var REQUEST_IMAGE_CAPTURE: Int = 0

    private var spinnerHospitalList = mutableListOf<String>()
    private var spinnerRepairStateList = mutableListOf<String>()
    private var spinnerEstStateList = mutableListOf<String>()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var repairId: String = ""
    private var deviceId: String = ""

    // Temp data
    private var tempRepair = Repair()
    private var tempDevice = Device()
    private var tempSignatureByteArray = byteArrayOf()
    private var tempPhotoByteArray = byteArrayOf()

    private var isEditable = false

    // Fetched data
    private var hospitalList = listOf<Hospital>()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                RepairViewModel.updateRoomHospitalListFlow(value)
                initHospitalListSpinner()
                if (tempRepair.hospitalId != "") { bindHospitalListSpinner(tempRepair) }
            }
        }
    private var estStateList = listOf<EstState>()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                RepairViewModel.updateRoomEstStateListFlow(value)
                initEstStateGroupButton()
                if(tempRepair.estStateId != ""){ bindRepairStateSpinner(tempRepair)}
            }
        }
    private var repairStateList = listOf<RepairState>()
        set(value) {
            if(value.isNotEmpty()) {
                field = value
                RepairViewModel.updateRoomRepairStateListFlow(value)
                initRepairStateSpinner()
                if(tempRepair.repairStateId != "") { bindRepairStateSpinner(tempRepair) }
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
    private var repair = Repair()
        set(value) {
            if(value.repairId != "") {
                field =value
                tempRepair = value
                bindRepairData(tempRepair)
                if(estStateList.isNotEmpty()){ bindEstStateSpinner(tempRepair)}
                if(hospitalList.isNotEmpty()) { bindHospitalListSpinner(tempRepair)}
                if(repairStateList.isNotEmpty()){ bindRepairStateSpinner(tempRepair)}
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
            if (parent?.id == R.id.editRepairHospitalSpinner) {
                val hospitalName = parent.getItemAtPosition(position).toString()
                val hospital = hospitalList.find { it.hospitalName == hospitalName }
                if (hospital != null) {
                    tempRepair.hospitalId = hospital.hospitalId
                }
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
            // lave empty here, i think
        }
    }
    val repairStateSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            p1: View?,
            position: Int,
            p3: Long
        ) {
            if (parent?.id == R.id.editRepairRepairStateSpinner) {
                val repairStateName = parent.getItemAtPosition(position).toString()
                val repairState = repairStateList.find { it.repairState == repairStateName }
                if(repairState != null){
                    tempRepair.repairStateId = repairState.repairStateId
                }
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
            // leave empty here, i think
        }
    }
    val repairEstStateSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            p1: View?,
            position: Int,
            p3: Long
        ) {
            if (parent?.id == R.id.editRepairEstStateSpinner) {
                val estStateName = parent.getItemAtPosition(position).toString()
                val estState = estStateList.find { it.estState == estStateName }
                if(estState != null){
                    tempRepair.estStateId = estState.estStateId
                }
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
            // lave empty here, i think
        }
    }
    val datePickerDialogListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            var date: Calendar = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            tempRepair.openingDate = date.timeInMillis.toString()
            binding.editRepairOpeningDateButton.setText(getDateString(date.timeInMillis))
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
        repairId = arguments?.getString("repairId", "") ?: ""
        deviceId = arguments?.getString("deviceId", "") ?: ""

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRepairEditBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* *************************** COLLECT STATE FLOW ****************************************** */
        if (repairId != "") {
            // repair
            viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.getRepairFlow(repairId).collect { result ->
                    when (result.resourceState) {
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Repair collecting error")
                        }
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                    repair = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                repair = result.data
                            }
                        }
                    }
                }
            }
        }
            // device
            viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.getDeviceFlow(deviceId).collect { result ->
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
                RepairViewModel.getSignatureFlow(repair.recipientSignatureId).collect { result ->
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
                            Log.d(EDIT_REPAIR_FRAGMENT, "Signature collectin error")
                        }

                    }
                }
            }
        }
        }
        // hospitalList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    RepairViewModel.getHospitalListFlow().collect { result ->
                        when (result.resourceState) {
                            ResourceState.ERROR -> {
                                Log.d(EDIT_REPAIR_FRAGMENT, "Hospital list collecting error")
                            }
                            ResourceState.SUCCESS -> {
                                if (result.data != null) {
                                    hospitalList = result.data
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
        // repairStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.getRepairStateListFlow().collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                repairStateList = result.data
                            }
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Repair state list collecting error")
                        }
                    }

                }
            }
        }
        // estStateList
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.getEstStateListFlow().collect { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                estStateList = result.data
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
        binding.editRepairOpeningDateButton.setOnClickListener {
            val dialog = RepairDatePickerDialog()
            dialog.show(childFragmentManager, "repair_time_picker")
        }
        binding.editRepairHospitalSpinner.onItemSelectedListener = hospitalSpinnerListener
        binding.editRepairSignatureImageButton.setOnClickListener {
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
        binding.editRepairRepairStateSpinner.onItemSelectedListener = repairStateSpinnerListener
        binding.editRepairEstStateSpinner.onItemSelectedListener = repairEstStateSpinnerListener
        binding.editRepairEditSaveButton.setOnClickListener {
            if(isEditable == true) {
                updateTempDevice()
                updateTempRepair()
                if (repairId != "") {
                    updateDevice()
                    updateRepair()
                    updateSignature()
                } else {
                    // TODO how to wait for deviceID ???????
                    createDevice()
                    createSignature()
                }
                setComponentsToNotEditable()
                binding.editRepairEditSaveButton.setImageResource(R.drawable.edit_icon)
                isEditable = false
            } else {
                setComponentsToEditable()
                binding.editRepairEditSaveButton.setImageResource(R.drawable.accept_icon)
                isEditable = true
            }
        }
        binding.editRepairCancelButton.setOnClickListener {
            if(isEditable == true ) {
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

        //  TODO Make photo button implementation
        /*
        binding.editRepairMakePhotoButton.setOnClickListener {
        takePicture()
        }
        */
        // TODO Add photo button implementation
        /*binding.editRepairAddPhotoButton.setOnClickListener {
        addPicture()
        }*/

        setComponentsToNotEditable() }

    /* ***************************** FUNCTIONS ************************************************** */

    private fun createDevice() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.createDeviceFlow(tempDevice).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            if (result.data != null) {
                                tempRepair.deviceId = result.data
                                tempDevice.deviceId = result.data
                                createRepair()
                            }
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create device success")

                        }
                        ResourceState.LOADING -> {
                            if (result.data != null) {
                                tempRepair.deviceId = result.data
                                tempDevice.deviceId = result.data
                            }
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create device loading")

                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create repair error")
                        }
                    }
                }

            }
        }
    }
    private fun createRepair() {
        viewLifecycleOwner.lifecycleScope.launch() {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.createRepairFlow(tempRepair).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Toast.makeText(requireContext(), "Repair created!", Toast.LENGTH_SHORT).show()
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create repair success")
                            if(result.data != null) {
                                tempRepair.recipientSignatureId = result.data
                                tempRepair.repairId = result.data
                                createSignature()
                            }
                        }
                        ResourceState.LOADING -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create repair loading")
                        }
                        ResourceState.ERROR -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create repair error")
                            Toast.makeText(requireContext(), "Repair NOT created!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private fun createSignature() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                RepairViewModel.createSignatureFlow(tempRepair.repairId, tempSignatureByteArray).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create signature success")
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
                RepairViewModel.updateDeviceFlow(tempDevice).collect() {
                }
            }
        }
    }
    private fun updateRepair() {
        viewLifecycleOwner.lifecycleScope.launch(){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.updateRepairFlow(tempRepair).collect() {
                }
            }
        }
    }
    private fun updateSignature() {
        viewLifecycleOwner.lifecycleScope.launch(){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RepairViewModel.updateSignatureFlow(tempRepair.repairId, tempSignatureByteArray).collect() { result ->
                    when (result.resourceState) {
                        ResourceState.SUCCESS -> {
                            Log.d(EDIT_REPAIR_FRAGMENT, "Create signature success")
                            if (result.data != null) {
                                tempRepair.recipientSignatureId = result.data
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
        }    }

    private fun setComponentsToNotEditable() {
        binding.editRepairOpeningDateButton.isEnabled = false
        binding.editRepairOpeningDateButton.isClickable = false
        binding.editRepairOpeningDateButton.isFocusableInTouchMode = false

        binding.editRepairNameEditText.isEnabled = false
        binding.editRepairNameEditText.isClickable = false
        binding.editRepairNameEditText.isFocusableInTouchMode = false
        binding.editRepairManufacturerEditText.isEnabled = false
        binding.editRepairManufacturerEditText.isClickable = false
        binding.editRepairManufacturerEditText.isFocusableInTouchMode = false
        binding.editRepairModelEditText.isEnabled = false
        binding.editRepairModelEditText.isClickable = false
        binding.editRepairModelEditText.isFocusableInTouchMode = false
        binding.editRepairSerialNumberEditText.isEnabled = false
        binding.editRepairSerialNumberEditText.isClickable = false
        binding.editRepairSerialNumberEditText.isFocusableInTouchMode = false
        binding.editRepairInventoryNumberEditText.isEnabled = false
        binding.editRepairInventoryNumberEditText.isClickable = false
        binding.editRepairInventoryNumberEditText.isFocusableInTouchMode = false

        binding.editRepairWardEditText.isEnabled = false
        binding.editRepairWardEditText.isClickable = false
        binding.editRepairWardEditText.isFocusableInTouchMode = false

        binding.editRepairDefectDescriptionEditText.isEnabled = false
        binding.editRepairDefectDescriptionEditText.isClickable = false
        binding.editRepairDefectDescriptionEditText.isFocusableInTouchMode = false

        binding.editRepairRepairDescriptionEditText.isEnabled = false
        binding.editRepairRepairDescriptionEditText.isClickable = false
        binding.editRepairRepairDescriptionEditText.isFocusableInTouchMode = false

        binding.editRepairRepairingDateButton.isEnabled = false
        binding.editRepairRepairingDateButton.isClickable = false
        binding.editRepairRepairingDateButton.isFocusableInTouchMode = false

        binding.editRepairPartDescriptionEditText.isEnabled = false
        binding.editRepairPartDescriptionEditText.isClickable = false
        binding.editRepairPartDescriptionEditText.isFocusableInTouchMode = false

        binding.editRepairRepairStateSpinner.isEnabled = false
        binding.editRepairRepairStateSpinner.isClickable = false
        binding.editRepairRepairStateSpinner.isFocusableInTouchMode = false

        binding.editRepairEstStateSpinner.isEnabled = false
        binding.editRepairEstStateSpinner.isClickable = false
        binding.editRepairEstStateSpinner.isFocusableInTouchMode = false

    }
    private fun setComponentsToEditable() {
        // TODO Signature not editable to implement
        // TODO Spinners not editable bug to reapir
        binding.editRepairOpeningDateButton.isEnabled = true
        binding.editRepairOpeningDateButton.isClickable = true
        binding.editRepairOpeningDateButton.isFocusable = true
        binding.editRepairNameEditText.isEnabled = true
        binding.editRepairNameEditText.isClickable = true
        binding.editRepairNameEditText.isFocusableInTouchMode = true
        binding.editRepairManufacturerEditText.isEnabled = true
        binding.editRepairManufacturerEditText.isClickable = true
        binding.editRepairManufacturerEditText.isFocusableInTouchMode = true
        binding.editRepairModelEditText.isEnabled = true
        binding.editRepairModelEditText.isClickable = true
        binding.editRepairModelEditText.isFocusableInTouchMode = true
        binding.editRepairSerialNumberEditText.isEnabled = true
        binding.editRepairSerialNumberEditText.isClickable = true
        binding.editRepairSerialNumberEditText.isFocusableInTouchMode = true
        binding.editRepairInventoryNumberEditText.isEnabled = true
        binding.editRepairInventoryNumberEditText.isClickable = true
        binding.editRepairInventoryNumberEditText.isFocusableInTouchMode = true
        binding.editRepairWardEditText.isEnabled = true
        binding.editRepairWardEditText.isClickable = true
        binding.editRepairWardEditText.isFocusableInTouchMode = true
        binding.editRepairDefectDescriptionEditText.isEnabled = true
        binding.editRepairDefectDescriptionEditText.isClickable = true
        binding.editRepairDefectDescriptionEditText.isFocusableInTouchMode = true
        binding.editRepairRepairDescriptionEditText.isEnabled = true
        binding.editRepairRepairDescriptionEditText.isClickable = true
        binding.editRepairRepairDescriptionEditText.isFocusableInTouchMode = true
        binding.editRepairRepairingDateButton.isEnabled = true
        binding.editRepairRepairingDateButton.isClickable = true
        binding.editRepairRepairingDateButton.isFocusableInTouchMode = true
        binding.editRepairPartDescriptionEditText.isEnabled = true
        binding.editRepairPartDescriptionEditText.isClickable = true
        binding.editRepairPartDescriptionEditText.isFocusableInTouchMode = true
        binding.editRepairRepairStateSpinner.isEnabled = true
        binding.editRepairRepairStateSpinner.isClickable = true
        binding.editRepairRepairStateSpinner.isFocusableInTouchMode = true
        binding.editRepairEstStateSpinner.isEnabled = true
        binding.editRepairEstStateSpinner.isClickable = true
        binding.editRepairEstStateSpinner.isFocusableInTouchMode = true
    }

    private fun updateTempRepair(){
        tempRepair.ward = binding.editRepairWardEditText.text.toString()
        tempRepair.defectDescription = binding.editRepairDefectDescriptionEditText.text.toString()
        tempRepair.repairDescription = binding.editRepairRepairDescriptionEditText.text.toString()
        tempRepair.partDescription = binding.editRepairPartDescriptionEditText.text.toString()
    }
    private fun updateTempDevice(){
        tempDevice.name = binding.editRepairNameEditText.text.toString()
        tempDevice.manufacturer = binding.editRepairManufacturerEditText.text.toString()
        tempDevice.model = binding.editRepairModelEditText.text.toString()
        tempDevice.inventoryNumber = binding.editRepairInventoryNumberEditText.text.toString()
        tempDevice.serialNumber = binding.editRepairSerialNumberEditText.text.toString()
    }

    // Binding data functions
    private fun bindRepairData(repair: Repair) {
        binding.editRepairIdTextView.setText("ID: "+ repair.repairId)
        binding.editRepairWardEditText.setText(repair.ward)
        binding.editRepairDefectDescriptionEditText.setText(repair.defectDescription)
        binding.editRepairRepairDescriptionEditText.setText(repair.repairDescription)
        binding.editRepairPartDescriptionEditText.setText(repair.partDescription)
    }
    private fun bindDeviceData(device: Device) {
        binding.editRepairManufacturerEditText.setText(device.manufacturer)
        binding.editRepairModelEditText.setText(device.model)
        binding.editRepairNameEditText.setText(device.name)
        binding.editRepairSerialNumberEditText.setText(device.serialNumber)
        binding.editRepairInventoryNumberEditText.setText(device.inventoryNumber)
    }
    private fun bindRepairStateSpinner(repair: Repair) {
        var position = 0
        for (item in repairStateList) {
            if (repair.repairStateId == item.repairStateId) {
                binding.editRepairRepairStateSpinner.setSelection(position)
            }
            position += 1

        }
    }
    private fun bindHospitalListSpinner(repair: Repair) {
        var position = 0
        for (item in hospitalList) {
            if (repair.hospitalId == item.hospitalId) {
                binding.editRepairHospitalSpinner.setSelection(position)
            }
            position += 1

        }
    }
    private fun bindEstStateSpinner(repair: Repair) {
        var position = 0
        for (item in estStateList) {
            if (repair.estStateId == item.estStateId) {
                binding.editRepairEstStateSpinner.setSelection(position)
            }
            position += 1
        }

    }
    // TODO HERE DOESN'T WORK
    private fun bindSignatureImageButton(bytes: ByteArray) {
        // binding.editRepairSignatureImageButton.setImageBitmap(convertByteArrayToBitmap(bytes))
    }

    // Components initialization
    private fun initEstStateGroupButton() {
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
        binding.editRepairEstStateSpinner.adapter =
            estStateListArrayAdapter
    }
    private fun initRepairStateSpinner() {
        for (item in repairStateList) {
                spinnerRepairStateList.add(item.repairState)
        }
        val repairStateListArrayAdapter = activity?.baseContext?.let { it ->
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                spinnerRepairStateList
            )
        }
        binding.editRepairRepairStateSpinner.adapter = repairStateListArrayAdapter

    }
    private fun initHospitalListSpinner() {
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
        binding.editRepairHospitalSpinner.adapter = hospitalListArrayAdapter
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
    private fun takePicture(): ByteArray {
        REQUEST_IMAGE_CAPTURE = 1
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
        return tempPhotoByteArray
    }
    private fun addPicture(): ByteArray {
        REQUEST_IMAGE_CAPTURE = 2
        val addPictureIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultLauncher.launch(addPictureIntent)
        return tempPhotoByteArray
    }
    private fun photoResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (REQUEST_IMAGE_CAPTURE == 1) {
                        val tempPhotoOutputStream = ByteArrayOutputStream()
                        val data: Intent? = result.data
                        val photoBitmap = data?.extras?.get("data") as Bitmap
                        photoBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            tempPhotoOutputStream
                        )
                        tempPhotoByteArray = tempPhotoOutputStream.toByteArray()
                    }
                    if (REQUEST_IMAGE_CAPTURE == 2) {
                        val data = result.data
                        val uri = data?.data
                        val inputStream =
                            uri?.let { activity?.contentResolver?.openInputStream(it) }
                        val bufferSize = 1024
                        val buffer = ByteArray(bufferSize)
                        val tempPhotoOutputStream = ByteArrayOutputStream()
                        var len = 0
                        while (inputStream!!.read(buffer).also { len = it } != -1) {
                            tempPhotoOutputStream.write(buffer, 0, len)
                        }
                        tempPhotoByteArray = tempPhotoOutputStream.toByteArray()
                    }

                }
            }
    }
    private fun bindData(){
        bindDeviceData(device)
        bindRepairData(repair)
        bindEstStateSpinner(repair)
        bindHospitalListSpinner(repair)
        bindSignatureImageButton(signatureByteArray)
        bindRepairStateSpinner(repair)
        //binding.editRepairRepairingDateButton.setText(getDateString(tempRepair.repairingDate.toLong()))
        //binding.editRepairOpeningDateButton.setText(getDateString(tempRepair.openingDate.toLong()))
    }

}
