package com.adrpien.tiemed.domain.use_case.parts

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Part
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPartList @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(): Flow<Resource<List<Part>>> {
        return repository.getPartList()
    }

}