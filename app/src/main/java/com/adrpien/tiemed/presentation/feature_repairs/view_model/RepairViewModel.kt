package com.adrpien.tiemed.presentation.feature_repairs.view_model

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
import com.adrpien.tiemed.domain.use_case.repairs.CreateRepair
import com.adrpien.tiemed.domain.use_case.repairs.GetRepair
import com.adrpien.tiemed.domain.use_case.repairs.GetRepairList
import com.adrpien.tiemed.domain.use_case.repairs.UpdateRepair
import com.adrpien.tiemed.domain.use_case.room.*
import com.adrpien.tiemed.domain.use_case.signatures.CreateSignature
import com.adrpien.tiemed.domain.use_case.signatures.GetSignature
import com.adrpien.tiemed.domain.use_case.signatures.UpdateSignature
import com.adrpien.tiemed.domain.use_case.states.GetEstStateList
import com.adrpien.tiemed.domain.use_case.states.GetRepairStateList
import com.adrpien.tiemed.domain.use_case.technicians.GetTechnicianList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.security.Signature
import javax.inject.Inject

@HiltViewModel
class RepairViewModel @Inject constructor(
    private val updateRoomDeviceList: UpdateRoomDeviceList,
    private val updateRoomEstStateList: UpdateRoomEstStateList,
    private val updateRoomHospitalList: UpdateRoomHospitalList,
    private val updateRoomTechnicianList: UpdateRoomTechnicianList,
    private val updateRoomRepairList: UpdateRoomRepairList,
    private val updateRoomRepairStateList: UpdateRoomRepairStateList,
    private val getTechnicianList: GetTechnicianList,
    private val getHospitalList: GetHospitalList,
    private val getRepairList: GetRepairList,
    private val getRepair: GetRepair,
    private val createRepair: CreateRepair,
    private val updateRepair: UpdateRepair,
    private val getSignature: GetSignature,
    private val createSignature: CreateSignature,
    private val updateSignature: UpdateSignature,
    private val getDevice: GetDevice,
    private val getDeviceList: GetDeviceList,
    private val createDevice: CreateDevice,
    private val updateDevice: UpdateDevice,
    private val getEstStateList: GetEstStateList,
    private val getRepairStateList: GetRepairStateList
) : ViewModel() {


    // You can here f.er create here function to change value of your State Flow object,
    // in order to store values and protect them against activity destroy

    /* ************************** REPAIRS ******************************************************* */
    fun getRepairListFlow(): StateFlow<Resource<List<Repair>>> = getRepairList().stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, emptyList<Repair>()),
        started = SharingStarted.Lazily
    )
    val repairListStateFlow = getRepairListFlow()
    fun getRepairFlow(repairId: String): StateFlow<Resource<Repair>> = getRepair(repairId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Repair(repairId = "")),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Repair>>
    fun createRepairFlow(repair: Repair): StateFlow<Resource<Boolean>> =
        createRepair(repair).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, false),
            started = SharingStarted.Lazily
        ) as StateFlow<Resource<Boolean>>
    fun updateRepairFlow(repair: Repair): StateFlow<Resource<Boolean>> =
        updateRepair(repair).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, false),
            started = SharingStarted.Lazily
        ) as StateFlow<Resource<Boolean>>

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
        ) as StateFlow<Resource<ByteArray>>
    fun createSignatureFlow(
        signatureId: String,
        byteArray: ByteArray
    ): StateFlow<Resource<Boolean>> = createSignature(signatureId, byteArray).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, false),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>
    fun updateSignatureFlow(
        signatureId: String,
        byteArray: ByteArray
    ): StateFlow<Resource<Boolean>> = updateSignature(signatureId, byteArray).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, false),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>

    /* ************************** DEVICES ******************************************************* */
    fun getDeviceFlow(deviceId: String): StateFlow<Resource<Device>> = getDevice(deviceId).stateIn(
        scope = viewModelScope,
        initialValue = Resource(ResourceState.LOADING, Device(deviceId = "")),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Device>>
    fun createDeviceFlow(device: Device): StateFlow<Resource<String?>> =
        createDevice(device).stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, false),
            started = SharingStarted.Lazily
        ) as StateFlow<Resource<String?>>
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
    fun getRepairStateListFlow(): StateFlow<Resource<List<RepairState>>> =
        getRepairStateList().stateIn(
            scope = viewModelScope,
            initialValue = Resource(ResourceState.LOADING, emptyList<RepairState>()),
            started = SharingStarted.Lazily
        )
    val repairStateListStateFlow = getRepairStateListFlow()

    /* ************************** ROOM ********************************************************** */
    fun updateRoomHospitalListFlow(hospitalList: List<Hospital>): StateFlow<Resource<Boolean>> = updateRoomHospitalList(hospitalList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, null)
    ) as StateFlow<Resource<Boolean>>
    fun updateRoomDeviceListFlow(deviceList: List<Device>): StateFlow<Resource<Boolean>> = updateRoomDeviceList(deviceList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, null)
    ) as StateFlow<Resource<Boolean>>
    fun updateRoomEstStateListFlow(estStateList: List<EstState>): StateFlow<Resource<Boolean>> = updateRoomEstStateList(estStateList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, null)
    ) as StateFlow<Resource<Boolean>>
    fun updateRoomRepairListFlow(repairList: List<Repair>): StateFlow<Resource<Boolean>> = updateRoomRepairList(repairList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, null)
    ) as StateFlow<Resource<Boolean>>
    fun updateRoomRepairStateListFlow(repairStateList: List<RepairState>): StateFlow<Resource<Boolean>> = updateRoomRepairStateList(repairStateList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, null)
    ) as StateFlow<Resource<Boolean>>
    fun updateRoomTechnicianListFlow(technicianList: List<Technician>): StateFlow<Resource<Boolean>> = updateRoomTechnicianList(technicianList).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        Resource(ResourceState.LOADING, null)
    ) as StateFlow<Resource<Boolean>>

}

