package com.example.nasamarsrovers.di

import android.content.Context
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.repository.net.NoInternetConnectionInterceptor
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    single { GalleryViewModel(get()) }
}

val repositoryModule = module {
    single { provideRetrofit(get()) }
    single { PhotosRepository(get()) }
}

private fun provideRetrofit(context: Context): Retrofit {
    return Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttp3Client(context))
        .baseUrl(BASE_URL)
        .build()
}

private fun provideOkHttp3Client(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(NoInternetConnectionInterceptor(context))
        .build()
}