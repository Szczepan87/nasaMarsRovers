package com.example.nasamarsrovers.model

import com.google.gson.annotations.SerializedName

data class PhotoManifestResponse(
    @SerializedName("photo_manifest")
    val photoManifest: PhotoManifest?
)
