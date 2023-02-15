package com.example.nasamarsrovers.ui.gallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.FragmentGalleryBinding
import com.example.nasamarsrovers.utils.OnSwipeTouchListener
import com.example.nasamarsrovers.utils.PhotosRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private lateinit var binding: FragmentGalleryBinding
    private val galleryRecyclerAdapter: PhotosRecyclerAdapter by lazy { PhotosRecyclerAdapter(::onPhotoClick) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        setUpObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnSwipeListener()
        galleryViewModel.loadFirstPage()
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = galleryViewModel
            galleryRecycler.adapter = galleryRecyclerAdapter
            setOverscrollListener()
        }
    }

    @ExperimentalCoroutinesApi
    private fun FragmentGalleryBinding.setOverscrollListener() {
        val layoutManager: LinearLayoutManager =
            galleryRecycler.layoutManager as? LinearLayoutManager ?: return
        val adapter = galleryRecycler.adapter
        galleryRecycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = layoutManager.findLastVisibleItemPosition()
                val items = adapter?.itemCount ?: 0

                if (position >= items.dec()) {
                    viewModel?.loadNextPage()
                }
            }
        })
    }

    private fun onPhotoClick(stringUrl: String?) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoFragment(stringUrl)
        findNavController().navigate(action)
    }

    @ExperimentalCoroutinesApi
    private fun setUpObservers() {
        galleryViewModel.listOfPhotos.observe(viewLifecycleOwner) {
            galleryRecyclerAdapter.submitList(it)
        }
        galleryViewModel.currentSol.observe(viewLifecycleOwner) {
            galleryViewModel.loadFirstPage()
        }
        galleryViewModel.currentEarthDate.observe(viewLifecycleOwner) {
            galleryViewModel.loadFirstPage()
        }
        galleryViewModel.currentRover.observe(viewLifecycleOwner) {
            galleryViewModel.loadFirstPage()
        }
        galleryViewModel.currentCamera.observe(viewLifecycleOwner) {
            galleryViewModel.loadFirstPage()
        }
        galleryViewModel.repositoryError.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
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
