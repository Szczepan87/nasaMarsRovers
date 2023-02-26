package com.example.nasamarsrovers.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nasamarsrovers.databinding.PhotoCardBinding
import com.example.data.model.PhotoDTO

class PhotosRecyclerAdapter(private val onItemCLick: ((String?) -> Unit)) :
    ListAdapter<PhotoDTO, PhotosRecyclerAdapter.PhotosViewHolder>(object :
            DiffUtil.ItemCallback<PhotoDTO>() {
            override fun areItemsTheSame(oldItem: PhotoDTO, newItem: PhotoDTO): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoDTO, newItem: PhotoDTO): Boolean =
                oldItem.imgSrc == newItem.imgSrc
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = PhotoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(currentList[position])
//        Log.d("RECYCLER", "Binding with ${currentList[position]}")
    }

    inner class PhotosViewHolder(private val binding: PhotoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var glideImageLoader: GlideImageLoader? = null

        fun bind(photo: PhotoDTO) {
            binding.photo = photo
            glideImageLoader =
                GlideImageLoader(binding.cardPhotoImageView, binding.cardPhotoProgressBar)
            glideImageLoader?.load(photo.imgSrc.toString())
            binding.photoCardLayout.setOnClickListener { onItemCLick.invoke(photo.imgSrc) }
        }
    }
}
