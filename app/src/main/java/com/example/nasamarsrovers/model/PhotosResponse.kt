package com.example.nasamarsrovers.model


import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photos")
    val photos: List<Photo>?
)