package com.example.nasamarsrovers.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.FragmentPhotoBinding
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.OnSwipeTouchListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnSwipeListener()
        url = arguments?.let { PhotoFragmentArgs.fromBundle(it).photoUrl }
        loadImage(url)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnSwipeListener() {
        binding.photoTouchImageView.setOnTouchListener(object :
                OnSwipeTouchListener(requireContext()) {
                override fun onSwipeRight() {
                    getPreviousImageUrl()
                    loadImage(url)
                }

                override fun onSwipeLeft() {
                    getNextImageUrl()
                    loadImage(url)
                }
            })
    }

    private fun getCurrentlyDisplayedImageIndex(): Int {
        return galleryViewModel.state.value.listOfPhotos.indexOfFirst { it.imgSrc == url }
    }

    private fun getPreviousImageUrl() {
        val list = galleryViewModel.state.value.listOfPhotos
        val index = getCurrentlyDisplayedImageIndex()
        if (index <= 0) return
        url = list[index - 1].imgSrc
    }

    private fun getNextImageUrl() {
        val list = galleryViewModel.state.value.listOfPhotos
        val index = getCurrentlyDisplayedImageIndex()
        if (index == -1 || index >= list.lastIndex) return
        url = list[index + 1].imgSrc
    }

    private fun loadImage(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this.requireContext()).load(it).into(binding.photoTouchImageView)
        }
    }
}
