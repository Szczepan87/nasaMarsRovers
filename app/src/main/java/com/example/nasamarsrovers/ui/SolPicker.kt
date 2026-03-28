package com.example.nasamarsrovers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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

        val galleryState = galleryViewModel.state.value
        with(binding) {
            state = galleryState
            solPickerDialogLayout.removeAllViews()
            val composeView = ComposeView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MaterialTheme {
                        SolPickerDialogContent(
                            state = galleryState,
                            onConfirm = { sol ->
                                galleryViewModel.setSol(sol)
                                this@SolPicker.dismiss()
                            },
                            onCancel = { this@SolPicker.dismiss() }
                        )
                    }
                }
            }
            solPickerDialogLayout.addView(composeView)
        }
    }
}
