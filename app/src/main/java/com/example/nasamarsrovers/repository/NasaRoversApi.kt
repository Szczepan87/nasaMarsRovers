package com.example.nasamarsrovers.repository

import com.example.nasamarsrovers.model.Photos
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaRoversApi {

    @GET("/curiosity/photos")
    fun getCuriosityPhotosBySol(@Query("sol") sol: Int): Call<Response<List<Photos>>>
}