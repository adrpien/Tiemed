package com.adrpien.tiemed.signature

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.adrpien.tiemed.R
import java.io.ByteArrayOutputStream

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
                        requireActivity().supportFragmentManager.setFragmentResult(
                            "REQUEST_KEY",
                            bundleOf("signature" to captureSignature(signatureLayout) ))
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

    // Capture Signature, compress to JPEG and return as ByteArray
    private fun captureSignature(signatureLayout: View): ByteArray {
        val signatureView = signatureLayout.findViewById<SignatureView>(R.id.signatureView)
        val signatureBitmap = Bitmap.createBitmap(signatureLayout.width, signatureLayout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(signatureBitmap)
        signatureLayout.draw(canvas)
        val signatureByteArrayOutputStream = ByteArrayOutputStream()
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, signatureByteArrayOutputStream)
        return signatureByteArrayOutputStream.toByteArray()

        /*  TODO("Not yet implemented") saving signature and sending to FireBAse Repository
        1. Add Repository function DONE
        2. Add to EditInspectionViewModel class new function DONE
        3. Compress View to jpg
        4. Move captured signature to EditInspectionFragment
        5. Edit updateTempInspection function DONE
        6. Add bind signature image button
        7.. Upload to Firebase using function added to EditInspectionViewModel DONE
         */
    }
}