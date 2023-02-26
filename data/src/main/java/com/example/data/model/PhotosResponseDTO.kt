package com.example.data.model

import com.google.gson.annotations.SerializedName

data class PhotosResponseDTO(
    @SerializedName("photos")
    val photos: List<PhotoDTO>?
)
