package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Part
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class CreatePart(
    private val repository: TiemedRepository
) {

    operator fun invoke(part: Part): Flow<Resource<String?>> {
        return repository.insertPart(part)
    }

}