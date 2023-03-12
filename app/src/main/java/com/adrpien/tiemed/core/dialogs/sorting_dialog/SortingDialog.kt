package com.adrpien.tiemed.core.dialogs.sorting_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Switch
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.adrpien.tiemed.R
import com.adrpien.tiemed.presentation.feature_repairs.view_model.RepairViewModel


class SortingDialog: DialogFragment() {

    // Lazy instance of ViewModelProvider
    val RepairListViewModel by viewModels<RepairViewModel>()

    // DefectDescriptionDialog class onCreateDialog function simplentation
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sortingDialogView = layoutInflater.inflate(R.layout.view_sorting, null)
        val alertDialogSwitch = sortingDialogView.findViewById<Switch>(R.id.alertDialogSortingSwitch)
        val builder = AlertDialog.Builder(activity)
            .setCancelable(true)
            .setTitle(R.string.filter_repair)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->
                val bundle = bundleOf(
                    "bundle_type" to "FILTER_BUNDLE",
                    "switch" to sortingDialogView.isActivated,
                    "value" to 0
                )
                requireActivity().supportFragmentManager.setFragmentResult(
                    "SORTING_REQUEST_KEY",
                    bundle
                )

            })
            .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setView(sortingDialogView)
        return builder.create()
    }

}