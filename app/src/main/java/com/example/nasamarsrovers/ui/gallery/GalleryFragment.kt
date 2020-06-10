package com.example.nasamarsrovers.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.FragmentGalleryBinding
import com.example.nasamarsrovers.utils.PhotosRecyclerAdapter
import org.koin.android.ext.android.get

class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel = get()
    private lateinit var binding: FragmentGalleryBinding
    private val galleryRecyclerAdapter = PhotosRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        galleryViewModel.listOfPhotos.observe(viewLifecycleOwner, Observer {
            galleryRecyclerAdapter.updateList(it)
            Log.d("GALLERY FRAGMENT", "Updating with ${it.first()}")
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryViewModel.updatePhotosList()
        with(binding){
            viewModel = galleryViewModel
            galleryRecycler.adapter = galleryRecyclerAdapter
        }
    }
}