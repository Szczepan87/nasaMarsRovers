package com.example.nasamarsrovers.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.nasamarsrovers.R

class GlideImageLoader(
    private val imageView: ImageView,
    private val progressBar: ProgressBar
) {

    fun load(url: String) {
        showProgressBar()

        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.ic_baseline_error_outline_24)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ) = hideProgressBar()

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ) = hideProgressBar()
            })
            .into(imageView)
    }

    private fun hideProgressBar(): Boolean {
        progressBar.visibility = View.INVISIBLE
        return false
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}
