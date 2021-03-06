package com.example.nasamarsrovers.ui.gallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.FragmentGalleryBinding
import com.example.nasamarsrovers.utils.OnSwipeTouchListener
import com.example.nasamarsrovers.utils.PhotosRecyclerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by inject()
    private lateinit var binding: FragmentGalleryBinding
    private val galleryRecyclerAdapter: PhotosRecyclerAdapter by lazy { PhotosRecyclerAdapter(::onPhotoClick) }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        setUpObservers()
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnSwipeListener()
        galleryViewModel.updatePhotosList()
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = galleryViewModel
            galleryRecycler.adapter = galleryRecyclerAdapter
        }
    }

    private fun onPhotoClick(stringUrl: String?) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoFragment(stringUrl)
        findNavController().navigate(action)
    }

    @ExperimentalCoroutinesApi
    private fun setUpObservers() {
        galleryViewModel.listOfPhotos.observe(viewLifecycleOwner, Observer {
            galleryRecyclerAdapter.updateList(it)
        })
        galleryViewModel.currentSol.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
        })
        galleryViewModel.currentEarthDate.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
        })
        galleryViewModel.currentRover.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
        })
        galleryViewModel.currentCamera.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
        })
        galleryViewModel.repositoryError.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnSwipeListener() {
        binding.galleryRecycler.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeRight() {
                if (galleryViewModel.isEarthDateUsed) galleryViewModel.previousEarthDate()
                else galleryViewModel.decreaseSolByOne()
            }

            override fun onSwipeLeft() {
                if (galleryViewModel.isEarthDateUsed) galleryViewModel.nextEarthDay()
                else galleryViewModel.increaseSolByOne()
            }
        })
    }
}