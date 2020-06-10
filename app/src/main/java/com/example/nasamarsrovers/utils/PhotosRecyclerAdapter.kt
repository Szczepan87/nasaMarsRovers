package com.example.nasamarsrovers.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasamarsrovers.databinding.PhotoCardBinding
import com.example.nasamarsrovers.model.Photo

class PhotosRecyclerAdapter : RecyclerView.Adapter<PhotosRecyclerAdapter.PhotosViewHolder>() {

    private val photosList = mutableListOf<Photo>()

    fun updateList(list: List<Photo>) {
        photosList.clear()
        Log.d("RECYCLER", "Adding to recycler list ${list.first()}")
        photosList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = PhotoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photosList[position])
        Log.d("RECYCLER", "Binding with ${photosList[position]}")
    }

    class PhotosViewHolder(private val binding: PhotoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.photo = photo
        }
    }
}