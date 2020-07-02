package com.example.nasamarsrovers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.net.NasaRoversApi
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosRepository(private val nasaRoversApi: NasaRoversApi) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    suspend fun retrievePhotos(roverName: String, sol: Int, camera: String) {
        val response = when (roverName) {
            CURIOSITY -> nasaRoversApi.getCuriosityPhotosBySolAndCamera(sol, camera)
            OPPORTUNITY -> nasaRoversApi.getOpportunityPhotosBySolAndCamera(sol, camera)
            SPIRIT -> nasaRoversApi.getSpiritPhotosBySolAndCamera(sol, camera)
            else -> nasaRoversApi.getCuriosityPhotosBySol(sol)
        }

        withContext(Dispatchers.IO) {
            val list: List<Photo> = response.await().photos ?: emptyList()
            _roverPhotos.postValue(list)
        }
    }

    suspend fun retrievePhotos(roverName: String, date: String, camera: String) {
        val response = when (roverName) {
            CURIOSITY -> nasaRoversApi.getCuriosityPhotosByDateAndCamera(date, camera)
            OPPORTUNITY -> nasaRoversApi.getOpportunityPhotosByDateAndCamera(date, camera)
            SPIRIT -> nasaRoversApi.getSpiritPhotosByDateAndCamera(date, camera)
            else -> nasaRoversApi.getCuriosityPhotosBySol(0)
        }

        withContext(Dispatchers.IO) {
            val list: List<Photo> = response.await().photos ?: emptyList()
            _roverPhotos.postValue(list)
        }
    }
}