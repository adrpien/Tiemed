package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.RepairState
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class GetRepairStateList(
    private val repository: TiemedRepository
) {

    operator fun invoke(): Flow<Resource<List<RepairState>>> {
        return repository.getRepairStateList()
    }
}