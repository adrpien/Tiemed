package com.adrpien.tiemed.signature

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.adrpien.tiemed.R
import kotlinx.coroutines.NonCancellable.cancel

class SignatureDialog(): DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Inflating signature layout with custom SignatureView
        val signatureLayout = layoutInflater.inflate(R.layout.signature_view, null)


        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(activity)
            builder
                .setCancelable(true)
                .setTitle(getString(R.string.signature))
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                        saveSignature()
                        Toast.makeText(it, "Confirmed", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(it, "Canceled", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
                .setView(signatureLayout)



            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")    }

    private fun saveSignature() {
        //  TODO("Not yet implemented") saving signature and sending to FireBAse Repository
    }
}