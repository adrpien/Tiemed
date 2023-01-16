package com.adrpien.tiemed.data

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.data.local.TiemedDao
import com.adrpien.tiemed.data.remote.FirebaseApi
import com.adrpien.tiemed.data.remote.dto.*
import com.adrpien.tiemed.domain.model.*
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.*


class  TiemedRepositoryImplementation(
    val tiemedDao: TiemedDao,
    val firebaseApi: FirebaseApi
): TiemedRepository {

    private val TIEMED_REPOSITORY_IMPLEMENTATION = "TIEMED REPOSITORY IMPLEMENTATION"

    /* ********************************* INSPECTIONS ******************************************** */

    private fun inspectionListFlow(): Flow<Resource<List<Inspection>>>{
        return firebaseApi.getInspectionList()
    }
    override suspend fun getInspectionList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val inspectionList = tiemedDao.getInspectionList()
        emit(Resource(ResourceState.LOADING, inspectionList.map { it.toInspection() }))
    }.flatMapLatest {
        inspectionListFlow()
    }
    override fun getInspection(repairId: String): Flow<Resource<Inspection>>  = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repair = tiemedDao.getInspection(repairId).toInspection()
        emit(Resource(ResourceState.SUCCESS, repair))
    }
    override fun insertInspection(repair: Inspection): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.createNewInspection(repair.toInspectionDto())
        emit(Resource(ResourceState.SUCCESS, true))

    }

    /* ********************************* REPAIRS ******************************************** */

    private fun repairListFlow(): Flow<Resource<List<Repair>>>{
        return firebaseApi.getRepairList()
    }
    override suspend fun getRepairList() = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repairList = tiemedDao.getRepairList()
        emit(Resource(ResourceState.LOADING, repairList.map { it.toRepair() }))
    }.flatMapLatest {
        repairListFlow()
    }
    override fun getRepair(repairId: String): Flow<Resource<Repair>>  = flow {
        emit(Resource(ResourceState.LOADING, null))
        val repair = tiemedDao.getRepair(repairId).toRepair()
        emit(Resource(ResourceState.SUCCESS, repair))
    }
    override fun insertRepair(repair: Repair): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.createNewRepair(repair.toRepairDto())
        emit(Resource(ResourceState.SUCCESS, true))

    }

    /* ********************************* PARTS ******************************************** */

    private fun partListFlow(): Flow<Resource<List<Part>>>{
        return firebaseApi.getPartList()
    }
    override suspend fun getPartList() = flow {
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
    override fun insertPart(part: Part): Flow<Resource<Boolean>> = flow {
        emit(Resource((ResourceState.LOADING), false))
        firebaseApi.createNewPart(part.toPartDto())
        emit(Resource(ResourceState.SUCCESS, true))
    }

    /* ********************************* HOSPITALS ******************************************** */


    override fun getHospitalList(): Flow<Resource<List<Hospital>>> {
        TODO("Not yet implemented")
    }

    /* ********************************* TECHNICIANS ******************************************** */


    override fun getTechnicianList(): Flow<Resource<List<Technician>>> {
        TODO("Not yet implemented")
    }

    /* ********************************* HOSPITALS ******************************************** */

    override fun getEstStateList(): Flow<Resource<List<EstState>>> {
        TODO("Not yet implemented")
    }

    /* ********************************* INSPECTION STATES ******************************************** */


        override fun getInspectionStateList(): Flow<Resource<List<InspectionState>>> {
        TODO("Not yet implemented")
    }

    /* ********************************* REPAIR STATES ******************************************** */


        override fun getRepairStateList(): Flow<Resource<List<RepairState>>> {
        TODO("Not yet implemented")
    }


}
