package com.example.nasamarsrovers.repository.net

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaRoversApi {

    @GET("curiosity/photos?&page=1&api_key=$API_KEY")
    fun getCuriosityPhotosBySol(@Query("sol") sol: Int): Deferred<Response<PhotosResponse>>

    @GET("opportunity/photos?&page=1&api_key=$API_KEY")
    fun getOpportunityPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("spirit/photos?&page=1&api_key=$API_KEY")
    fun getSpiritPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("curiosity/photos?&api_key=$API_KEY")
    fun getCuriosityPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): Deferred<Response<PhotosResponse>>

    @GET("opportunity/photos?&api_key=$API_KEY")
    fun getOpportunityPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): Deferred<Response<PhotosResponse>>

    @GET("spirit/photos?&api_key=$API_KEY")
    fun getSpiritPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): Deferred<Response<PhotosResponse>>

    @GET("curiosity/photos?&api_key=$API_KEY")
    fun getCuriosityPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): Deferred<Response<PhotosResponse>>

    @GET("opportunity/photos?&api_key=$API_KEY")
    fun getOpportunityPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): Deferred<Response<PhotosResponse>>

    @GET("spirit/photos?&api_key=$API_KEY")
    fun getSpiritPhotosByDateAndCamera(
        @Query("earth_date") date: String,
        @Query("camera") camera: String
    ): Deferred<Response<PhotosResponse>>
}