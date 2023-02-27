package com.adrpien.tiemed.domain.use_case.room

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.model.Technician
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateRoomTechnicianList @Inject constructor(
    private val tiemedRepository: TiemedRepository
) {

    operator fun invoke(technicianList: List<Technician>): Flow<Resource<Boolean>> {
        return tiemedRepository.updateRoomTechnicianList(technicianList)
    }
}