package com.example.nasamarsrovers.ui.navigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.model.CuriosityCameras
import com.example.nasamarsrovers.model.Photo
import com.example.nasamarsrovers.model.SpiritAndOpportunityCameras
import com.example.nasamarsrovers.ui.CameraPickerDialogContent
import com.example.nasamarsrovers.ui.EarthDatePickerDialogContent
import com.example.nasamarsrovers.ui.PhotoScreen
import com.example.nasamarsrovers.ui.PhotoUiState
import com.example.nasamarsrovers.ui.SolPickerDialogContent
import com.example.nasamarsrovers.ui.gallery.GalleryEvent
import com.example.nasamarsrovers.ui.gallery.GalleryScreen
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.DATE_FORMAT
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

private object Routes {
    const val GALLERY = "gallery"
    const val PHOTO = "photo?url={photoUrl}"

    fun photo(url: String?): String =
        if (url != null) "photo?url=${Uri.encode(url)}" else "photo"
}

@Composable
fun NasaMarsRoversApp(galleryViewModel: GalleryViewModel) {
    val state by galleryViewModel.state.collectAsState()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isOnGallery = currentRoute == Routes.GALLERY

    var showSolPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCameraPicker by remember { mutableStateOf(false) }

    // Collect one-shot events
    LaunchedEffect(Unit) {
        galleryViewModel.events.collectLatest { event ->
            when (event) {
                is GalleryEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ---- Dialogs ----
    if (showSolPicker) {
        Dialog(onDismissRequest = { showSolPicker = false }) {
            Surface(shape = RoundedCornerShape(8.dp)) {
                SolPickerDialogContent(
                    state = state,
                    onConfirm = { sol ->
                        galleryViewModel.setSol(sol)
                        showSolPicker = false
                    },
                    onCancel = { showSolPicker = false }
                )
            }
        }
    }

    if (showDatePicker) {
        Dialog(onDismissRequest = { showDatePicker = false }) {
            Surface(shape = RoundedCornerShape(8.dp)) {
                val maxDateCalendar = remember(state.maxEarthDate) {
                    parseDateToCalendar(state.maxEarthDate)
                }
                val minDateCalendar = remember(state.landingDate) {
                    parseDateToCalendar(state.landingDate)
                }
                val selectedDateCalendar = remember(state.currentEarthDate, state.maxEarthDate) {
                    parseDateToCalendar(state.currentEarthDate)
                        ?: maxDateCalendar
                        ?: Calendar.getInstance()
                }

                EarthDatePickerDialogContent(
                    initialDateMillis = selectedDateCalendar.timeInMillis,
                    minDateMillis = minDateCalendar?.timeInMillis,
                    maxDateMillis = maxDateCalendar?.timeInMillis,
                    onConfirm = { year, month, dayOfMonth ->
                        val cal =
                            Calendar.getInstance().apply { set(year, month, dayOfMonth) }
                        galleryViewModel.setEarthDate(DATE_FORMAT.format(cal.time))
                        showDatePicker = false
                    },
                    onCancel = { showDatePicker = false }
                )
            }
        }
    }

    if (showCameraPicker) {
        Dialog(onDismissRequest = { showCameraPicker = false }) {
            Surface(shape = RoundedCornerShape(8.dp)) {
                val cameraOptions = if (state.currentRover == CURIOSITY) {
                    CuriosityCameras.entries.map { stringResource(it.resId) }
                } else {
                    SpiritAndOpportunityCameras.entries.map { stringResource(it.resId) }
                }

                CameraPickerDialogContent(
                    cameraOptions = cameraOptions,
                    onSelect = { camera ->
                        galleryViewModel.setCurrentCamera(camera)
                        showCameraPicker = false
                    },
                    onCancel = { showCameraPicker = false }
                )
            }
        }
    }

    // ---- Main scaffold ----
    Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = state.currentRover,
                                style = MaterialTheme.typography.h6
                            )
                            Text(
                                text = stringResource(
                                    R.string.camera_with_value,
                                    state.currentCamera
                                        ?: stringResource(R.string.all_cameras)
                                ),
                                style = MaterialTheme.typography.caption
                            )
                        }
                    },
                    navigationIcon = {
                        if (isOnGallery) {
                            IconButton(
                                onClick = {
                                    scope.launch { scaffoldState.drawerState.open() }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.navigation_drawer_open)
                                )
                            }
                        } else {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.navigation_drawer_close)
                                )
                            }
                        }
                    }
                )
            },
            drawerContent = if (isOnGallery) {
                {
                    DrawerContent(
                        state = state,
                        onRoverSelected = { rover ->
                            galleryViewModel.setCurrentRover(rover)
                            scope.launch { scaffoldState.drawerState.close() }
                        },
                        onSolPickerClick = {
                            showSolPicker = true
                            scope.launch { scaffoldState.drawerState.close() }
                        },
                        onDatePickerClick = {
                            showDatePicker = true
                            scope.launch { scaffoldState.drawerState.close() }
                        },
                        onCameraPickerClick = {
                            showCameraPicker = true
                            scope.launch { scaffoldState.drawerState.close() }
                        }
                    )
                }
            } else null,
            drawerGesturesEnabled = isOnGallery
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Routes.GALLERY,
                modifier = Modifier.padding(padding)
            ) {
                composable(Routes.GALLERY) {
                    GalleryScreen(
                        state = state,
                        onPhotoClick = { photoUrl ->
                            navController.navigate(Routes.photo(photoUrl))
                        },
                        onLoadNextPage = { galleryViewModel.loadNextPage() },
                        onSwipeRight = {
                            if (state.isEarthDateUsed) galleryViewModel.previousEarthDate()
                            else galleryViewModel.decreaseSolByOne()
                        },
                        onSwipeLeft = {
                            if (state.isEarthDateUsed) galleryViewModel.nextEarthDay()
                            else galleryViewModel.increaseSolByOne()
                        }
                    )
                }

                composable(
                    Routes.PHOTO,
                    arguments = listOf(
                        navArgument("photoUrl") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val photoUrl = backStackEntry.arguments?.getString("photoUrl")
                    PhotoRoute(
                        galleryViewModel = galleryViewModel,
                        initialPhotoUrl = photoUrl
                    )
                }
            }
        }
}

@Composable
private fun PhotoRoute(
    galleryViewModel: GalleryViewModel,
    initialPhotoUrl: String?
) {
    val galleryState by galleryViewModel.state.collectAsState()
    var currentPhotoUrl by remember { mutableStateOf(initialPhotoUrl) }

    val uiState = remember(galleryState.listOfPhotos, currentPhotoUrl) {
        createPhotoUiState(galleryState.listOfPhotos, currentPhotoUrl)
    }

    PhotoScreen(
        state = uiState,
        onSwipeRight = {
            val list = galleryState.listOfPhotos
            val index = list.indexOfFirst { it.imgSrc == currentPhotoUrl }
            if (index > 0) currentPhotoUrl = list[index - 1].imgSrc
        },
        onSwipeLeft = {
            val list = galleryState.listOfPhotos
            val index = list.indexOfFirst { it.imgSrc == currentPhotoUrl }
            if (index != -1 && index < list.lastIndex) currentPhotoUrl = list[index + 1].imgSrc
        }
    )
}

private fun createPhotoUiState(photos: List<Photo>, currentPhotoUrl: String?): PhotoUiState {
    val index = photos.indexOfFirst { it.imgSrc == currentPhotoUrl }
    return PhotoUiState(
        imageUrl = currentPhotoUrl,
        hasPrevious = index > 0,
        hasNext = index != -1 && index < photos.lastIndex
    )
}

private fun parseDateToCalendar(dateString: String?): Calendar? {
    if (dateString.isNullOrBlank()) return null
    val date = DATE_FORMAT.parse(dateString) ?: return null
    return Calendar.getInstance().apply { time = date }
}

