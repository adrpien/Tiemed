package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.InspectionState
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow

class GetInspectionStateList(
    private val repository: TiemedRepository
) {

    operator fun invoke(): Flow<Resource<List<InspectionState>>> {
        return repository.getInspectionStateList()
    }

}