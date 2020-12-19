package com.example.nasamarsrovers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nasamarsrovers.ui.CameraPicker
import com.example.nasamarsrovers.ui.DatePickerDialog
import com.example.nasamarsrovers.ui.SolPicker
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val galleryViewModel: GalleryViewModel by inject()
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpToolbarBehaviour()
        setUpNavView()
    }

    private fun setUpToolbarBehaviour() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = CURIOSITY
        supportActionBar?.subtitle = "Camera: FHAZ"
        setUpObservers()
    }

    private fun setUpObservers() {
        galleryViewModel.currentRover.observe(this, { supportActionBar?.title = it })
        galleryViewModel.currentCamera.observe(this, { supportActionBar?.subtitle = "Camera: $it" })
    }

    private fun setUpNavView() {
        val navView: NavigationView = findViewById(R.id.navView)
        val navController = findNavController(R.id.navHostFragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.galleryFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpMenuItems(navView, navController)
    }

    private fun setUpMenuItems(navView: NavigationView, navController: NavController) {
        with(navView) {
            setupWithNavController(navController)
            menu.getItem(0).isChecked = true
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
        val navController = findNavController(R.id.navHostFragment)
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
        SolPicker(galleryViewModel).show(supportFragmentManager, "SOL_PICKER")
        closeDrawer()
        return true
    }

    private fun launchCameraPicker(): Boolean {
        CameraPicker(galleryViewModel).show(supportFragmentManager, "CAMERA_PICKER")
        closeDrawer()
        return true
    }

    private fun launchDatePicker(): Boolean {
        DatePickerDialog(galleryViewModel).show(supportFragmentManager, "EARTH_DATE_DIALOG")
        closeDrawer()
        return true
    }
}