package com.adrpien.tiemed.presentation.feature_inspections.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class InspectionViewModel @Inject constructor(
    private val getHospitalList: GetHospitalList,
    private val getInspectionList: GetInspectionList,
    private val getInspection: GetInspection,
    private val createInspection: CreateInspection,
    private val updateInspection: UpdateInspection,
    private val getSignature: GetSignature,
    private val createSignature: CreateSignature,
    private val updateSignature: UpdateSignature,
    private val getDevice: GetDevice,
    private val createDevice: CreateDevice,
    private  val updateDevice: UpdateDevice,
    private val getEstStateList: GetEstStateList,
    private val getInspectionStateList: GetInspectionStateList
) : ViewModel() {

    // You can here f.e. create here function to change value of your State Flow object,
    // in order to store values and protect them against activity destroy

    /* ************************** INSPECTIONS ************************************************* */
    /* ************************** getInspectionList ************************************************* */
    fun getInspectionListFlow(): StateFlow<Resource<List<Inspection>>> = getInspectionList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Inspection>()),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<Inspection>>>
    val inspectionListStateFlow = getInspectionListFlow()
    /* ************************** getInspection ***************************************************** */
    fun getInspectionFlow(inspectionId: String): StateFlow<Resource<Inspection>> = getInspection(inspectionId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Inspection(inspectionId = "")),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Inspection>>
    /* ************************** createInspection ************************************************** */
    fun createInspectionFlow(inspection: Inspection): StateFlow<Resource<String?>> = createInspection(inspection).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<String?>>
    /* ************************** updateInspection ************************************************** */
    fun updateInspectionFlow(inspection: Inspection): StateFlow<Resource<Boolean>> = updateInspection(inspection).stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>

    /* ************************** HOSPITAL LIST ************************************************* */
    /* ************************** getHospitalList *********************************************** */
    fun getHospitalListFlow(): StateFlow<Resource<List<Hospital>>> = getHospitalList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Hospital>()),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<Hospital>>>
    val hospitalListStateFlow = getHospitalListFlow()

    /* ************************** SIGNATURES ************************************************* */
    /* ************************** getSignature ************************************************** */
    fun getSignatureFlow(signatureId: String): StateFlow<Resource<ByteArray>> = getSignature(signatureId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Inspection(inspectionId = "")),
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

    /* ************************** DEVICES ************************************************* */
    /* ************************** getDevice ***************************************************** */
    fun getDeviceFlow(deviceId: String): StateFlow<Resource<Device>> = getDevice(deviceId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Device(deviceId = "")),
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
        initialValue = Resource(ResourceState.LOADING, emptyList<Hospital>()),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<EstState>>>
    val estStateListStateFlow = getHospitalListFlow()


    /* ************************** INSPECTION STATE  LIST *********************************************** */
    fun getinspectionStateListFlow(): StateFlow<Resource<List<InspectionState>>> = getInspectionStateList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Hospital>()),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<InspectionState>>>
    val inspectionStateListStateFlow = getHospitalListFlow()

}