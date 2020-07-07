package com.example.nasamarsrovers.utils.glide

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class GlideImageLoader(
    private val mImageView: ImageView?,
    private val mProgressBar: ProgressBar?
) {

    fun load(url: String?, options: RequestOptions?) {
        if (options == null) return
        Log.d("GLIDE IMAGE LOADER", "Loading: $url")
        onConnecting()

        DispatchingProgressManager.expect(url, object : UIonProgressListener {
            override val granularityPercentage: Float
                get() = 1.0f

            override fun onProgress(byteRead: Long, expectedLength: Long) {
                Log.d("GLIDE LOADER", "ProgressBar is null ${mProgressBar == null}")
                if (mProgressBar != null) {
                    mProgressBar.progress = (100 * byteRead / expectedLength).toInt()
                    Log.d("GLIDE LOADER", "Updating with: ${100 * byteRead / expectedLength}")
                }
            }
        })

        Glide.with(mImageView!!.context)
            .load(url)
            .apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    DispatchingProgressManager.forget(url)
                    onFinished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    DispatchingProgressManager.forget(url)
                    onFinished()
                    return false
                }
            })
            .into(mImageView)
    }

    private fun onConnecting() {
        if (mProgressBar != null) mProgressBar.visibility = View.VISIBLE
    }

    private fun onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.visibility = View.INVISIBLE
            mImageView.visibility = View.VISIBLE
        }
    }
}