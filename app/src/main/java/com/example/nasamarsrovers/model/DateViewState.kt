package com.example.nasamarsrovers.model

data class DateViewState(
    val isEarthDateUsed: Boolean = false,
    val sol: Int? = null,
    val earthDate: String? = null
)
