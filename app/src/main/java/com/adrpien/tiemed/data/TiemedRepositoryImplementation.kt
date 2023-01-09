package com.adrpien.tiemed.data

import androidx.lifecycle.MutableLiveData
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.data.local.TiemedDao
import com.adrpien.tiemed.data.remote.FirebaseApi
import com.adrpien.tiemed.data.remote.dto.*
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.model.Part
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class  TiemedRepositoryImplementation(
    val tiemedDao: TiemedDao,
    val firebaseApi: FirebaseApi
): TiemedRepository {

    /*
    *********************************
    INSPECTIONS
    *********************************
     */


    override fun getInspectionList(): Flow<Resource<List<Inspection>>> = flow {
        emit(Resource(ResourceState.LOADING))
        val inspectionList = tiemedDao.getInspectionList().map { it.toInspection() }
        emit(Resource(ResourceState.LOADING, inspectionList))
        val inspectionListEntity = firebaseApi.getInspectionList()
            .map {
                it.toInspectionEntity()
            }
            .forEach {
            tiemedDao.deleteInspection(it.inspectionId)
            tiemedDao.insertInspection(it)
            }
        emit(Resource(ResourceState.SUCCESS, tiemedDao.getInspectionList().map { it.toInspection() }))


    }




    override fun getInspection(): MutableLiveData<InspectionDto> {
        TODO("Not yet implemented")
    }

    override fun insertInspection(inspection: Inspection) {
        TODO("Not yet implemented")
    }

    override fun getRepair(repairId: String): MutableLiveData<RepairDto> {
        TODO("Not yet implemented")
    }

    override fun getRepairList(): MutableLiveData<List<RepairDto>> {
        TODO("Not yet implemented")
    }

    override fun insertRepair(repair: Repair) {
        TODO("Not yet implemented")
    }

    override fun getPart(partId: String): MutableLiveData<PartDto> {
        TODO("Not yet implemented")
    }

    override fun getPartList(): MutableLiveData<List<PartDto>> {
        TODO("Not yet implemented")
    }

    override fun insertPart(part: Part) {
        TODO("Not yet implemented")
    }

    override fun getHospitalList(): MutableLiveData<List<HospitalDto>> {
        TODO("Not yet implemented")
    }

    override fun getTechnicianList(): MutableLiveData<List<TechnicianDto>> {
        TODO("Not yet implemented")
    }

    override fun getEstStateList(): MutableLiveData<List<EstStateDto>> {
        TODO("Not yet implemented")
    }

    override fun getInspectionStateList(): MutableLiveData<List<InspectionStateDto>> {
        TODO("Not yet implemented")
    }

    override fun getRepairStateList(): MutableLiveData<List<RepairStateDto>> {
        TODO("Not yet implemented")
    }
}
