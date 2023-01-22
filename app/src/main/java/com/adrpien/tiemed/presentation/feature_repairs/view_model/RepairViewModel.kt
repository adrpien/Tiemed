package com.adrpien.tiemed.presentation.feature_repairs.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrpien.dictionaryapp.core.util.Resource
import com.adrpien.tiemed.domain.model.Repair
import com.adrpien.tiemed.domain.repository.TiemedRepository
import com.adrpien.tiemed.domain.use_case.GetRepair
import com.adrpien.tiemed.domain.use_case.GetRepairList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RepairViewModel @Inject constructor(
    private val getRepairList: GetRepairList,
    private val getRepair: GetRepair,
    private val insertRepair: GetRepairList
) : ViewModel() {

    // You can here f.e. create here function to change value of your State Flow object,
    // in order to store values and protect them against activity destroy

    /* ************************** getRepairList *************************************************** */
    val repairListFlow: StateFlow<Resource<List<Repair>>> = getRepairList().stateIn(
        scope = viewModelScope,
        initialValue = emptyList<Repair>(),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<List<Repair>>>

    val repairListStateFlow = repairListFlow

    /* ************************** getRepair *************************************************** */
    val repairFlow: StateFlow<Resource<Repair>> = getRepairList().stateIn(
        scope = viewModelScope,
        initialValue = Repair(repairId = ""),
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Repair>>

    val repairStateFlow = repairFlow

    /* ************************** insertRepair *************************************************** */
    val insertRepairFlow: StateFlow<Resource<Boolean>> = insertRepair().stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.Lazily
    ) as StateFlow<Resource<Boolean>>

    val insertRepairStateFlow = insertRepairFlow





}

