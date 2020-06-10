package com.example.nasamarsrovers.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosRepository(private val nasaRoversApi: NasaRoversApi) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    suspend fun retrievePhotos(){
        val response = nasaRoversApi.getCuriosityPhotosBySol()
        withContext(Dispatchers.IO){
            val list: List<Photo> = response.await().photos ?: emptyList()
            _roverPhotos.postValue(list)
            Log.d("REPOSITORY", "Updating with ${list.first()}")
        }
    }
}