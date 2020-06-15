package com.example.nasamarsrovers.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.databinding.DialogSolPickerBinding
import org.koin.android.ext.android.get

class SolPicker : DialogFragment() {

    private lateinit var binding: DialogSolPickerBinding
    private val galleryViewModel: GalleryViewModel = get()

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
            solDialogCancelButton.setOnClickListener { this@SolPicker.dismiss() }
            solDialogOkButton.setOnClickListener {
                galleryViewModel.sol.postValue(
                    editTextNumber.text.toString().toInt()
                )
            }
        }
    }
}