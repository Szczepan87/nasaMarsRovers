package com.example.nasamarsrovers

import com.example.nasamarsrovers.repository.ApiResponseWrapper
import com.example.nasamarsrovers.repository.net.NoInternetConnectionException
import com.example.nasamarsrovers.repository.safeApiCall
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class ApiCallTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Test
    fun `should return wrapper success when lambda is successful`() {
        runBlockingTest {
            val result = safeApiCall(dispatcher) { true }
            assertEquals(ApiResponseWrapper.Success(true), result)
        }
    }

    @Test
    fun `should return wrapper general error when receive IO Exception`(){
        runBlockingTest {
            val result = safeApiCall(dispatcher){ throw IOException("IO Exception")}
            assertEquals(ApiResponseWrapper.GeneralError("IO Exception"), result)
        }
    }

    @Test
    fun `should return wrapper general error when receive NoInternetConnectionException`(){
        runBlockingTest {
            val result = safeApiCall(dispatcher){ throw NoInternetConnectionException()}
            assertEquals(ApiResponseWrapper.GeneralError("No Internet connection! Please check and retry."), result)
        }
    }
}