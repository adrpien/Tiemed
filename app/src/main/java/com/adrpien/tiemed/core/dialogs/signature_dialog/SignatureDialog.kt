package com.adrpien.tiemed.core.dialogs.signature_dialog

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

        // Inflating signatureLayout View with custom SignatureView layout
        val signatureLayout = layoutInflater.inflate(R.layout.view_signature, null)

        // Receive result form DialogFragment
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(activity)
            builder
                .setCancelable(true)
                .setTitle(getString(R.string.signature))
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                        requireActivity().supportFragmentManager.setFragmentResult(
                            getString(R.string.signature_request_key),
                            // Adding ByteArray with Bitmap to Bundle
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

    // Children objects has method to draw on a canvas, not the other way round?
    // Capture Signature, compress to JPEG and return as ByteArray
    private fun captureSignature(signatureLayout: View): ByteArray {
        // Creates empty Bitmap
        val signatureBitmap = Bitmap.createBitmap(signatureLayout.width, signatureLayout.height, Bitmap.Config.ARGB_8888)
        // Creates Canvas with specified (empty) Bitmap
        val canvas = Canvas(signatureBitmap)
        // Manually render this view (and all of its children) to the given Canvas.
        // Updates Bitmap with signature
        signatureLayout.draw(canvas)

        // Converting Bitmap with signature to ByteArray
        val signatureByteArrayOutputStream = ByteArrayOutputStream()
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, signatureByteArrayOutputStream)
        return signatureByteArrayOutputStream.toByteArray()
    }
}