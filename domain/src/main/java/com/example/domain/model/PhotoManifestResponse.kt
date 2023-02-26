package com.example.domain.model

import com.example.domain.model.PhotoManifest
import com.google.gson.annotations.SerializedName

data class PhotoManifestResponse(
    @SerializedName("photo_manifest")
    val photoManifest: PhotoManifest?
)
