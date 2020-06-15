package com.example.nasamarsrovers.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.PhotosRepository
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: PhotosRepository) : ViewModel() {
    private val _currentRover = MutableLiveData<String>()
    val currentRover: LiveData<String>
        get() = _currentRover

    val sol = MutableLiveData<Int>(0)

    private val _listOfPhotos = MutableLiveData<List<Photo>>()
    val listOfPhotos: LiveData<List<Photo>>
        get() = _listOfPhotos

    init {
        repository.roverPhotos.observeForever(Observer { _listOfPhotos.postValue(it) })
    }

    fun updatePhotosList() {
        viewModelScope.launch { repository.retrievePhotos() }
    }

    fun setCurrentRover(roverName: String) {
        _currentRover.postValue(roverName)
    }
}