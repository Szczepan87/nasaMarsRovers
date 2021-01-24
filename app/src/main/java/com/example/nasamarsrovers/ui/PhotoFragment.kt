package com.example.nasamarsrovers.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.FragmentPhotoBinding
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.OnSwipeTouchListener
import kotlinx.android.synthetic.main.fragment_photo.*
import org.koin.android.ext.android.inject

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private val galleryViewModel: GalleryViewModel by inject()
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        return galleryViewModel.listOfPhotos.value?.indexOfFirst { it.imgSrc == url } ?: 0
    }

    private fun getPreviousImageUrl() {
        val list = galleryViewModel.listOfPhotos.value ?: return
        val index = getCurrentlyDisplayedImageIndex()
        url = if (index > 0) list[index.minus(1)].imgSrc else return
    }

    private fun getNextImageUrl() {
        val list = galleryViewModel.listOfPhotos.value ?: return
        val index = getCurrentlyDisplayedImageIndex()
        url = if (index < (list.size).plus(1)) list[index.plus(1)].imgSrc else return
    }

    private fun loadImage(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this.requireContext()).load(it).into(photoTouchImageView)
        }
    }
}