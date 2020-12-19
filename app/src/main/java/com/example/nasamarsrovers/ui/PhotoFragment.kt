package com.example.nasamarsrovers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nasamarsrovers.R
import kotlinx.android.synthetic.main.fragment_photo.*

class PhotoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val url = arguments?.let { PhotoFragmentArgs.fromBundle(it).photoUrl }
        Glide.with(this.requireContext()).load(url).into(photoTouchImageView)
    }
}