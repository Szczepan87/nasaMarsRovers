package com.example.nasamarsrovers.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ApiResponseWrapper<T> {
    return withContext(dispatcher) {
        try {
            ApiResponseWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    ApiResponseWrapper.NetworkError(throwable.code(), throwable.message)
                }
                is IOException -> {
                    ApiResponseWrapper.GeneralError(throwable.message)
                }
                else -> {
                    ApiResponseWrapper.GeneralError(throwable.message)
                }
            }
        }
    }
}