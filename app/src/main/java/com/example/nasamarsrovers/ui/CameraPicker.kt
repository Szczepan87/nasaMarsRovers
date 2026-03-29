package com.example.nasamarsrovers.ui

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
import com.example.nasamarsrovers.R

@Composable
fun CameraPickerDialogContent(
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
