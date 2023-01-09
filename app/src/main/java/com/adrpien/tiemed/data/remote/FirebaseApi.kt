package com.adrpien.tiemed.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.data.remote.dto.*
import com.adrpien.tiemed.datamodels.Hospital
import com.adrpien.tiemed.domain.model.Technician
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow


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

    fun getInspectionList():List<InspectionDto>{
        firebaseFirestore.collection("inspections")
            .get()
            .addOnSuccessListener{
                inspectionList = it.toObjects(InspectionDto::class.java)
                Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection records fetched")

            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return inspectionList
    }
    fun getInspection(inspectionId: String): InspectionDto {
        firebaseFirestore.collection("inspections")
            .document(inspectionId)
            .get()
            .addOnSuccessListener {
                inspection = it.toObject(InspectionDto::class.java)!!
                Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record fetched")
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return inspection
    }
    fun createNewInspection(inspection: InspectionDto){
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
        documentReference.set(map)
    }
    fun updateInspection(inspection: InspectionDto){
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
        firebaseFirestore.collection("inspections")
            .document(inspection.inspectionId)
            .update(map)
            .addOnSuccessListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, "Inspection record updated")
            }
            .addOnFailureListener{
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
    }

    /* ********************************* REPAIRS ************************************************ */

    private lateinit var repair: RepairDto
    private lateinit var repairList: List<RepairDto>

    fun getRepairList():List<RepairDto>{
        firebaseFirestore.collection("repairs")
            .get()
            .addOnSuccessListener{
                repairList = it.toObjects(RepairDto::class.java)
                Log.d(TIEMED_REPOSITORY_DEBUG, "Repair records fetched")

            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return repairList
    }
    fun getRepair(repairId: String): RepairDto {
        firebaseFirestore.collection("repairs")
            .document(repairId)
            .get()
            .addOnSuccessListener {
                repair = it.toObject(RepairDto::class.java)!!
                Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record fetched")
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return repair
    }
    fun createNewRepair(repair: RepairDto){
        var documentReference = firebaseFirestore.collection("repairs")
            .document()
        var map = mapOf<String, String>(
            "repairId" to documentReference.id,
            "repairStateId" to inspection.inspectionStateId,
            "deviceId" to inspection.deviceId,
            "hospitalId" to inspection.hospitalId,
            "ward" to inspection.ward,
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
        documentReference.set(map)
    }
    fun updateRepair(repair: RepairDto){
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
        firebaseFirestore.collection("repairs")
            .document(repair.repairId)
            .update(map)
            .addOnSuccessListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, "Repair record updated")
            }
            .addOnFailureListener{
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
    }

    /* ********************************* SIGNATURES ********************************************* */

    private lateinit var signatureURL: String
    private lateinit var signature: ByteArray

    fun uploadSignature(signatureBytes: ByteArray, signatureId: String){
        firebaseStorage.getReference("signatures")
            .child("${signatureId}.jpg")
            .putBytes(signatureBytes)
            .addOnSuccessListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, "Signature uploaded")
            }
            .addOnFailureListener{
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
    }
    fun getSignature(signatureId: String): ByteArray {
        firebaseStorage.getReference("signatures")
            .child("${signatureId}.jpg")
            .getBytes(10000000) // 10MB
            .addOnSuccessListener {
                signature = it
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return signature
    }

    /* ********************************* DEVICES ************************************************ */

    private lateinit var device: DeviceDto
    private lateinit var deviceList: List<DeviceDto>

    fun getDeviceList():List<DeviceDto>{
        firebaseFirestore.collection("devices")
            .get()
            .addOnSuccessListener{
                deviceList = it.toObjects(DeviceDto::class.java)
                Log.d(TIEMED_REPOSITORY_DEBUG, "Device records fetched")

            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return deviceList
    }
    fun getDevice(deviceId: String): DeviceDto {
        firebaseFirestore.collection("devices")
            .document(deviceId)
            .get()
            .addOnSuccessListener {
                device = it.toObject(DeviceDto::class.java)!!
                Log.d(TIEMED_REPOSITORY_DEBUG, "Device record fetched")
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return device
    }
    fun createNewDevice(device: DeviceDto){
        var documentReference = firebaseFirestore.collection("devices")
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
        documentReference.set(map)
    }
    fun updateDevice(repair: DeviceDto){
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
        firebaseFirestore.collection("devicess")
            .document(device.deviceId)
            .update(map)
            .addOnSuccessListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, "Device record updated")
            }
            .addOnFailureListener{
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
    }


    /* ********************************* HOSPITALS ********************************************** */

    private lateinit var hospitalList: List<HospitalDto>

    fun getHospitalList(): List<HospitalDto>{
        firebaseFirestore.collection("hospitals")
            .get()
            .addOnSuccessListener {
                val hospitalList = it.toObjects(HospitalDto::class.java)
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return hospitalList
    }

    /* ********************************* TECHNICIANS ******************************************** */

    private lateinit var technicianList: List<TechnicianDto>

    fun getTechnicianList(): List<TechnicianDto>{
        firebaseFirestore.collection("technicians")
            .get()
            .addOnSuccessListener {
                val technicianList = it.toObjects(TechnicianDto::class.java)
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return technicianList
    }

    /* ********************************* EST STATES ********************************************* */

    private lateinit var estStateList: Flow<Resource<List<EstStateDto>>>

    fun getEstStateList(): Flow<Resource<List<EstStateDto>>>{
        firebaseFirestore.collection("est_states")
            .get()
            .addOnSuccessListener {
                estStateList = it.toObjects(EstStateDto::class.java)
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return estStateList
    }

    /* ********************************* REPAIR STATES ****************************************** */

    private lateinit var repairStateList: List<RepairStateDto>

    fun getRepairStateList(): List<RepairStateDto>{
        firebaseFirestore.collection("repair_states")
            .get()
            .addOnSuccessListener {
                repairStateList = it.toObjects(RepairStateDto::class.java)
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return repairStateList
    }

    /* ********************************* INSPECTIONS STATES ************************************* */

    private lateinit var inspectionStateList: List<InspectionStateDto>

    fun getInspectionStateList(): List<InspectionStateDto>{
        firebaseFirestore.collection("inspection_states")
            .get()
            .addOnSuccessListener {
                inspectionStateList = it.toObjects(InspectionStateDto::class.java)
            }
            .addOnFailureListener {
                Log.d(TIEMED_REPOSITORY_DEBUG, it.message.toString())
            }
        return inspectionStateList
    }
}