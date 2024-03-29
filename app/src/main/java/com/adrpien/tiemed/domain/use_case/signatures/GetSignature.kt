package com.adrpien.tiemed.domain.use_case.signatures

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.Signature
import javax.inject.Inject

class GetSignature @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(signatureId: String): Flow<Resource<ByteArray>> {
        if(signatureId.isBlank()){
            return flow { }
        }
        return repository.getSignature(signatureId)
    }

}