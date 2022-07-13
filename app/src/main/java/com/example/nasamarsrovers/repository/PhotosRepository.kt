package com.example.nasamarsrovers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.net.interfaces.RoverApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val roverApi: RoverApi) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    @ExperimentalCoroutinesApi
    fun getPhotosFlow(roverName: String, sol: Int, camera: String): Flow<List<Photo>> {
        return flow {
            val list = roverApi.getPhotosBySolAndCamera(roverName, sol, camera).photos
                ?: emptyList()
            emit(list)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getPhotosFlow(roverName: String, date: String, camera: String): Flow<List<Photo>> {
        return flow {
            val list = roverApi.getPhotosByDateAndCamera(roverName, date, camera).photos
                ?: emptyList()
            emit(list)
        }.flowOn(Dispatchers.IO)
    }

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