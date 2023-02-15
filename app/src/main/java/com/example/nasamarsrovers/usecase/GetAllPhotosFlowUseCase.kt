package com.example.nasamarsrovers.usecase

import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import javax.inject.Inject

class GetAllPhotosFlowUseCase @Inject constructor(
    private val repository: PhotosRepository
) {
    operator fun invoke(params: RoverQueryParameters, page: Int = 1) {
        repository.getFlowForNoCamera(params, page)
    }
}
