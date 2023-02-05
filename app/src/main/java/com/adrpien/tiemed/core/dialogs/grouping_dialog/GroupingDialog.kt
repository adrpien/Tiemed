package com.adrpien.tiemed.core.dialogs.grouping_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Switch
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.adrpien.tiemed.R
import com.adrpien.tiemed.core.dialogs.date.DatePickerDialog
import android.app.DatePickerDialog.*
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel
import java.util.Calendar


class GroupingDialog: DialogFragment(), OnDateSetListener {

    // Lazy instance of ViewModelProvider
    val RepairListViewModel by viewModels<RepairViewModel>()

    private val DEFECT_DESCRIPTION_DIALOG_TAG = "DefectPickerDialog"

    private val date = Calendar.getInstance()
    private var milliseconds: Long = 0

    // DefectDescriptionDialog class onCreateDialog function simplentation
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val filteringDialogView = layoutInflater.inflate(R.layout.view_filtering, null)
        val alertDialogButton = filteringDialogView.findViewById<Switch>(R.id.filteringAlertDialogDateButton)
        alertDialogButton.setOnClickListener {
            // Create TimePicker
            val dialog = DatePickerDialog(Calendar.getInstance().timeInMillis)
            // show MyTimePicker
            dialog.show(childFragmentManager, "date_time_picker")
        }
        val alertDialogSwitch = filteringDialogView.findViewById<Switch>(R.id.alertDialogSortingSwitch)
        val builder = AlertDialog.Builder(activity)
            .setCancelable(true)
            .setTitle(R.string.filter_repair)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->
                val bundle = bundleOf(
                    "bundle_type" to "FILTER_BUNDLE",
                    "switch" to filteringDialogView.isActivated,
                    "value" to milliseconds
                )
                requireActivity().supportFragmentManager.setFragmentResult(
                    "request_key",
                    bundle
                )

            })
            .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setView(filteringDialogView)
        return builder.create()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date.set(year, month, dayOfMonth)
        milliseconds = date.timeInMillis
    }
}