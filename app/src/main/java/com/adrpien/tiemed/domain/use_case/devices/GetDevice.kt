package com.adrpien.tiemed.domain.use_case.devices

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDevice @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(deviceId: String): Flow<Resource<Device>> {
        if(deviceId.isBlank()) {
            return flow {  }
        }
        return repository.getDevice(deviceId)
    }

}