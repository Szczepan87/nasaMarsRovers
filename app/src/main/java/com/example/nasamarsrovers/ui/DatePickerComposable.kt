package com.example.nasamarsrovers.ui

import android.widget.DatePicker
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
import androidx.compose.runtime.mutableStateOf
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
import java.util.Calendar

private data class DateSelection(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int
)

@Composable
fun EarthDatePickerDialogContent(
    initialDateMillis: Long,
    minDateMillis: Long?,
    maxDateMillis: Long?,
    onConfirm: (year: Int, month: Int, dayOfMonth: Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selection by remember(initialDateMillis) {
        mutableStateOf(initialDateFromMillis(initialDateMillis))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_earth_date),
            style = MaterialTheme.typography.h6
        )

        EarthDatePicker(
            selection = selection,
            minDateMillis = minDateMillis,
            maxDateMillis = maxDateMillis,
            onSelectionChanged = { selection = it }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onCancel) {
                Text(text = stringResource(R.string.cancel))
            }
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = {
                    onConfirm(selection.year, selection.month, selection.dayOfMonth)
                }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        }
    }
}

@Composable
private fun EarthDatePicker(
    selection: DateSelection,
    minDateMillis: Long?,
    maxDateMillis: Long?,
    onSelectionChanged: (DateSelection) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = {
            DatePicker(context).apply {
                minDateMillis?.let { minDate = it }
                maxDateMillis?.let { maxDate = it }
                init(selection.year, selection.month, selection.dayOfMonth) { _, year, month, dayOfMonth ->
                    onSelectionChanged(DateSelection(year, month, dayOfMonth))
                }
            }
        },
        update = { picker ->
            minDateMillis?.let { picker.minDate = it }
            maxDateMillis?.let { picker.maxDate = it }

            if (
                picker.year != selection.year ||
                picker.month != selection.month ||
                picker.dayOfMonth != selection.dayOfMonth
            ) {
                picker.updateDate(selection.year, selection.month, selection.dayOfMonth)
            }

            picker.init(selection.year, selection.month, selection.dayOfMonth) { _, year, month, dayOfMonth ->
                onSelectionChanged(DateSelection(year, month, dayOfMonth))
            }
        }
    )
}

private fun initialDateFromMillis(millis: Long): DateSelection {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    return DateSelection(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    )
}

@Preview(showBackground = true)
@Composable
private fun EarthDatePickerDialogContentPreview() {
    MaterialTheme {
        EarthDatePickerDialogContent(
            initialDateMillis = Calendar.getInstance().timeInMillis,
            minDateMillis = null,
            maxDateMillis = null,
            onConfirm = { _, _, _ -> },
            onCancel = {}
        )
    }
}

