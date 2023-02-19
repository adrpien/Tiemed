package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Part
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPart @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(partId: String): Flow<Resource<Part>> {
        if(partId.isBlank()) {
            return flow {  }
        }
        return repository.getPart(partId)
    }

}