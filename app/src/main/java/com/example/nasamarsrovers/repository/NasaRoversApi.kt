package com.example.nasamarsrovers.repository

import com.example.nasamarsrovers.model.PhotosResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface NasaRoversApi {

    @GET("curiosity/photos?sol=1&api_key=F1jrnZRZ2bdZmfx19dfiSvZypHVYk2rgMohbwVBG")
    fun getCuriosityPhotosBySol(): Deferred<PhotosResponse>
}