package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRepair(
    private val repository: TiemedRepository
) {

    operator fun invoke(repairId: String): Flow<Resource<Repair>> {
        if(repairId.isBlank()) {
            return flow {  }
        }
        return  repository.getRepair(repairId)
    }

}