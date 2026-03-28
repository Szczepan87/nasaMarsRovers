package com.example.nasamarsrovers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.model.CuriosityCameras
import com.example.nasamarsrovers.model.SpiritAndOpportunityCameras
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.CURIOSITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraPicker : DialogFragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cameraOptions = getCameraOptions()

        val composeView = ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    CameraPickerDialogContent(
                        cameraOptions = cameraOptions,
                        onSelect = { camera ->
                            galleryViewModel.setCurrentCamera(camera)
                            dismiss()
                        },
                        onCancel = { dismiss() }
                    )
                }
            }
        }

        return AlertDialog.Builder(requireActivity())
            .setView(composeView)
            .create()
    }

    private fun getCameraOptions(): List<String> {
        val currentRover = galleryViewModel.state.value.currentRover
        return if (currentRover == CURIOSITY) {
            CuriosityCameras.entries.map { getString(it.resId) }
        } else {
            SpiritAndOpportunityCameras.entries.map { getString(it.resId) }
        }
    }
}

@Composable
private fun CameraPickerDialogContent(
    cameraOptions: List<String>,
    onSelect: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_camera),
            style = MaterialTheme.typography.h6
        )

        cameraOptions.forEach { camera ->
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onSelect(camera) }
            ) {
                Text(
                    text = camera,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onCancel) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CameraPickerDialogContentPreview() {
    MaterialTheme {
        CameraPickerDialogContent(
            cameraOptions = listOf("FHAZ", "RHAZ", "NAVCAM", "ALL"),
            onSelect = {},
            onCancel = {}
        )
    }
}
