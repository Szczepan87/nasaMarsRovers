package com.example.nasamarsrovers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosRepository(private val nasaRoversApi: NasaRoversApi) {

    private val _roverPhotos = MutableLiveData<List<Photos>>()
    val roverPhotos: LiveData<List<Photos>>
        get() = _roverPhotos

    suspend fun retrievePhotos(sol: Int){
        val response = nasaRoversApi.getCuriosityPhotosBySol(sol)
        withContext(Dispatchers.IO){
            val list: List<Photos> = response.execute().body() ?: emptyList()
            _roverPhotos.postValue(list)
        }
    }
}