package com.example.nasamarsrovers.di

import com.example.nasamarsrovers.API_KEY
import com.example.nasamarsrovers.BASE_URL
import com.example.nasamarsrovers.repository.NasaRoversApi
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { GalleryViewModel() }
}

private fun provideRetrofit(): NasaRoversApi {
    return Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttp3Client())
        .baseUrl(BASE_URL)
        .build()
        .create(NasaRoversApi::class.java)
}

private fun provideOkHttp3Client(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder().header("api_key", API_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }).build()
}