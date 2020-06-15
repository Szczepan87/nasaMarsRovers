package com.example.nasamarsrovers.repository

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface NasaRoversApi {

    @GET("curiosity/photos?sol=1000&camera=fhaz&api_key=$API_KEY")
    fun getCuriosityPhotosBySol(): Deferred<PhotosResponse>
}