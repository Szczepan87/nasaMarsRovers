package com.example.nasamarsrovers.di

import com.example.nasamarsrovers.utils.API_KEY
import com.example.nasamarsrovers.utils.BASE_URL
import com.example.nasamarsrovers.repository.net.NasaRoversApi
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    single { GalleryViewModel(get()) }
}

val repositoryModule = module {
    single { provideRetrofit() }
    single { PhotosRepository(get()) }
}

private fun provideRetrofit(): NasaRoversApi {
    return Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        // .client(provideOkHttp3Client())
        .baseUrl(BASE_URL)
        .build()
        .create(NasaRoversApi::class.java)
}

private fun provideOkHttp3Client(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder().header("api_key",
                API_KEY
            )
            val request = requestBuilder.build()
            chain.proceed(request)
        }).build()
}