package com.example.nasamarsrovers.ui.gallery

import android.util.Log
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
        value = null
    }
    val repositoryError: LiveData<String?>
        get() = _repositoryError

    val _isLoading = MutableLiveData<Boolean>(true)
//    val isLoading: LiveData<Boolean>
//        get() = _isLoading

    val _isListEmpty = MutableLiveData<Boolean>(false)

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

    @ExperimentalCoroutinesApi
    fun updatePhotosList() {
        viewModelScope.launch {
            if (isEarthDateUsed) {
                repository.getPhotosFlow(
                    currentRover.value ?: CURIOSITY,
                    currentEarthDate.value ?: "2012-08-06",
                    currentCamera.value ?: "FHAZ"
                )
                    .onStart {
                        _isLoading.postValue(true)
                        _listOfPhotos.postValue(emptyList())
                        Log.d("VIEW MODEl", "LOADING ON START ${_isLoading.value}")
                    }
                    .catch { error ->
                        _repositoryError.postValue(error.message)
                        _isLoading.postValue(false)
                        Log.d("VIEW MODEl", "LOADING ON ERROR ${_isLoading.value}")
                    }
                    .collect { list ->
                        _listOfPhotos.postValue(list)
                        _isLoading.postValue(false)
                        Log.d("VIEW MODEl", "LOADING ON SUCCESS ${_isLoading.value}")
                    }
            } else {
                repository.getPhotosFlow(
                    currentRover.value ?: CURIOSITY,
                    currentSol.value ?: 0,
                    currentCamera.value ?: "FHAZ"
                )
                    .onStart {
                        _isLoading.postValue(true)
                        Log.d("VIEW MODEl", "LOADING ON START ${_isLoading.value}")
                    }
                    .catch { error ->
                        _repositoryError.postValue(error.message)
                        _isLoading.postValue(false)
                        _isListEmpty.postValue(true)
                        Log.d("VIEW MODEl", "LOADING ON ERROR ${_isLoading.value}")
                    }
                    .collect { list ->
                        _listOfPhotos.postValue(list)
                        _isLoading.postValue(false)
                        if (list.isNullOrEmpty()) {
                            _isListEmpty.postValue(true)
                        } else {
                            _isListEmpty.postValue(false)
                        }
                        Log.d("VIEW MODEl", "LOADING ON SUCCESS ${_isLoading.value}")
                    }
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

    override fun onCleared() {
        // TODO remove observers
        super.onCleared()
    }

    // TODO retrieve manifest data about max sol, date etc.
}