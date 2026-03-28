package com.example.nasamarsrovers.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import com.example.nasamarsrovers.usecase.GetAllPhotosFlowUseCase
import com.example.nasamarsrovers.usecase.GetPhotosFlowUseCase
import com.example.nasamarsrovers.utils.DATE_FORMAT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModel @Inject constructor(
    private val getAllPhotosFlowUseCase: GetAllPhotosFlowUseCase,
    private val getPhotosFlowUseCase: GetPhotosFlowUseCase,
    private val repository: PhotosRepository
) : ViewModel() {
    companion object {
        private const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000
    }

    private val _state = MutableStateFlow(GalleryState())
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<GalleryEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<GalleryEvent> = _events.asSharedFlow()

    private var shouldLoadNextPage = true
    private var currentPage = 1
    private var queryVersion = 0L

    init {
        observeFilters()
        observeCurrentRover()
    }

    fun loadNextPage() {
        val currentState = state.value
        if (currentState.isLoading || !shouldLoadNextPage) return

        val nextPage = currentPage + 1
        val currentQueryVersion = queryVersion
        val params = currentState.toQueryParameters()

        viewModelScope.launch {
            loadPage(
                params = params,
                page = nextPage,
                shouldClearPreviousPage = false,
                requestQueryVersion = currentQueryVersion
            )
        }
    }

    fun setCurrentRover(roverName: String) {
        _state.update { currentState ->
            currentState.copy(currentRover = roverName)
        }
    }

    fun setSol(solNo: Int) {
        _state.update { currentState ->
            currentState.copy(
                currentSol = solNo,
                isEarthDateUsed = false
            )
        }
    }

    fun setCurrentCamera(currentCamera: String?) {
        _state.update { currentState ->
            currentState.copy(currentCamera = currentCamera?.takeUnless { it == "ALL" })
        }
    }

    fun setEarthDate(date: String) {
        _state.update { currentState ->
            currentState.copy(
                currentEarthDate = date,
                isEarthDateUsed = true
            )
        }
    }

    fun increaseSolByOne() {
        val nextSol = (state.value.currentSol ?: 0) + 1
        setSol(nextSol)
    }

    fun decreaseSolByOne() {
        val currentSol = state.value.currentSol ?: 0
        if (currentSol > 0) {
            setSol(currentSol - 1)
        }
    }

    fun nextEarthDay() {
        val currentEarthDateString = state.value.currentEarthDate ?: return
        val date = DATE_FORMAT.parse(currentEarthDateString) ?: return
        val nextDayInMillis = date.time + ONE_DAY_IN_MILLIS

        _state.update { currentState ->
            currentState.copy(currentEarthDate = DATE_FORMAT.format(nextDayInMillis))
        }
    }

    fun previousEarthDate() {
        val currentEarthDateString = state.value.currentEarthDate ?: return
        val date = DATE_FORMAT.parse(currentEarthDateString) ?: return
        val previousDayInMillis = date.time - ONE_DAY_IN_MILLIS

        _state.update { currentState ->
            currentState.copy(currentEarthDate = DATE_FORMAT.format(previousDayInMillis))
        }
    }

    private fun observeFilters() {
        viewModelScope.launch {
            state
                .map { currentState -> currentState.toQueryParameters() }
                .distinctUntilChanged()
                .collectLatest { params ->
                    queryVersion += 1
                    currentPage = 1
                    shouldLoadNextPage = true
                    loadPage(
                        params = params,
                        page = currentPage,
                        shouldClearPreviousPage = true,
                        requestQueryVersion = queryVersion
                    )
                }
        }
    }

    private fun observeCurrentRover() {
        viewModelScope.launch {
            state
                .map { currentState -> currentState.currentRover }
                .distinctUntilChanged()
                .collectLatest { roverName ->
                    loadRoverMeta(roverName)
                }
        }
    }

    private suspend fun loadPage(
        params: RoverQueryParameters,
        page: Int,
        shouldClearPreviousPage: Boolean,
        requestQueryVersion: Long
    ) {
        getPhotosFlow(params, page)
            .onStart { doOnStart(shouldClearPreviousPage) }
            .catch { error ->
                if (isLatestRequest(requestQueryVersion)) {
                    doOnError(error, shouldClearPreviousPage)
                }
            }
            .collect { list ->
                if (isLatestRequest(requestQueryVersion)) {
                    doOnCollect(list, shouldClearPreviousPage, page)
                }
            }
    }

    private fun getPhotosFlow(params: RoverQueryParameters, page: Int) =
        if (params.camera == null) {
            getAllPhotosFlowUseCase(params, page)
        } else {
            getPhotosFlowUseCase(params, page)
        }

    private fun doOnStart(shouldClearPreviousPage: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                listOfPhotos = if (shouldClearPreviousPage) emptyList() else currentState.listOfPhotos,
                isLoading = true,
                isListEmpty = false
            )
        }
    }

    private fun doOnError(exception: Throwable, shouldClearPreviousPage: Boolean) {
        val errorMessage = exception.message ?: exception.localizedMessage ?: return
        _events.tryEmit(GalleryEvent.ShowError(errorMessage))
        _state.update { currentState ->
            currentState.copy(
                isLoading = false,
                isListEmpty = if (shouldClearPreviousPage) currentState.listOfPhotos.isEmpty() else currentState.isListEmpty
            )
        }
    }

    private fun doOnCollect(list: List<Photo>, shouldClearPreviousPage: Boolean, page: Int) {
        currentPage = page
        shouldLoadNextPage = list.isNotEmpty()

        _state.update { currentState ->
            val updatedList = if (shouldClearPreviousPage) {
                list
            } else {
                currentState.listOfPhotos + list
            }

            currentState.copy(
                listOfPhotos = updatedList,
                isLoading = false,
                isListEmpty = updatedList.isEmpty()
            )
        }
    }

    private suspend fun loadRoverMeta(roverName: String) {
        _state.update { currentState ->
            currentState.copy(
                maxSolForRover = 0,
                maxEarthDate = null,
                landingDate = null
            )
        }

        runCatching {
            coroutineScope {
                val maxSolDeferred = async { repository.getMaxSolForRover(roverName).first() }
                val maxEarthDateDeferred = async { repository.getMaxEarthDateForRover(roverName).first() }
                val landingDateDeferred = async { repository.getLandingDateForRover(roverName).first() }

                Triple(
                    maxSolDeferred.await(),
                    maxEarthDateDeferred.await(),
                    landingDateDeferred.await()
                )
            }
        }.onSuccess { (maxSol, maxEarthDate, landingDate) ->
            _state.update { currentState ->
                currentState.copy(
                    maxSolForRover = maxSol,
                    maxEarthDate = maxEarthDate,
                    landingDate = landingDate
                )
            }
        }.onFailure { error ->
            val errorMessage = error.message ?: error.localizedMessage ?: return
            _events.tryEmit(GalleryEvent.ShowError(errorMessage))
        }
    }

    private fun isLatestRequest(requestQueryVersion: Long): Boolean =
        requestQueryVersion == queryVersion
}
