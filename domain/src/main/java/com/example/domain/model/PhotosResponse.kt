package com.example.domain.model

import com.example.domain.model.Photo
import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photos")
    val photos: List<Photo>?
)
