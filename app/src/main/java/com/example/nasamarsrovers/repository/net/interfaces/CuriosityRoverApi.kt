package com.example.nasamarsrovers.repository.net.interfaces

import com.example.nasamarsrovers.model.PhotoManifest
import com.example.nasamarsrovers.model.PhotoManifestResponse
import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface CuriosityRoverApi : BasicRoverInterface {

    @GET("rovers/curiosity/photos?&page=1&api_key=$API_KEY")
    override suspend fun getPhotosBySol(@Query("sol") sol: Int): PhotosResponse

    @GET("rovers/curiosity/photos?&api_key=$API_KEY")
    override suspend fun getPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("rovers/curiosity/photos?&api_key=$API_KEY")
    override suspend fun getPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("manifests/curiosity?api_key=$API_KEY")
    override suspend fun getRoverManifest(): PhotoManifestResponse
}