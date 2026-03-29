package com.example.nasamarsrovers

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.ui.navigation.NasaMarsRoversApp
import com.example.nasamarsrovers.ui.theme.NasaMarsRoversTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NasaMarsRoversTheme {
                NasaMarsRoversApp(galleryViewModel = galleryViewModel)
            }
        }
    }
}
