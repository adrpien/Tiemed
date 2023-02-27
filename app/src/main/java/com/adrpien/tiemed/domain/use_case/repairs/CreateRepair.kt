package com.adrpien.tiemed.domain.use_case.repairs

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Device
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

class CreateRepair @Inject constructor (
    private val repository: TiemedRepository
) {

    operator fun invoke(repair: Repair): Flow<Resource<String?>> {
        return repository.insertRepair(repair)
    }
}