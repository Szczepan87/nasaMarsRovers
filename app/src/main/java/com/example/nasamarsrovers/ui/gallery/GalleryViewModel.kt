package com.example.nasamarsrovers.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.DATE_FORMAT
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*

class GalleryViewModel(private val repository: PhotosRepository) : ViewModel() {
    private val _currentRover = MutableLiveData<String>()
    val currentRover: LiveData<String>
        get() = _currentRover

    private val _currentCamera = MutableLiveData<String>()
    val currentCamera: LiveData<String>
        get() = _currentCamera

    private val _currentSol = MutableLiveData<Int>()
    val currentSol: LiveData<Int>
        get() = _currentSol

    private val _currentEarthDate = MutableLiveData<String>()
    val currentEarthDate: LiveData<String>
        get() = _currentEarthDate

    private val _listOfPhotos = MutableLiveData<List<Photo>>()
    val listOfPhotos: LiveData<List<Photo>>
        get() = _listOfPhotos

    private val _repositoryError = MutableLiveData<String?>().apply {
        value = null }
    val repositoryError: LiveData<String?>
        get() = _repositoryError

    var isEarthDateUsed = false

    init {
        repository.roverPhotos.observeForever(Observer { _listOfPhotos.postValue(it) })
        currentSol.observeForever(Observer { Log.d("VIEW MODEL", "SOL: ${currentSol.value}") })
        currentRover.observeForever(Observer {
            Log.d(
                "VIEW MODEL",
                "ROVER: ${currentRover.value}"
            )
        })
        currentCamera.observeForever { Log.d("VIEW MODEl", "CAMERA: ${currentCamera.value}") }
        currentEarthDate.observeForever { Log.d("VIEW MODEl", "DATE: ${currentEarthDate.value}") }
    }

    fun updatePhotosList() {
        viewModelScope.launch {
            if (isEarthDateUsed) {
                repository.getPhotosFlow(
                    currentRover.value ?: CURIOSITY,
                    currentEarthDate.value ?: "2012-08-06",
                    currentCamera.value ?: "FHAZ"
                )
                    .onStart { // info about loading
                    }
                    .catch { error -> _repositoryError.postValue(error.message) }
                    .collect { list -> _listOfPhotos.postValue(list) }
            } else {
                repository.getPhotosFlow(
                    currentRover.value ?: CURIOSITY,
                    currentSol.value ?: 0,
                    currentCamera.value ?: "FHAZ"
                )
                    .onStart { // info about loading
                     }
                    .catch { error -> _repositoryError.postValue(error.message) }
                    .collect { list -> _listOfPhotos.postValue(list) }

            }
        }
    }

    fun setCurrentRover(roverName: String) {
        _currentRover.postValue(roverName)
    }

    fun setSol(solNo: Int) {
        _currentSol.postValue(solNo)
    }

    fun setCurrentCamera(currentCamera: String) {
        _currentCamera.postValue(currentCamera)
    }

    fun setEarthDate(date: String) {
        _currentEarthDate.postValue(date)
    }

    // TODO retrieve manifest data about max sol, date etc.
}