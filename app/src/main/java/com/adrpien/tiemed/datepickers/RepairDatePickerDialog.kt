package com.adrpien.tiemed.datepickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class RepairDatePickerDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        // Create date (moment of class instance creating) and set day, month, year values
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        /*
        Create DatePickerDialog and deliver year, month and day arguments
        Cast activity to OnDateSetListener (remember to implement this interface in MainActivity),
        beacuse MainActivity is component which should receive data from listener.
        Our Main Activity should be listener.
         */
        return DatePickerDialog(requireActivity(), parentFragment as DatePickerDialog.OnDateSetListener, year, month, day)
    }
}