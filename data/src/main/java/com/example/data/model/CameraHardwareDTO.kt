package com.example.data.model

import com.google.gson.annotations.SerializedName

data class CameraHardwareDTO(
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("name")
    val name: String?
)
