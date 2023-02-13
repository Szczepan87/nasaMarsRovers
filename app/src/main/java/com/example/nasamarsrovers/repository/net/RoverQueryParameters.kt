package com.example.nasamarsrovers.repository.net

import com.example.nasamarsrovers.utils.CURIOSITY


data class RoverQueryParameters(
    val rover: String = CURIOSITY,
    val sol: Int? = null,
    val camera: String? = null,
    val earthDate: String? = null,
    val page: Int = 1
)