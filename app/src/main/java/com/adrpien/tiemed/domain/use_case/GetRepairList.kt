package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class GetRepairList(
    private val repository: TiemedRepository
) {

    operator fun invoke(): Flow<Resource<List<Repair>>> {
        return repository.getRepairList()
    }

}