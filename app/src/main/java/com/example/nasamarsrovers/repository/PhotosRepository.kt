package com.example.nasamarsrovers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import com.example.nasamarsrovers.repository.net.interfaces.RoverApi
import com.example.nasamarsrovers.utils.DEFAULT_CAMERA
import com.example.nasamarsrovers.utils.DEFAULT_DATE
import com.example.nasamarsrovers.utils.DEFAULT_SOL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val roverApi: RoverApi) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    @ExperimentalCoroutinesApi
    fun getPhotosFlow(params: RoverQueryParameters): Flow<List<Photo>> {
        return when {
            params.camera == null -> {
                getFlowForNoCamera(params)
            }
            params.sol != null -> {
                getFlowBySolAndCamera(params)
            }
            params.earthDate != null -> {
                getFlowByDateAndCamera(params)
            }
            else -> emptyFlow()
        }
    }

    private fun getFlowByDateAndCamera(params: RoverQueryParameters) =
        flow {
            emit(
                roverApi.getPhotosByDateAndCamera(
                    rover = params.rover,
                    camera = params.camera ?: DEFAULT_CAMERA,
                    date = params.earthDate ?: DEFAULT_DATE,
                    page = params.page
                ).photos
                    ?: emptyList()
            )
        }.flowOn(Dispatchers.IO)

    private fun getFlowBySolAndCamera(params: RoverQueryParameters) =
        flow {
            emit(
                roverApi.getPhotosBySolAndCamera(
                    rover = params.rover,
                    sol = params.sol ?: DEFAULT_SOL,
                    camera = params.camera ?: DEFAULT_CAMERA,
                    page = params.page
                ).photos
                    ?: emptyList()
            )
        }.flowOn(Dispatchers.IO)

    private fun getFlowForNoCamera(params: RoverQueryParameters) =
        flow {
            emit(
                roverApi.getPhotosBySol(
                    rover = params.rover,
                    sol = params.sol ?: DEFAULT_SOL,
                    page = params.page
                ).photos ?: emptyList()
            )
        }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    fun getMaxSolForRover(roverName: String): Flow<Int> {
        return flow {
            emit(roverApi.getRoverManifest(roverName).photoManifest?.maxSol ?: 0)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getMaxEarthDateForRover(roverName: String): Flow<String> {
        return flow {
            emit(roverApi.getRoverManifest(roverName).photoManifest?.maxDate.orEmpty())
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getLandingDateForRover(roverName: String): Flow<String> {
        return flow {
            emit(roverApi.getRoverManifest(roverName).photoManifest?.landingDate.orEmpty())
        }.flowOn(Dispatchers.IO)
    }
}