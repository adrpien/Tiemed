package com.adrpien.tiemed.domain.use_case.devices

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateDevice @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(device: Device): Flow<Resource<String>> {
        return repository.insertDevice(device)
    }
}