package com.example.nasamarsrovers

import android.app.Application
import com.example.nasamarsrovers.di.viewModelModule
import org.koin.core.context.startKoin

class NasaMarsRovers : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            this@NasaMarsRovers.applicationContext
            modules(viewModelModule)
        }
    }
}