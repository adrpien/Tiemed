package com.adrpien.tiemed.domain.repository

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.data.remote.dto.InspectionDto
import com.adrpien.tiemed.domain.model.*
import kotlinx.coroutines.flow.Flow

interface TiemedRepository {

    /* ***** Inspections ************************************************************************ */
    fun getInspection(inspectionId: String): Flow<Resource<Inspection>>
    suspend fun getInspectionList() : Flow<Resource<List<Inspection>>>
    fun insertInspection (inspection: Inspection): Flow<Resource<Boolean>>

    /* ***** Repairs **************************************************************************** */
    fun getRepair(repairId: String): Flow<Resource<Repair>>
    suspend fun getRepairList(): Flow<Resource<List<Repair>>>
    fun insertRepair (repair: Repair): Flow<Resource<Boolean>>

    /* ***** Devices **************************************************************************** */

    fun getDevice(deviceId: String): Flow<Resource<Device>>
    suspend fun getDeviceList(): Flow<Resource<List<Device>>>
    fun insertDevice (device: Device): Flow<Resource<Boolean>>

    /* ***** Parts ****************************************************************************** */
    fun getPart(partId: String): Flow<Resource<Part>>
    suspend fun getPartList(): Flow<Resource<List<Part>>>
    fun insertPart (part: Part): Flow<Resource<Boolean>>

    /* ***** Hospitals ************************************************************************** */
    suspend fun getHospitalList(): Flow<Resource<List<Hospital>>>

    /* ***** Technicians ************************************************************************ */
    suspend fun getTechnicianList(): Flow<Resource<List<Technician>>>

    /* ***** EstStates ************************************************************************** */
    suspend fun getEstStateList(): Flow<Resource<List<EstState>>>

    /* ***** InspectionState ******************************************************************** */
    suspend fun getInspectionStateList(): Flow<Resource<List<InspectionState>>>

    /* ***** RepairStates *********************************************************************** */
    suspend fun getRepairStateList(): Flow<Resource<List<RepairState>>>

}