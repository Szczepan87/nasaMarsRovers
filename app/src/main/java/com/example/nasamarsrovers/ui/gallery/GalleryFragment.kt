package com.example.nasamarsrovers.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

// fragment_gallery.xml is intentionally kept for future reference.
@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by galleryViewModel.state.collectAsState()
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    galleryViewModel.events.collectLatest { event ->
                        when (event) {
                            is GalleryEvent.ShowError -> {
                                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                MaterialTheme {
                    GalleryScreen(
                        state = state,
                        onPhotoClick = ::onPhotoClick,
                        onLoadNextPage = { galleryViewModel.loadNextPage() },
                        onSwipeRight = {
                            if (galleryViewModel.state.value.isEarthDateUsed) {
                                galleryViewModel.previousEarthDate()
                            } else {
                                galleryViewModel.decreaseSolByOne()
                            }
                        },
                        onSwipeLeft = {
                            if (galleryViewModel.state.value.isEarthDateUsed) {
                                galleryViewModel.nextEarthDay()
                            } else {
                                galleryViewModel.increaseSolByOne()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun onPhotoClick(stringUrl: String?) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoFragment(stringUrl)
        findNavController().navigate(action)
    }
}
