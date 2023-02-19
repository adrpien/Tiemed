package com.adrpien.tiemed.domain.use_case

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Technician
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTechnicianList @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(): Flow<Resource<List<Technician>>> {
        return repository.getTechnicianList()
    }
}