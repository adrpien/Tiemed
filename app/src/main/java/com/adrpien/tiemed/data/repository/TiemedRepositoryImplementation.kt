package com.adrpien.tiemed.data.repository

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.data.local.TiemedDao
import com.adrpien.tiemed.data.remote.FirebaseApi
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class  TiemedRepositoryImplementation(
    val tiemedDao: TiemedDao,
    val firebaseApi: FirebaseApi
): TiemedRepository {

    private val TIEMED_REPOSITORY_IMPLEMENTATION = "TIEMED REPOSITORY IMPLEMENTATION"

    /* ********************************* INSPECTIONS ******************************************** */
    private fun inspectionListFlow(): Flow<Resource<List<Inspection>>> {
        return firebaseApi.getInspectionList()
    }
    override fun getInspectionList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val inspectionList = tiemedDao.getInspectionList()
        emit(Resource(ResourceState.LOADING, inspectionList.map { it.toInspection() }))
    }.flatMapLatest {
        inspectionListFlow()
    }
    override fun getInspection(repairId: String): Flow<Resource<Inspection>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repair = tiemedDao.getInspection(repairId).toInspection()
        emit(Resource(ResourceState.SUCCESS, repair))
    }
    override fun insertInspection(inspection: Inspection): Flow<Resource<String?>> = flow {
        emit(Resource((ResourceState.LOADING), null))
        firebaseApi.createInspection(inspection)
        emit(Resource(ResourceState.SUCCESS, null))

    }
    override fun updateInspection(inspection: Inspection): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.updateInspection(inspection)
        emit(Resource(ResourceState.SUCCESS, true))

    }

    /* ********************************* REPAIRS ************************************************ */
    private fun repairListFlow(): Flow<Resource<List<Repair>>> {
        return firebaseApi.getRepairList()
    }
    override fun getRepairList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repairList = tiemedDao.getRepairList()
        emit(Resource(ResourceState.LOADING, repairList.map { it.toRepair() }))
    }.flatMapConcat {
        repairListFlow()
    }
    private fun repairFlow(repairId: String): Flow<Resource<Repair>> {
        return firebaseApi.getRepair(repairId)
    }
    override fun getRepair(repairId: String): Flow<Resource<Repair>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repair = tiemedDao.getRepair(repairId).toRepair()
        emit(Resource(ResourceState.SUCCESS, repair))
    }.flatMapConcat {
        repairFlow(repairId)
    }
    override fun insertRepair(repair: Repair): Flow<Resource<String>> = flow {
        emit(Resource((ResourceState.LOADING), null))
        firebaseApi.createRepair(repair)
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updateRepair(repair: Repair): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.updateRepair(repair)
        emit(Resource(ResourceState.SUCCESS, true))
    }

    /* ********************************* DEVICES ************************************************ */
    private fun deviceListFlow(): Flow<Resource<List<Device>>> {
        return firebaseApi.getDeviceList()
    }
    override fun getDeviceList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val deviceList = tiemedDao.getDeviceList()
        emit(Resource(ResourceState.LOADING, deviceList.map { it.toDevice() }))
    }.flatMapLatest {
        deviceListFlow()
    }
    override fun getDevice(deviceId: String): Flow<Resource<Device>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val device = tiemedDao.getDevice(deviceId).toDevice()
        emit(Resource(ResourceState.SUCCESS, device))
    }
    override fun insertDevice(device: Device): Flow<Resource<String?>> = flow {
        emit(Resource((ResourceState.LOADING), null))
        firebaseApi.createDevice(device)
        emit(Resource(ResourceState.SUCCESS, null))

    }
    override fun updateDevice(device: Device): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.updateDevice(device)
        emit(Resource(ResourceState.SUCCESS, true))

    }


    /* ********************************* PARTS ************************************************** */
    private fun partListFlow(): Flow<Resource<List<Part>>>{
        return firebaseApi.getPartList()
    }
    override fun getPartList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val partList = tiemedDao.getPartList()
        emit(Resource(ResourceState.LOADING, partList.map { it.toPart() }))
    }.flatMapLatest {
        partListFlow()
    }
    override fun getPart(partId: String): Flow<Resource<Part>>  = flow {
        emit(Resource(ResourceState.LOADING, null))
        val part = tiemedDao.getPart(partId).toPart()
        emit(Resource(ResourceState.SUCCESS, part))
    }
    override fun insertPart(part: Part): Flow<Resource<String>> = flow {
        emit(Resource((ResourceState.LOADING), null))
        firebaseApi.createPart(part)
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updatePart(part: Part): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.updatePart(part)
        emit(Resource(ResourceState.SUCCESS, true))
    }


    /* ********************************* HOSPITALS ********************************************* */
    private fun hospitalListFlow(): Flow<Resource<List<Hospital>>>{
        return firebaseApi.getHospitalList()
    }
    override fun getHospitalList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val hospitalList = tiemedDao.getHospitalList()
        emit(Resource(ResourceState.LOADING, hospitalList.map { it.toHospital() }))
    }.flatMapLatest {
        hospitalListFlow()
    }

    /* ********************************* TECHNICIANS ******************************************** */
    private fun technicianListFlow(): Flow<Resource<List<Technician>>>{
        return firebaseApi.getTechnicianList()
    }
    override fun getTechnicianList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val technicianList = tiemedDao.getTechnicianList()
        emit(Resource(ResourceState.LOADING, technicianList.map { it.toTechnician() }))
    }.flatMapLatest {
        technicianListFlow()
    }

    /* ********************************* INSPECTION STATES ************************************** */
    private fun inspectionStateListFlow(): Flow<Resource<List<InspectionState>>>{
        return firebaseApi.getInspectionStateList()
    }
    override fun getInspectionStateList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val inspectionStateList = tiemedDao.getInspectionStateList()
        emit(Resource(ResourceState.LOADING, inspectionStateList.map { it.toInspectionState()}))
    }.flatMapLatest {
        inspectionStateListFlow()
    }

    /* ********************************* REPAIR STATES ****************************************** */
    private fun repairStateListFlow(): Flow<Resource<List<RepairState>>>{
        return firebaseApi.getRepairStateList()
    }
    override fun getRepairStateList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repairStateList = tiemedDao.getRepairStateList()
        emit(Resource(ResourceState.LOADING, repairStateList.map { it.toRepairState()}))
    }.flatMapLatest {
        repairStateListFlow()
    }

    /* ********************************* EST STATES ********************************************* */
    private fun estStateListFlow(): Flow<Resource<List<EstState>>>{
        return firebaseApi.getEstStateList()
    }
    override fun getEstStateList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val estStateList = tiemedDao.getEstStateList()
        emit(Resource(ResourceState.LOADING, estStateList.map { it.toEstState()}))
    }.flatMapLatest {
        estStateListFlow()
    }

    /* ********************************* SIGNATURES ********************************************* */
    override fun getSignature(signatureId: String): Flow<Resource<ByteArray>> {
    return  firebaseApi.getSignature(signatureId)
    }
    override fun updateSignature(signatureId: String, byteArray: ByteArray): Flow<Resource<String?>> {
        return firebaseApi.uploadSignature(signatureId, byteArray)
    }
    override fun createSignature(signatureId: String, byteArray: ByteArray): Flow<Resource<String?>> {
        return firebaseApi.uploadSignature(signatureId, byteArray)
    }

}
