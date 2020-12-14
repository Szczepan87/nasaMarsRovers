package com.example.nasamarsrovers.repository.net.interfaces

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface SpiritRoverApi : BasicRoverInterface {

    @GET("spirit/photos?&page=1&api_key=$API_KEY")
    override suspend fun getPhotosBySol(@Query("sol") sol: Int): PhotosResponse

    @GET("spirit/photos?&api_key=$API_KEY")
    override suspend fun getPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("spirit/photos?&api_key=$API_KEY")
    override suspend fun getPhotosByDateAndCamera(
        @Query("sol") date: String,
        @Query("camera") camera: String
    ): PhotosResponse
}