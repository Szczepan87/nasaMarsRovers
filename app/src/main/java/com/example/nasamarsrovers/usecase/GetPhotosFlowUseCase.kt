package com.example.nasamarsrovers.usecase

import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.repository.PhotosRepository
import com.example.nasamarsrovers.repository.net.RoverQueryParameters
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosFlowUseCase @Inject constructor(
    private val repository: PhotosRepository
) {
    operator fun invoke(params: RoverQueryParameters, page: Int = 1): Flow<List<Photo>> {
        return if (params.earthDate == null) {
            repository.getFlowBySolAndCamera(params, page)
        } else {
            repository.getFlowByDateAndCamera(params, page)
        }
    }
}
