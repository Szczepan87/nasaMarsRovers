package com.example.nasamarsrovers.repository

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaRoversApi {

    @GET("curiosity/photos?&camera=fhaz&api_key=$API_KEY")
    fun getCuriosityPhotosBySol(@Query("sol") sol:Int): Deferred<PhotosResponse>
}