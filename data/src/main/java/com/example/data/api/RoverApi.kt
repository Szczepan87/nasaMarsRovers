package com.example.data.api

import com.example.data.model.PhotosResponseDTO
import com.example.data.model.PhotoManifestResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoverApi {

    @GET("rovers/{rover}/photos?&api_key=$API_KEY")
    suspend fun getPhotosBySol(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("page") page: Int
    ): PhotosResponseDTO

    @GET("rovers/{rover}/photos?&api_key=$API_KEY")
    suspend fun getPhotosByDate(
        @Path("rover") rover: String,
        @Query("earth_date") date: String,
        @Query("page") page: Int
    ): PhotosResponseDTO

    @GET("rovers/{rover}/photos?&api_key=$API_KEY")
    suspend fun getPhotosBySolAndCamera(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("camera") camera: String,
        @Query("page") page: Int
    ): PhotosResponseDTO

    @GET("rovers/{rover}/photos?&api_key=$API_KEY")
    suspend fun getPhotosByDateAndCamera(
        @Path("rover") rover: String,
        @Query("earth_date") date: String,
        @Query("camera") camera: String,
        @Query("page") page: Int
    ): PhotosResponseDTO

    @GET("manifests/{rover}?api_key=$API_KEY")
    suspend fun getRoverManifest(
        @Path("rover") rover: String
    ): PhotoManifestResponseDTO

    companion object{
        private const val API_KEY = "F1jrnZRZ2bdZmfx19dfiSvZypHVYk2rgMohbwVBG"
    }
}
