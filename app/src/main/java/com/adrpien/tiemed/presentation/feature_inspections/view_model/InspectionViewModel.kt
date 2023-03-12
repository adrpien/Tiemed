package com.adrpien.tiemed.presentation.feature_inspections.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.domain.use_case.*
import com.adrpien.tiemed.domain.use_case.devices.CreateDevice
import com.adrpien.tiemed.domain.use_case.devices.GetDevice
import com.adrpien.tiemed.domain.use_case.devices.GetDeviceList
import com.adrpien.tiemed.domain.use_case.devices.UpdateDevice
import com.adrpien.tiemed.domain.use_case.hospitals.GetHospitalList
import com.adrpien.tiemed.domain.use_case.inspections.CreateInspection
import com.adrpien.tiemed.domain.use_case.inspections.GetInspection
import com.adrpien.tiemed.domain.use_case.inspections.GetInspectionList
import com.adrpien.tiemed.domain.use_case.inspections.UpdateInspection
import com.adrpien.tiemed.domain.use_case.room.*
import com.adrpien.tiemed.domain.use_case.signatures.CreateSignature
import com.adrpien.tiemed.domain.use_case.signatures.GetSignature
import com.adrpien.tiemed.domain.use_case.signatures.UpdateSignature
import com.adrpien.tiemed.domain.use_case.states.GetEstStateList
import com.adrpien.tiemed.domain.use_case.states.GetInspectionStateList
import com.adrpien.tiemed.domain.use_case.technicians.GetTechnicianList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.security.Signature
import javax.inject.Inject

@HiltViewModel
class InspectionViewModel @Inject constructor(
    private val updateRoomDeviceList: UpdateRoomDeviceList,
    private val updateRoomEstStateList: UpdateRoomEstStateList,
    private val updateRoomHospitalList: UpdateRoomHospitalList,
    private val updateRoomTechnicianList: UpdateRoomTechnicianList,
    private val updateRoomInspectionList: UpdateRoomInspectionList,
    private val updateRoomInspectionStateList: UpdateRoomInspectionStateList,
    private val getTechnicianList: GetTechnicianList,
    private val getHospitalList: GetHospitalList,
    private val getInspectionList: GetInspectionList,
    private val getInspection: GetInspection,
    private val createInspection: CreateInspection,
    private val updateInspection: UpdateInspection,
    private val getSignature: GetSignature,
    private val createSignature: CreateSignature,
    private val updateSignature: UpdateSignature,
    private val getDevice: GetDevice,
    private val getDeviceList: GetDeviceList,
    private val createDevice: CreateDevice,
    private val updateDevice: UpdateDevice,
    private val getEstStateList: GetEstStateList,
    private val getInspectionStateList: GetInspectionStateList
) : ViewModel() {


    // You can here f.er create here function to change value of your State Flow object,
    // in order to store values and protect them against activity destroy

    /* ************************** INSPECTIONS ******************************************************* */
    fun getInspectionListFlow(): StateFlow<Resource<List<Inspection>>> = getInspectionList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Inspection>()),
        started = SharingStarted.Lazily
    )
    val inspectionListStateFlow = getInspectionListFlow()
    fun getInspectionFlow(inspectionId: String): StateFlow<Resource<Inspection>> = getInspection(inspectionId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Inspection(inspectionId = "")),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Inspection>>

    fun createInspectionFlow(inspection: Inspection): StateFlow<Resource<String>> =
        createInspection(inspection).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, ""),
            started = SharingStarted.Lazily
        )
    fun updateInspectionFlow(inspection: Inspection): StateFlow<Resource<Boolean>> =
        updateInspection(inspection).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, false),
            started = SharingStarted.Lazily
        )

    /* ************************** HOSPITAL LIST ************************************************* */
    fun getHospitalListFlow(): StateFlow<Resource<List<Hospital>>> = getHospitalList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Hospital>()),
        started = SharingStarted.Lazily
    )
    val hospitalListStateFlow = getHospitalListFlow()

    /* ************************** HOSPITAL LIST ************************************************* */
    fun getTechnicianListFlow(): StateFlow<Resource<List<Technician>>> = getTechnicianList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Technician>()),
        started = SharingStarted.Lazily
    )
    val technicianListStateFlow = getTechnicianListFlow()

    /* ************************** SIGNATURES ******************************************************* */
    fun getSignatureFlow(signatureId: String): StateFlow<Resource<ByteArray>> =
        getSignature(signatureId).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, byteArrayOf()),
            started = SharingStarted.Lazily
        )
    fun createSignatureFlow(
        signatureId: String,
        byteArray: ByteArray
    ): StateFlow<Resource<String>> = createSignature(signatureId, byteArray).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, ""),
        started = SharingStarted.Lazily
    )
    fun updateSignatureFlow(
        signatureId: String,
        byteArray: ByteArray
    ): StateFlow<Resource<String>> = updateSignature(signatureId, byteArray).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, ""),
        started = SharingStarted.Lazily
    )

    /* ************************** DEVICES ******************************************************* */
    fun getDeviceFlow(deviceId: String): StateFlow<Resource<Device>> = getDevice(deviceId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Device(deviceId = "")),
        started = SharingStarted.Lazily
    )
    fun createDeviceFlow(device: Device): StateFlow<Resource<String>> =
        createDevice(device).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, ""),
            started = SharingStarted.Lazily
        )
    fun updateDeviceFlow(device: Device): StateFlow<Resource<Boolean>> =
        updateDevice(device).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, false),
            started = SharingStarted.Lazily
        )
    fun getDeviceListFlow(): StateFlow<Resource<List<Device>>> = getDeviceList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Device>()),
        started = SharingStarted.Lazily
    )
    val deviceListStateFlow = getDeviceListFlow()

    /* ************************** EST STATE LIST ************************************************* */
    fun getEstStateListFlow(): StateFlow<Resource<List<EstState>>> = getEstStateList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<EstState>()),
        started = SharingStarted.Lazily
    )
    val estStateListStateFlow = getHospitalListFlow()

    /* ************************** REPAIR STATE LIST ************************************************* */
    fun getInspectionStateListFlow(): StateFlow<Resource<List<InspectionState>>> =
        getInspectionStateList().stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, emptyList<InspectionState>()),
            started = SharingStarted.Lazily
        )
    val inspectionStateListStateFlow = getInspectionStateListFlow()

    /* ************************** ROOM ********************************************************** */
    fun updateRoomHospitalListFlow(hospitalList: List<Hospital>): StateFlow<Resource<Boolean>> = updateRoomHospitalList(hospitalList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, false)
    )
    fun updateRoomDeviceListFlow(deviceList: List<Device>): StateFlow<Resource<Boolean>> = updateRoomDeviceList(deviceList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, false)
    )
    fun updateRoomEstStateListFlow(estStateList: List<EstState>): StateFlow<Resource<Boolean>> = updateRoomEstStateList(estStateList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, false)
    )
    fun updateRoomInspectionListFlow(inspectionList: List<Inspection>): StateFlow<Resource<Boolean>> = updateRoomInspectionList(inspectionList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, false)
    )
    fun updateRoomInspectionStateListFlow(inspectionStateList: List<InspectionState>): StateFlow<Resource<Boolean>> = updateRoomInspectionStateList(inspectionStateList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, false)
    )
    fun updateRoomTechnicianListFlow(technicianList: List<Technician>): StateFlow<Resource<Boolean>> = updateRoomTechnicianList(technicianList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, false)
    )

}

