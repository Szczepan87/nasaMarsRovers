package com.example.data.model

import com.example.data.model.PhotoManifestDTO
import com.google.gson.annotations.SerializedName

data class PhotoManifestResponseDTO(
    @SerializedName("photo_manifest")
    val photoManifest: PhotoManifestDTO?
)
