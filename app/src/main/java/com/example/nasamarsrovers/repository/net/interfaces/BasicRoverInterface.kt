package com.example.nasamarsrovers.repository.net.interfaces

import com.example.nasamarsrovers.model.PhotosResponse

interface BasicRoverInterface {

    suspend fun getPhotosBySol(sol: Int): PhotosResponse
    suspend fun getPhotosBySolAndCamera(sol: Int, camera: String): PhotosResponse
    suspend fun getPhotosByDateAndCamera(date: String, camera: String): PhotosResponse
}