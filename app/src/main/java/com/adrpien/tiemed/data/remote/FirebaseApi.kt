package com.adrpien.tiemed.data.remote

import android.util.Log
import androidx.annotation.RequiresFeature
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.data.remote.dto.*
import com.adrpien.tiemed.domain.model.EstState
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.model.Repair
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


// Repository class

class  FirebaseApi(
    val firebaseFirestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth,
    val firebaseStorage: FirebaseStorage
) {

    private val TIEMED_REPOSITORY_DEBUG = "INSPECTION_REPOSITORY_DEBUG"


    /* ********************************* INSPECTIONS ********************************* */

    private lateinit var inspection: InspectionDto
    private lateinit var inspectionList: List<InspectionDto>

    fun getInspectionList(): Flow<Resource<List<Inspection>>> = flow {
        // emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("inspections")
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObjects(Inspection::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection list fetch error")
        }
    }
    fun getInspection(inspectionId: String): Flow<Resource<Inspection>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("inspections")
            .document(inspectionId)
            val result = documentReference.get()
            result.await()
            if (result.isSuccessful) {
                val data =  result.result.toObject(Inspection::class.java)
                emit(Resource(ResourceState.SUCCESS, data))
                Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection list fetched")

            } else {
                emit(Resource(ResourceState.ERROR, null))
                Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection list fetch error")
            }
    }
    // TODO Need to implement caching mechanism
    fun createNewInspection(inspection: InspectionDto): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        var documentReference = firebaseFirestore.collection("inspections")
            .document()
        var map = mapOf<String, String>(
            "inspectionId" to documentReference.id,
            "inspectionStateId" to inspection.inspectionStateId,
            "deviceId" to inspection.deviceId,
            "hospitalId" to inspection.hospitalId,
            "ward" to inspection.ward,
            "estState" to inspection.estStateId,
            "comment" to inspection.comment,
            "inspectionDate" to inspection.inspectionDate,
            "recipient" to inspection.recipient,
            "signature" to inspection.recipientSignature,
            "technicianId" to inspection.technicianId
        )
        val result = documentReference.set(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record created")

        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record creation error")

        }
    }
    // TODO Need to implement caching mechanism
    fun updateInspection(inspection: InspectionDto): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        var map = mapOf<String, String>(
            "inspectionId" to inspection.inspectionId,
            "inspectionStateId" to inspection.inspectionStateId,
            "deviceId" to inspection.deviceId,
            "hospitalId" to inspection.hospitalId,
            "ward" to inspection.ward,
            "estState" to inspection.estStateId,
            "comment" to inspection.comment,
            "inspectionDate" to inspection.inspectionDate,
            "recipient" to inspection.recipient,
            "signature" to inspection.recipientSignature,
            "technicianId" to inspection.technicianId
        )
        val documentReference = firebaseFirestore.collection("inspections").document(inspection.inspectionId)
        val result = documentReference.update(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record updated")
        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record update error")
        }

    }

    /* ********************************* REPAIRS ************************************************ */

    private lateinit var repair: RepairDto
    private lateinit var repairList: List<RepairDto>

    fun getRepairList(): Flow<Resource<List<Repair>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repairs")
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObjects(Repair::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair list fetch error")
        }
    }
    fun getRepair(repairId: String): Flow<Resource<RepairDto>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repairs")
            .document(repairId)
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObject(RepairDto::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair list fetch error")
        }
    }
    // TODO Need to implement caching mechanism
    fun createNewRepair(repair: RepairDto): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        var documentReference = firebaseFirestore.collection("repairs")
            .document()
        var map = mapOf<String, String>(
            "repairId" to repair.repairId,
            "repairStateId" to repair.repairStateId,
            "deviceId" to repair.deviceId,
            "hospitalId" to repair.hospitalId,
            "ward" to repair.ward,
            // "photoList"  to repair.photoList,
            "defectDescription" to repair.defectDescription,
            "repairDescription" to repair.repairDescription,
            // "partList" to repair.partList,
            "partDescription" to repair.partDescription,
            "comment" to repair.comment,
            "estTestId" to repair.estTestId,
            "closingDate" to repair.closingDate,
            "openingDate" to repair.openingDate,
            "repairingDate" to repair.repairingDate,
            "pickupTechnicianId" to repair.pickupTechnicianId,
            "repairTechnicianId" to repair.repairTechnicianId,
            "returnTechnicianId" to repair.returnTechnicianId,
            "rate" to repair.rate,
            "recipient" to repair.recipient,
            "recipientSignature" to repair.recipientSignatureId,
        )
        val result = documentReference.set(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record created")

        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record creation error")

        }
    }
    // TODO Need to implement caching mechanism§
    fun updateRepair(repair: RepairDto): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        var map = mapOf<String, String>(
            "repairId" to repair.repairId,
            "repairStateId" to repair.repairStateId,
            "deviceId" to repair.deviceId,
            "hospitalId" to repair.hospitalId,
            "ward" to repair.ward,
            // "photoList"  to repair.photoList,
            "defectDescription" to repair.defectDescription,
            "repairDescription" to repair.repairDescription,
            // "partList" to repair.partList,
            "partDescription" to repair.partDescription,
            "comment" to repair.comment,
            "estTestId" to repair.estTestId,
            "closingDate" to repair.closingDate,
            "openingDate" to repair.openingDate,
            "repairingDate" to repair.repairingDate,
            "pickupTechnicianId" to repair.pickupTechnicianId,
            "repairTechnicianId" to repair.repairTechnicianId,
            "returnTechnicianId" to repair.returnTechnicianId,
            "rate" to repair.rate,
            "recipient" to repair.recipient,
            "recipientSignature" to repair.recipientSignatureId,
        )
        val documentReference = firebaseFirestore.collection("repairs").document(repair.repairId)
        val result = documentReference.update(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record updated")
        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record update error")
        }

    }

    /* ********************************* DEVICES ************************************************ */

    private lateinit var device: DeviceDto
    private lateinit var deviceList: List<DeviceDto>

    fun getDeviceList(): Flow<Resource<List<DeviceDto>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repairs")
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObjects(DeviceDto::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetch error")
        }
    }
    fun getDevice(repairId: String): Flow<Resource<DeviceDto>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repairs")
            .document(repairId)
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObject(DeviceDto::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetch error")
        }
    }
    // TODO Need to implement caching mechanism
    fun createNewDevice(repair: DeviceDto): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        var documentReference = firebaseFirestore.collection("repairs")
            .document()
        var map = mapOf<String, String>(
            "deviceId" to device.deviceId,
            "name" to device.name,
            "manufacturer" to device.manufacturer,
            "model" to device.model,
            "serialNumber" to device.serialNumber,
            "inventoryNumber" to device.inventoryNumber,
            // "inspections" to device.inspections,
            // "repairs" to device.repairs
        )
        val result = documentReference.set(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device record created")

        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device record creation error")

        }
    }
    // TODO Need to implement caching mechanism
    fun updateDevice(repair: DeviceDto): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        var map = mapOf<String, String>(
            "deviceId" to device.deviceId,
            "name" to device.name,
            "manufacturer" to device.manufacturer,
            "model" to device.model,
            "serialNumber" to device.serialNumber,
            "inventoryNumber" to device.inventoryNumber,
            // "inspections" to device.inspections,
            // "repairs" to device.repairs
        )
        val documentReference = firebaseFirestore.collection("repairs").document(device.deviceId)
        val result = documentReference.update(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device record updated")
        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device record update error")
        }

    }


    /* ********************************* SIGNATURES ********************************************* */

    private lateinit var signatureURL: String
    private lateinit var signature: ByteArray

    fun uploadSignature(signatureBytes: ByteArray, signatureId: String): Flow<Resource<Boolean>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseStorage.getReference("signatures")
            .child("${signatureId}.jpg")
            val result = documentReference.putBytes(signatureBytes)
            result.await()
                    if (result.isSuccessful) {
                        emit(Resource(ResourceState.SUCCESS, true))
                        Log.d(TIEMED_REPOSITORY_DEBUG, "Signature uploaded")

                    } else {
                        emit(Resource(ResourceState.ERROR, false))
                        Log.d(TIEMED_REPOSITORY_DEBUG, "Signature upload error")
                    }

    }
    fun getSignature(signatureId: String): Flow<Resource<ByteArray>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseStorage.getReference("signatures")
            .child("${signatureId}.jpg")
            val result = documentReference.getBytes(10000000) //  10MB
            result.await()
                    if (result.isSuccessful){
                        val data =  result.result
                        emit(Resource(ResourceState.SUCCESS, data))
                    } else {
                        emit(Resource(ResourceState.LOADING, null))
                        Log.d(TIEMED_REPOSITORY_DEBUG, "Signature fetch error")
                    }
    }

    /* ********************************* HOSPITALS ********************************************** */

    private lateinit var hospitalList: List<HospitalDto>

    fun getHospitalList(): Flow<Resource<List<HospitalDto>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("hospitals")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val hospitalList = data.result.toObjects(HospitalDto::class.java)
            emit(Resource(ResourceState.SUCCESS, hospitalList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetch error")
        }
    }

    /* ********************************* TECHNICIANS ******************************************** */

    private lateinit var technicianList: List<TechnicianDto>

    fun getTechnicianList(): Flow<Resource<List<HospitalDto>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("hospitals")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val hospitalList = data.result.toObjects(HospitalDto::class.java)
            emit(Resource(ResourceState.SUCCESS, hospitalList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetch error")
        }
    }

    /* ********************************* EST STATES ********************************************* */

    private lateinit var estStateList: List<EstStateDto>

    fun getEstStateList(): Flow<Resource<List<EstStateDto>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("hospitals")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val hospitalList = data.result.toObjects(EstStateDto::class.java)
            emit(Resource(ResourceState.SUCCESS, hospitalList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Est states list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Est states list fetch error")
        }
    }


    /* ********************************* REPAIR STATES ****************************************** */

    private lateinit var repairStateList: List<RepairStateDto>


    fun getRepairStateList(): Flow<Resource<List<RepairStateDto>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repair_states")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val repairStateList = data.result.toObjects(RepairStateDto::class.java)
            emit(Resource(ResourceState.SUCCESS, repairStateList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair states list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair states list fetch error")
        }
    }


    /* ********************************* INSPECTIONS STATES ************************************* */

    private lateinit var inspectionStateList: List<InspectionStateDto>

    fun getInspectionStateList(): Flow<Resource<List<InspectionStateDto>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repair_states")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val inspectionStateList = data.result.toObjects(InspectionStateDto::class.java)
            emit(Resource(ResourceState.SUCCESS, inspectionStateList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection states list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection states list fetch error")
        }
    }
}