package com.adrpien.tiemed.data.repository

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.data.local.TiemedDao
import com.adrpien.tiemed.data.remote.FirebaseApi
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.domain.repository.TiemedRepository
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.flow.*


class  TiemedRepositoryImplementation(
    val tiemedDao: TiemedDao,
    val firebaseApi: FirebaseApi
): TiemedRepository {

    private val TIEMED_REPOSITORY_IMPLEMENTATION = "TIEMED REPOSITORY IMPLEMENTATION"

    /* ********************************* Room DB ************************************************ */
    override fun updateRoomEstStateList(estStateList: List<EstState>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllEstStates()
            for (item in estStateList) {
                tiemedDao.insertEstState(item.toEstStateEntity() )
            }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))

    }
    override fun updateRoomDeviceList(deviceList: List<Device>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllDevices()
            for (item in deviceList) {
                tiemedDao.insertDevice(item.toDeviceEntity())
            }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))

    }
    override fun updateRoomRepairStateList(repairStateList: List<RepairState>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllRepairStates()
            for (item in repairStateList) {
                tiemedDao.insertRepairState(item.toRepairStateEntity() )
        }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updateRoomInspectionStateList(inspectionStateList: List<InspectionState>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllInspectionStates()
            for (item in inspectionStateList) {
                tiemedDao.insertInspectionState(item.toInspectionStateEntity()) }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updateRoomTechnicianList(technicianList: List<Technician>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllTechnicians()
            for (item in technicianList) {
                tiemedDao.insertTechnician(item.toTechncianEntity())
            }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updateRoomInspectionList(inspectionList: List<Inspection>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllInspections()
            for( item in inspectionList) {
                tiemedDao.insertInspection(item.toInspectionEntity())
            }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updateRoomRepairList(repairList: List<Repair>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllRepairs()
            for(item in repairList){
                tiemedDao.insertRepair(item.toRepairEntity())
            }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))
    }
    override fun updateRoomHospitalList(hospitalList: List<Hospital>): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        try {
            tiemedDao.deleteAllHospitals()
            for (item in hospitalList){
                tiemedDao.insertHospital(item.toHospitalEntity())
            }
        } catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        catch (e: HttpException) {
            emit(Resource(ResourceState.ERROR, null))
        }
        emit(Resource(ResourceState.SUCCESS, null))
    }

    /* ********************************* INSPECTIONS ******************************************** */
    private fun inspectionListFlow(): Flow<Resource<List<Inspection>>> {
        return firebaseApi.getInspectionList()
    }
    override fun getInspectionList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val inspectionList = tiemedDao.getInspectionList()
        emit(Resource(ResourceState.LOADING, inspectionList.map { it.toInspection() }))
    }.flatMapConcat {
        inspectionListFlow()
    }
    private fun inspectionFlow(inspectionId: String): Flow<Resource<Inspection>> {
        return firebaseApi.getInspection(inspectionId)
    }
    override fun getInspection(inspectionId: String): Flow<Resource<Inspection>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repair = tiemedDao.getInspection(inspectionId).toInspection()
        emit(Resource(ResourceState.SUCCESS, repair))
    }.flatMapConcat {
        inspectionFlow(inspectionId)
    }
    override fun insertInspection(inspection: Inspection): Flow<Resource<String?>> = flow {
        firebaseApi.createInspection(inspection)
    }
    override fun updateInspection(inspection: Inspection): Flow<Resource<Boolean>> = flow {
        firebaseApi.updateInspection(inspection)
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
    override fun insertRepair(repair: Repair): Flow<Resource<String?>> = flow {
        firebaseApi.createRepair(repair)
    }
    override fun updateRepair(repair: Repair): Flow<Resource<Boolean>> = flow {
        firebaseApi.updateRepair(repair)
    }

    /* ********************************* DEVICES ************************************************ */
    private fun deviceListFlow(): Flow<Resource<List<Device>>> {
        return firebaseApi.getDeviceList()
    }
    override fun getDeviceList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val deviceList = tiemedDao.getDeviceList()
        emit(Resource(ResourceState.LOADING, deviceList.map { it.toDevice() }))
    }.flatMapConcat {
        deviceListFlow()
    }
    private fun deviceFlow(deviceId: String): Flow<Resource<Device>> = flow {
        firebaseApi.getDevice(deviceId)
    }
    override fun getDevice(deviceId: String): Flow<Resource<Device>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val device = tiemedDao.getDevice(deviceId).toDevice()
        emit(Resource(ResourceState.SUCCESS, device))
    }.flatMapLatest {
        deviceFlow(deviceId)
    }
    override fun insertDevice(device: Device): Flow<Resource<String?>> = flow {
        firebaseApi.createDevice(device)

    }
    override fun updateDevice(device: Device): Flow<Resource<Boolean>> = flow {
        firebaseApi.updateDevice(device)
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
    private fun partFlow(partId: String): Flow<Resource<Part>> = flow {
        firebaseApi.getPart(partId)
    }
    override fun getPart(partId: String): Flow<Resource<Part>>  = flow {
        emit(Resource(ResourceState.LOADING, null))
        val part = tiemedDao.getPart(partId).toPart()
        emit(Resource(ResourceState.SUCCESS, part))
    }.flatMapConcat {
        partFlow(partId)
    }
    override fun insertPart(part: Part): Flow<Resource<String>> = flow {
        firebaseApi.createPart(part)
    }
    override fun updatePart(part: Part): Flow<Resource<Boolean>> = flow {
        firebaseApi.updatePart(part)
    }

    /* ********************************* SIGNATURES ********************************************* */

    private fun signatureFlow(signatureId: String): Flow<Resource<String>> = flow {
        firebaseApi.getSignature(signatureId)

    }
    override fun getSignature(signatureId: String): Flow<Resource<ByteArray>> = flow {
        signatureFlow(signatureId)
    }
    override fun updateSignature(signatureId: String, byteArray: ByteArray): Flow<Resource<String?>> {
        return firebaseApi.uploadSignature(signatureId, byteArray)
    }
    override fun createSignature(signatureId: String, byteArray: ByteArray): Flow<Resource<String?>> {
        return firebaseApi.uploadSignature(signatureId, byteArray)
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
}
