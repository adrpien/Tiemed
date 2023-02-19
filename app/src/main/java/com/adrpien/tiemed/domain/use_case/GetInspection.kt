package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetInspection @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(inspectionId: String): Flow<Resource<Inspection>> {
        if(inspectionId.isBlank()) {
            return flow {  }
        }
        return repository.getInspection(inspectionId)
    }

}