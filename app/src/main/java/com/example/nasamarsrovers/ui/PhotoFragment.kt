package com.example.nasamarsrovers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private var currentPhotoUrl: String? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPhotoUrl = arguments?.let { PhotoFragmentArgs.fromBundle(it).photoUrl }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val galleryState by galleryViewModel.state.collectAsState()
                val uiState = remember(galleryState.listOfPhotos, currentPhotoUrl) {
                    createUiState(galleryState.listOfPhotos)
                }

                MaterialTheme {
                    PhotoScreen(
                        state = uiState,
                        onSwipeRight = { showPreviousImage() },
                        onSwipeLeft = { showNextImage() }
                    )
                }
            }
        }
    }

    private fun createUiState(photos: List<Photo>): PhotoUiState {
        val index = photos.indexOfFirst { it.imgSrc == currentPhotoUrl }
        return PhotoUiState(
            imageUrl = currentPhotoUrl,
            hasPrevious = index > 0,
            hasNext = index != -1 && index < photos.lastIndex
        )
    }

    private fun showPreviousImage() {
        val list = galleryViewModel.state.value.listOfPhotos
        val index = list.indexOfFirst { it.imgSrc == currentPhotoUrl }
        if (index <= 0) return
        currentPhotoUrl = list[index - 1].imgSrc
    }

    private fun showNextImage() {
        val list = galleryViewModel.state.value.listOfPhotos
        val index = list.indexOfFirst { it.imgSrc == currentPhotoUrl }
        if (index == -1 || index >= list.lastIndex) return
        currentPhotoUrl = list[index + 1].imgSrc
    }
}
