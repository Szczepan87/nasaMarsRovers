package com.example.nasamarsrovers.repository.net

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaRoversApi {

    @GET("curiosity/photos?&page=1&api_key=$API_KEY")
    suspend fun getCuriosityPhotosBySol(@Query("sol") sol: Int): PhotosResponse

    @GET("opportunity/photos?&page=1&api_key=$API_KEY")
    fun getOpportunityPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("spirit/photos?&page=1&api_key=$API_KEY")
    fun getSpiritPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("curiosity/photos?&api_key=$API_KEY")
    suspend fun getCuriosityPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("opportunity/photos?&api_key=$API_KEY")
    suspend fun getOpportunityPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("spirit/photos?&api_key=$API_KEY")
    suspend fun getSpiritPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("curiosity/photos?&api_key=$API_KEY")
    suspend fun getCuriosityPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("opportunity/photos?&api_key=$API_KEY")
    suspend fun getOpportunityPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): PhotosResponse

    @GET("spirit/photos?&api_key=$API_KEY")
    suspend fun getSpiritPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): PhotosResponse
}