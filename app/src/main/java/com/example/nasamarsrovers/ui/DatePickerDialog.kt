package com.example.nasamarsrovers.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import java.util.*

class DatePickerDialog(private val galleryViewModel: GalleryViewModel) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year,month,dayOfMonth)
        TODO("Not yet implemented")
    }
}
