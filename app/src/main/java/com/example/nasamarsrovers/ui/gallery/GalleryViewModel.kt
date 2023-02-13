package com.example.nasamarsrovers.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import com.example.nasamarsrovers.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: PhotosRepository) : ViewModel() {
    companion object {
        private const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000
    }

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

    private val _maxSolForRover = MutableLiveData<Int>()
    val maxSolForRover: LiveData<Int> = _maxSolForRover

    private val _maxEarthDate = MutableLiveData<String>()
    val maxEarthDate = _maxEarthDate

    private val _landingDate = MutableLiveData<String>()
    val landingDate = _landingDate

    var isEarthDateUsed = false

    private var currentPage: Int = 1

    @ExperimentalCoroutinesApi
    fun loadFirstPage() {
        getMaxSolForRover()
        getMaxEarthDateForRover()
        getLandingDateForRover()
        currentPage = 1
        viewModelScope.launch {
            val params = getRoverQueryParams()
            repository.getPhotosFlow(params)
                .onStart { doOnStart() }
                .catch { error -> doOnError(error) }
                .collect { list -> doOnCollect(list, true) }
        }
    }

    @ExperimentalCoroutinesApi
    fun loadNextPage() {
        viewModelScope.launch {
            currentPage = currentPage.inc()
            val params = getRoverQueryParams().copy(page = currentPage)
            repository.getPhotosFlow(params)
                .onStart { doOnStart() }
                .catch { error ->
                    currentPage = currentPage.dec()
                    doOnError(error)
                }
                .collect { list ->
                    if (list.isEmpty()) currentPage = currentPage.dec()
                    doOnCollect(list, false)
                }
        }
    }


    private fun getRoverQueryParams(): RoverQueryParameters {
        val params = if (isEarthDateUsed) {
            RoverQueryParameters(
                rover = currentRover.value ?: CURIOSITY,
                earthDate = currentEarthDate.value ?: DEFAULT_DATE,
                camera = currentCamera.value ?: DEFAULT_CAMERA
            )
        } else {
            RoverQueryParameters(
                rover = currentRover.value ?: CURIOSITY,
                sol = currentSol.value ?: DEFAULT_SOL,
                camera = currentCamera.value ?: DEFAULT_CAMERA
            )
        }
        return params
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

    private fun doOnCollect(list: List<Photo>, shouldClearPreviousPage: Boolean) {
        if (shouldClearPreviousPage) {
            _listOfPhotos.postValue(list)
        } else {
            val currentList = _listOfPhotos.value ?: emptyList()
            _listOfPhotos.postValue(currentList.plus(list))
        }
        if (list.isEmpty() && shouldClearPreviousPage) {
            _isListEmpty.postValue(true)
        } else {
            _isListEmpty.postValue(false)
        }
        _isLoading.postValue(false)
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

    fun increaseSolByOne() {
        val currentSol = _currentSol.value ?: 0
        setSol(currentSol + 1)
    }

    fun decreaseSolByOne() {
        val currentSol = _currentSol.value ?: 0
        if (currentSol > 0) setSol(currentSol - 1) else return
    }

    fun nextEarthDay() {
        val currentEarthDateString = _currentEarthDate.value ?: return
        val date = DATE_FORMAT.parse(currentEarthDateString) ?: return
        val nextDayInMillis = date.time.plus(ONE_DAY_IN_MILLIS)
        _currentEarthDate.value = DATE_FORMAT.format(nextDayInMillis)
    }

    fun previousEarthDate() {
        val currentEarthDateString = _currentEarthDate.value ?: return
        val date = DATE_FORMAT.parse(currentEarthDateString) ?: return
        val previousDayInMillis = date.time.minus(ONE_DAY_IN_MILLIS)
        _currentEarthDate.value = DATE_FORMAT.format(previousDayInMillis)
    }

    @ExperimentalCoroutinesApi
    private fun getMaxSolForRover() {
        viewModelScope.launch {
            repository.getMaxSolForRover(currentRover.value ?: CURIOSITY)
                .catch { doOnError(it) }
                .collect {
                    Log.d("VIEW MODEL ", "MAX SOL: $it")
                    _maxSolForRover.value = it
                }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getMaxEarthDateForRover() {
        viewModelScope.launch {
            repository.getMaxEarthDateForRover(currentRover.value ?: CURIOSITY)
                .catch { doOnError(it) }
                .collect { _maxEarthDate.value = it }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getLandingDateForRover() {
        viewModelScope.launch {
            repository.getLandingDateForRover(currentRover.value ?: CURIOSITY)
                .catch { doOnError(it) }
                .collect { _landingDate.value = it }
        }
    }
}