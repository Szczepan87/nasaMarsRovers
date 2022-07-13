package com.example.nasamarsrovers.repository.net.interfaces

import com.example.nasamarsrovers.model.PhotoManifestResponse
import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoverApi {

    @GET("rovers/{rover}/photos?&page=1&api_key=$API_KEY")
    suspend fun getPhotosBySol(
        @Path("rover") rover: String,
        @Query("sol") sol: Int
    ): PhotosResponse

    @GET("rovers/{rover}/photos?&api_key=$API_KEY")
    suspend fun getPhotosBySolAndCamera(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("rovers/{rover}/photos?&api_key=$API_KEY")
    suspend fun getPhotosByDateAndCamera(
        @Path("rover") rover: String,
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("manifests/{rover}?api_key=$API_KEY")
    suspend fun getRoverManifest(
        @Path("rover") rover: String
    ): PhotoManifestResponse
}