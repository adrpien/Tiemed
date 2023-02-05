package com.adrpien.tiemed.presentation.feature_repairs.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RepairViewModel @Inject constructor(
    private val getHospitalList: GetHospitalList,
    private val getRepairList: GetRepairList,
    private val getRepair: GetRepair,
    private val createRepair: CreateRepair,
    private val updateRepair: UpdateRepair,
    private val getSignature: GetSignature,
    private val createSignature: CreateSignature,
    private val updateSignature: UpdateSignature,
    private val getDevice: GetDevice,
    private val createDevice: CreateDevice,
    private  val updateDevice: UpdateDevice,
    private val getEstStateList: GetEstStateList,
    private val getRepairStateList: GetRepairStateList
) : ViewModel() {

    // You can here f.e. create here function to change value of your State Flow object,
    // in order to store values and protect them against activity destroy

    /* ************************** REPAIRS ******************************************************* */
    /* ************************** getRepairList ************************************************* */
    fun getRepairListFlow(): StateFlow<Resource<List<Repair>>> = getRepairList().stateIn(
        scope = viewModelScope,
        initialValue = emptyList<Repair>(),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<Repair>>>
    val repairListStateFlow = getRepairListFlow()
    /* ************************** getRepair ***************************************************** */
    fun getRepairFlow(repairId: String): StateFlow<Resource<Repair>> = getRepair(repairId).stateIn(
        scope = viewModelScope,
        initialValue = Repair(repairId = ""),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Repair>>
    /* ************************** createRepair ************************************************** */
    fun createRepairFlow(repair: Repair, device: Device): StateFlow<Resource<Boolean>> = createRepair(repair, device).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>
    /* ************************** updateRepair ************************************************** */
    fun updateRepairFlow(repair: Repair): StateFlow<Resource<Boolean>> = updateRepair(repair).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>

    /* ************************** HOSPITAL LIST ************************************************* */
    /* ************************** getHospitalList *********************************************** */
    fun getHospitalListFlow(): StateFlow<Resource<List<Hospital>>> = getHospitalList().stateIn(
        scope = viewModelScope,
        initialValue = emptyList<Hospital>(),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<Hospital>>>
    val hospitalListStateFlow = getHospitalListFlow()

    /* ************************** SIGNATURES ******************************************************* */
    /* ************************** getSignature ************************************************** */
    fun getSignatureFlow(signatureId: String): StateFlow<Resource<ByteArray>> = getSignature(signatureId).stateIn(
        scope = viewModelScope,
        initialValue = Repair(repairId = ""),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<ByteArray>>
    /* ************************** createSignature ************************************************** */
    fun createSignatureFlow(signatureId: String, byteArray: ByteArray): StateFlow<Resource<Boolean>> = createSignature(signatureId, byteArray).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>
    /* ************************** updateSignature ************************************************** */
    fun updateSignatureFlow(signatureId: String, byteArray: ByteArray): StateFlow<Resource<Boolean>> = updateSignature(signatureId, byteArray).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>

    /* ************************** DEVICES ******************************************************* */
    /* ************************** getDevice ***************************************************** */
    fun getDeviceFlow(deviceId: String): StateFlow<Resource<Device>> = getDevice(deviceId).stateIn(
        scope = viewModelScope,
        initialValue = Device(deviceId = ""),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Device>>
    /* ************************** createDevice ************************************************** */
    fun createDeviceFlow(device: Device): StateFlow<Resource<String?>> = createDevice(device).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<String?>>
    /* ************************** updateDevice ************************************************** */
    fun updateDeviceFlow(device: Device): StateFlow<Resource<Boolean>> = updateDevice(device).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>

    /* ************************** EST STATE LIST ************************************************* */
    /* ************************** getEstStateList *********************************************** */
    fun getEstStateListFlow(): StateFlow<Resource<List<EstState>>> = getEstStateList().stateIn(
        scope = viewModelScope,
        initialValue = emptyList<Hospital>(),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<EstState>>>
    val estStateListStateFlow = getHospitalListFlow()

    /* ************************** REPAIR STATE LIST ************************************************* */
    /* ************************** getRepairStateList *********************************************** */
    fun getrepairStateListFlow(): StateFlow<Resource<List<RepairState>>> = getRepairStateList().stateIn(
        scope = viewModelScope,
        initialValue = emptyList<Hospital>(),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<RepairState>>>
    val repairStateListStateFlow = getHospitalListFlow()

}

