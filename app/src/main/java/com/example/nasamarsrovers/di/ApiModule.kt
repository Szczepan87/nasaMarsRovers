package com.example.nasamarsrovers.di

import android.content.Context
import com.example.nasamarsrovers.repository.net.NoInternetConnectionInterceptor
import com.example.nasamarsrovers.repository.net.interfaces.RoverApi
import com.example.nasamarsrovers.utils.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRoverApi(retrofit: Retrofit): RoverApi {
        return retrofit.create(RoverApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttp3Client(noInternetConnectionInterceptor: NoInternetConnectionInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(noInternetConnectionInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideNoInternetConnectionInterceptor(@ApplicationContext context: Context): NoInternetConnectionInterceptor{
        return NoInternetConnectionInterceptor(context)
    }
}