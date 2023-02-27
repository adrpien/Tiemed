package com.adrpien.tiemed.data.local

import androidx.room.*
import com.adrpien.tiemed.data.local.entities.*
import com.adrpien.tiemed.domain.model.*

@Dao
interface TiemedDao {

    /* ***** Repairs **************************************************************************** */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepair(repairEntity: RepairEntity)

    @Transaction
    @Query("SELECT * FROM repairentity WHERE repairId LIKE :repairId")
    suspend fun getRepair(repairId: String): RepairEntity

    @Transaction
    @Query("SELECT * FROM repairentity")
    suspend fun getRepairList(): List<RepairEntity>

    @Transaction
    @Query("DELETE FROM repairentity WHERE repairId LIKE :repairId")
    suspend fun deleteRepair(repairId: String)

    @Transaction
    @Query("DELETE FROM repairentity")
    suspend fun deleteAllRepairs()

    /* ***** Inspections ************************************************************************ */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspectionEntity: InspectionEntity)

    @Transaction
    @Query("SELECT * FROM inspectionentity WHERE inspectionId LIKE :inspectionId")
    suspend fun getInspection(inspectionId: String): InspectionEntity

    @Transaction
    @Query("SELECT * FROM inspectionentity")
    suspend fun getInspectionList(): List<InspectionEntity>

    @Transaction
    @Query("DELETE FROM inspectionentity WHERE inspectionId LIKE :inspectionId")
    suspend fun deleteInspection(inspectionId: String)

    @Transaction
    @Query("DELETE FROM inspectionentity")
    suspend fun deleteAllInspections()

    /* ***** Parts ****************************************************************************** */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPart(partEntity: PartEntity)

    @Transaction
    @Query("SELECT * FROM partentity WHERE partId LIKE :partId")
    suspend fun getPart(partId: String): PartEntity

    @Transaction
    @Query("SELECT * FROM partentity")
    suspend fun getPartList(): List<PartEntity>

    @Transaction
    @Query("DELETE FROM partentity WHERE partId LIKE :partId")
    suspend fun deletePart(partId: String)

    @Transaction
    @Query("DELETE FROM partentity")
    suspend fun deleteAllParts()


    /* ***** Devices **************************************************************************** */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(deviceEntity: DeviceEntity)

    @Transaction
    @Query("SELECT * FROM deviceentity WHERE deviceId LIKE :deviceId")
    suspend fun getDevice(deviceId: String): DeviceEntity

    @Transaction
    @Query("SELECT * FROM deviceentity")
    suspend fun getDeviceList(): List<DeviceEntity>

    @Transaction
    @Query("DELETE FROM deviceentity WHERE deviceId LIKE :deviceId")
    suspend fun deleteDevice(deviceId: String)

    @Transaction
    @Query("DELETE FROM deviceentity")
    suspend fun deleteAllDevices()

    /* ***** Hospitals ************************************************************************** */
    @Transaction
    @Query("SELECT * FROM hospitalentity")
    suspend fun getHospitalList(): List<HospitalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHospital(hospitalEntity: HospitalEntity)

    @Transaction
    @Query("SELECT * FROM hospitalentity WHERE hospitalId LIKE :hospitalId")
    suspend fun getHospital(hospitalId: String): HospitalEntity

    @Transaction
    @Query("DELETE FROM hospitalentity WHERE hospitalId LIKE :hospitalId")
    suspend fun deleteHospital(hospitalId: String)

    @Transaction
    @Query("DELETE FROM hospitalentity")
    suspend fun deleteAllHospitals()

    /* ***** Technicians ************************************************************************ */
    @Transaction
    @Query("SELECT * FROM technicianentity")
    suspend fun getTechnicianList(): List<TechnicianEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechnician(technicianEntity: TechnicianEntity)

    @Transaction
    @Query("SELECT * FROM technicianentity WHERE technicianId LIKE :technicianId")
    suspend fun getTechnician(technicianId: String): TechnicianEntity

    @Transaction
    @Query("DELETE FROM technicianentity WHERE technicianId LIKE :technicianId")
    suspend fun deleteTechnician(technicianId: String)

    @Transaction
    @Query("DELETE FROM technicianentity")
    suspend fun deleteAllTechnicians()

    /* ***** EstStates ************************************************************************** */
    @Transaction
    @Query("SELECT * FROM eststateentity")
    suspend fun getEstStateList(): List<EstStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstState(estState: EstStateEntity)

    @Transaction
    @Query("SELECT * FROM eststateentity WHERE estStateId LIKE :estStateId")
    suspend fun getEstState(estStateId: String): EstStateEntity

    @Transaction
    @Query("DELETE FROM eststateentity WHERE estStateId LIKE :estStateId")
    suspend fun deleteEstState(estStateId: String)

    @Transaction
    @Query("DELETE FROM eststateentity")
    suspend fun deleteAllEstStates()

    /* ***** InspectionState ******************************************************************** */
    @Transaction
    @Query("SELECT * FROM inspectionstateentity")
    suspend fun getInspectionStateList(): List<InspectionStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspectionState(inspectionState: InspectionStateEntity)

    @Transaction
    @Query("SELECT * FROM inspectionstateentity WHERE inspectionStateId LIKE :inspectionStateId")
    suspend fun getInspectionState(inspectionStateId: String): InspectionStateEntity

    @Transaction
    @Query("DELETE FROM inspectionstateentity WHERE inspectionStateId LIKE :inspectionStateId")
    suspend fun deleteInspectionState(inspectionStateId: String)

    @Transaction
    @Query("DELETE FROM inspectionstateentity")
    suspend fun deleteAllInspectionStates()

    /* ***** RepairStates *********************************************************************** */
    @Transaction
    @Query("SELECT * FROM repairstateentity")
    suspend fun getRepairStateList(): List<RepairStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepairState(repairState: RepairStateEntity)

    @Transaction
    @Query("SELECT * FROM repairstateentity WHERE repairStateId LIKE :repairStateId")
    suspend fun getRepairState(repairStateId: String): RepairStateEntity

    @Transaction
    @Query("DELETE FROM repairstateentity WHERE repairStateId LIKE :repairStateId")
    suspend fun deleteRepairState(repairStateId: String)

    @Transaction
    @Query("DELETE FROM repairstateentity")
    suspend fun deleteAllRepairStates()

}