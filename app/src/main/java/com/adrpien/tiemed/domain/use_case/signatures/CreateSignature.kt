package com.adrpien.tiemed.domain.use_case.signatures

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateSignature @Inject constructor (
    private val repository: TiemedRepository
    ) {

    operator fun invoke(signatureId: String, byteArray: ByteArray): Flow<Resource<String>> {
        return repository.createSignature(signatureId, byteArray)
    }

}