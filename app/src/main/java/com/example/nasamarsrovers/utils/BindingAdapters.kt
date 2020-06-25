package com.example.nasamarsrovers.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.nasamarsrovers.R

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
}