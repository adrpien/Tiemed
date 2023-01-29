package com.adrpien.tiemed.core.dialogs.filtering_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.adrpien.tiemed.R

import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel


class FilteringDialog: DialogFragment() {

    // Lazy instance of ViewModelProvider
    val RepairListViewModel by viewModels<RepairViewModel>()

    val DEFECT_DESCRIPTION_DIALOG_TAG = "DefectPickerDialog"

    // DefectDescriptionDialog class onCreateDialog function simplentation
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // TODO Set dialog layout
        val filteringDialogView = layoutInflater.inflate(R.layout.view_filtering, null)
        val builder = AlertDialog.Builder(activity)
            .setCancelable(true)
            .setTitle(R.string.filter_repair)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->
                requireActivity().supportFragmentManager.setFragmentResult(
                    "request_key",
                    bundleOf("defect_description" to filteringDialogView.text)
                )

            })
            .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setView(filteringDialogView)
        return builder.create()
    }
}