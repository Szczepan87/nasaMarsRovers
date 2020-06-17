package com.example.nasamarsrovers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.model.Rover
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosRepository(private val nasaRoversApi: NasaRoversApi) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    suspend fun retrievePhotos(roverName: String, sol: Int) {
        val response = when (roverName) {
            CURIOSITY -> nasaRoversApi.getCuriosityPhotosBySol(sol)
            OPPORTUNITY -> nasaRoversApi.getOpportunityPhotosBySol(sol)
            SPIRIT -> nasaRoversApi.getSpiritPhotosBySol(sol)
            else -> nasaRoversApi.getCuriosityPhotosBySol(sol)
        }

        withContext(Dispatchers.IO) {
            val list: List<Photo> = response.await().photos ?: emptyList()
            _roverPhotos.postValue(list)
        }
    }
}