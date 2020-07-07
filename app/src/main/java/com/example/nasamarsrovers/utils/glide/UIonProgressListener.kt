package com.example.nasamarsrovers.utils.glide

interface UIonProgressListener {

    val granularityPercentage: Float

    fun onProgress(byteRead:Long, expectedLength: Long)
}