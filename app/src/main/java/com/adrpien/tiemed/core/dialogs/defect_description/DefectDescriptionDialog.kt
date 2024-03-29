package com.adrpien.tiemed.core.dialogs.defect_description

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.adrpien.tiemed.R
import com.adrpien.tiemed.domain.model.Repair

import com.adrpien.tiemed.presentation.feature_inspections.view_model.InspectionViewModel

// TODO Defect description dialog to finish, if used
class DefectDescriptionDialog: DialogFragment() {

    val viewModelProvider by viewModels<InspectionViewModel>()

    val DEFECT_DESCRIPTION_DIALOG_TAG = "DefectPickerDialog log"

    // DefectDescriptionDialog class onCreateDialog function simplentation
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val defectDescriptionEditText = EditText(context)
        val builder = AlertDialog.Builder(activity)
            .setCancelable(true)
            .setTitle(R.string.defect_decription)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->
                requireActivity().supportFragmentManager.setFragmentResult(
                    "request_key",
                    bundleOf("defect_description" to defectDescriptionEditText.text)
                )
                // Creating
                val repair = Repair(
                    defectDescription = defectDescriptionEditText.text.toString()
                )
                //viewModelProvider.c(repair)
                Log.d(DEFECT_DESCRIPTION_DIALOG_TAG, "Confirmed")
            })
            .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                Log.d(DEFECT_DESCRIPTION_DIALOG_TAG, "Canceled")
            })
            .setView(defectDescriptionEditText)
        return builder.create()
    }
}