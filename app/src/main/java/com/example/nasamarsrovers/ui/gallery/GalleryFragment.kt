package com.example.nasamarsrovers.ui.gallery

import android.os.Bundle
import android.util.Log
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
import com.example.nasamarsrovers.utils.PhotosRecyclerAdapter
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by inject()
    private lateinit var binding: FragmentGalleryBinding
    private val galleryRecyclerAdapter = PhotosRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        setUpObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryViewModel.updatePhotosList()
        with(binding) {
            viewModel = galleryViewModel
            galleryRecycler.adapter = galleryRecyclerAdapter
            galleryRecyclerAdapter.onItemClickListener = {
                val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun setUpObservers() {
        galleryViewModel.listOfPhotos.observe(viewLifecycleOwner, Observer {
            galleryRecyclerAdapter.updateList(it)
        })
        galleryViewModel.currentSol.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
            Log.d("GALLERY FRAGMENT", "UPDATING PHOTOS BECAUSE OF SOL CHANGE")
        })
        galleryViewModel.currentEarthDate.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
            Log.d("GALLERY FRAGMENT", "UPDATING PHOTOS BECAUSE OF EARTH DATE CHANGE")
        })
        galleryViewModel.currentRover.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
            Log.d("GALLERY FRAGMENT", "UPDATING PHOTOS BECAUSE OF ROVER CHANGE")
        })
        galleryViewModel.currentCamera.observe(viewLifecycleOwner, Observer {
            galleryViewModel.updatePhotosList()
            Log.d("GALLERY FRAGMENT", "UPDATING PHOTOS BECAUSE OF CAMERA CHANGE")
        })
        galleryViewModel.repositoryError.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}