package com.adrpien.tiemed.presentation.feature_repairs.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.R
import com.adrpien.tiemed.presentation.feature_users.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.core.date_picker_dialog.RepairDatePickerDialog
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.core.signature_dialog.SignatureDialog
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*


class EditRepairFragment : BaseFragment(), DatePickerDialog.OnDateSetListener,
    onRepairItemClickListener,
    AdapterView.OnItemSelectedListener {

    // ViewBinding
    private var _binding: FragmentEditRepairBinding? = null
    private val binding
        get() = _binding!!

    // ViewModel
    val repairViewModel by viewModels<RepairViewModel>()

    private val REPAIR_UPDATE_TAG = "INSPECTION UPDATE TAG"
    private val SIGNATURE_DIALOG_TAG = "SIGNATURE DIALOG TAG"
    private var REQUEST_IMAGE_CAPTURE: Int = 0

    private var spinnerHospitalList = mutableListOf<String>()
    private var spinnerRepairStateList = mutableListOf<String>()
    private var spinnerEstStateList = mutableListOf<String>()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    // Repair id
    private var repairId: String = ""

    // temp data
    private lateinit var tempRepair: Repair
    private lateinit var tempSignatureByteArray: ByteArray
    private lateinit var tempDevice: Device
    private lateinit var tempPhotoByteArray: ByteArray


    // Fetched data
    private lateinit var hospitalList: List<Hospital>
    private lateinit var repair: Repair
    private lateinit var estStateList: List<EstState>
    private lateinit var repairStateList: List<RepairState>
    private lateinit var device: Device
    private lateinit var signatureByteArray: ByteArray


    // Init
    init{
        setHasOptionsMenu(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoResultLauncher()

        // Get repair uid if passed in bundle
        repairId = arguments?.getString("repairId", "") ?: ""

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repairViewModel.getRepairFlow(repairId).collect { result ->
                    when(result.resourceState) {
                        ResourceState.ERROR -> {
                            repair = result.data ?: Repair(repairId = "")
                            tempRepair = repair
                        }
                        ResourceState.SUCCESS -> {
                            repair = result.data ?: Repair(repairId = "")
                            tempRepair = repair
                        }
                        ResourceState.LOADING -> {
                            repair = result.data ?: Repair(repairId = "")
                            tempRepair = repair
                        }
                    }
                }
                repairViewModel.getHospitalListFlow().collect { result ->
                    when(result.resourceState) {
                        ResourceState.ERROR -> {
                        Toast.makeText(context, "Fetching hospital list error ", Toast.LENGTH_SHORT).show()
                        }
                        ResourceState.SUCCESS -> {
                            // Hospital spinner implementation
                            hospitalList = result.data ?: emptyList()

                        }
                        ResourceState.LOADING -> {
                        }
                    }
                    for (item in hospitalList){
                    item.name.let {
                        spinnerHospitalList.add(item.name)
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
                repairViewModel.getSignatureFlow(repair.repairId).collect{ result ->
                    signatureByteArray = result.data?: byteArrayOf()
                    tempSignatureByteArray = signatureByteArray
                    bindSignatureImageButton(tempSignatureByteArray)
                }
                repairViewModel.getrepairStateListFlow().collect{ result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            repairStateList = result.data ?: emptyList()
                        }
                        ResourceState.LOADING -> {
                            repairStateList = result.data ?: emptyList()
                        }
                        ResourceState.ERROR -> {
                            repairStateList = emptyList()
                            Toast.makeText(context, "Fetching repair state list error", Toast.LENGTH_SHORT)

                        }
                    }
                    for (item in hospitalList){
                        item.name.let {
                            spinnerRepairStateList.add(item.name)
                        }
                    }
                    val repairStateListArrayAdapter = activity?.baseContext?.let { it ->
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            spinnerRepairStateList
                        )
                    }
                    binding.editRepairHospitalSpinner.adapter = repairStateListArrayAdapter

                }
                repairViewModel.getDeviceFlow(repair.deviceId).collect{ result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            device = result.data ?: Device("")
                            tempDevice = device
                        }
                        ResourceState.LOADING -> {
                            device = result.data ?: Device("")
                            tempDevice = device
                        }
                        ResourceState.ERROR -> {
                            device = result.data ?: Device("")
                            tempDevice = device
                            Toast.makeText(context, "Fetching repair state list error", Toast.LENGTH_SHORT)
                        }
                    }
                    bindRepairData(tempRepair, tempDevice )
                }
                repairViewModel.getEstStateListFlow().collect{ result ->
                    when(result.resourceState) {
                        ResourceState.SUCCESS -> {
                            estStateList = result.data ?: emptyList()
                        }
                        ResourceState.LOADING -> {
                            estStateList = result.data ?: emptyList()
                        }
                        ResourceState.ERROR -> {
                            estStateList = emptyList()
                            Toast.makeText(context, "Fetching est state list error", Toast.LENGTH_SHORT)

                        }
                    }
                    for (item in hospitalList){
                        item.name.let {
                            spinnerEstStateList.add(item.name)
                        }
                    }
                    val estStateListArrayAdapter = activity?.baseContext?.let { it ->
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            spinnerEstStateList
                        )
                    }
                    binding.editRepairHospitalSpinner.adapter = estStateListArrayAdapter

                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditRepairBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make photo button implementation
        binding.editRepairMakePhotoButton.setOnClickListener {
            takePicture()
        }

        // Add photo button implementation
        binding.editRepairAddPhotoButton.setOnClickListener {
            addPicture()
        }

        // Showing Id is unnecessary
        // Setting TextViews with values
        // binding.editRepairIdInputEditText.setText(arguments?.getString("id"))

        // Setting openingButton listener
        binding.editRepairOpeningDateButton.setOnClickListener {
             // Create TimePicker
             val dialog = RepairDatePickerDialog()
             // show MyTimePicker
             dialog.show(childFragmentManager, "repair_time_picker")
         }

        // TODO Add parts button implementation

        // TODO Opening date button implementation

        // TODO closing date implementation

        // TODO Parts recycerview implementation

        // EST spinner implementation
        binding.editRepairESTRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            tempRepair.estStateId= group.findViewById<AppCompatRadioButton>(checkedId).text.toString()
                .uppercase()
                .replace(" ", "_")

        }

        // TODO State spinner implementation

        binding.editRepairHospitalSpinner.onItemSelectedListener = this

        // Signature image button implementation
        binding.editRepairSignatureImageButton.setOnClickListener {

            // Open SignatureDialog when signatureImageButtonClicked
            val dialog = SignatureDialog()

            // Dialog fragment result listener
            // Saves ByteArray stored in bundle and converts to Bitmap; sets this bitmap as signatureImageButton image
            requireActivity().supportFragmentManager.setFragmentResultListener(
                getString(R.string.signature_request_key),
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
                    binding.editRepairSignatureImageButton.setImageBitmap(bmp)
                })
            // show MyTimePicker
            dialog.show(childFragmentManager, SIGNATURE_DIALOG_TAG)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.edit_repair_options_menu, menu)

        // Setting action bar title
        setActionBarTitle("Edit Repair")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId){
        R.id.saveRepairItem -> {
            if (arguments?.getString("id") != null){
                //viewModelProvider.updateRepair(mapOf("id" to binding.idEditText.text.toString()))
                repairViewModel.updateRepairFlow(tempRepair)
            }
            else {
                repairViewModel.createRepairFlow(tempRepair)
            }
            return true
        }
        else -> super.onOptionsItemSelected(item)
    }
    }

    // Implementing DatePickerDialog.OnDateSetListener in fragment to use ViewModel, which is not in adapter
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Set values
        var date: Calendar = Calendar.getInstance()
        date.set(year, month, dayOfMonth)

        // Set button text
        tempRepair.openingDate = date.timeInMillis.toString()
        binding.editRepairOpeningDateButton.setText(getDateString(date.timeInMillis))    }


    // Repair row click implementation
    override fun setOnRepairItemClick(itemView: View) {
        TODO("Not yet implemented")
    }

    // Repair edit button click implementation
    override fun setOnEditRepairButtonClick(itemView: View) {
        TODO("Not yet implemented")
    }

    // Repair view button click implementation
    override fun setOnViewRepairButtonClick(itemView: View) {
        TODO("Not yet implemented")
    }

    /* ************************** extracted functions ******************************************* */
    // Bind all textviews, spinners, dates and radiogroups
    private fun bindRepairData(repair: Repair, device:Device) {
        binding.editRepairIdInputEditText.setText(tempRepair.repairId)
        binding.editRepairNameEditText.setText(device.name)
        binding.editRepairManufacturerEditText.setText(device.manufacturer)
        binding.editRepairModelEditText.setText(device.model)
        binding.editRepairInventoryNumberEditText.setText(device.inventoryNumber)
        binding.editRepairSerialNumberEditText.setText(device.serialNumber)
        binding.editRepairWardTextInputEditText.setText(tempRepair.ward)
        binding.editRepairDefectDescriptionTextInputEditText.setText(tempRepair.defectDescription)
        binding.editRepairRepairDescriptionTextInputEditText.setText(tempRepair.repairDescription)
        binding.editRepairRepairingDateButton.setText(getDateString(tempRepair.repairingDate.toLong()))
        binding.editRepairOpeningDateButton.setText(getDateString(tempRepair.openingDate.toLong()))

        bindEstRadioButton(tempRepair)
        bindHospitalSpinner(tempRepair)



    }

    // Binding Hospital spinner with repair selection
    private fun bindHospitalSpinner(repair: Repair) {
        var position = 0
        var selection = 1
        for (item in spinnerHospitalList) {
            if (repair.hospitalId == item.toString()) {
                position = selection
            }
            selection += 1
        }
        binding.editRepairHospitalSpinner.setSelection(position)
    }

    // takePicture implementation
    /*
    Funtion opens camera using intent and returns result as ByteArray
     */
    private fun takePicture():  ByteArray{
        REQUEST_IMAGE_CAPTURE = 1
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
        return tempPhotoByteArray
    }

    // taddPicture implementation
    /*
    Function opens gallery and returns result as ByteArray
     */
    private fun addPicture(): ByteArray {
        REQUEST_IMAGE_CAPTURE = 2
        val addPictureIntent = Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(addPictureIntent)
        return tempPhotoByteArray
    }

    // Checks if photo is taken by camera or taken from gallery and saves it to ByteArray
    private fun   photoResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (REQUEST_IMAGE_CAPTURE == 1) {
                        val tempPhotoOutputStream = ByteArrayOutputStream()
                        val data: Intent? = result.data
                        val photoBitmap = data?.extras?.get("data") as Bitmap
                        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, tempPhotoOutputStream)
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

    // Binding data of inspection in state spinner
    private fun bindEstRadioButton(repair: Repair) {
        var position = 0
        var selection = 0
        for (item in estStateList) {
            if (repair.estStateId == item.toString()) {
                position = selection
            }
            selection += 1
        }
        binding.editRepairESTRadioGroup.check(binding.editRepairESTRadioGroup.getChildAt(position).id)
    }

    // Bind signature image button
    private fun bindSignatureImageButton(bytes: ByteArray){
        val options = BitmapFactory.Options()
        options.inMutable = true
        val bmp = BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size,
            options)
        binding.editRepairSignatureImageButton.setImageBitmap(bmp)
    }

    // Hospital spinner implementation - onItemSelected
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.id == R.id.inspectionHospitalSpinner){
            tempRepair.hospitalId = parent.getItemAtPosition(position).toString()
        }    }

    // Hospital spinner implementation - onNothingSelected
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}