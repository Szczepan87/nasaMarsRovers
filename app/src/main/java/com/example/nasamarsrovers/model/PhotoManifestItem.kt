package com.example.nasamarsrovers.model

import com.google.gson.annotations.SerializedName

data class PhotoManifestItem(
    @SerializedName("sol")
    val sol: Int?,
    @SerializedName("earth_date")
    val earthDate: String?,
    @SerializedName("total_photos")
    val totalPhotos: Int?,
    @SerializedName("cameras")
    val cameras: List<String>?
)
