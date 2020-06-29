package com.example.nasamarsrovers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel

class CameraPicker(private val galleryViewModel: GalleryViewModel) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle("Camera")
        builder.setItems(
            // TODO set array depending on currently selected rover
            R.array.curiosity_cameras
        ) { dialog, which ->
            // TODO set currently selected camera in VM
            dialog.dismiss() }
        return builder.create()
    }
}