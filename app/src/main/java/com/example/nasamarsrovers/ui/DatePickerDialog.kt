package com.example.nasamarsrovers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.DATE_FORMAT
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DatePickerDialog : DialogFragment() {

    private val calendar = Calendar.getInstance()
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val galleryState = galleryViewModel.state.value

        val maxDateCalendar = parseDateToCalendar(galleryState.maxEarthDate)
        val minDateCalendar = parseDateToCalendar(galleryState.landingDate)
        val selectedDateCalendar =
            parseDateToCalendar(galleryState.currentEarthDate)
                ?: maxDateCalendar
                ?: Calendar.getInstance()

        val composeView = ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    com.example.nasamarsrovers.ui.EarthDatePickerDialogContent(
                        initialDateMillis = selectedDateCalendar.timeInMillis,
                        minDateMillis = minDateCalendar?.timeInMillis,
                        maxDateMillis = maxDateCalendar?.timeInMillis,
                        onConfirm = { year: Int, month: Int, dayOfMonth: Int ->
                            calendar.set(year, month, dayOfMonth)
                            galleryViewModel.setEarthDate(DATE_FORMAT.format(calendar.time))
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

    private fun parseDateToCalendar(dateString: String?): Calendar? {
        if (dateString.isNullOrBlank()) return null
        val date = DATE_FORMAT.parse(dateString) ?: return null

        return Calendar.getInstance().apply {
            time = date
        }
    }
}
