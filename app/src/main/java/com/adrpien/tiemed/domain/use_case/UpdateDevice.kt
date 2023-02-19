package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateDevice @Inject constructor (
    private val repository: TiemedRepository
    ) {

    operator fun invoke(device: Device): Flow<Resource<Boolean>> {
        return repository.updateDevice(device)
    }

}