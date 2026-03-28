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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.FragmentGalleryBinding
import com.example.nasamarsrovers.utils.OnSwipeTouchListener
import com.example.nasamarsrovers.utils.PhotosRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            galleryRecycler.adapter = galleryRecyclerAdapter
            setOverscrollListener()
        }
        setOnSwipeListener()
        collectUi()
    }

    private fun FragmentGalleryBinding.setOverscrollListener() {
        val layoutManager: LinearLayoutManager =
            galleryRecycler.layoutManager as? LinearLayoutManager ?: return
        galleryRecycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = layoutManager.findLastVisibleItemPosition()
                val items = galleryRecyclerAdapter.itemCount

                if (items > 0 && position >= items.dec()) {
                    galleryViewModel.loadNextPage()
                }
            }
        })
    }

    private fun onPhotoClick(stringUrl: String?) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoFragment(stringUrl)
        findNavController().navigate(action)
    }

    private fun collectUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    galleryViewModel.state.collectLatest { state ->
                        binding.state = state
                        galleryRecyclerAdapter.submitList(state.listOfPhotos)
                    }
                }
                launch {
                    galleryViewModel.events.collectLatest { event ->
                        when (event) {
                            is GalleryEvent.ShowError -> {
                                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnSwipeListener() {
        binding.galleryRecycler.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeRight() {
                if (galleryViewModel.state.value.isEarthDateUsed) galleryViewModel.previousEarthDate()
                else galleryViewModel.decreaseSolByOne()
            }

            override fun onSwipeLeft() {
                if (galleryViewModel.state.value.isEarthDateUsed) galleryViewModel.nextEarthDay()
                else galleryViewModel.increaseSolByOne()
            }
        })
    }
}
