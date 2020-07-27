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

    private val _repositoryError = MutableLiveData<String?>().apply {
        value = null
    }
    val repositoryError: LiveData<String?>
        get() = _repositoryError

    suspend fun retrievePhotos(roverName: String, sol: Int, camera: String) {
        val response = when (roverName) {
            CURIOSITY ->
                safeApiCall(Dispatchers.IO) {
                    nasaRoversApi.getCuriosityPhotosBySolAndCamera(sol, camera)
                }
            OPPORTUNITY ->
                safeApiCall(Dispatchers.IO) {
                    nasaRoversApi.getOpportunityPhotosBySolAndCamera(sol, camera)
                }
            SPIRIT ->
                safeApiCall(
                    Dispatchers.IO
                ) {
                    nasaRoversApi.getSpiritPhotosBySolAndCamera(sol, camera)
                }
            else -> safeApiCall(Dispatchers.IO) { nasaRoversApi.getCuriosityPhotosBySol(sol) }
        }

        withContext(Dispatchers.IO) {

            var list: List<Photo> = emptyList()
            when (response) {
                is ApiResponseWrapper.Success -> {
                    list = response.value.await().body()?.photos ?: emptyList()
                    _roverPhotos.postValue(list)
                    _repositoryError.postValue(null)
                }
                is ApiResponseWrapper.GeneralError -> {
                    _roverPhotos.postValue(list)
                    _repositoryError.postValue(response.errorMessage)
                }
                is ApiResponseWrapper.NetworkError -> {
                    _roverPhotos.postValue(list)
                    _repositoryError.postValue("${response.code}: ${response.errorMessage}")
                }
            }
        }
    }

    suspend fun retrievePhotos(roverName: String, date: String, camera: String) {
        val response = when (roverName) {
            CURIOSITY -> safeApiCall(Dispatchers.IO) {
                nasaRoversApi.getCuriosityPhotosByDateAndCamera(
                    date,
                    camera
                )
            }
            OPPORTUNITY -> safeApiCall(Dispatchers.IO) {
                nasaRoversApi.getOpportunityPhotosByDateAndCamera(
                    date,
                    camera
                )
            }
            SPIRIT -> safeApiCall(Dispatchers.IO) {
                nasaRoversApi.getSpiritPhotosByDateAndCamera(
                    date,
                    camera
                )
            }
            else -> safeApiCall(Dispatchers.IO) { nasaRoversApi.getCuriosityPhotosBySol(0) }
        }

        withContext(Dispatchers.IO) {
            var list: List<Photo> = emptyList()
            when (response) {
                is ApiResponseWrapper.Success -> {
                    list = response.value.await().body()?.photos ?: emptyList()
                    _roverPhotos.postValue(list)
                    _repositoryError.postValue(null)
                }
                is ApiResponseWrapper.GeneralError -> {
                    _roverPhotos.postValue(list)
                    _repositoryError.postValue(response.errorMessage)
                }
                is ApiResponseWrapper.NetworkError -> {
                    _roverPhotos.postValue(list)
                    _repositoryError.postValue("${response.code}: ${response.errorMessage}")
                }
            }
        }
    }
}