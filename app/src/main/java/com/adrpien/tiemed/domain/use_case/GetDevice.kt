package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDevice(
    private val repository: TiemedRepository
) {

    operator fun invoke(deviceId: String): Flow<Resource<Device>> {
        if(deviceId.isBlank()){
            return flow { }
        }
        return repository.getDevice(deviceId)
    }

}