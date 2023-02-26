package com.example.data.model

data class RoverQueryParameters(
    val rover: String = DEFAULT_ROVER,
    val sol: Int? = null,
    val camera: String? = null,
    val earthDate: String? = null
){

    companion object {
        private const val DEFAULT_ROVER = "Curiosity"
    }
}
