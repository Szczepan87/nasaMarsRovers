package com.example.nasamarsrovers.repository

import com.example.nasamarsrovers.di.IODispatcher
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import com.example.nasamarsrovers.repository.net.interfaces.RoverApi
import com.example.nasamarsrovers.utils.DEFAULT_CAMERA
import com.example.nasamarsrovers.utils.DEFAULT_DATE
import com.example.nasamarsrovers.utils.DEFAULT_SOL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotosRepository @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val roverApi: RoverApi
) {
    fun getFlowByDateAndCamera(params: RoverQueryParameters, page: Int) =
        flow {
            emit(
                roverApi.getPhotosByDateAndCamera(
                    rover = params.rover,
                    camera = params.camera ?: DEFAULT_CAMERA,
                    date = params.earthDate ?: DEFAULT_DATE,
                    page = page
                ).photos ?: emptyList()
            )
        }.flowOn(coroutineDispatcher)

    fun getFlowBySolAndCamera(params: RoverQueryParameters, page: Int) =
        flow {
            emit(
                roverApi.getPhotosBySolAndCamera(
                    rover = params.rover,
                    sol = params.sol ?: DEFAULT_SOL,
                    camera = params.camera ?: DEFAULT_CAMERA,
                    page = page
                ).photos ?: emptyList()
            )
        }.flowOn(coroutineDispatcher)

    fun getFlowForNoCamera(params: RoverQueryParameters, page: Int) =
        flow {
            emit(
                if (params.earthDate == null) {
                    roverApi.getPhotosBySol(
                        rover = params.rover,
                        sol = params.sol ?: DEFAULT_SOL,
                        page = page
                    ).photos ?: emptyList()
                } else {
                    roverApi.getPhotosByDate(
                        rover = params.rover,
                        date = params.earthDate,
                        page = page
                    ).photos ?: emptyList()
                }
            )
        }.flowOn(coroutineDispatcher)

    @ExperimentalCoroutinesApi
    fun getMaxSolForRover(roverName: String): Flow<Int> {
        return flow {
            emit(roverApi.getRoverManifest(roverName).photoManifest?.maxSol ?: 0)
        }.flowOn(coroutineDispatcher)
    }

    @ExperimentalCoroutinesApi
    fun getMaxEarthDateForRover(roverName: String): Flow<String> {
        return flow {
            emit(roverApi.getRoverManifest(roverName).photoManifest?.maxDate.orEmpty())
        }.flowOn(coroutineDispatcher)
    }

    @ExperimentalCoroutinesApi
    fun getLandingDateForRover(roverName: String): Flow<String> {
        return flow {
            emit(roverApi.getRoverManifest(roverName).photoManifest?.landingDate.orEmpty())
        }.flowOn(coroutineDispatcher)
    }
}
