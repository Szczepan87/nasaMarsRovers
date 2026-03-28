package com.example.nasamarsrovers

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nasamarsrovers.databinding.ActivityMainBinding
import com.example.nasamarsrovers.ui.CameraPicker
import com.example.nasamarsrovers.ui.DatePickerDialog
import com.example.nasamarsrovers.ui.SolPicker
import com.example.nasamarsrovers.ui.gallery.GalleryState
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val galleryViewModel: GalleryViewModel by viewModels()
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbarBehaviour()
        setUpNavView()
    }

    private fun setUpToolbarBehaviour() {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.title = CURIOSITY
        supportActionBar?.subtitle = getString(R.string.camera_not_picked)
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                galleryViewModel.state.collectLatest { state ->
                    supportActionBar?.title = state.currentRover
                    supportActionBar?.subtitle = getString(
                        R.string.camera_with_value,
                        state.currentCamera ?: getString(R.string.all_cameras)
                    )
                    setSolDrawerText(state)
                    setEarthDateDrawerText(state)
                }
            }
        }
    }

    private fun setEarthDateDrawerText(state: GalleryState) {
        binding.navView.menu[EARTH_DATE_DRAWER_INDEX].title =
            if (state.isEarthDateUsed) {
                binding.navView.menu[SOL_DRAWER_INDEX].title =
                    getString(R.string.sol_not_picked)
                getString(
                    R.string.earth_date,
                    state.currentEarthDate ?: getString(R.string.value_not_selected)
                )
            } else {
                getString(R.string.earth_date_not_picked)
            }
    }

    private fun setSolDrawerText(state: GalleryState) {
        binding.navView.menu[SOL_DRAWER_INDEX].title =
            if (state.isEarthDateUsed.not()) {
                binding.navView.menu[EARTH_DATE_DRAWER_INDEX].title =
                    getString(R.string.earth_date_not_picked)
                getString(
                    R.string.sol,
                    state.currentSol ?: getString(R.string.value_not_selected)
                )
            } else {
                getString(R.string.sol_not_picked)
            }
    }

    private fun setUpNavView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(setOf(R.id.galleryFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpMenuItems(binding.navView, navController)
    }

    private fun setUpMenuItems(navView: NavigationView, navController: NavController) {
        with(navView) {
            setupWithNavController(navController)
            menu[CURIOSITY_DRAWER_INDEX].isChecked = true
            setNavigationItemSelectedListener {
                return@setNavigationItemSelectedListener when (it.itemId) {
                    R.id.sol_drawer_item -> launchSolPicker()
                    R.id.curiosity_drawer_item -> setCurrentlyDisplayedRover(CURIOSITY)
                    R.id.opportunity_drawer_item -> setCurrentlyDisplayedRover(OPPORTUNITY)
                    R.id.spirit_drawer_item -> setCurrentlyDisplayedRover(SPIRIT)
                    R.id.date_drawer_item -> launchDatePicker()
                    R.id.camera_drawer_item -> launchCameraPicker()
                    else -> {
                        closeDrawer()
                        true
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setCurrentlyDisplayedRover(roverName: String): Boolean {
        galleryViewModel.setCurrentRover(roverName)
        closeDrawer()
        return true
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun launchSolPicker(): Boolean {
        SolPicker().show(supportFragmentManager, SOL_PICKER_TAG)
        closeDrawer()
        return true
    }

    private fun launchCameraPicker(): Boolean {
        CameraPicker().show(supportFragmentManager, CAMERA_PICKER_TAG)
        closeDrawer()
        return true
    }

    private fun launchDatePicker(): Boolean {
        val datePicker = DatePickerDialog()
        datePicker.show(supportFragmentManager, EARTH_DATE_PICKER_TAG)
        closeDrawer()
        return true
    }

    companion object {
        const val SOL_PICKER_TAG = "SOL_PICKER"
        const val CAMERA_PICKER_TAG = "CAMERA_PICKER"
        const val EARTH_DATE_PICKER_TAG = "EARTH_DATE_DIALOG"
        private const val CURIOSITY_DRAWER_INDEX = 0
        private const val SOL_DRAWER_INDEX = 3
        private const val EARTH_DATE_DRAWER_INDEX = 4
    }
}
