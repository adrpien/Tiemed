package com.adrpien.tiemed.datepickers

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class DefectPickerDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // TODO onCreate defect description dialog
        return Dialog(requireContext())
    }
}