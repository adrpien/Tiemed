package com.adrpien.tiemed.domain.use_case.inspections

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateInspection @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(inspection:Inspection): Flow<Resource<Boolean>> {
        return repository.updateInspection(inspection)
    }
}