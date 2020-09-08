package com.example.nasamarsrovers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.repository.net.NasaRoversApi
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PhotosRepository(private val nasaRoversApi: NasaRoversApi) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    fun getPhotosFlow(roverName: String, sol: Int, camera: String): Flow<List<Photo>> {
        return flow {
            val list = when (roverName) {
                CURIOSITY -> nasaRoversApi.getCuriosityPhotosBySolAndCamera(sol, camera).photos
                    ?: emptyList()
                OPPORTUNITY -> nasaRoversApi.getOpportunityPhotosBySolAndCamera(sol, camera).photos
                    ?: emptyList()
                SPIRIT -> nasaRoversApi.getSpiritPhotosBySolAndCamera(sol, camera).photos
                    ?: emptyList()
                else -> nasaRoversApi.getCuriosityPhotosBySol(sol).photos ?: emptyList()
            }
            emit(list)
        }.flowOn(Dispatchers.IO)
    }

    fun getPhotosFlow(roverName: String, date:String, camera: String): Flow<List<Photo>> {
        return flow {
            val list = when (roverName) {
                CURIOSITY -> nasaRoversApi.getCuriosityPhotosByDateAndCamera(date, camera).photos
                    ?: emptyList()
                OPPORTUNITY -> nasaRoversApi.getOpportunityPhotosByDateAndCamera(date, camera).photos
                    ?: emptyList()
                SPIRIT -> nasaRoversApi.getSpiritPhotosByDateAndCamera(date, camera).photos
                    ?: emptyList()
                else -> nasaRoversApi.getCuriosityPhotosBySol(0).photos ?: emptyList()
            }
            emit(list)
        }.flowOn(Dispatchers.IO)
    }
}