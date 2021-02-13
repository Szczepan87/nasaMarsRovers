package com.example.nasamarsrovers.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.net.interfaces.CuriosityRoverApi
import com.example.nasamarsrovers.repository.net.interfaces.OpportunityRoverApi
import com.example.nasamarsrovers.repository.net.interfaces.SpiritRoverApi
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class PhotosRepository(retrofit: Retrofit) {

    private val _roverPhotos = MutableLiveData<List<Photo>>()
    val roverPhotos: LiveData<List<Photo>>
        get() = _roverPhotos

    private val curiosityApi = retrofit.create(CuriosityRoverApi::class.java)
    private val spiritApi = retrofit.create(SpiritRoverApi::class.java)
    private val opportunityApi = retrofit.create(OpportunityRoverApi::class.java)

    @ExperimentalCoroutinesApi
    fun getPhotosFlow(roverName: String, sol: Int, camera: String): Flow<List<Photo>> {
        return flow {
            val list = when (roverName) {
                CURIOSITY -> curiosityApi.getPhotosBySolAndCamera(sol, camera).photos
                    ?: emptyList()
                OPPORTUNITY -> opportunityApi.getPhotosBySolAndCamera(sol, camera).photos
                    ?: emptyList()
                SPIRIT -> spiritApi.getPhotosBySolAndCamera(sol, camera).photos
                    ?: emptyList()
                else -> emptyList()
            }
            emit(list)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getPhotosFlow(roverName: String, date: String, camera: String): Flow<List<Photo>> {
        return flow {
            val list = when (roverName) {
                CURIOSITY -> curiosityApi.getPhotosByDateAndCamera(date, camera).photos
                    ?: emptyList()
                OPPORTUNITY -> opportunityApi.getPhotosByDateAndCamera(date, camera).photos
                    ?: emptyList()
                SPIRIT -> spiritApi.getPhotosByDateAndCamera(date, camera).photos
                    ?: emptyList()
                else -> emptyList()
            }
            emit(list)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getMaxSolForRover(roverName: String): Flow<Int> {
        return flow {
            val maxSol = when (roverName) {
                CURIOSITY -> {
                    Log.d("REPOSITORY", "MAX SOL FOR ROVER: $roverName")
                    Log.d("REPOSITORY", "MAX SOL: ${curiosityApi.getRoverManifest().photoManifest?.maxSol}")
                    Log.d("REPOSITORY", "MAX SOL MANIFEST: ${curiosityApi.getRoverManifest()}")
                    curiosityApi.getRoverManifest().photoManifest?.maxSol ?: 0}
                OPPORTUNITY -> opportunityApi.getRoverManifest().photoManifest?.maxSol ?: 0
                SPIRIT -> spiritApi.getRoverManifest().photoManifest?.maxSol ?: 0
                else -> 0
            }
            emit(maxSol)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getMaxEarthDateForRover(roverName: String): Flow<String> {
        return flow {
            val maxDate = when (roverName) {
                CURIOSITY -> curiosityApi.getRoverManifest().photoManifest?.maxDate.orEmpty()
                OPPORTUNITY -> opportunityApi.getRoverManifest().photoManifest?.maxDate.orEmpty()
                SPIRIT -> spiritApi.getRoverManifest().photoManifest?.maxDate.orEmpty()
                else -> ""
            }
            emit(maxDate)
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getLandingDateForRover(roverName: String):Flow<String>{
        return flow {
            val landingDate = when(roverName){
                CURIOSITY -> curiosityApi.getRoverManifest().photoManifest?.landingDate.orEmpty()
                OPPORTUNITY -> opportunityApi.getRoverManifest().photoManifest?.landingDate.orEmpty()
                SPIRIT -> spiritApi.getRoverManifest().photoManifest?.landingDate.orEmpty()
                else -> ""
            }
            emit(landingDate)
        }.flowOn(Dispatchers.IO)
    }
}