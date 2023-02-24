package com.example.nasamarsrovers.model

import com.google.gson.annotations.SerializedName

data class CameraHardware(
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("name")
    val name: String?
)
