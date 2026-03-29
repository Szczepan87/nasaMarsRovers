package com.example.nasamarsrovers.ui.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.model.Photo

@Composable
fun GalleryCard(
    photo: Photo,
    onPhotoClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onPhotoClick(photo.imgSrc) }
    ) {
        SubcomposeAsyncImage(
            model = photo.imgSrc,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_error_outline_24),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryCardPreview() {
    MaterialTheme {
        GalleryCard(
            photo = Photo(
                camera = null,
                earthDate = null,
                id = 1,
                imgSrc = null,
                rover = null,
                sol = null
            ),
            onPhotoClick = {}
        )
    }
}

