package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.EstState
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class GetEstStateList(
    private val repository: TiemedRepository
) {

    operator fun invoke(): Flow<Resource<List<EstState>>> {
        return repository.getEstStateList()
    }

}