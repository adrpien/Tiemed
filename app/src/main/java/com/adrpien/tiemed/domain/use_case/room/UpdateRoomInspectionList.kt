package com.adrpien.tiemed.domain.use_case.room

import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Inspection
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.repository.TiemedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateRoomInspectionList @Inject constructor(
    private val tiemedRepository: TiemedRepository
) {

    operator fun invoke(inspectionList: List<Inspection>): Flow<Resource<Boolean>> {
        return tiemedRepository.updateRoomInspectionList(inspectionList)
    }
}