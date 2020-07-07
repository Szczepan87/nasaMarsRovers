package com.example.nasamarsrovers.utils

import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.utils.glide.GlideImageLoader

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun ImageView.imageUrl(url: String) {
        Glide.with(this.context)
            .load(url)
            .error(R.drawable.ic_baseline_error_outline_24)
            .placeholder(R.drawable.ic_baseline_camera_300)
            .into(this)
    }

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