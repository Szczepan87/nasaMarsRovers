package com.example.domain.usecase

import com.example.data.repository.PhotosRepository
import com.example.data.model.RoverQueryParameters
import javax.inject.Inject

class GetAllPhotosFlowUseCase @Inject constructor(
    private val repository: PhotosRepository
) {
    operator fun invoke(params: RoverQueryParameters, page: Int = 1) =
        repository.getFlowForNoCamera(params, page)
}
