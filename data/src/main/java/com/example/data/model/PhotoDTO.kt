package com.example.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDTO(
    @SerializedName("camera")
    val camera: CameraDTO?,
    @SerializedName("earth_date")
    val earthDate: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("img_src")
    val imgSrc: String?,
    @SerializedName("rover")
    val rover: RoverDTO?,
    @SerializedName("sol")
    val sol: Int?
)
