package com.example.nasamarsrovers.repository

sealed class ApiResponseWrapper<out T> {
    data class Success<out T>(val  value: T): ApiResponseWrapper<T>()
    data class NetworkError(val code: Int, val  errorMessage: String?): ApiResponseWrapper<Nothing>()
    data class GeneralError(val  errorMessage: String?): ApiResponseWrapper<Nothing>()
}