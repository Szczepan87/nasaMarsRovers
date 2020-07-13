package com.example.nasamarsrovers.utils

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.nasamarsrovers.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("value")
    fun NumberPicker.bindValue(value: Int) {
        this.value = value
    }

    @JvmStatic
    @BindingAdapter("earthDate")
    fun TextView.earthDate(earthDate: String) {
        text = resources.getString(R.string.earth_date, earthDate )
    }

    @JvmStatic
    @BindingAdapter("sol")
    fun TextView.sol(sol: Int) {
        text = resources.getString(R.string.sol, sol)
    }
}