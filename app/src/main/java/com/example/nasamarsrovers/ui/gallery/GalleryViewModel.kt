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
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: PhotosRepository) : ViewModel() {
    private val _currentRover = MutableLiveData<String>(CURIOSITY)
    val currentRover: LiveData<String>
        get() = _currentRover

    private val _currentCamera = MutableLiveData<String>("FHAZ")
    val currentCamera: LiveData<String>
        get() = _currentCamera

    private val _currentSol = MutableLiveData<Int>(0)
    val currentSol: LiveData<Int>
        get() = _currentSol

    private val _listOfPhotos = MutableLiveData<List<Photo>>()
    val listOfPhotos: LiveData<List<Photo>>
        get() = _listOfPhotos

    init {
        repository.roverPhotos.observeForever(Observer { _listOfPhotos.postValue(it) })
        currentSol.observeForever(Observer { Log.d("VIEW MODEL", "SOL: ${currentSol.value}") })
        currentRover.observeForever(Observer {
            Log.d(
                "VIEW MODEL",
                "ROVER: ${currentRover.value}"
            )
        })
        currentCamera.observeForever {Log.d("VIEW MODEl", "CAMERA: ${currentCamera.value}")}
    }

    fun updatePhotosList() {
        viewModelScope.launch {
            repository.retrievePhotos(
                currentRover.value ?: CURIOSITY,
                currentSol.value ?: 0,
                currentCamera.value ?: "FHAZ"
            )
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

    // TODO retrieve manifest data about max sol, date etc.
}