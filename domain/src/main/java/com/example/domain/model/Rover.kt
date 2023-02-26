package com.example.domain.model

import com.example.domain.model.CameraHardware
import com.google.gson.annotations.SerializedName

data class Rover(
    @SerializedName("cameras")
    val cameras: List<CameraHardware>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("landing_date")
    val landingDate: String?,
    @SerializedName("launch_date")
    val launchDate: String?,
    @SerializedName("max_date")
    val maxDate: String?,
    @SerializedName("max_sol")
    val maxSol: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("total_photos")
    val totalPhotos: Int?
)
