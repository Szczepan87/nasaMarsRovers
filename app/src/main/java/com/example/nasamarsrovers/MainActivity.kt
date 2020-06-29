package com.example.nasamarsrovers

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.nasamarsrovers.ui.CameraPicker
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.ui.SolPicker
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val galleryViewModel: GalleryViewModel = get()
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        galleryViewModel.currentRover.observe(this, Observer { toolbar.title = it })

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.galleryFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpNavView(navView, navController)
    }

    private fun setUpNavView(navView: NavigationView, navController: NavController) {
        with(navView) {
            setupWithNavController(navController)
            menu.getItem(0).isChecked = true
            setNavigationItemSelectedListener {
                return@setNavigationItemSelectedListener when (it.itemId) {
                    R.id.sol_drawer_item -> {
                        SolPicker(
                            galleryViewModel
                        ).show(supportFragmentManager, "SOL_PICKER")
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                    R.id.curiosity_drawer_item -> {
                        galleryViewModel.setCurrentRover(CURIOSITY)
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                    R.id.opportunity_drawer_item -> {
                        galleryViewModel.setCurrentRover(OPPORTUNITY)
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                    R.id.spirit_drawer_item -> {
                        galleryViewModel.setCurrentRover(SPIRIT)
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                    R.id.camera_drawer_item -> {
                        CameraPicker().show(supportFragmentManager, "CAMERA_PICKER")
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                    else -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}