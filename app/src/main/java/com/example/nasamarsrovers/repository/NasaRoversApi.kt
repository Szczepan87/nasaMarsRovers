package com.example.nasamarsrovers.repository

import com.example.nasamarsrovers.model.PhotosResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface NasaRoversApi {

    @GET("curiosity/photos?sol=1000&api_key=DEMO_KEY")
    fun getCuriosityPhotosBySol(): Deferred<PhotosResponse>
}