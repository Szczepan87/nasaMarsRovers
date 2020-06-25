package com.example.nasamarsrovers.repository.net

import com.example.nasamarsrovers.model.PhotosResponse
import com.example.nasamarsrovers.utils.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaRoversApi {

    @GET("curiosity/photos?&page=1&api_key=$API_KEY")
    fun getCuriosityPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("opportunity/photos?&page=1&api_key=$API_KEY")
    fun getOpportunityPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("spirit/photos?&page=1&api_key=$API_KEY")
    fun getSpiritPhotosBySol(@Query("sol") sol: Int): Deferred<PhotosResponse>

    @GET("curiosity/photos?&api_key=$API_KEY")
    fun getCuriosityPhotosBySolAndCamera(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): Deferred<PhotosResponse>

    @GET("opportunity/photos?&api_key=$API_KEY")
    fun getOpportunityPhotosBySol(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): Deferred<PhotosResponse>

    @GET("spirit/photos?&api_key=$API_KEY")
    fun getSpiritPhotosBySol(
        @Query("sol") sol: Int,
        @Query("camera") camera: String
    ): Deferred<PhotosResponse>
}