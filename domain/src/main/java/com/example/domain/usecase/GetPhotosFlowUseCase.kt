package com.example.domain.usecase

import com.example.data.model.PhotoDTO
import com.example.data.model.RoverQueryParameters
import com.example.data.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosFlowUseCase @Inject constructor(
    private val repository: PhotosRepository
) {
    operator fun invoke(params: RoverQueryParameters, page: Int = 1): Flow<List<PhotoDTO>> {
        return if (params.earthDate == null) {
            repository.getFlowBySolAndCamera(params, page)
        } else {
            repository.getFlowByDateAndCamera(params, page)
        }
    }
}
