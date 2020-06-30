package com.example.nasamarsrovers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.CURIOSITY

class CameraPicker(private val galleryViewModel: GalleryViewModel) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)

        val currentRover = galleryViewModel.currentRover.value
        val cameraArray =
            if (currentRover == CURIOSITY) resources.getStringArray(R.array.curiosity_cameras)
            else resources.getStringArray(R.array.spirit_and_opportunity_cameras)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select camera:")
        builder.setItems(cameraArray) { dialog, which ->
            galleryViewModel.setCurrentCamera(cameraArray[which])
            dialog.dismiss()
        }
        return builder.create()
    }
}