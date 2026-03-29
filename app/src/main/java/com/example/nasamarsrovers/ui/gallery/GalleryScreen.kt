package com.example.nasamarsrovers.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.ui.theme.NasaMarsRoversTheme.disabledBackground

@Composable
fun GalleryScreen(
    state: GalleryState,
    onPhotoClick: (String?) -> Unit,
    onLoadNextPage: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val swipeThresholdPx = with(LocalDensity.current) { 64.dp.toPx() }
    var swipeDragAmount by remember { mutableFloatStateOf(0f) }

    // rememberUpdatedState ensures gesture handler always calls the latest callbacks,
    // even though pointerInput(Unit) is only set up once.
    val currentOnSwipeRight by rememberUpdatedState(onSwipeRight)
    val currentOnSwipeLeft by rememberUpdatedState(onSwipeLeft)
    val currentOnLoadNextPage by rememberUpdatedState(onLoadNextPage)

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            val totalItems = gridState.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisible >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) currentOnLoadNextPage()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        swipeDragAmount += dragAmount
                    },
                    onDragEnd = {
                        when {
                            swipeDragAmount > swipeThresholdPx -> currentOnSwipeRight()
                            swipeDragAmount < -swipeThresholdPx -> currentOnSwipeLeft()
                        }
                        swipeDragAmount = 0f
                    },
                    onDragCancel = { swipeDragAmount = 0f }
                )
            }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, end = 8.dp)
        ) {
            items(
                items = state.listOfPhotos,
                key = { photo -> photo.id ?: photo.imgSrc.hashCode() }
            ) { photo ->
                GalleryCard(
                    photo = photo,
                    onPhotoClick = onPhotoClick
                )
            }
        }

        if (state.isListEmpty) {
            Text(
                text = stringResource(R.string.empty_list_info),
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.disabledBackground)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenEmptyPreview() {
    MaterialTheme {
        GalleryScreen(
            state = GalleryState(isListEmpty = true),
            onPhotoClick = {},
            onLoadNextPage = {},
            onSwipeRight = {},
            onSwipeLeft = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenLoadingPreview() {
    MaterialTheme {
        GalleryScreen(
            state = GalleryState(isLoading = true),
            onPhotoClick = {},
            onLoadNextPage = {},
            onSwipeRight = {},
            onSwipeLeft = {}
        )
    }
}
