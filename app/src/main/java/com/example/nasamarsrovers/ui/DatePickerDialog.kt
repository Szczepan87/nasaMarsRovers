package com.example.nasamarsrovers.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.DATE_FORMAT
import java.util.*

class DatePickerDialog :
    DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val maxStringDate = galleryViewModel.maxEarthDate.value
        val maxYear = maxStringDate?.split("-")?.first()?.toInt()
        val maxMonth = maxStringDate?.split("-")?.get(1)?.toInt()?.minus(1)
        val maxDay = maxStringDate?.split("-")?.last()?.toInt()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireActivity(),
            this,
            maxYear ?: year,
            maxMonth ?: month,
            maxDay ?: day
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        galleryViewModel.isEarthDateUsed = true
        Log.d("DATE PICKER", "EARTH DATE USED: ${galleryViewModel.isEarthDateUsed}")

        galleryViewModel.setEarthDate(DATE_FORMAT.format(calendar.time))
    }
}
