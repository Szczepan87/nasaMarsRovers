package com.example.nasamarsrovers.repository.net.interfaces

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface OpportunityRoverApi : BasicRoverInterface {
    @GET("opportunity/photos?&page=1&api_key=$API_KEY")
    override suspend fun getPhotosBySol(@Query("sol") sol: Int): PhotosResponse

    @GET("opportunity/photos?&api_key=$API_KEY")
    override suspend fun getPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("opportunity/photos?&api_key=$API_KEY")
    override suspend fun getPhotosByDateAndCamera(
        @Query("sol") date: String,
        @Query("camera") camera: String
    ): PhotosResponse
}