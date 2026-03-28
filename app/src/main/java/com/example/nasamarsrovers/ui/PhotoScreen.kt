package com.example.nasamarsrovers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun PhotoScreen(
    state: PhotoUiState,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(IntOffset.Zero) }
    var swipeDragAmount by remember { mutableFloatStateOf(0f) }
    val swipeThresholdPx = with(LocalDensity.current) { 64.dp.toPx() }

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        if (scale > 1f) {
            offset = IntOffset(
                x = offset.x + panChange.x.roundToInt(),
                y = offset.y + panChange.y.roundToInt()
            )
        } else {
            offset = IntOffset.Zero
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(state.imageUrl, state.hasPrevious, state.hasNext, scale) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        if (scale <= 1.05f) {
                            swipeDragAmount += dragAmount
                        }
                    },
                    onDragEnd = {
                        if (scale <= 1.05f) {
                            when {
                                swipeDragAmount > swipeThresholdPx && state.hasPrevious -> onSwipeRight()
                                swipeDragAmount < -swipeThresholdPx && state.hasNext -> onSwipeLeft()
                            }
                        }
                        swipeDragAmount = 0f
                    },
                    onDragCancel = { swipeDragAmount = 0f }
                )
            }
            .transformable(state = transformableState)
    ) {
        if (state.imageUrl.isNullOrBlank()) {
            Text(
                text = "Brak zdjecia do wyswietlenia",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AsyncImage(
                model = state.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x.toFloat()
                        translationY = offset.y.toFloat()
                    }
            )
        }
    }
}

@Preview(
    name = "PhotoScreen Empty",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
private fun PhotoScreenPreview() {
    MaterialTheme {
        PhotoScreen(
            state = PhotoUiState(
                imageUrl = null,
                hasPrevious = false,
                hasNext = false
            ),
            onSwipeRight = {},
            onSwipeLeft = {}
        )
    }
}
