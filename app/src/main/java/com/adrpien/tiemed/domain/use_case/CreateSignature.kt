package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class CreateSignature(
    private val repository: TiemedRepository
    ) {

    operator fun invoke(signatureId: String, byteArray: ByteArray): Flow<Resource<Boolean>> {
        return repository.createSignature(signatureId, byteArray)
    }

}