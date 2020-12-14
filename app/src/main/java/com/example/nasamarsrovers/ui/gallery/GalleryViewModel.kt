package com.example.nasamarsrovers.ui.gallery

import androidx.lifecycle.*
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.utils.CURIOSITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: PhotosRepository) : ViewModel() {
    private val _currentRover = MutableLiveData<String>()
    val currentRover: LiveData<String> = _currentRover

    private val _currentCamera = MutableLiveData<String>()
    val currentCamera: LiveData<String> = _currentCamera

    private val _currentSol = MutableLiveData<Int>()
    val currentSol: LiveData<Int> = _currentSol

    private val _currentEarthDate = MutableLiveData<String>()
    val currentEarthDate: LiveData<String> = _currentEarthDate

    private val _listOfPhotos = MutableLiveData<List<Photo>>()
    val listOfPhotos: LiveData<List<Photo>> = _listOfPhotos

    private val _repositoryError = MutableLiveData<String?>(null)
    val repositoryError: LiveData<String?> = _repositoryError

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isListEmpty = MutableLiveData<Boolean>(false)
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    var isEarthDateUsed = false

    var roverPhotosObserver = Observer<List<Photo>> { _listOfPhotos.postValue(it) }

    init {
        repository.roverPhotos.observeForever(roverPhotosObserver)
    }

    @ExperimentalCoroutinesApi
    fun updatePhotosList() {
        viewModelScope.launch {
            if (isEarthDateUsed) {
                repository.getPhotosFlow(
                    currentRover.value ?: CURIOSITY,
                    currentEarthDate.value ?: "2012-08-06",
                    currentCamera.value ?: "FHAZ"
                )
                    .onStart { doOnStart() }
                    .catch { error -> doOnError(error) }
                    .collect { list -> doOnCollect(list) }
            } else {
                repository.getPhotosFlow(
                    currentRover.value ?: CURIOSITY,
                    currentSol.value ?: 0,
                    currentCamera.value ?: "FHAZ"
                )
                    .onStart { doOnStart() }
                    .catch { error -> doOnError(error) }
                    .collect { list -> doOnCollect(list) }
            }
        }
    }

    private fun doOnStart() {
        _isLoading.postValue(true)
        _isListEmpty.postValue(false)
    }

    private fun doOnError(exception: Throwable) {
        _repositoryError.postValue(exception.message)
        _isLoading.postValue(false)
        _isListEmpty.postValue(true)
    }

    private fun doOnCollect(list: List<Photo>) {
        _listOfPhotos.postValue(list)
        _isLoading.postValue(false)
        if (list.isNullOrEmpty()) {
            _isListEmpty.postValue(true)
        } else {
            _isListEmpty.postValue(false)
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

    override fun onCleared() {
        repository.roverPhotos.removeObserver(roverPhotosObserver)
        super.onCleared()
    }

    // TODO retrieve manifest data about max sol, date etc.
}