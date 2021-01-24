package com.example.nasamarsrovers.model


import com.google.gson.annotations.SerializedName

data class PhotoManifest(
    @SerializedName("name")
    val name: String?,
    @SerializedName("landing_date")
    val landingDate: String?,
    @SerializedName("launch_date")
    val launchDate: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("max_sol")
    val maxSol: Int?,
    @SerializedName("max_date")
    val maxDate: String?,
    @SerializedName("total_photos")
    val totalPhotos: Int?,
    @SerializedName("photos")
    val photos: List<PhotoManifestItem>?
)