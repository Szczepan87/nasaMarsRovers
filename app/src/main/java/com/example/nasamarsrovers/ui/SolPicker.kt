package com.example.nasamarsrovers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.DialogSolPickerBinding
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel

class SolPicker(private val galleryViewModel: GalleryViewModel) : DialogFragment() {

    private lateinit var binding: DialogSolPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_sol_picker, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel = galleryViewModel
            with(solNumberPicker) {
                minValue = 0
                maxValue = 2200
            }
            solDialogCancelButton.setOnClickListener { this@SolPicker.dismiss() }
            solDialogOkButton.setOnClickListener {
                galleryViewModel.setSol(
                    solNumberPicker.value
                )
                this@SolPicker.dismiss()
            }
        }
    }
}