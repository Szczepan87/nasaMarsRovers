package com.example.nasamarsrovers.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasamarsrovers.model.Photos
import com.example.nasamarsrovers.repository.PhotosRepository
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: PhotosRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    private val _listOfPhotos = MutableLiveData<List<Photos>>()
    val listOfPhotos: LiveData<List<Photos>>
        get() = _listOfPhotos

    init {
        repository.roverPhotos.observeForever(Observer { })
    }

    fun updatePhotosList() {
        viewModelScope.launch { repository.retrievePhotos(1) }
    }
}