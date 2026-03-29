package com.example.nasamarsrovers.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.nasamarsrovers.R

private val AppLightColors
    @Composable
    get() = lightColors(
        primary = colorResource(R.color.colorPrimary),
        primaryVariant = colorResource(R.color.colorPrimaryDark),
        secondary = colorResource(R.color.colorAccent)
    )

object NasaMarsRoversTheme {
    val Colors.disabledBackground: Color
        @Composable
        get() = colorResource(R.color.colorDisabledBackground)
}

@Composable
fun NasaMarsRoversTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AppLightColors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

