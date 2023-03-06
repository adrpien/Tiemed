package com.adrpien.tiemed.data.remote

import android.util.Log
import android.widget.Toast
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.dictionaryapp.core.util.ResourceState
import com.adrpien.tiemed.domain.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
    fun getInspectionList(): Flow<Resource<List<Inspection>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
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
    fun createInspection(inspection: Inspection): Flow<Resource<String?>> = flow {
        // TODO Need to implement caching mechanism in createInspection fun in FirebaseApi
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
            emit(Resource(ResourceState.SUCCESS, documentReference.id))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record created")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record creation error")

        }
    }
    fun updateInspection(inspection: Inspection): Flow<Resource<Boolean>> = flow {
        // TODO Need to implement caching mechanism in updateInspection fun in FirebaseApi
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
    fun getRepair(repairId: String): Flow<Resource<Repair>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repairs")
            .document(repairId)
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val  data = result.result.toObject<Repair>()
            //val data =  result.result.toObject(Repair::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair list fetch error")
        }
    }
    fun createRepair(repair: Repair): Flow<Resource<String?>> = flow {
        // TODO Need to implement caching mechanism in createRepair fun in FirebaseApi
        emit(Resource(ResourceState.LOADING, null))
        var documentReference = firebaseFirestore.collection("repairs")
            .document()
        var map = mapOf<String, String>(
            "repairId" to documentReference.id,
            "repairStateId" to repair.repairStateId,
            "deviceId" to repair.deviceId,
            "hospitalId" to repair.hospitalId,
            "ward" to repair.ward,
            "defectDescription" to repair.defectDescription,
            "repairDescription" to repair.repairDescription,
            "partDescription" to repair.partDescription,
            "comment" to repair.comment,
            "estTestId" to repair.estStateId,
            "closingDate" to repair.closingDate,
            "openingDate" to repair.openingDate,
            "repairingDate" to repair.repairingDate,
            "pickupTechnicianId" to repair.pickupTechnicianId,
            "repairTechnicianId" to repair.repairTechnicianId,
            "returnTechnicianId" to repair.returnTechnicianId,
            "rate" to repair.rate,
            "recipient" to repair.recipient,
            "recipientSignatureId" to repair.repairId,
        )
        val result = documentReference.set(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, documentReference.id))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record created")

        } else {
            emit(Resource(ResourceState.ERROR, documentReference.id))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record creation error")

        }
    }
    fun updateRepair(repair: Repair): Flow<Resource<Boolean>> = flow {
        // TODO Need to implement caching mechanism in updateRepair fun in FirebaseApi
        emit(Resource(ResourceState.LOADING, null))
        var map = mapOf<String, String>(
            "repairId" to repair.repairId,
            "repairStateId" to repair.repairStateId,
            "deviceId" to repair.deviceId,
            "hospitalId" to repair.hospitalId,
            "ward" to repair.ward,
            "defectDescription" to repair.defectDescription,
            "repairDescription" to repair.repairDescription,
            "partDescription" to repair.partDescription,
            "comment" to repair.comment,
            "estStateId" to repair.estStateId,
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
    fun getDeviceList(): Flow<Resource<List<Device>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("devices")
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObjects(Device::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetch error")
        }
    }
    fun getDevice(deviceId: String): Flow<Resource<Device>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("devices")
            .document(deviceId)
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObject<Device>()
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device list fetch error")
        }
    }
    fun createDevice(device: Device): Flow<Resource<String>> = flow {
        // TODO Need to implement caching mechanism in createDevice fun in FirebaseApi
        emit(Resource(ResourceState.LOADING, null))
        var documentReference = firebaseFirestore.collection("repairs")
            .document()
        var map = mapOf<String, String>(
            "deviceId" to documentReference.id,
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
            emit(Resource(ResourceState.SUCCESS, documentReference.id))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device record created")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Device record creation error")

        }
    }
    fun  updateDevice(device: Device): Flow<Resource<Boolean>> = flow {
        // TODO Need to implement caching mechanism in updateDevice fun in FirebaseApi
        emit(Resource(ResourceState.LOADING, false))
        var map = mapOf<String, String>(
            "deviceId" to device.deviceId,
            "name" to device.name,
            "manufacturer" to device.manufacturer,
            "model" to device.model,
            "serialNumber" to device.serialNumber,
            "inventoryNumber" to device.inventoryNumber,
        )
        val documentReference = firebaseFirestore.collection("devices").document(device.deviceId)
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
    fun uploadSignature(signatureId: String, signatureBytes: ByteArray): Flow<Resource<String?>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseStorage.getReference("signatures")
            .child("${signatureId}.jpg")
            val result = documentReference.putBytes(signatureBytes)
            result.await()
                    if (result.isSuccessful) {
                        // TODO Need implement storing photos in
                        emit(Resource(ResourceState.SUCCESS, documentReference.downloadUrl.toString()))
                        Log.d(TIEMED_REPOSITORY_DEBUG, "Signature uploaded")

                    } else {
                        emit(Resource(ResourceState.ERROR, null))
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

    /* ********************************* PARTS ************************************************** */
    fun getPartList(): Flow<Resource<List<Part>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("parts")
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObjects(Part::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part list fetch error")
        }
    }
    fun getPart(repairId: String): Flow<Resource<Part>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("parts")
            .document(repairId)
        val result = documentReference.get()
        result.await()
        if (result.isSuccessful) {
            val data =  result.result.toObject(Part::class.java)
            emit(Resource(ResourceState.SUCCESS, data))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part list fetch error")
        }
    }
    fun createPart(part: Part): Flow<Resource<String?>> = flow {
        // TODO Need to implement caching mechanism in createPart fun in FirebaseApi
        emit(Resource(ResourceState.LOADING, null))
        var documentReference = firebaseFirestore.collection("parts")
            .document()
        var map = mapOf<String, String>(
            "partId" to part.partId,
            "name" to part.name,
            "quantity" to part.quantity,
        )
        val result = documentReference.set(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, documentReference.id))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part record created")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part record creation error")

        }
    }
    fun updatePart(part: Part): Flow<Resource<Boolean>> = flow {
        // TODO Need to implement caching mechanism in updatePart fun in FirebaseApi
        emit(Resource(ResourceState.LOADING, null))
        var map = mapOf<String, String>(
            "partId" to part.partId,
            "name" to part.name,
            "quantity" to part.quantity,
        )
        val documentReference = firebaseFirestore.collection("parts").document(part.partId)
        val result = documentReference.update(map)
        result.await()
        if (result.isSuccessful) {
            emit(Resource(ResourceState.SUCCESS, true))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part record updated")
        } else {
            emit(Resource(ResourceState.ERROR, false))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Part record update error")
        }

    }

    /* ********************************* HOSPITALS ********************************************** */
    fun getHospitalList(): Flow<Resource<List<Hospital>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("hospitals")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val hospitalList = data.result.toObjects(Hospital::class.java)
            emit(Resource(ResourceState.SUCCESS, hospitalList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetch error")
        }
    }

    /* ********************************* TECHNICIANS ******************************************** */
    fun getTechnicianList(): Flow<Resource<List<Technician>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("technicians")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val hospitalList = data.result.toObjects(Technician::class.java)
            emit(Resource(ResourceState.SUCCESS, hospitalList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Hospital list fetch error")
        }
    }

    /* ********************************* EST STATES ********************************************* */
    fun getEstStateList(): Flow<Resource<List<EstState>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("est_states")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val hospitalList = data.result.toObjects(EstState::class.java)
            emit(Resource(ResourceState.SUCCESS, hospitalList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Est states list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Est states list fetch error")
        }
    }

    /* ********************************* REPAIR STATES ****************************************** */
    fun getRepairStateList(): Flow<Resource<List<RepairState>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("repair_states")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val repairStateList = data.result.toObjects(RepairState::class.java)
            emit(Resource(ResourceState.SUCCESS, repairStateList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair states list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Repair states list fetch error")
        }
    }

    /* ********************************* INSPECTIONS STATES ************************************* */
    fun getInspectionStateList(): Flow<Resource<List<InspectionState>>> = flow {
        emit(Resource(ResourceState.LOADING, null))
        val documentReference = firebaseFirestore.collection("inspection_states")
        val data = documentReference.get()
        data.await()
        if(data.isSuccessful) {
            val inspectionStateList = data.result.toObjects(InspectionState::class.java)
            emit(Resource(ResourceState.SUCCESS, inspectionStateList))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection states list fetched")

        } else {
            emit(Resource(ResourceState.ERROR, null))
            Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection states list fetch error")
        }
    }
}