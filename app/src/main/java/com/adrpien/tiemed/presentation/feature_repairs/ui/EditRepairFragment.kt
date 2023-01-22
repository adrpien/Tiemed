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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.adrpien.tiemed.R
import com.adrpien.tiemed.presentation.feature_users.onRepairItemClickListener
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.datamodels.ESTState
import com.adrpien.tiemed.core.date_picker_dialog.RepairDatePickerDialog
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.core.signature_dialog.SignatureDialog
import com.adrpien.tiemed.domain.model.Hospital
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
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
    val viewModelProvider by viewModels<RepairViewModel>()

    // UID
    private var uid: String? = null

    // tempRepairByteArray
    private var tempRepair: Repair = Repair()

    // tempSignatureByteArray
    private var tempSignatureByteArray = byteArrayOf()

    // tempPhotoByte Array
    private var tempPhotoByteArray = byteArrayOf()

    private val REPAIR_UPDATE_TAG = "INSPECTION UPDATE TAG"
    private  val SIGNATURE_DIALOG_TAG = "SIGNATURE DIALOG TAG"

    private lateinit var hospitalList: MutableLiveData<MutableList<Hospital>>
    private lateinit var repair: MutableLiveData<Repair>
    private lateinit var signature: MutableLiveData<ByteArray>

    private var spinnerHospitalList = mutableListOf<String>()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    private var REQUEST_IMAGE_CAPTURE: Int = 0


    // Init
    init{
        setHasOptionsMenu(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoResultLauncher()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditRepairBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get repair uid if passed in bundle
        uid = arguments?.getString("uid", null)
        // Fill fields with record data if
        if(uid != null) {
            repair = viewModelProvider.getRepair(uid!!)
            viewModelProvider.getRepair(uid!!).observe(viewLifecycleOwner) { repair ->
                // Filling fields with data of opened record (when record is updated)
                bindRepairData(repair)
            }
        }

        // Make photo button implementation
        binding.editRepairMakePhotoButton.setOnClickListener {
            takePicture()
        }

        // Add photo button implementation
        binding.editRepairAddPhotoButton.setOnClickListener {
            addPicture()
        }

        // Setting TextViews with values
        binding.editRepairIdInputEditText.setText(arguments?.getString("id"))

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
            tempRepair.electricalSafetyTestString= group.findViewById<AppCompatRadioButton>(checkedId).text.toString()
                .uppercase()
                .replace(" ", "_")

        }

        // TODO State spinner implementation

        // Hospital spinner implementation
        hospitalList = viewModelProvider.getHospitalList()
        viewModelProvider.getHospitalList().observe(viewLifecycleOwner) {
            for (item in it) {
                item.name.let {
                    spinnerHospitalList.add(item.name)
                }
            }
            var hospitalListArrayAdapter = activity?.baseContext?.let { it ->
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    spinnerHospitalList
                )
            }
            binding.editRepairHospitalSpinner.adapter = hospitalListArrayAdapter
        }
        binding.editRepairHospitalSpinner.onItemSelectedListener = this

        // Signature image button implementation
        if(uid != null) {
            signature = viewModelProvider.getSignature(uid!!)
            viewModelProvider.getSignature(uid!!).observe(viewLifecycleOwner) { bytes ->
                bindSignatureImageButton(bytes)
            }
        }
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
            if (arguments?.getString("uid") != null){
                //viewModelProvider.updateRepair(mapOf("id" to binding.idEditText.text.toString()))
            }
            else {
                viewModelProvider.createRepair(Repair(repairUid = binding.editRepairIdInputEditText.text.toString()))
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


    // Bind all textviews, spinners, dates and radiogroups
    private fun bindRepairData(repair: Repair) {

        tempRepair = repair

        binding.editRepairIdInputEditText.setText(tempRepair.repairUid)
        binding.editRepairNameEditText.setText(tempRepair.name)
        binding.editRepairManufacturerEditText.setText(tempRepair.manufacturer)
        binding.editRepairModelEditText.setText(tempRepair.model)
        binding.editRepairInventoryNumberEditText.setText(tempRepair.inventoryNumber)
        binding.editRepairSerialNumberEditText.setText(tempRepair.serialNumber)
        binding.editRepairWardTextInputEditText.setText(tempRepair.ward)
        binding.editRepairDefectDescriptionTextInputEditText.setText(tempRepair.defectDescription)
        binding.editRepairRepairDescriptionTextInputEditText.setText(tempRepair.repairDescription)
        binding.editRepairRepairingDateButton.setText(getDateString(tempRepair.repairingDate))

        bindESTRadioButton(repair)
        bindHospitalSpinner(tempRepair)

        binding.editRepairOpeningDateButton.setText(getDateString(tempRepair.openingDate))


    }

    // Binding Hospital spinner with repair selection
    private fun bindHospitalSpinner(repair: Repair) {
        var position = 0
        var selection = 1
        for (item in spinnerHospitalList) {
            if (repair.hospitalString == item.toString()) {
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

    // Checks if photo is takie by camera or taken from gallery and saves it to ByteArray
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
    private fun bindESTRadioButton(repair: Repair) {
        var position = 0
        var selection = 0
        for (item in ESTState.values()) {
            if (repair.electricalSafetyTestString == item.toString()) {
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
            tempRepair.hospitalString = parent.getItemAtPosition(position).toString()
        }    }

    // Hospital spienner implementation - onNothingSelected
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}