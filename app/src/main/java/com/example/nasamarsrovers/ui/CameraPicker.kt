package com.example.nasamarsrovers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.model.CuriosityCameras
import com.example.nasamarsrovers.model.SpiritAndOpportunityCameras
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.CURIOSITY

class CameraPicker : DialogFragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)

        val currentRover = galleryViewModel.currentRover.value
        val cameraArray: Array<String> =
            if (currentRover == CURIOSITY || currentRover == null) {
                CuriosityCameras.values()
                    .map { resources.getString(it.resId) }
                    .toTypedArray()
            } else {
                SpiritAndOpportunityCameras.values()
                    .map { resources.getString(it.resId) }
                    .toTypedArray()
            }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.select_camera))
        builder.setItems(cameraArray) { dialog, which ->
            galleryViewModel.setCurrentCamera(cameraArray[which])
            dialog.dismiss()
        }
        return builder.create()
    }
}
