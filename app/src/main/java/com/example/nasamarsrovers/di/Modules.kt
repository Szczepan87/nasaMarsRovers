package com.example.nasamarsrovers.di

import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GalleryViewModel() }
}