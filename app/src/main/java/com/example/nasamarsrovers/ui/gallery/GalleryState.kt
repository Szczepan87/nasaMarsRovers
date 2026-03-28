package com.example.nasamarsrovers.ui.gallery

import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.DEFAULT_DATE
import com.example.nasamarsrovers.utils.DEFAULT_SOL

data class GalleryState(
    val currentRover: String = CURIOSITY,
    val currentCamera: String? = null,
    val currentSol: Int? = null,
    val currentEarthDate: String? = null,
    val listOfPhotos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val isListEmpty: Boolean = false,
    val maxSolForRover: Int = 0,
    val maxEarthDate: String? = null,
    val landingDate: String? = null,
    val isEarthDateUsed: Boolean = false
) {
    fun toQueryParameters(): RoverQueryParameters {
        return if (isEarthDateUsed) {
            RoverQueryParameters(
                rover = currentRover,
                earthDate = currentEarthDate ?: DEFAULT_DATE,
                camera = currentCamera
            )
        } else {
            RoverQueryParameters(
                rover = currentRover,
                sol = currentSol ?: DEFAULT_SOL,
                camera = currentCamera
            )
        }
    }
}

sealed interface GalleryEvent {
    data class ShowError(val message: String) : GalleryEvent
}

