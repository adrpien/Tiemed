package com.adrpien.tiemed.domain.repository

import com.adrpien.dictionaryapp.core.util.Event
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Inspection
import kotlinx.coroutines.flow.Flow

interface TiemedRepository {
    fun getInspection(inspectionId: String): Flow<Resource<List<Inspection>>>
}