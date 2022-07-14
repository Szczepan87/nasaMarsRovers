package com.example.nasamarsrovers.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.DialogSolPickerBinding
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolPicker : DialogFragment() {

    private lateinit var binding: DialogSolPickerBinding
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_sol_picker, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel = galleryViewModel
            with(solNumberPicker) {
                minValue = 0
                maxValue = galleryViewModel.maxSolForRover.value ?: 0
            }
            solDialogCancelButton.setOnClickListener { this@SolPicker.dismiss() }
            solDialogOkButton.setOnClickListener {
                galleryViewModel.isEarthDateUsed = false
                Log.d("SOL PICKER", "EARTH DATE USED: ${galleryViewModel.isEarthDateUsed}")
                galleryViewModel.setSol(
                    solNumberPicker.value
                )
                this@SolPicker.dismiss()
            }
        }
    }
}