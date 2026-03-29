package com.example.nasamarsrovers.ui

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.ui.gallery.GalleryState

@Composable
fun SolPickerDialogContent(
    state: GalleryState,
    onConfirm: (Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxSol = state.maxSolForRover.coerceAtLeast(0)
    var selectedSol by remember(state.currentSol, maxSol) {
        mutableIntStateOf((state.currentSol ?: 0).coerceIn(0, maxSol))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sol_dialog_title),
            style = MaterialTheme.typography.h6
        )

        SolNumberPicker(
            value = selectedSol,
            min = 0,
            max = maxSol,
            onValueChange = { selectedSol = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onCancel) {
                Text(text = stringResource(R.string.cancel))
            }
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = { onConfirm(selectedSol) }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        }
    }
}

@Composable
fun SolNumberPicker(
    value: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            NumberPicker(context).apply {
                minValue = min
                maxValue = max
                wrapSelectorWheel = false
                this.value = value.coerceIn(min, max)
                setOnValueChangedListener { _, _, newVal -> onValueChange(newVal) }
            }
        },
        update = { picker ->
            if (picker.minValue != min) picker.minValue = min
            if (picker.maxValue != max) picker.maxValue = max
            val coercedValue = value.coerceIn(min, max)
            if (picker.value != coercedValue) {
                picker.value = coercedValue
            }
            picker.setOnValueChangedListener { _, _, newVal -> onValueChange(newVal) }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SolPickerDialogContentPreview() {
    MaterialTheme {
        SolPickerDialogContent(
            state = GalleryState(currentSol = 10, maxSolForRover = 100),
            onConfirm = {},
            onCancel = {}
        )
    }
}

