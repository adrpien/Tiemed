package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class UpdateInspection(
    private val repository: TiemedRepository
) {

    operator fun invoke(inspection:Inspection): Flow<Resource<Boolean>> {
        return repository.insertInspection(inspection)
    }
}