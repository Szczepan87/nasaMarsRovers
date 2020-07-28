package com.example.nasamarsrovers

import android.app.Application
import com.example.nasamarsrovers.di.repositoryModule
import com.example.nasamarsrovers.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NasaMarsRovers : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NasaMarsRovers)
            modules(viewModelModule, repositoryModule)
        }
    }
}