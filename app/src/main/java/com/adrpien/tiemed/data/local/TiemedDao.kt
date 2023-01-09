package com.adrpien.tiemed.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.adrpien.tiemed.data.local.entities.*
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.model.Part
import com.adrpien.tiemed.domain.model.Repair

@Dao
interface TiemedDao {

    /* ***** Repairs **************************************************************************** */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepair(repair: RepairEntity)

    @Transaction
    @Query("SELECT * FROM repairentity WHERE repairId LIKE :repairId")
    suspend fun getRepair(repairId: String): List<RepairEntity>

    @Transaction
    @Query("SELECT * FROM repairentity")
    suspend fun getRepairList(): List<RepairEntity>

    @Transaction
    @Query("DELETE FROM repairentity WHERE repairId LIKE :repairId")
    suspend fun deleteRepair(repairId: String)

    /* ***** Inspections ************************************************************************ */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: InspectionEntity)

    @Transaction
    @Query("SELECT * FROM inspectionentity WHERE inspectionId LIKE :inspectionId")
    suspend fun getInspection(inspectionId: String): List<InspectionEntity>

    @Transaction
    @Query("SELECT * FROM inspectionentity")
    suspend fun getInspectionList(): List<InspectionEntity>

    @Transaction
    @Query("DELETE FROM inspectionentity WHERE inspectionId LIKE :inspectionId")
    suspend fun deleteInspection(inspectionId: String)

    /* ***** Parts ****************************************************************************** */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPart(part: Part)

    @Transaction
    @Query("SELECT * FROM partentity WHERE partId LIKE :partId")
    suspend fun getPart(partId: String): List<PartEntity>

    @Transaction
    @Query("SELECT * FROM partentity")
    suspend fun getPartList(): List<PartEntity>

    @Transaction
    @Query("DELETE FROM partentity WHERE partId LIKE :partId")
    suspend fun deletePart(partId: String)

    /* ***** Hospitals ************************************************************************** */
    @Transaction
    @Query("SELECT * FROM hospitalentity")
    suspend fun getHospitalList(): List<HospitalEntity>

    /* ***** Technicians ************************************************************************ */
    @Transaction
    @Query("SELECT * FROM technicianentity")
    suspend fun getTechnicianList(): List<TechnicianEntity>

    /* ***** EstStates ************************************************************************** */
    @Transaction
    @Query("SELECT * FROM eststateentity")
    suspend fun getESTStateList(): List<EstStateEntity>

    /* ***** InspectionState ******************************************************************** */
    @Transaction
    @Query("SELECT * FROM inspectionstateentity")
    suspend fun getInspectionStateList(): List<InspectionStateEntity>

    /* ***** RepairStates *********************************************************************** */
    @Transaction
    @Query("SELECT * FROM repairstateentity")
    suspend fun getRepairStateList(): List<RepairStateEntity>

}