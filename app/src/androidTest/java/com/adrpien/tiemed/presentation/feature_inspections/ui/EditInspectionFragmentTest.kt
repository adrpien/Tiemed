package com.adrpien.tiemed.presentation.feature_inspections.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EditInspectionFragmentTest {

    private lateinit var editInspectionFragment: EditInspectionFragment

    @Before
    fun setup(){
    editInspectionFragment = EditInspectionFragment()
    }

    @Test
    fun spinnerListSizeEqualsHospitalListSize_returnsTrue() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        editInspectionFragment.initHospitalListSpinner()

    }
}